package com.yaheen.cis.util.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    /**
     * 判断是否手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isPhoneNumber(String mobiles) {
        // Pattern p =
        // Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
