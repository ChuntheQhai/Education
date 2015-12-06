package servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import cart.*;
import config.CurrencyStatus;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookCashierServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        response.setContentType("text/html");
        String contextPath = request.getContextPath();
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Cashier</title></head><body>");
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
         // Go back to catalog
         out.println("<p> &nbsp; <p><strong><a href='" +response.encodeURL(
                 contextPath+"/BookCatalog") +"'>Back to Catalog</a>" );                

        try {
            // Print out the total and the form for the user
            out.println("<p>Your total purchase amount is:<strong>"+CurrencyStatus.getInstance().getCurrencyStatus()+"&nbsp;" +
                    cart.getTotal() + "</strong><p>To purchase the items in your shopping cart, please provide us with the following information:<form action='" +
                    response.encodeURL(contextPath+ "/BookReceipt") +
                    "' method='post'><table summary='layout'><tr>" +
                    "<td><strong>Name:</strong></td>" +
                    "<td><input type='text' name='cardname'" +
                    "value='Tong Sam Pah' size='19'></td></tr>" +
                    "<tr><td><strong>Credit Card Number:</strong></td>" +
                    "<td><input type='text' name='cardnum' " +
                    "value='xxxx xxxx xxxx xxxx' size='19'></td></tr>" +
                    "<tr><td></td><td><input type='submit' value='Submit Information'></td></tr></table>" +
                    "</form></body></html>");
        } catch (Exception ex) {
            Logger.getLogger(BookCashierServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.close();
    }
    @Override
    public String getServletInfo() {
        return "Takes the user's name and credit card number so that the user can buy the books.";
    }
}
