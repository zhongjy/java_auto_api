package com.lemon.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luojie
 * @date 2020/6/16 - 20:23
 * 柠檬班创新教育极致服务
 *
 * 常量类 final
 */
public class Constants {
    //数据驱动excel路径
    //public static final String EXCEL_PATH = Constants.class.getClassLoader().getResource("./cases_v3.xlsx").getPath();
    public static final String EXCEL_PATH = "D:\\code\\java19_auto_api_v8\\src\\test\\resources\\cases_v3.xlsx";
    //默认请求头
    public static final Map<String,String> HEADERS = new HashMap<>();
    //excel 响应回写列
    public static final int RESPONSE_WRTIE_BACK_CELLNUM = 8;
    //excel 断言回写列
    public static final int ASSERT_WRTIE_BACK_CELLNUM = 10;

    //数据库连接URL                                jdbc:数据库名称://ip:port/数据库名称
                                         //jdbc:oracle:thin:@//127.0.0.1:1521/orcl
    public static final String JDBC_URL = "jdbc:mysql://api.lemonban.com:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
    //数据库用户名
    public static final String JDBC_USERNAME = "future";
    //数据库密码
    public static final String JDBC_PASSWORD = "123456";
}
