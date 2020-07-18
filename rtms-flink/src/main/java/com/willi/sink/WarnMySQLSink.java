package com.willi.sink;

import com.alibaba.druid.pool.DruidDataSource;
import com.willi.Bean.WarnMessage;
import com.willi.utils.DBDruid;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeutils.base.VoidSerializer;
import org.apache.flink.api.java.typeutils.runtime.kryo.KryoSerializer;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.TwoPhaseCommitSinkFunction;

import javax.xml.crypto.dsig.keyinfo.PGPData;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * \* project: bigdataplatform
 * \* package: com.willi.sink
 * \* author: Willi Wei
 * \* date: 2020-07-18 16:46:39
 * \* description:
 * \
 */
public class WarnMySQLSink extends TwoPhaseCommitSinkFunction<WarnMessage, Connection, Void> {
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/flink";
    String user = "root";
    String password = "weizefeng";



    private DruidDataSource dataSource;
    private QueryRunner queryRunner;


    public WarnMySQLSink() {
        super(new KryoSerializer<>(Connection.class,new ExecutionConfig()), VoidSerializer.INSTANCE);
    }

    /**
     * 初始化并开启连接，初始化一个DBUtils
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {

    }

    /**
     * 执行数据入库操作
     * @param connection
     * @param data
     * @param context
     * @throws Exception
     */
    @Override
    protected void invoke(Connection connection, WarnMessage data, Context context) throws Exception {
        System.out.println("start invoke...");
        queryRunner.update(
                "insert into warnings(production_line, device, warn_information, warn_status, timestamp, duration"
                /*"insert into sensor(id, timestamp, temperature) values(?, ?, ?) on duplicate key update `timestamp` = ?, temperature = ?"*/
                , data.getProductionLineName(), data.getDeviceName(), data.getWarnInformation(), data.getWarnStatus(), data.getTimestamp(), data.getWarnDuration());
//        try {
//            if (data.getTemperature() == 30){
//                data.setTemperature((double) (30 / 0));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        System.out.println("invoke 执行完毕");
    }

    /**
     * 获取连接，手动提交事务
     * @return
     * @throws Exception
     */
    @Override
    protected Connection beginTransaction() throws Exception {
        dataSource = DBDruid.getDruidDataSource(driver, url, user, password);;
        dataSource.getConnection().setAutoCommit(false);
        queryRunner = new QueryRunner(dataSource);
        System.out.println("queryRunnerhashcode为" + queryRunner.hashCode());
        return dataSource.getConnection();
    }

    /**
     * 预提交
     * @param connection
     * @throws Exception
     */
    @Override
    protected void preCommit(Connection connection) throws Exception {
        System.out.println("precommit...");
    }

    /**
     * 如果invoke正常则提交事务
     * @param connection
     */
    @Override
    protected void commit(Connection connection) {
        try {
            connection = dataSource.getConnection();
            System.out.println("commit中的connection为" + connection.hashCode());
            connection.commit();
            System.out.println("commit transaction successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 如果invoke异常则回滚事务，下一次checkpoint也不会执行
     * @param connection
     */
    @Override
    protected void abort(Connection connection) {
        System.out.println("start abort....");
        try {
            connection = dataSource.getConnection();
            if (connection != null){
                connection.rollback();
            }else {
                System.out.println("connection is null rollback failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     *
//     * @throws Exception
//     */
//    @Override
//    public void close() throws Exception {
//
//    }
}