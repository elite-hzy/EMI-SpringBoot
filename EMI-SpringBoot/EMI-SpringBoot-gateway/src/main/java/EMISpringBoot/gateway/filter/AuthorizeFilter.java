package EMISpringBoot.gateway.filter;

import EMISpringBoot.model.admin.pojos.AdminUser;
import EMISpringBoot.model.user.pojos.CustomerUser;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.SignatureException;

/**
 * 统一鉴权过滤器
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //注意：必须对登录请求实施放行，否则无法登录（无法获取token）
        String uri = request.getURI().getPath();   // /admin/login/in
        if (uri.contains("/login")) {
            return chain.filter(exchange);
        }
        //1 网关接收名为token的请求头
        String token = request.getHeaders().getFirst("token");

        //2 判断token是否存在，不存在，返回401状态码（代表权限不足）
        if (StringUtils.isEmpty(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //结束请求
            return response.setComplete();
        }

        //3.校验token是否合法,（包含是否过期），不合法，返回401状态码（代表权限不足）'
        JwtParser jwtParser = Jwts.parser(); //获取jwt解析器
        jwtParser.setSigningKey("qianfeng");
        try {
            //如果token正确(密码，有效期)则正常运行，否则抛出异常
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            Claims body = claimsJws.getBody();//获取body
//            System.out.println("body = " + body);
            String key = body.get("key", String.class);
            String status = body.get("status", String.class);
            System.out.println("status = " + status);
            System.out.println("key = " + key);
            if (uri.contains("/admin")||uri.contains("/expressDelivery")) {
                if (!status.equals("admin")){
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
                AdminUser user = JSON.parseObject(key, AdminUser.class);
                request.mutate().header("UserId", user.getUserId().toString());
            } else {
                if (!status.equals("customer")){
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
                CustomerUser user = JSON.parseObject(key, CustomerUser.class);
                request.mutate().header("UserId", user.getUserid().toString());
            }
            return chain.filter(exchange);
        } catch (Exception e) {
            System.out.println("签名错误");
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //结束请求
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
