package com.example.cinema.web;

public final class Pages{
    private Pages() {
    }

    public static final String ERROR_404 = "/error_page/error404.jsp";
    public static final String ERROR_500 = "/error_page/error500.jsp";

    public static final String MAIN = "/controller?command=createFilmsPage";
    public static final String PROFILE = "/controller?command=createProfile";
    public static final String FILM = "/controller?command=createFilmPage&film_id=";
    public static final String SESSIONS = "/controller?command=createSessionsPage";
    public static final String AUTHORIZATION = "/controller?command=cookies";
    public static final String TICKET_ORDER_PANEL = "/controller?command=createTicketPanel&session_id=";


    public static final String ADMIN_SESSION_PAGE = "/controller?command=createAdminSessionPage&operation=";
    public static final String EDIT_FILM_PAGE = "/controller?command=createEditFilmPage&film_id=";
    public static final String ADD_FILM_PAGE = "/controller?command=createAddFilmPage";

    public static final String MAIN_JSP = "/page/main.jsp";
    public static final String PROFILE_JSP = "/page/profile.jsp";
    public static final String EDIT_JSP = "/page/edit.jsp?editing=";
    public static final String FILM_JSP = "/page/film.jsp";
    public static final String LOGGING_JSP = "/page/logging.jsp";
    public static final String REGISTRATION_JSP = "/page/registration.jsp";
    public static final String SESSIONS_JSP = "/page/sessions.jsp";
    public static final String TICKET_ORDER_JSP = "/page/ticketOrder.jsp";
    public static final String RECEIPT_JSP = "/page/orderReceipt.jsp";

    public static final String ADMIN_SESSION_JSP = "/page/admin/adminSession.jsp";
    public static final String EDIT_FILM_JSP = "/page/admin/editFilm.jsp";
    public static final String ADD_FILM_JSP = "/page/admin/addFilm.jsp";
}
