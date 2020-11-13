package per.liu.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import per.liu.crm.settings.domain.User;
import per.liu.crm.settings.service.UserService;
import per.liu.crm.utils.MD5Util;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/setting/user")
public class UserController {

    @Resource
    private UserService userService;

    /*登录账号 "setting/user/login.do"*/
    @RequestMapping("/login.do")
    @ResponseBody
    public Map<String, Object> login(String loginAct, String loginPwd, HttpServletRequest request, HttpServletResponse response) {
        //用于存放返回值
        Map<String, Object> map = new HashMap<>();

        System.out.println("进入到验证登录操作");

        //将密码的明文形式转为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收浏览器端的IP地址
        String ip = request.getRemoteAddr();
        System.out.println("ip---->" + ip);

        try {

            User user = userService.login(loginAct, loginPwd, ip);
            //将取得的user放在session中备用
            request.getSession().setAttribute("user", user);

            //程序成功运行到此处，说明业务层没有为controller抛出任何异常，登录成功
            //此时应该通过ajax发给前台这个消息  {"success":true}

            map.put("success", true);

        } catch (Exception e) {
            e.printStackTrace();
            //一旦程序执行到catch块，说明业务层为我们的验证失败，为controller抛出异常，登录失败
            //此时应该通过ajax发给前台这个消息  {"success":false, "msg":?}
            String msg = e.getMessage();
            map.put("success", false);
            map.put("msg", msg);
        }
        return map;
    }

}
