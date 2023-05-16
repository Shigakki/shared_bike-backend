package com.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author: swh 20301169@bjtu.edu.cn
 * @description: 日期与字符串之间的转化  未测试
 **/

public class DateUtils {
    public static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static Date stringToDate(String string) throws ParseException {
        return dateFormat.parse(string);
    }
    public static String dataToString(Date date){
        return dateFormat.format(date);
    }
}
