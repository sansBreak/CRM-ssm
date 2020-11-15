package per.liu.crm.web.filter;


import per.liu.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到验证有没有登陆过的过滤器");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getServletPath();
        //登录页和登录请求不应该拦截
        if ("/login.jsp".equals(path) || "/setting/user/login.do".equals(path)){
            System.out.println("放行！");
            chain.doFilter(req, res);
        }else {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            //如果user不为null，说明登陆过,放行
            if (user != null){
                chain.doFilter(req, res);
            }else {
                System.out.println("不符合流程，拦截它。。。。");
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }




    }

    @Override
    public void destroy() {

    }
}
