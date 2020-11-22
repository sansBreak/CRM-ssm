package per.liu.crm.web.listener;

import javafx.application.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import per.liu.crm.settings.domain.DicValue;
import per.liu.crm.settings.service.DicService;
import per.liu.crm.settings.service.impl.DicServiceImpl;
import per.liu.crm.utils.ServiceFactory;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {

    /*
        在spring里，各个模块的启动顺序如下：listener >  filter > servlet >  spring
            所以在listener里是无法自动注入bean的！！！
    @Resource
    DicService dicService;*/
    //ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext()


    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("服务器缓存处理数据字典开始");

        /*
            Spring上下文获取及Bean获取
        */

          //取得上下文作用域对象application
        ServletContext application = event.getServletContext();

          //取得ApplicationContext 容器，用于装配对象
        WebApplicationContext applicationContext = (WebApplicationContext)application.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

          //通过ApplicationContext对象，手动获取bean
        DicService dicService = (DicService) applicationContext.getBean("dicServiceImpl");

        Map<String, List<DicValue>> map = dicService.getAll();

        //将map解析为上下文域对象中保存的键值对
        Set<String> set = map.keySet();
        for (String key:set){
            application.setAttribute(key,map.get(key));
        }
        System.out.println("服务器缓存处理数据字典结束");

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
