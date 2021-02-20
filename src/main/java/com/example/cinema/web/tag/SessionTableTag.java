package com.example.cinema.web.tag;

import com.example.cinema.db.entity.Ticket;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.service.TicketServiceImpl;
import com.example.cinema.db.service.UserServiceImpl;
import com.example.cinema.db.service.iface.TicketService;
import com.example.cinema.db.service.iface.UserService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.*;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SessionTableTag extends SimpleTagSupport {
    public Collection<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Collection<Ticket> tickets) {
        this.tickets = tickets;
    }
    private static final Logger LOG = Logger.getLogger(SessionTableTag.class);

    private Collection<Ticket> tickets;
    @Override
    public void doTag() throws JspException, IOException {
        LOG.info("SessionTableTag starts with tickets: "+ tickets);
        JspWriter out = getJspContext().getOut();
        try {
            for (Ticket ticket : tickets) {
                out.print("<tr>");
                out.print("      <td> " + (ticket.getId() == -1? "new" : ticket.getId())  + "</td>\n");
                out.print("      <td>\n");
                out.print("            " + ticket.getSession().getFilm().getTitle());
                out.print("      </td>\n");
                out.print("      <td>" + ticket.getPlaceRow() + "</td>\n");
                out.print("      <td>" + ticket.getPlaceColumn() + "</td>\n");
                out.print("      <td>" + ticket.getOrderDate() + "</td>\n");
                out.print("      <td>\n");
                out.print((ticket.getSession().isThreeDSupporting()) ? "+" : "-");
                out.print("      </td>\n");
                out.print("      <td>" + ticket.getPrice() + "</td>\n");
                out.print("<tr>");
            }
        } catch (IOException e) {
            LOG.error("Cannot create SessionTableTag " + e);
        }
        LOG.info("SessionTableTag created");
    }
}
