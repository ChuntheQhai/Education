package servlets;
import com.giftx.cx.ChangeCurrencyService_Service;
import config.CurrencyStatus;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import database.*;
import exception.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebServiceRef;
public class BookDetailsServlet extends HttpServlet {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/ChangeCurrencyService/ChangeCurrencyService.wsdl")
    private ChangeCurrencyService_Service service;
    private BookDBAO bookDB;
    @Override
    public void init() throws ServletException {
        bookDB = (BookDBAO) getServletContext().getAttribute("bookDB");
        if (bookDB == null) throw new UnavailableException("Couldn't get database.");
    }
    @Override
    public void destroy() {bookDB = null;}
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html");
        response.setBufferSize(8192);
        PrintWriter out = response.getWriter();
        String bookId = request.getParameter("Id");
        out.println("<html><head><title>Book Details:" +bookId+ "</title></head><body>");
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
        
        
        if (bookId != null) {
            try {
                BookDetails bd = bookDB.getBookDetails(bookId);
                out.println("<br/>"+
                    "<table border='1'>"+
                    "<tr><th colspan='2'>Book Information</th><tr>"+
                    "<tr><td>ID</td><td>"+bookId+"</td></tr>"+
                    "<tr><td>Title</td><td>"+bd.getTitle()+"</td></tr>"+
                    "<tr><td>Author</td><td>"+bd.getAuthor()+"</td></tr>"+
                    "<tr><td>Year</td><td>"+bd.getYear()+"</td></tr>"+
                    "<tr><td>Price</td><td>"+CurrencyStatus.getInstance().getCurrencyStatus()+"&nbsp;"+bd.getPrice()+"</td></tr></table>");
                
            // Go back to catalog
            out.println("<p> &nbsp; <p><strong><a href='" +
                response.encodeURL(request.getContextPath() + "/BookCatalog") +
                "'>Continue Shopping</a>" );                
            } catch (BookNotFoundException ex) {
                out.println("<center><h1>Book Not Found</h1></center>");
            } catch (Exception ex) {
                Logger.getLogger(BookDetailsServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally{
                out.println("</body></html>");
                out.close();
            }
        }        
    }
    @Override
    public String getServletInfo() {return "Returns information about book";}

 
}
