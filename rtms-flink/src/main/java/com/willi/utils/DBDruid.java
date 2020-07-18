package com.willi.utils;

import com.alibaba.druid.pool.DruidDataSource;

import java.io.Serializable;

/**
 * \* project: bigdataplatform
 * \* package: com.willi.utils
 * \* author: Willi Wei
 * \* date: 2020-07-18 18:56:58
 * \* description:
 * \
 */
public class DBDruid implements Serializable {

    public static final Integer INI_SIZE = 6;
    public static final Integer MAX_ACTIVE = 20;
    public static final Integer MIN_IDLE = 5;
    public static final Integer MAX_WAIT = 5 * 10000;
    public static final Integer ABANDONED_TIMEOUT = 600;

    /**
     * 获得连接池的实例
     * @param driver
     * @param url
     * @param user
     * @param password
     * @return 返回druid的实例
     */
    public static DruidDataSource getDruidDataSource(String driver, String url, String user, String password){

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        // 连接池属性
        dataSource.setInitialSize(INI_SIZE);
        dataSource.setMaxActive(MAX_ACTIVE);
        dataSource.setMinIdle(MIN_IDLE);
        dataSource.setMaxWait(MAX_WAIT);
        dataSource.setDefaultTransactionIsolation(1);

        // 手动提交事务，而非自动提交事务
        dataSource.setDefaultAutoCommit(false);
        // 超出限制是否回收

        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(ABANDONED_TIMEOUT);
//        dataSource.setDefaultTransactionIsolation();
        return dataSource;
    }
}