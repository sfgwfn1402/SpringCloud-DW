//package com.dwcloud.auth.common.utils;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
//
//import java.security.MessageDigest;
//
///**
// * EncryptUtils加密类的实现
// */
//public class EncryptUtil {
//
//    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);
//
//
//    private final static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
//            'e', 'f'};
//    public static String BASE64Encrypt;
//
//    public final static String MD5ToString(String signed) {
//
//        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
//        try {
//
//            byte[] res = signed.getBytes("UTF-8");
//            MessageDigest mdTemp = MessageDigest.getInstance("MD5".toUpperCase());
//            mdTemp.update(res);
//            byte[] md = mdTemp.digest();
//
//            // 把密文转换成十六进制的字符串形式
//            int j = md.length;
//            char str[] = new char[j * 2];
//            int k = 0;
//            for (int i = 0; i < j; i++) {
//                byte byte0 = md[i];
//                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//                str[k++] = hexDigits[byte0 & 0xf];
//            }
//            return new String(str);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return null;
//        }
//    }
//
//    public final static byte[] MD5(String str) {
//        try {
//            byte[] res = str.getBytes("UTF-8");
//            MessageDigest mdTemp = MessageDigest.getInstance("MD5".toUpperCase());
//            mdTemp.update(res);
//            byte[] hash = mdTemp.digest();
//            return hash;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    /**
//     * MD5值计算<p>
//     * MD5的算法在RFC1321 中定义:
//     * 在RFC 1321中，给出了Test suite用来检验你的实现是否正确：
//     * MD5 ("") = d41d8cd98f00b204e9800998ecf8427e
//     * MD5 ("a") = 0cc175b9c0f1b6a831c399e269772661
//     * MD5 ("abc") = 900150983cd24fb0d6963f7d28e17f72
//     * MD5 ("message digest") = f96b697d7cb7938d525a2f31aaf161d0
//     * MD5 ("abcdefghijklmnopqrstuvwxyz") = c3fcd3d76192e4007dfb496cca67e13b
//     *
//     * @param res 源字符串
//     * @return md5值
//     */
//    public final static byte[] MD5EncrtyReutrnhexDigitsByteArray(String str) {
//        try {
//            byte[] res = str.getBytes("UTF-8");
//            MessageDigest mdTemp = MessageDigest.getInstance("MD5".toUpperCase());
//            mdTemp.update(res);
//            byte[] hash = mdTemp.digest();
//            return hash;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public final static String MD5EncrtyReturnString(String str) {
//
//        byte[] b = MD5EncrtyReutrnhexDigitsByteArray(str);
//
//        StringBuffer resultSb = new StringBuffer();
//        for (int i = 0; i < b.length; i++) {
//            int n = b[i];
//            if (n < 0)
//                n = 256 + n;
//            int d1 = n / 16;
//            int d2 = n % 16;
//            resultSb.append(hexDigits[d1]);
//            resultSb.append(hexDigits[d2]);
//        }
//        return resultSb.toString();
//
//    }
//
//    // 加密后解密
//    public static String JM(byte[] inStr) {
//        String newStr = new String(inStr);
//        char[] a = newStr.toCharArray();
//        for (int i = 0; i < a.length; i++) {
//            a[i] = (char) (a[i] ^ 't');
//        }
//        String k = new String(a);
//        return k;
//    }
//
//    /**
//     * BASE64加密MD5EncrtyReutrnhexDigitsByteArray
//     *
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public static String BASE64Encrypt(byte[] key) throws ComponentException {
//        String edata = null;
//        try {
//            edata = (new BASE64Encoder()).encodeBuffer(key).trim();
//        } catch (Exception e) {
//            throw new ComponentException(e.getMessage() +
//                    "BASE64编码错误！key=" + new String(key) + ", error=" + e.getMessage());
//        }
//        return edata;
//    }
//
//    /**
//     * BASE64解密
//     *
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public static byte[] BASE64Decrypt(String data) {
//        if (data == null)
//            return null;
//        byte[] edata = null;
//        try {
//            edata = (new BASE64Decoder()).decodeBuffer(data);
//            return edata;
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//        return null;
//    }
//
//
//    /**
//     * 方法用途: 签名加密<br>
//     * 实现步骤: <br>
//     *
//     * @param signStr ：签名的字符串
//     * @return
//     */
//    public static String encryptSigned(String signed) throws ComponentException {
//
//        try {
//            byte[] md5SignStr = MD5EncrtyReutrnhexDigitsByteArray(signed);
//            String b64SignStr = BASE64Encrypt(md5SignStr);
//            return b64SignStr;
//        } catch (Exception e) {
//            throw new ComponentException(e.getMessage() + "BASE64或MD5加密签名错误！signed=" + signed + ", error=" + e.getMessage());
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        System.out.println(encryptSigned("app"));
//        System.out.println(encryptSigned("123"));
//    }
//
//}