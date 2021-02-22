package com.example.cinema.web.comand;

import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.UserServiceImpl;
import com.example.cinema.db.service.iface.UserService;
import com.example.cinema.web.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

class EditProfileCommand implements Command {
    private static final Logger LOG = Logger.getLogger(EditProfileCommand.class);
    private User user;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException {
        LOG.info("Start edit profile");
        LOG.info("Current URI -> " + request.getRequestURI());
        String editing = request.getParameter("editing");
        LOG.info("Editing operation -> " + editing);
        HttpSession session = request.getSession();
        user = (User) session.getAttribute("user");
        try {
            //editing - что именно надо изменять пользователю
            switch (editing.toUpperCase(Locale.ROOT)) {
                case "NAME":
                    changeName(request);
                    break;
                case "GENERAL":
                    changeGeneral(request);
                    break;
                case "PASSWORD":
                    changePassword(request);
                    break;
                case "COINS":
                    addCoins(request);
                    break;
                default:
                    return request.getContextPath() + Pages.PROFILE;
            }
        } catch (ServiceException e) {
            session.setAttribute("exception", e.getMessage());
            return request.getContextPath() + Pages.EDIT_JSP + editing;
        }
        LOG.info("Edit profile");
        return request.getContextPath() + Pages.PROFILE;
    }

    private void changeName(HttpServletRequest request) throws TransactionException,ServiceException {
        String firstName = request.getParameter("first_name").isEmpty() ? null : request.getParameter("first_name");
        String lastName = request.getParameter("last_name").isEmpty() ? null : request.getParameter("last_name");
        LOG.info("first name -> " + firstName + " last name -> " + lastName);
        UserService userService = new UserServiceImpl();
        user.setFirstName(firstName).setLastName(lastName);
        userService.updateUser(user);

    }

    private void changeGeneral(HttpServletRequest request) throws TransactionException, ServiceException {
        String phone = request.getParameter("phone").isEmpty() ? null : request.getParameter("phone");

        UserService userService = new UserServiceImpl();
        user.setPhoneNumber(phone);
        userService.updateUser(user);
    }

    private void addCoins(HttpServletRequest request) throws TransactionException, ServiceException {
        int coins = Integer.parseInt(request.getParameter("coins"));
        LOG.info("Top up " + coins + " coins");
        UserService userService = new UserServiceImpl();
        userService.addCoins(user.getId(), coins);
        user = userService.getUserById(user.getId());
    }

    private void changePassword(HttpServletRequest request) throws TransactionException, ServiceException {
        UserService userService = new UserServiceImpl();
        String oldPassword = request.getParameter("oldPassword");
        User userFromDB = userService.getUserById(user.getId());

        if (!User.check(oldPassword, userFromDB.getPassword())) {
            LOG.info("Password isn`t correct");
            throw new ServiceException("Current password is incorrect");
        }

        String newPassword = request.getParameter("newPassword");

        user.setPassword(User.getSaltedHash(newPassword));
        userService.updateUser(user);
    }
}
