package com.willi.flink;

import com.willi.Bean.WarnMessage;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.runtime.operators.util.AssignerWithPeriodicWatermarksAdapter;
import org.apache.flink.util.Collector;
import scala.collection.mutable.ListBuffer;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * \* project: bigdataplatform
 * \* package: com.willi.flink
 * \* author: Willi Wei
 * \* date: 2020-07-21 11:06:40
 * \* description:
 * \
 */
public class RealTimeWarnAnalyse {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // 读取数据，并提取timestamp
        DataStreamSource<String> source = env.readTextFile("data/warn_20_data.csv");
        SingleOutputStreamOperator<WarnMessage> dataStream = source.map(data -> new WarnMessage(
                        data.split("\t")[0].trim(),
                        data.split("\t")[1].trim(),
                        Long.parseLong(data.split("\t")[2].trim()),
                        data.split("\t")[3].trim(),
                        data.split("\t")[4].trim(),
                        data.split("\t")[5].trim(),
                        data.split("\t")[6].trim(),
                        data.split("\t")[7].trim()
                )
        ).returns(WarnMessage.class)
                .assignTimestampsAndWatermarks(WatermarkStrategy.<WarnMessage>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                        .withTimestampAssigner(new SerializableTimestampAssigner<WarnMessage>() {
                            @Override
                            public long extractTimestamp(WarnMessage element, long recordTimestamp) {
                                return element.getTimestamp();
                            }
                        }));


        // 将数据按照生产线进行分流
        dataStream.keyBy(WarnMessage::getDeviceName)
                .timeWindow(Time.seconds(5))
                    // 自定义聚合函数，自定义窗口函数输出平均值
                .aggregate(new AggregateFunction<WarnMessage, Long, Long>() {
                    @Override
                    public Long createAccumulator() {
                        return 0L;
                    }


                    @Override
                    public Long add(WarnMessage value, Long accumulator) {
                        // 来一条数据累加器 + 1
                        return accumulator + 1;
                    }

                    @Override
                    public Long getResult(Long accumulator) {
                        return accumulator;
                    }


                    @Override
                    public Long merge(Long a, Long b) {
                        // 遇到重分区时，将两个分区的累加器相加
                        return a + b;
                    }
                }, new WindowFunction<Long, Tuple3<String, Long, Long>, String, TimeWindow>() {
                    @Override
                    public void apply(String key, TimeWindow window, Iterable<Long> input, Collector<Tuple3<String, Long, Long>> out) throws Exception {
                        // 获取窗口的开启时间和关闭时间，左闭右开
                        System.out.println(key + " " + window.getStart() + "-> " + window.getEnd());
                        out.collect(new Tuple3<String, Long, Long>(key, window.getEnd(), input.iterator().next()));
                    }
                })
                // 将累加完的数据按照窗口的结束时间分类
                .keyBy(data->data.f1)
                // 因为时keyBy之后的数据，所以采用KeyedProcessFunction
                .process(new KeyedProcessFunction<Long, Tuple3<String, Long, Long>, String>() {

                    private ListState<Tuple3<String, Long, Long>> warnState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        // 第一条数据进来时，创建状态
                        warnState = getRuntimeContext().getListState(
                                new ListStateDescriptor<Tuple3<String, Long, Long>>
                                        ("warn-state", Types.TUPLE(TypeInformation.of(new TypeHint<Tuple3<String, Long, Long>>() {})))
                        );
                    }

                    /**
                     * 处理每一条数据
                     * @param value
                     * @param ctx
                     * @param out
                     * @throws Exception
                     */
                    @Override
                    public void processElement(Tuple3<String, Long, Long> value, Context ctx, Collector<String> out) throws Exception {
                        // 将每条数据存入状态列表
                        warnState.add(value);
                        // 注册一个定时器
                        // Registers a timer to be fired when the event time watermark passes the given time
                        System.out.println("注册了" + (value.f1 + 1));
                        // 会⌘册ᖃ前 key Ⲵ event time
                        //
                        ctx.timerService().registerEventTimeTimer(value.f1 + 1);
                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        ;
                        ArrayList<Tuple3<String, Long, Long>> allWarns = new ArrayList<>();
                        warnState.get().forEach(allWarns::add);
                        // 按照count大小，并取前N个
                        allWarns.sort((t1, t2) -> (int) (t2.f2 - t1.f2));

                        // 清空状态
                        warnState.clear();

                        // 将排名格式化输出
                        StringBuilder result = new StringBuilder();
                        System.out.println("===================================");
                        result.append("触发时间戳为：").append(timestamp - 1).append("\n");
                        System.out.println(result);
                        // 输出每一个警报的信息
                        allWarns.forEach(System.out::println);
                        System.out.println("===================================");
                        System.out.println();
                    }

                }).print();

        env.execute();
    }
}