package com.stylefeng.guns.rest.modular.auth.filter;

import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.util.RenderUtil;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import io.jsonwebtoken.JwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 对客户端请求的jwt token验证过滤器
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:04
 */
public class AuthFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtProperties jwtProperties;
//    @Autowired
//    Jedis jedis;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${myexpire}")
    int expireSeconds;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getServletPath().equals("/" + jwtProperties.getAuthPath())) {
            chain.doFilter(request, response);
            return;
        }
        String path = request.getServletPath();
        String unIgnorelUrlString = jwtProperties.getUnIgnorelUrl();
        String[] unIgnoreUrl = unIgnorelUrlString.split(",");


//        if (ignoreUrl != null && ignoreUrl.length > 0){
//            List ignoreList = Arrays.asList(ignoreUrl);
//            if (ignoreList.contains(path)){
//                chain.doFilter(request, response);
//                return;
//            }
//        }

        if (unIgnoreUrl == null || unIgnoreUrl.length == 0) {
            //不忽略的为空    直接放行
            chain.doFilter(request, response);
            return;
        }

        List unIgnoreList = Arrays.asList(unIgnoreUrl);
        if (!unIgnoreList.contains(path)){
            //如果不包含     直接放行
            chain.doFilter(request, response);
            return;
        }


        final String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken = null;

        if (requestHeader == null) {
            RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_NOTFOUND.getCode(), BizExceptionEnum.TOKEN_NOTFOUND.getMessage()));
            return;
        }
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);

            //验证token是否过期,包含了验证jwt是否正确
            try {
//                boolean flag = jwtTokenUtil.isTokenExpired(authToken);
//                String flag = jedis.get(authToken);
                String flag = redisTemplate.opsForValue().get(authToken);
                if (flag == null) {
                    RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_EXPIRED.getCode(), BizExceptionEnum.TOKEN_EXPIRED.getMessage()));
                    return;
                }
            } catch (JwtException e) {
                //有异常就是token解析失败
                RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_ERROR.getCode(), BizExceptionEnum.TOKEN_ERROR.getMessage()));
                return;
            }
        } else {
            //header没有带Bearer字段
            RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_ERROR.getCode(), BizExceptionEnum.TOKEN_ERROR.getMessage()));
            return;
        }
//        jedis.expire(authToken, expireSeconds);
        redisTemplate.expire(authToken, expireSeconds, TimeUnit.SECONDS);
        chain.doFilter(request, response);
    }
}
