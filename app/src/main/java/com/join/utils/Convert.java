package com.join.utils;

/**
 * Created by join on 2017/5/25.
 */

public class Convert {
    /**
     * 十六进制字节转换成十六进制字符
     * 这个是十六进制的字符转换成十进制int Integer in = Integer.valueOf("1f",16);
     *
     * @return
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }

        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
