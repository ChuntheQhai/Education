import database.BookDBAO;
import javax.servlet.*;
public final class ContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event)  {
        ServletContext context = event.getServletContext();
         try {
            context.setAttribute("bookDB", new BookDBAO());
        } catch (Exception ex) {
            throw new UnsupportedOperationException(
                    "Couldn't create BookDBAO object:" +ex.getMessage());
       }
    }
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        BookDBAO bookDB = (BookDBAO) context.getAttribute("bookDB");
        if (bookDB != null) bookDB.remove();
        context.removeAttribute("bookDB");
    }
}
