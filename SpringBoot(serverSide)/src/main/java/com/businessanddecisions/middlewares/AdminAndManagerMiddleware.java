package com.businessanddecisions.middlewares;

import config.JwtTokenUtil;
import org.hibernate.annotations.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static config.Constants.HEADER_STRING;
import static config.Constants.TOKEN_PREFIX;
import static org.springframework.boot.web.servlet.FilterRegistrationBean.REQUEST_WRAPPER_FILTER_MAX_ORDER;

public class AdminAndManagerMiddleware extends GenericFilterBean {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void initFilterBean() throws ServletException {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        this.jwtTokenUtil = webApplicationContext.getBean(JwtTokenUtil.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("executed adminAndManagerMiddleware");
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse rep= (HttpServletResponse)servletResponse;
        String header = req.getHeader(HEADER_STRING);
        String authToken = null;

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX, "");
            String role = jwtTokenUtil.getRole(authToken);
            if (role.equals("admin") || role.equals("manager")) {
                filterChain.doFilter(req, rep);
            } else {
                rep.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }else{
            rep.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
