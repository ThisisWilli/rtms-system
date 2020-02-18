package com.willi;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @program: bigdataplatform
 * @description:
 * @author: Hoodie_Willi
 * @create: 2020-02-18 20:41
 **/

public class StreamTest {
    public static void main(String[] args) throws Exception {
        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> source = env.readTextFile("rtms-flink/data/data1");
        System.out.println("开始执行任务");
        source.map(line->{
            return line + "=======";
        }).setParallelism(1).print();
        /*
         * Here, you can start creating your execution plan for Flink.
         *
         * Start with getting some data from the environment, like
         * 	env.readTextFile(textPath);
         *
         * then, transform the resulting DataStream<String> using operations
         * like
         * 	.filter()
         * 	.flatMap()
         * 	.join()
         * 	.coGroup()
         *
         * and many more.
         * Have a look at the programming guide for the Java API:
         *
         * http://flink.apache.org/docs/latest/apis/streaming/index.html
         *
         */

        // execute program
        env.execute("Flink Streaming Java API Skeleton");
    }
}
