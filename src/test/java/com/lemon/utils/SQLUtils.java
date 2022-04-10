package com.lemon.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.util.Map;

/**
 * @author luojie
 * @date 2020/6/23 - 20:42
 * 柠檬班创新教育极致服务
 */
public class SQLUtils {

    public static void main(String[] args) {
        String sql = "select leave_amount from member a where id = 162049;";
        Object result = getSingleResult(sql);
        System.out.println(result);
        System.out.println(result.getClass());
    }
    /**
     * 查询数据库单行单列结果集。
     * @param sql       sql语句
     * @return          查询结果
     */
    public static Object getSingleResult(String sql) {
        if(StringUtils.isBlank(sql)) {
            System.out.println("sql为空");
            return null;
        }
        Object result = null;
        QueryRunner runner = new QueryRunner();
        //获取数据库连接
        Connection conn = JDBCUtils.getConnection();
        try {
            //创建处理结果集对象
            ScalarHandler handler = new ScalarHandler();
            //执行查询语句
            result = runner.query(conn,sql,handler);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn);
        }
        return result;
    }

    public static void mapHandler() {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        try {
            String sql = "SELECT * FROM member a where a.mobile_phone = '15670890431'";
            MapHandler handler = new MapHandler();
            Map<String, Object> map =  runner.query(conn,sql,handler);
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn);
        }
    }
}
