package com.example.cinema.web.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebFilter(filterName = "Encoding",
        urlPatterns = "/*",
        initParams = @WebInitParam(name = "encoding",value = "UTF-8")
)
public class EncodingFilter implements Filter {
    private String encoding;
    private static final Logger LOG = Logger.getLogger(EncodingFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOG.info("Encoding filter starts");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOG.info("Request URI -> " + request.getRequestURI());
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String currentEncoding = request.getCharacterEncoding();
        if (currentEncoding == null) {
            LOG.info("Set encoding ->" + encoding);
            request.setCharacterEncoding(encoding);
            response.setCharacterEncoding(encoding);
            LOG.info("Encoding has been set");
        }
        LOG.info("Encoding filter is finished");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
