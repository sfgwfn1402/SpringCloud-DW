package com.dwcloud.oss;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class OssHandler {
    public static void main(String[] args) throws IOException {
        OssHandler ossBucket = new OssHandler();
        URL url = OssHandler.class.getResource("/picture.jpg");
        ossBucket.uploadFile("chewangtest", url.getFile(), "picture.jpg");
    }

    /**
     * 上传文件到OSS
     * @param bucketName bucket名称
     * @param filePath 文件路径
     * @param objectName 上传到OSS的文件名
     * @return
     * @throws IOException
     */
    public static String uploadFile(String bucketName, String filePath, String objectName) throws IOException {
        try {
            Properties properties = new Properties();
            // 设置config.ini文件路径
            String configFilePath = "/config.ini";

            // 读取配置文件
            URL url = OssHandler.class.getResource(configFilePath);
            FileInputStream input = new FileInputStream(url.getFile());
//        FileInputStream input = new FileInputStream(configFilePath);
            properties.load(input);
            input.close();

            // 从配置文件中获取AK和SK
            String accessKeyId = properties.getProperty("alibaba_cloud_access_key_id");
            String accessKeySecret = properties.getProperty("alibaba_cloud_access_key_secret");

            // 从环境变量中获取访问凭证。运行本代码示例之前，请先配置环境变量。
            CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);

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
                // 使用文件输入流上传文件
                try (FileInputStream fis = new FileInputStream(filePath)) {
                    ossClient.putObject(new PutObjectRequest(bucketName, objectName, fis));

                    // 构造访问 URL
                    // 显式设置对象的公共读权限
                    ossClient.setObjectAcl(bucketName, objectName, CannedAccessControlList.PublicRead);

                    // 设置过期时间为N小时后
//                    Date expiration = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2));
//                    URL resultUrl = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
//                    System.out.println("File uploaded successfully. Access URL: " + resultUrl.toString());

                    String accessUrl = "https://" + bucketName + "." + endpoint + "/" + objectName;
                    System.out.println("File uploaded successfully. Access URL: " + accessUrl);
                    return accessUrl;
                }
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

        return "";
    }
}