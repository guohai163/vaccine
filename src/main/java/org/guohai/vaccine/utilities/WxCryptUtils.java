package org.guohai.vaccine.utilities;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;


public class WxCryptUtils {
    /**
     * 小程序 数据解密
     *
     * @param encryptData 加密数据
     * @param iv          对称解密算法初始向量
     * @param sessionKey  对称解密秘钥
     * @return 解密数据
     */
    public static String decrypt(String encryptData, String iv, String sessionKey) throws Exception {
        AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("AES");
        algorithmParameters.init(new IvParameterSpec(Base64.decodeBase64(iv)));
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(sessionKey), "AES"), algorithmParameters);
        byte[] decode = PKCS7Encoder.decode(cipher.doFinal(Base64.decodeBase64(encryptData)));
        String decryptStr = new String(decode, StandardCharsets.UTF_8);
        return decryptStr;
    }


}
