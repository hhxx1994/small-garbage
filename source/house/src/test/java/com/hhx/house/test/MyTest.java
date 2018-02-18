package com.hhx.house.test;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hhx
 * @since 2018/2/18 19:51
 */
public class MyTest {

    //155人关注/58次带看2个月以前发布房本满五年535万单价54498元/平米
    @Test
    public void getNum() {
        String str="155人关注/58次带看2个月以前发布房本满五年535万单价54498元/平米";
        Pattern pattern = Pattern.compile("(\\d+)\\D+(\\d+)");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            //System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }

    }

}
