package com.dwcloud.mysql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.hibernate.validator.internal.util.logging.Log;

public class FileUtils {
    /**
     * <p>
     * 创建文件
     * </p>
     *
     * @param path
     * @param fileName
     * @return
     * @throws Exception
     * @author 叶新东（18126064335） 2018-6-21 上午11:50:26
     */
    public static File createFile(String path, String fileName) throws Exception {
        try {
// 目录
            File file = new File(path);
// 判断文件夹/文件 是否存在
            if (!file.exists()) {
// 创建目录
                file.mkdirs();
            }
// 文件
            String filePath = path + fileName;
            Date now = new Date();
            SimpleDateFormat f = new SimpleDateFormat("今天是" + "yyyy年MM月dd日 E kk点mm分ss秒");
            String suffix = f.format(now);
            file = new File(filePath + suffix + ".txt");
// 判断文件夹/文件 是否存在
            if (!file.exists()) {
// 如果不存在
// 创建文件
                file.createNewFile();
            } else {
// 如果已存在，创建一个新的文件
                file = new File(filePath);
                file.createNewFile();
            }
            return file;
        } catch (Exception e) {
            System.err.println("创建文件异常");
            throw e;
        }
    }

    /**
     * <创建文件>
     *
     * @param
     * @param
     * @return
     * @throws Exception
     * @author 叶新东（18126064335）  2018年9月8日 下午1:27:16
     */
    public static File createFileFolder(String filePath) throws Exception {
        try {
// 目录
            File file = new File(filePath);
// 判断文件夹/文件 是否存在
            if (file.exists()) {
                return file;
            }
            File parentFile = file.getParentFile();
//判断文件夹是否存在
            if (!parentFile.exists()) {
//如果不存在创建目录
                parentFile.mkdirs();
            }
//创建文件
            file.createNewFile();
            return file;
        } catch (Exception e) {
            System.err.println("创建文件异常");
            throw e;
        }
    }

    /**
     * <p>
     * 追加形式向文件写入内容
     * </p>
     *
     * @param filePath
     * @param content
     * @return
     * @throws Exception
     * @author 叶新东（18126064335） 2018-6-21 上午11:59:04
     */
    public static void writeFileContent(String filePath, String content) throws Exception {
        FileOutputStream fileOutStream = null;
        PrintWriter printWriter = null;
        try {
            // true 表示以追加的形式写入内容
            fileOutStream = new FileOutputStream(filePath, true);
            printWriter = new PrintWriter(fileOutStream);
            printWriter.write(content.toCharArray());
            printWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("向文件写入内容异常");
            throw e;
        } finally {
            try {
                if (null != fileOutStream) {
                    fileOutStream.close();
                }
                if (null != printWriter) {
                    printWriter.close();
                }
            } catch (Exception e2) {
                System.out.println("文件流关闭失败");
            }
        }
    }

    /**
     * <写入文件>
     *
     * @param filePath
     * @param input
     * @throws Exception
     * @author 叶新东（18126064335） 2018年9月7日 下午5:56:21
     */
    public static void writeFile(String filePath, InputStream input) throws Exception {
        OutputStream outputStream = null;
        try {
//先创建文件
            File file = createFileFolder(filePath);
            outputStream = new FileOutputStream(file);
//inputStream 转 outputStream
            int bytesWritten = 0;
            int byteCount = 0;
            byte[] bytes = new byte[1024];
            while ((byteCount = input.read(bytes)) != -1) {
                outputStream.write(bytes, bytesWritten, byteCount);
                bytesWritten += byteCount;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
//关闭流
            if (null != input) {
                input.close();
            }
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }
}