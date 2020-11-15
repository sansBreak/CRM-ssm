package per.liu.crm.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import per.liu.crm.settings.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle开始运行");
        //获取请求的RUi:去除http:localhost:8080这部分剩下的
        String uri = request.getRequestURI();
        //URL:除了login.jsp是可以公开访问的，其他的URL都进行拦截控制
        if (uri.indexOf("/login") >= 0) {
            System.out.println("是登录请求，准许通行！");
            return true;
        }

        //获取session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        //判断session中是否有用户数据，如果有，则返回true，继续向下执行
        if (user != null) {
            System.out.println("不是登录请求，但在session中找到了user，运行通行！");
            return true;
        }

        //不符合条件的给出提示信息，并重定向到登录页面
        //request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        System.out.println("未登录，重定向回登录页！");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return false;
    }
}
