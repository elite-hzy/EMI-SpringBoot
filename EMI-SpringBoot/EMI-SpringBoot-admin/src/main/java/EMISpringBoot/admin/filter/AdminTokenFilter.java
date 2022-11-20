package EMISpringBoot.admin.filter;

import EMISpringBoot.model.admin.pojos.AdminUser;
import EMISpringBoot.utils.ThreadLocalUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 *  作用：统一获取请求头的用户信息，共享给其他Controller去使用
 *    @WebFilter: 声明过滤器参数
 *      filterName: 过滤器名称
 *      urlPatterns: 拦截路径  /* 拦截全部请求
 */
@Component
@WebFilter(filterName = "AdminTokenFilter",urlPatterns = "admin/*")
public class AdminTokenFilter extends GenericFilter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //获取请求和响应
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        //从请求头获取userId
        String userId = request.getHeader("UserId");
        System.out.println("userId = " + userId);
        if(StringUtils.isNotEmpty(userId)){
            //把用户信息存入ThreadLocal对象
            AdminUser adminUser = new AdminUser();
            adminUser.setUserId(Integer.valueOf(userId));
            //存入ThreadLocal
            ThreadLocalUtils.set(adminUser);
        }

        //放行请求
        filterChain.doFilter(request,response);
    }
}
