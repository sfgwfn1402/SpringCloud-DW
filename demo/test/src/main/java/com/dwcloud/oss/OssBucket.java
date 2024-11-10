package com.dwcloud.oss;

import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import com.aliyun.oss.*;
import com.aliyun.oss.common.comm.SignVersion;

public class OssBucket {
    public static void main(String[] args) throws IOException {
        OssBucket ossBucket = new OssBucket();
        ossBucket.createBucket("chewangtest");
    }

    /**
     * 创建存储空间
     * @param bucketName 存储空间名称
     * @return
     * @throws IOException
     */
    public String createBucket(String bucketName) throws IOException {
        try {
            Properties properties = new Properties();
            // 设置config.ini文件路径
            String configFilePath = "/config.ini";

            // 读取配置文件
            URL url = OssBucket.class.getResource(configFilePath);
            FileInputStream input = new FileInputStream(url.getFile());
//        FileInputStream input = new FileInputStream(configFilePath);
            properties.load(input);
            input.close();

            // 从配置文件中获取AK和SK
            String accessKeyId = properties.getProperty("alibaba_cloud_access_key_id");
            String accessKeySecret = properties.getProperty("alibaba_cloud_access_key_secret");

            // 从环境变量中获取访问凭证。运行本代码示例之前，请先配置环境变量。
            CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);
//        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();


            // 使用credentialsProvider初始化客户端并进行后续操作...
            // // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
            String endpoint = "oss-cn-wuhan-lr.aliyuncs.com";
            // 填写Endpoint对应的Region信息，例如cn-hangzhou。
            String region = "cn-wuhan-lr";

            // 创建OSSClient实例。
            ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
            clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
            OSS ossClient = OSSClientBuilder.create()
                    .endpoint(endpoint)
                    .credentialsProvider(credentialsProvider)
                    .clientConfiguration(clientBuilderConfiguration)
                    .region(region)
                    .build();
            try {
                // 创建存储空间。
                ossClient.createBucket(bucketName);

            } catch (OSSException oe) {
                System.out.println("Caught an OSSException, which means your request made it to OSS, "
                        + "but was rejected with an error response for some reason.");
                System.out.println("Error Message:" + oe.getErrorMessage());
                System.out.println("Error Code:" + oe.getErrorCode());
                System.out.println("Request ID:" + oe.getRequestId());
                System.out.println("Host ID:" + oe.getHostId());
            } catch (ClientException ce) {
                System.out.println("Caught an ClientException, which means the client encountered "
                        + "a serious internal problem while trying to communicate with OSS, "
                        + "such as not being able to access the network.");
                System.out.println("Error Message:" + ce.getMessage());
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return "Bucket " + bucketName + " created successfully.";
    }
}