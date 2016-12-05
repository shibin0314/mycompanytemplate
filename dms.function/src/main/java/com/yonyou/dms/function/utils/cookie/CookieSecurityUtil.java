/*
* Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
*
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : cmol.common.function
*
* @File name : CookieSecurityUtil.java
*
* @Author : 
*
* @Date : 2016年2月29日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年2月29日                                    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/

package com.yonyou.dms.function.utils.cookie;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.yonyou.dms.function.exception.UtilException;
import com.yonyou.dms.function.utils.common.StringUtils;

/*
*
* @author 
* cookie 加密 util
* @date 2016年2月29日
*/

public class CookieSecurityUtil {

    /** 加密、解密key. */
    private static final String PASS_WORD_CRYPT_KEY = "kEHrDooxWHCWtfeSxvDvgqZq";

    /** 加密算法,可用 DES,DESede,Blowfish. */
    private final static String ALGORITHM = "DES";

    /*
     * @author 对数据进行DES解密.
     * @date 2016年2月29日
     * @param data
     * @return 返回经过DES加密后的数据
     * @throws UtilException
     */
    public final static String decrypt(String data) throws UtilException {
        try {
            if (StringUtils.isNullOrEmpty(data)) {
                throw new UtilException("input data cannot be null or empty");
            }
            return new String(decrypt(hex2byte(data.getBytes()), PASS_WORD_CRYPT_KEY.getBytes()));
        } catch (Exception e) {
            throw new UtilException(e);
        }
    }

    /*
     * @author 对用DES加密过的数据进行加密.
     * @date 2016年2月29日
     * @param data
     * @return 返回解密后的数据
     * @throws UtilException
     */

    public final static String encrypt(String data) throws UtilException {
        if (StringUtils.isNullOrEmpty(data)) {
            throw new UtilException("input data cannot be null or empty");
        }
        return byte2hex(encrypt(data.getBytes(), PASS_WORD_CRYPT_KEY.getBytes()));
    }

    /*
     * @author 用指定的key对数据进行DES加密.
     * @date 2016年2月29日
     * @param data 待加密的数据
     * @param key DES加密的key
     * @return 返回DES加密后的数据
     * @throws UtilException
     */

    private static byte[] encrypt(byte[] data, byte[] key) throws UtilException {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new UtilException(e);

        }
    }

    /*
     * @author 用指定的key对数据进行DES解密.
     * @date 2016年2月29日
     * @param data 待解密的数据
     * @param key DES解密的key
     * @return 返回DES解密后的数据
     * @throws UtilException
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws UtilException {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            // 现在，获取数据并解密
            // 正式执行解密操作
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new UtilException(e);
        }
    }

    public static byte[] hex2byte(byte[] b) throws UtilException {
        if ((b.length % 2) != 0) throw new UtilException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public static String byte2hex(byte[] b) throws UtilException {
        StringBuffer hs = new StringBuffer();
        for (int n = 0; n < b.length; n++) {
            String stmp = java.lang.Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append("0");
                hs.append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

}
