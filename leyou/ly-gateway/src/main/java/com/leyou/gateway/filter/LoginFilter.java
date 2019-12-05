package com.leyou.gateway.filter;

import com.leyou.auth.entry.UserInfo;
import com.leyou.auth.util.JwtUtils;
import com.leyou.common.util.CookieUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/28 9:52
 * @Version 1.0
 */
@Slf4j
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
@Component
public class LoginFilter extends ZuulFilter {
	@Autowired
	private JwtProperties prop;
	@Autowired
	FilterProperties filterProperties;
	@Override
	public String filterType() {
		return PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 5;
	}

	@Override
	public boolean shouldFilter() {
		//1获取上下文
		RequestContext ctx = RequestContext.getCurrentContext();
		//2获取uri
		String uri = ctx.getRequest().getRequestURI();
		//3判断uri是否是白名单
		for (String allowPath : filterProperties.getAllowPaths()) {
			if (uri.startsWith(allowPath)){
				return false;
			}
		}
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		//获取token
		String token = CookieUtils.getCookieValue(request, prop.getCookieName());
		//解析token
		try {
			UserInfo userInfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
		} catch (Exception e) {
			log.error("未携带token或token被篡改");
			//设置不响应
			context.setSendZuulResponse(false);
			context.setResponseStatusCode(403);
			log.error("非法访问，未登录，地址：{}", request.getRemoteHost(), e );
			e.printStackTrace();
		}
		//鉴权 todo
		return null;
	}
}
