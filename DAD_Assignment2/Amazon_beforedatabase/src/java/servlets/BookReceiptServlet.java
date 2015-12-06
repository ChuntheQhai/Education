package servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import cart.ShoppingCart;
import database.*;
import exception.OrderException;

public class BookReceiptServlet extends HttpServlet {
    private BookDBAO bookDB;
    @Override
    public void init() throws ServletException {
        bookDB = (BookDBAO) getServletContext().getAttribute("bookDB");
        if (bookDB == null)  throw new UnavailableException("Couldn't get database.");
    }
    @Override
    public void destroy() { bookDB = null;}

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean orderCompleted = true;
        HttpSession session = request.getSession(true);
        String contextPath = request.getContextPath();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        orderCompleted = false;
        // Update the inventory
        try {
            bookDB.buyBooks(cart);
        } catch (OrderException e) {
            System.err.println(e.getMessage());
            orderCompleted = false;
        }
        orderCompleted = true;

        // Payment received -- invalidate the session
        session.invalidate();

        // set content type header before accessing the Writer
        response.setContentType("text/html");
        response.setBufferSize(8192);

        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Receipt</title></head><body>");

        getServletContext().getRequestDispatcher("/Banner").include(request, response);

        if (orderCompleted) {
            out.println("<h3>"+request.getParameter("cardname")+"Thank you for purchasing your books from us.");
        } else {
            out.println("<h3>Your order could not be completed due to insufficient inventory.");
        }
        out.println("<p><strong><a href='" +response.encodeURL(contextPath+ "/Bookstore")+
                "'>Continue Shopping</a> &nbsp; &nbsp; &nbsp;</body></html>");
        out.close();
    }
    @Override
    public String getServletInfo() {
        return "Updates the inventory, invalidates the user session, thanks the user for the order.";
    }
}
