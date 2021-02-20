package com.example.cinema.web.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebFilter(filterName = "Locale", urlPatterns = "/*")
public class SessionLocaleFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(SessionLocaleFilter.class);
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOG.info("SessionLocaleFilter starts");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        Cookie[] cookie = req.getCookies();
        Arrays.stream(cookie)
                .filter(c -> "lang".equals(c.getName()))
                .findFirst()
                .ifPresent(lang -> {
                    LOG.info("Getting locale from cookie -> " + lang);
                    req.getSession().setAttribute("lang", lang.getValue());
                    LOG.info("Lang " + lang + " has been set to session");
                });

        if (req.getParameter("lang") != null) {
            String lang = req.getParameter("lang");
            LOG.info("Getting locale from request" + lang);
            req.getSession().setAttribute("lang", lang);
            resp.addCookie(new Cookie("lang", lang));
        }
        LOG.info("SessionLocaleFilter is finished");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
