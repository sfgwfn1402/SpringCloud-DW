package com.dwcloud.mysql.createSQL;

import cn.hutool.core.util.StrUtil;
import com.dwcloud.mysql.entity.UserPO;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class GeneratedTableUtils {
    public static void main(String[] args) {
        // 生成mysql的create语句
        StringBuilder sql = tableGeneratedCreateUtil();
        // 打印或执行SQL语句
        System.out.println(sql.toString());
    }

    /**
     * 生成mysql的create语句
     *
     * @param tableName    表名称
     * @param tableComment 表备注
     * @param clazz        实体类
     */
    static StringBuilder tableGeneratedCreateUtil() {
        String tableName = "demo_user";
        String tableComment = "用户" + "表"; // 手动添加的表备注
        Class<?> clazz = UserPO.class; // 假设这是你的数据对象类

        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS  " + tableName + " (\n");

        try {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                // 表字段
                String columnName = StrUtil.toUnderlineCase(field.getName());   // name修改为表字段格式
                // 数据类型
                String columnType = fieldTypeConvDataTypeUtil(field.getType());
                sql.append("  " + columnName + " " + columnType + " COMMENT '" + field.getName() + "',\n");
            }

            // 拼接通用字段   (若不需要则可注释掉)
//            StringBuilder baseDoSql = baseDoSqlGenerated();
//            sql.append(baseDoSql);

            // 移除最后一个逗号，并添加结束括号
           /* if (sql.length() > 0) {
                sql.setLength(sql.length() - 2); // 移除最后的逗号和换行符
            }*/
            // 设置主键id
            sql.append("PRIMARY KEY(`id`)");
            sql.append("\n)");

            // 添加表注释（如果数据库支持）
            if (!tableComment.isEmpty()) {
                sql.append("  COMMENT='" + tableComment + "';");
            }

            // 打印或执行SQL语句
//            System.out.println(sql.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql;
    }

    /**
     * 根据字段类型设置SQL中的数据类型
     *
     * @param fileType 字段类型
     * @return columnType  数据类型
     */
    static String fieldTypeConvDataTypeUtil(Class<?> fileType) {
        String columnType = null;
        // 根据字段类型设置SQL中的数据类型
        if (fileType == Long.class || fileType == long.class) {
            columnType = "BIGINT";
        } else if (fileType == Integer.class || fileType == int.class) {
            columnType = "INT";
        } else if (fileType == Short.class || fileType == short.class) {
            columnType = "SMALLINT";
        } else if (fileType == Byte.class || fileType == byte.class) {
            columnType = "TINYINT";
        } else if (fileType == Float.class || fileType == float.class) {
            columnType = "FLOAT";
        } else if (fileType == Double.class || fileType == double.class) {
            columnType = "DOUBLE";
        } else if (fileType == Boolean.class || fileType == boolean.class) {
            columnType = "TINYINT(1)"; // 布尔类型通常可以映射为TINYINT(1)
        } else if (fileType == String.class) {
            // 假设字符串字段最大长度为255
            columnType = "VARCHAR(255)";
        } else if (fileType == java.util.Date.class || fileType == java.sql.Date.class || fileType == java.sql.Timestamp.class || fileType == java.time.LocalDate.class || fileType == java.time.LocalDateTime.class) {
            // 日期和时间类型
            columnType = "DATETIME"; // 或者你可以选择TIMESTAMP, DATE等
        } else if (fileType == BigDecimal.class) {
            // 假设BigDecimal的精度为19，小数位为2
            columnType = "DECIMAL(19, 2)";
        } else if (fileType.isArray()) {
            // 数组类型在SQL中通常不直接支持，可能需要序列化或转换为其他类型
            throw new IllegalStateException("Unsupported field type: array");
        } else if (fileType.isEnum()) {
            // 枚举类型通常映射为VARCHAR或INT，取决于存储方式
            columnType = "VARCHAR(255)"; // 假设使用VARCHAR存储枚举的字符串表示
        } else {
            // 其他类型可能需要特殊处理或自定义映射
            throw new IllegalStateException("不支持的字段类型: " + fileType);
        }
        return columnType;
    }

}
