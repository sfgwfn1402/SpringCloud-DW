package com.dwcloud.mysql;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 利用jdbc备份mysql数据库--不用mysqldump
 */
public class BakDateBase {
    private String DRIVER = "com.mysql.cj.jdbc.Driver";
    private String URL = "jdbc:mysql://127.0.0.1:3306/oauth-auth-server?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowMultiQueries=true&serverTimezone=Asia/Shanghai";
    private String USERNAME = "root";// "root";
    private String PASSWORD = "123456";//"woaini";
    // 备份的文件地址
    private String filePath;
    private Connection conn = null;
    private String SQL = "SELECT * FROM ";// 数据库操作

    /**
     * <构造函数>
     *
     * @param ip          数据库ip地址
     * @param database    数据库名称
     * @param userName    数据库用户名
     * @param password    密码
     * @param bakFilePath 备份的地址
     */
    public BakDateBase(String ip, String database, String userName, String password, String bakFilePath) {
        try {
            Class.forName(this.DRIVER);
            this.URL = String.format("jdbc:mysql://%s:3306/%s?useUnicode=true&characterEncoding=utf8", ip, database);
            this.USERNAME = userName;
            this.PASSWORD = password;
            SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-ddHH时mm分ss秒");
            String datetime = tempDate.format(new java.util.Date());
//自动加上时间戳
            datetime = datetime + "_数据库名称：" + database;
            if (bakFilePath.indexOf(".") != -1) {
                bakFilePath = bakFilePath.replace(".", datetime + ".");
            } else {
                bakFilePath = datetime + ".sql";
            }
            this.filePath = bakFilePath;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("can not load jdbc driver");
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    private Connection getConnection() {
        try {
            if (null == conn) {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("get connection failure");
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     */
    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("close connection failure");
            }
        }
    }

    /**
     * 获取数据库下的所有表名
     */
    private List<String> getTableNames() {
        List<String> tableNames = new ArrayList<String>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
// 获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
// 从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("getTableNames failure");
        } finally {
            try {
                if (null != rs) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("close ResultSet failure");
            }
        }
        return tableNames;
    }

    /**
     * 获取表中所有字段名称
     *
     * @param tableName 表名
     * @return
     */
    private List<String> getColumnNames(String tableName) {
        List<String> columnNames = new ArrayList<String>();
        // 与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            // 结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            // 表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
            }
        } catch (SQLException e) {
            System.err.println("getColumnNames failure");
            e.printStackTrace();
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("getColumnNames close pstem and connection failure");
                }
            }
        }
        return columnNames;
    }

    /**
     * 获取表中所有字段类型
     *
     * @param tableName
     * @return
     */
    private List<String> getColumnTypes(String tableName) {
        List<String> columnTypes = new ArrayList<String>();
// 与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
// 结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
// 表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnTypes.add(rsmd.getColumnTypeName(i + 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("getColumnTypes failure");
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("getColumnTypes close pstem and connection failure");
                }
            }
        }
        return columnTypes;
    }

    /**
     * <p>
     * 生成建表语句
     * </p>
     *
     * @param tableName
     * @return
     * @author 叶新东（18126064335） 2018年9月6日 上午9:35:49
     */
    private String generateCreateTableSql(String tableName) {
        String sql = String.format("SHOW CREATE TABLE %s", tableName);
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
// 返回建表语句语句，查询结果的第二列是建表语句，第一列是表名
                return rs.getString(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (null != pstmt) {
                    pstmt.close();
                }
            } catch (Exception e2) {
                e.printStackTrace();
                System.err.println("关闭流异常");
            }
        }
        return null;
    }

    /**
     * 获取表中字段的所有注释
     *
     * @param tableName
     * @return
     */
    private List<String> getColumnComments(String tableName) {
// 与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        List<String> columnComments = new ArrayList<String>();// 列名注释集合
        ResultSet rs = null;
        try {
            pStemt = conn.prepareStatement(tableSql);
            rs = pStemt.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                columnComments.add(rs.getString("Comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("getColumnComments close ResultSet and connection failure");
                }
            }
        }
        return columnComments;
    }

    /**
     * <p>
     * 备份表数据
     * </p>
     *
     * @param tableName
     * @return
     * @author （） 2018年9月6日 上午10:18:07
     */
    private String bakTableData(String tableName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            // 备份建表语句
            String createTableSql = generateCreateTableSql(tableName);
            createTableSql = String.format("\n\n\n/**\n * table name :<%s>\n *\n */\n%s\n", tableName, createTableSql);
            FileUtils.writeFileContent(filePath, createTableSql);
            // 获取字段类型
            List<String> columnTypes = getColumnTypes(tableName);
            // 获取所有 字段
            List<String> columnNames = getColumnNames(tableName);
            String columnArrayStr = null;
            for (String column : columnNames) {
                if (null == columnArrayStr) {
                    columnArrayStr = "`" + column + "`";
                } else {
                    columnArrayStr = columnArrayStr + "," + "`" + column + "`";
                }
            }
            String sql = String.format("select %s from %s", columnArrayStr, tableName);
            conn = getConnection();
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String rowValues = getRowValues(rs, columnNames.size(), columnTypes);
                // 返回建表语句语句，查询结果的第二列是建表语句，第一列是表名
                String insertSql = String.format("insert into %s (%s) values(%s);", tableName, columnArrayStr, rowValues);
                System.out.println(insertSql);
                insertSql = insertSql.replaceAll("\n", "<br/>");
                insertSql = insertSql + "\n";
                FileUtils.writeFileContent(filePath, insertSql);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (null != pstmt) {
                    pstmt.close();
                }
            } catch (Exception e2) {
                e.printStackTrace();
                System.err.println("关闭流异常");
            }
        }
        return null;
    }

    /**
     * <p>
     * 获取表数据一行的所有值
     * </p>
     *
     * @param rs
     * @param size
     * @author 2018年9月6日 上午11:03:05
     */
    private String getRowValues(ResultSet rs, int size, List<String> columnTypeList) {
        try {
            String rowValues = null;
            for (int i = 1; i <= size; i++) {
                String columnValue = null;
// 获取字段值
                columnValue = getValue(rs, i, columnTypeList.get(i - 1));
// 如果是空值不添加单引号
                if (null != columnValue) {
                    columnValue = "'" + columnValue + "'";
                }
// 拼接字段值
                if (null == rowValues) {
                    rowValues = columnValue;
                } else {
                    rowValues = rowValues + "," + columnValue;
                }
            }
            return rowValues;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取表数据一行的所有值异常");
            return null;
        }
    }

    /**
     * <p>
     * 根据类型获取字段值
     * </p>
     *
     * @return
     * @author 2018年9月6日 上午11:16:00
     */
    private String getValue(ResultSet resultSet, Integer index, String columnType) {
        try {
            if ("int".equals(columnType) || "INT".equals(columnType)) {
// 整数
                Object intValue = resultSet.getObject(index);
                if (null == intValue) {
                    return null;
                }
                return intValue + "";
            } else if ("bigint".equals(columnType) || "BIGINT".equals(columnType)) {
// 长整形
                Object value = resultSet.getObject(index);
                if (null == value) {
                    return null;
                }
                return value + "";
            } else if ("smallint".equals(columnType) || "SMALLINT".equals(columnType)) {
// 整数
                Object value = resultSet.getObject(index);
                if (null == value) {
                    return null;
                }
                return value + "";
            } else if ("tinyint".equals(columnType) || "TINYINT".equals(columnType)) {
// 整数
                Object value = resultSet.getObject(index);
                if (null == value) {
                    return null;
                }
                return value + "";
            } else if ("mediumint".equals(columnType) || "MEDIUMINT".equals(columnType)) {
// 长整形
                Object value = resultSet.getObject(index);
                if (null == value) {
                    return null;
                }
                return value + "";
            } else if ("integer".equals(columnType) || "INTEGER".equals(columnType)) {
// 整数
                Object value = resultSet.getObject(index);
                if (null == value) {
                    return null;
                }
                return value + "";
            } else if ("float".equals(columnType) || "FLOAT".equals(columnType)) {
// 浮点数
                Object value = resultSet.getObject(index);
                if (null == value) {
                    return null;
                }
                return value + "";
            } else if ("double".equals(columnType) || "DOUBLE".equals(columnType)) {
// 浮点数
                Object value = resultSet.getObject(index);
                if (null == value) {
                    return null;
                }
                return value + "";
            } else if ("decimal".equals(columnType) || "DECIMAL".equals(columnType)) {
// 浮点数-金额类型
                BigDecimal value = resultSet.getBigDecimal(index);
                if (null == value) {
                    return null;
                }
                return value.toString();
            } else if ("char".equals(columnType) || "CHAR".equals(columnType)) {
// 字符串类型
                String value = resultSet.getString(index);
                return value;
            } else if ("varchar".equals(columnType) || "VARCHAR".equals(columnType)) {
// 字符串类型
                String value = resultSet.getString(index);
                return value;
            } else if ("tinytext".equals(columnType) || "TINYTEXT".equals(columnType)) {
// 字符串类型
                String value = resultSet.getString(index);
                return value;
            } else if ("text".equals(columnType) || "TEXT".equals(columnType)) {
// 字符串类型
                String value = resultSet.getString(index);
                return value;
            } else if ("mediumtext".equals(columnType) || "MEDIUMTEXT".equals(columnType)) {
// 字符串类型
                String value = resultSet.getString(index);
                return value;
            } else if ("longtext".equals(columnType) || "LONGTEXT".equals(columnType)) {
// 字符串类型
                String value = resultSet.getString(index);
                return value;
            } else if ("year".equals(columnType) || "YEAR".equals(columnType)) {
// 时间类型：范围 1901/2155 格式 YYYY
                String year = resultSet.getString(index);
                if (null == year) {
                    return null;
                }
// 只需要年的字符即可，
                return year.substring(0, 4);
            } else if ("date".equals(columnType) || "DATE".equals(columnType)) {
// 时间类型：范围 '1000-01-01'--'9999-12-31' 格式 YYYY-MM-DD
                return resultSet.getString(index);
            } else if ("time".equals(columnType) || "TIME".equals(columnType)) {
// 时间类型：范围 '-838:59:59'到'838:59:59' 格式 HH:MM:SS
                return resultSet.getString(index);
            } else if ("datetime".equals(columnType) || "DATETIME".equals(columnType)) {
// 时间类型：范围 '1000-01-01 00:00:00'--'9999-12-31 23:59:59' 格式 YYYY-MM-DD HH:MM:SS
                return resultSet.getString(index);
            } else if ("timestamp".equals(columnType) || "TIMESTAMP".equals(columnType)) {
// 时间类型：范围 1970-01-01 00:00:00/2037 年某时 格式 YYYYMMDD HHMMSS 混合日期和时间值，时间戳
                return resultSet.getString(index);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("获取数据库类型值异常");
            return null;
        }
    }

    /**
     * <开始备份>
     *
     * @author 2018年9月6日 下午3:30:43
     */
    public void startBak() {
        try {
            List<String> tableNames = getTableNames();
            System.out.println("tableNames:" + tableNames);
            for (String tableName : tableNames) {
                bakTableData(tableName);
                // System.out.println(generateCreateTableSql(tableName));
                // System.out.println("ColumnNames:" + getColumnNames(tableName));
                // System.out.println("ColumnTypes:" + getColumnTypes(tableName));
                // System.out.println("ColumnComments:" + getColumnComments(tableName));
            }
        // 统一关闭连接
            closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BakDateBase("127.0.0.1", "oauth-auth-server", "root", "123456", "/Users/duwei/bak.sql").startBak();
    }
}