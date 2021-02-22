package com.example.cinema.web.comand;

import com.example.cinema.db.exception.CommandException;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public final class CommandContainer {

    private static final Logger LOG = Logger.getLogger(CommandContainer.class);
    private static final Map<String, Command> commands = new HashMap<>();

    private CommandContainer() {}

    static {
        LOG.info("Start initializing command container");
        //команды авторизации
        commands.put("login" , new LoginCommand());
        commands.put("register", new RegisterCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("cookies", new AuthorizeCookieCommand());
        //команды создания страниц
        commands.put("createFilmsPage", new CreateFilmsPageCommand());
        commands.put("createFilmPage", new CreateFilmPageCommand());
        commands.put("createReceiptPage", new CreateReceiptPageCommand());
        commands.put("createSessionsPage", new CreateSessionsPageCommand());
        commands.put("createEditFilmPage", new CreateEditFilmPage());
        commands.put("createAdminSessionPage", new CreateAdminSessionPage());
        commands.put("createProfile", new CreateProfilePageCommand());
        commands.put("createTicketPanel", new CreateTicketPanelCommand());
        commands.put("createAddFilmPage", new CreateAddFilmPage());
        //команда работы с комментариями
        commands.put("addComment" , new AddCommentCommand());
        commands.put("deleteComment", new DeleteCommentCommand());
        //команда изменения данных пользователя
        commands.put("editProfile", new EditProfileCommand());
        //команда заказа билетов
        commands.put("orderTicket", new OrderTicketCommand());
        //команда работы на фильмом
        commands.put("addFilm", new AddFilmCommand());
        commands.put("editFilm", new EditFilmCommand());
        commands.put("deleteFilm", new DeleteFilmCommand());
        //команды работы над сеансом
        commands.put("addSession", new AddSessionCommand());
        commands.put("deleteSession" , new DeleteSessionCommand());
        commands.put("editSession" , new EditSessionCommand());

        LOG.info("Initializing container has been finished");
    }

    public static Command getCommand(String commandName) throws CommandException {
        LOG.info("Get command -> " + commandName);
        Command command = commands.get(commandName);

        if (command == null) {
            LOG.warn("Command isn`t found -> " + commandName);
            throw new CommandException("Command isn`t found");
        }
        LOG.info("Command " + commandName + " has been gotten");
        return command;
    }
}
