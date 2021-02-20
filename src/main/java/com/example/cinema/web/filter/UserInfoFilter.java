package com.example.cinema.web.filter;


import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.UserServiceImpl;
import com.example.cinema.db.service.iface.UserService;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "userInfo", urlPatterns = "/*")
public class UserInfoFilter implements Filter {

    private static final UserService userService = new UserServiceImpl();
    private static final Logger LOG = Logger.getLogger(UserInfoFilter.class);
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOG.info("UserInfoFilter starts");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        LOG.info("Sessions user ->" + user);
        if (user != null) {
            try {
                user = userService.getUserById(user.getId());
                LOG.info("Updating user to " + user);
                session.setAttribute("user",user);
                LOG.info("User has been set to session");
            } catch (ServiceException | TransactionException e) {
                LOG.error("Can`t set user to session: " + e);
                throw new ServletException(e);
            }
        }
        LOG.info("UserInfoFilter finishes");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
