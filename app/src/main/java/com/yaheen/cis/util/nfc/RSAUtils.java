package com.yaheen.cis.util.nfc;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * AES对称加密工具类
 */
public class RSAUtils {

    /**
     * 密钥大小,如果太大会影响运行速度,推荐4096/2048/1024,1024位的RSA密钥基本安全,2048位的密钥极其安全。
     */
    public static final int KEY_SIZE = 1024;

    /**
     * RSA最大加密明文大小
     */
    public static final int MAX_ENCRYPT_BLOCK = (KEY_SIZE / 8) - 11;

    /**
     * RSA最大解密密文大小
     */
    public static final int MAX_DECRYPT_BLOCK = (KEY_SIZE / 8);

    /**
     * 校验数字签名
     *
     * @param data
     *            加密数据
     * @param publicKey
     *            公钥
     * @param sign
     *            数字签名(Base64)
     *
     * @return 校验成功返回true,失败返回false
     *
     */
    public static boolean verify(byte[] data, String publicKey, String sign) {
        try {
            // 解密由base64编码的公钥
            byte[] keyBytes = Base64Utils.decode(publicKey);
            // 构造X509EncodedKeySpec对象
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            // KEY_ALGORITHM 指定的加密算法
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // 取公钥匙对象
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(pubKey);
            signature.update(data);
            // 验证签名是否正常
            return signature.verify(Base64Utils.decode(sign));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] encrypt(byte[] data, Key key) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] encrypted = new byte[0];
        try {
            if (key != null) {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// "算法/模式/补码方式"
                cipher.init(Cipher.ENCRYPT_MODE, key);
                // 分段加密---START---
                int inputLen = data.length;
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段加密
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(data, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_ENCRYPT_BLOCK;
                }
                out.flush();
                return out.toByteArray();
                // 分段加密---END---
//                encrypted = cipher.doFinal(sSrc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] encryptByPublicKey(byte[] sSrc, byte[] sKey) {
        byte[] encrypted = new byte[0];
        try {
            if (sKey != null) {
                // 得到公钥对象
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(sKey);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey pubKey = keyFactory.generatePublic(keySpec);
                encrypted = encrypt(sSrc, pubKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    public static byte[] encryptByPrivateKey(byte[] sSrc, byte[] sKey) {
        byte[] encrypted = new byte[0];
        try {
            if (sKey != null) {
                // 得到私钥对象
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(sKey);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PrivateKey keyPrivate = kf.generatePrivate(keySpec);
                encrypted = encrypt(sSrc, keyPrivate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    public static byte[] encryptByPublicKey(String sSrc, String sKey) {
        byte[] encrypted = new byte[0];
        try {
            byte[] bKeys = Base64Utils.decode(sKey);
            byte[] bSrcs = sSrc.getBytes("UTF-8");
            encrypted = encryptByPublicKey(bSrcs, bKeys);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    public static byte[] encryptByPrivateKey(String sSrc, String sKey) {
        byte[] encrypted = new byte[0];
        try {
            byte[] bKeys = Base64Utils.decode(sKey);
            byte[] bSrcs = sSrc.getBytes("UTF-8");
            encrypted = encryptByPrivateKey(bSrcs, bKeys);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    public static String encryptByPublicKeyToString(byte[] sSrc, byte[] sKey) {
        byte[] encrypted = encryptByPublicKey(sSrc, sKey);
        return Base64Utils.encode(encrypted);
    }

    public static String encryptByPrivateKeyToString(byte[] sSrc, byte[] sKey) {
        byte[] encrypted = encryptByPrivateKey(sSrc, sKey);
        return Base64Utils.encode(encrypted);
    }

    public static String encryptByPublicKeyToString(String sSrc, String sKey) {
        byte[] encrypted = encryptByPublicKey(sSrc, sKey);
        return Base64Utils.encode(encrypted);
    }

    public static String encryptByPrivateKeyToString(String sSrc, String sKey) {
        byte[] encrypted = encryptByPrivateKey(sSrc, sKey);
        return Base64Utils.encode(encrypted);
    }

    public static byte[] decrypt(byte[] data, Key key) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] original = new byte[0];
        try {
            if (key != null) {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, key);
//                original = cipher.doFinal(sSrc);
                // 分段解密---START---
                int inputLen = data.length;
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段解密
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                        cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(data, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_DECRYPT_BLOCK;
                }
                out.flush();
                return out.toByteArray();
                // 分段解密---END---
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
//        return original;
    }

    public static byte[] decryptByPublicKey(byte[] sSrc, byte[] sKey) {
        byte[] original = new byte[0];
        try {
            if (sKey != null) {// 必须为16字节
                // 得到公钥对象
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(sKey);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey pubKey = keyFactory.generatePublic(keySpec);
                original = decrypt(sSrc, pubKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return original;
    }

    public static byte[] decryptByPrivateKey(byte[] sSrc, byte[] sKey) {
        byte[] original = new byte[0];
        try {
            if (sKey != null) {// 必须为16字节
                // 得到私钥对象
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(sKey);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PrivateKey keyPrivate = kf.generatePrivate(keySpec);
                original = decrypt(sSrc, keyPrivate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return original;
    }

    public static byte[] decryptByPublicKey(String sSrc, String sKey) {
        byte[] original = new byte[0];
        try {
            byte[] bKeys = Base64Utils.decode(sKey);
            byte[] bSrcs = Base64Utils.decode(sSrc);
            original = decryptByPublicKey(bSrcs, bKeys);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return original;
    }

    public static byte[] decryptByPrivateKey(String sSrc, String sKey) {
        byte[] original = new byte[0];
        try {
            byte[] bKeys = Base64Utils.decode(sKey);
            byte[] bSrcs = Base64Utils.decode(sSrc);
            original = decryptByPrivateKey(bSrcs, bKeys);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return original;
    }

    public static String decryptByPublicKeyToString(byte[] sSrc, byte[] sKey) {
        String retVal = "";
        try {
            byte[] decrypted = decryptByPublicKey(sSrc, sKey);
            retVal = new String(decrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static String decryptByPrivateKeyToString(byte[] sSrc, byte[] sKey) {
        String retVal = "";
        try {
            byte[] decrypted = decryptByPrivateKey(sSrc, sKey);
            retVal = new String(decrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static String decryptByPublicKeyToString(String sSrc, String sKey) {
        String retVal = "";
        try {
            byte[] decrypted = decryptByPublicKey(sSrc, sKey);
            retVal = new String(decrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static String decryptByPrivateKeyToString(String sSrc, String sKey) {
        String retVal = "";
        try {
            byte[] decrypted = decryptByPrivateKey(sSrc, sKey);
            retVal = new String(decrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static void main(String[] args) throws Exception {
        /*
         * 此处使用RSAECBPKCS1Padding算法
         */
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGKXHz1Kr0PZs6t/usPwvhPbXWhHr4p+4gHMl+IjMH6kjHXEus+TIBRyV3NemCDkQQ447MdQ/DDCTyw4S8xfmUpzLqdaex1+coDq9y5IxVRju9WegKlnGrDppzDd18DeFgScFPRShjQcbIiztFy4/rDeWvLPqtrf8hwGDs+PoBIwIDAQAB";

        String privateKey = "MIICXAIBAAKBgQCGKXHz1Kr0PZs6t/usPwvhPbXWhHr4p+4gHMl+IjMH6kjHXEus+TIBRyV3NemCDkQQ447MdQ/DDCTyw4S8xfmUpzLqdaex1+coDq9y5IxVRju9WegKlnGrDppzDd18DeFgScFPRShjQcbIiztFy4/rDeWvLPqtrf8hwGDs+PoBIwIDAQABAoGAGGhj3dtb+rFWk1kzhUO3EpRah5t0j9ZlYRw7oCHyrMb31KU6804mxcq+yzBYuhuEUJpFuXa2lmKYOj5N4NYXsKtDNX5PmLM4iNu26ww8OHJlGWHL8Js0wWPFesxSpeco6Lb09DjpC4m3INULewKX94gkqeCeBt5RxzAvxOaoTMECQQC1IZHknsJNtwYLE9DYl2cDXUSnzB/mUTtwEw7RvYqSCTs0Y3X13QPFXeIUQ0VDD7PCXow3d/vXipgbJtVhsNAhAkEAvZ3Qr6wYKsPwMh6BJlFI5iUjPjN7vmAxQX9T4Mxv8+SvyikRrJ+jMIQ+IKXwNqzJjciUfgEgqgSDE3XjJjx4wwJBAJ2tco6dsbHrgORFxMm3yyI/oBOee6qIzoA65uyo3qdQR+zMJJT6aFz45GUVRochdS/gFJn45hB1gwIn5Rtcy2ECQF5SGnTITl03ikvkEKpW1MRmxNk0EuHoR5glFjxEdYwEvcz6EOiloC+KGL10zcDkwn3cwMDNHspz5B/SDGQAwLECQGD0K+EljU7/t9j/rIGEl6QSg2QzJetUx6g2K6ZAaqjq1vMGV5nsU/we8hTyDOAf/F5jUP4vxRClAMNngHIIXQQ=";

        // 需要加密的字串
        String cSrc = "2|广州首润机电工程有限公司|222222|A|2016-10-20|广州|BDD50EF4DC9DFD0482B9A1AE08EAB8C5";
        System.out.println(cSrc);
        // 私钥加密
        String enString = RSAUtils.encryptByPrivateKeyToString(cSrc, privateKey);
        System.out.println("加密后的字串是：" + enString);
        // 公钥解密
        String DeString = RSAUtils.decryptByPublicKeyToString(enString, publicKey);
        System.out.println("解密后的字串是：" + DeString);
    }
}
