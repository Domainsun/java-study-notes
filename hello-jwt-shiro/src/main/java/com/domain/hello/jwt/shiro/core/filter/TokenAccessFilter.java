package com.domain.hello.jwt.shiro.core.filter;

import com.domain.hello.jwt.shiro.core.exception.BusinessException;
import com.domain.hello.jwt.shiro.core.utils.JWTUtil;
import com.domain.hello.jwt.shiro.domain.ResponseBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @description:
 * @author: domain
 * @create: 2019/8/18 17:57
 */

public class TokenAccessFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        if(((HttpServletRequest) servletRequest).getMethod().equalsIgnoreCase("OPTIONS")) {
            return true;
        }
        return false;
    }


    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws BusinessException, IOException {
        String token = ((HttpServletRequest) servletRequest).getHeader("token");
        if (token==null || token.length()==0) {
            responseError(servletResponse,401,"尚未登录");
            return false;
        }
        String username = JWTUtil.getUsername(token);

        if (!JWTUtil.verify(token,username)) {
            responseError(servletResponse,401,"token 验证失败");
            return  false;
        }

        return true;
    }

    private void responseError(ServletResponse response,int code,String errorMsg) throws  IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Origin","*");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");		httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpServletResponse.setContentType("application/json; charset=UTF-8");
        ResponseBean baseResponse = new ResponseBean(code,errorMsg,null);
        OutputStream os = httpServletResponse.getOutputStream();
        os.write(new ObjectMapper().writeValueAsString(baseResponse).getBytes("UTF-8"));
        os.flush();
        os.close();
    }
}
