package per.liu.setting.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import per.liu.crm.utils.DateTimeUtil;

import java.util.List;

public class Test01 {
    @Test
    public void test01(){
        //ʧЧʱ��
        String expireTime = "2019-11-12 21:44:14";
        //��ȡ��ǰʱ��
        String currentTime = DateTimeUtil.getSysTime();
        int count = expireTime.compareTo(currentTime);

        System.out.println(count);


    }
}