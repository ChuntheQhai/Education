package servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import database.*;
import exception.*;

public class BookStoreServlet extends HttpServlet {
    private BookDBAO bookDB;
    @Override
    public void init() throws ServletException {
        bookDB = (BookDBAO) getServletContext().getAttribute("bookDB");
        if (bookDB == null) throw new UnavailableException("Couldn't get database.");
    }
    @Override
    public void destroy() { bookDB = null; }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html");
        response.setBufferSize(8192);
        String contextPath = request.getContextPath();
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Amazon.COM</title></head><body>");
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
        try {
            String bookOfTheDayID="101";
            bookDB = (BookDBAO) getServletContext().getAttribute("bookDB");            
            BookDetails bd = bookDB.getBookDetails(bookOfTheDayID);
            out.println("<b>Book of the day</b>:<a href='" +
                response.encodeURL(contextPath+"/BookDetails?Id="+bookOfTheDayID+"'>") + 
                bd.getTitle() +"</a><p><a href=\'" +response.encodeURL(contextPath+ "/BookCatalog") +
                "'><b>Start Shopping</b></a></font><br/>");
        } catch (BookNotFoundException ex) {
             out.println("<center><h1>HAHA"+ex.getMessage()+"</h1></center>");
       }
       finally{ 
           out.println("</body></html>");
           out.close();
       }
    }
    @Override
    public String getServletInfo() {return "The Amazon.COM BookStore Main Servlet";}
}
