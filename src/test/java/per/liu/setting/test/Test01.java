package per.liu.setting.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import per.liu.crm.utils.DateTimeUtil;
import per.liu.crm.utils.MD5Util;

import java.util.List;

public class Test01 {
    @Test
    public void test01(){
        //失效时间
        String expireTime = "2019-11-12 21:44:14";
        //获取当前时间
        String currentTime = DateTimeUtil.getSysTime();
        int count = expireTime.compareTo(currentTime);

        System.out.println(count);


    }

    @Test
    public void test02(){
        System.out.println(MD5Util.getMD5("4"));
    }
}
