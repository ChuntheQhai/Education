package servlets;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import database.*;
import cart.*;
import com.giftx.cx.ChangeCurrencyService_Service;
import config.Config;
import config.CurrencyStatus;
import exception.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.ws.WebServiceRef;

public class BookCatalogServlet extends HttpServlet {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/ChangeCurrencyService/ChangeCurrencyService.wsdl")
    private ChangeCurrencyService_Service service;
    private BookDBAO bookDB;
    private Connection con;
    
    @Override
    public void init() throws ServletException {
        try {
            bookDB = (BookDBAO) getServletContext().getAttribute("bookDB");
            if (bookDB == null) throw new UnavailableException("Couldn't get database.");
            
           
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/BookDB");
            con = ds.getConnection();
            
            String cStatus = "";
            PreparedStatement getStmt = con.prepareStatement("select currency from books");
  
            ResultSet resultSet = getStmt.executeQuery();
            
            
            while(resultSet.next()){
                cStatus = resultSet.getString(1);
            }
            System.out.println("##1 Debug: "+cStatus);
            
            switch(cStatus){
                case "USD":
                    cStatus = Config.USD_SIGN;
                    
                    break;
                case "MYR":
                    cStatus = Config.MYR_SIGN;
                    
                    break;
                default:
                    cStatus = Config.USD_SIGN;
                    break;
            }
            
            CurrencyStatus.getInstance().setCurrencyStatus(cStatus);
            System.out.println("##2 Debug: " + CurrencyStatus.getInstance().getCurrencyStatus());

        } catch (SQLException ex) {
            Logger.getLogger(BookCatalogServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(BookCatalogServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BookCatalogServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void destroy() {bookDB = null;}
    
    
    private String getParamWithoutNull(HttpServletRequest request,String param){
         String s=request.getParameter(param);
         return (s==null)?"":s;
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, BookNotFoundException {
        HttpSession session = request.getSession(true);
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        String sMode=getParamWithoutNull(request,"currency");
        
        
        
        response.setContentType("text/html");
        response.setBufferSize(8192);
        String contextPath = request.getContextPath();
        PrintWriter out = response.getWriter();
       
        
        out.println("<html><head><title>Book Catalog</title></head><body>");
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
        String bookId = request.getParameter("Id");
        if (bookId != null) {
            try {
                BookDetails book = bookDB.getBookDetails(bookId);
                cart.add(bookId, book);
                out.println("<p><h3><font color='red'>You added <i>" + book.getTitle() +
                    "</i> to your shopping cart.</font></h3>");
            } catch (BookNotFoundException ex) {
                response.reset();
                throw ex;
            }
        }
        //Give the option of checking cart or checking out if cart not empty
        if (cart.getNumberOfItems() > 0) {
            out.println("<p><strong><a href='" +
                response.encodeURL(contextPath+ "/BookShowCart") +
                "'>Check Shopping Cart</a>&nbsp;&nbsp;&nbsp;<a href='" +
                response.encodeURL(contextPath+ "/BookCashier")+"'>Buy Your Books</a></p></strong>");
        }
        // Always prompt the user to buy more -- get and show the catalog
        out.println("<h3>Please choose from our selections:</h3><center><table border='1' summary='layout'>");
        out.println("<h2>Choose your currency rate:</h2>"
                + "<a href="+ response.encodeURL(contextPath+"/BookCatalog?currency=USD") + " style='margin-right:10px;margin-bottom:10px'>USD</a>"
                + "<a href="+ response.encodeURL(contextPath+"/BookCatalog?currency=MYR") + ">MYR</a>");
        
        try {
            Collection coll = bookDB.getBooks();
            Iterator i = coll.iterator();
            if(!"RM".equals(CurrencyStatus.getInstance().getCurrencyStatus())) {
                if(sMode.equals("USD")){
                    CurrencyStatus.getInstance().setCurrencyStatus(Config.USD_SIGN);
                }
                if(sMode.equals("MYR")){
                    CurrencyStatus.getInstance().setCurrencyStatus(Config.MYR_SIGN);
                }
                while (i.hasNext()) {
                    BookDetails book = (BookDetails) i.next();
                    bookDB.updateBookCurrency(book.getId(),sMode);
                }
            }
            
            if(!Config.USD_SIGN.equals(CurrencyStatus.getInstance().getCurrencyStatus())) {
                if(sMode.equals("USD")){
                    CurrencyStatus.getInstance().setCurrencyStatus(Config.USD_SIGN);
                }
                if(sMode.equals("MYR")){
                    CurrencyStatus.getInstance().setCurrencyStatus(Config.MYR_SIGN);
                }
                while (i.hasNext()) {
                    BookDetails book = (BookDetails) i.next();
                    bookDB.updateBookCurrency(book.getId(),sMode);
                }
            }
        } catch (BooksNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(BookCatalogServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Collection coll = bookDB.getBooks();
            Iterator i = coll.iterator();
            while (i.hasNext()) {
                BookDetails book = (BookDetails) i.next();
                bookId = book.getId();
                //Print out info on each book in its own two rows
                out.println("<tr><td bgcolor='#ffffaa'><a href='" +
                    response.encodeURL(contextPath+"/BookDetails?Id=" + bookId) + 
                    "'> <strong>" +book.getTitle()+"&nbsp;</strong></a></td>" +
                    "<td bgcolor='#ffffaa' rowspan='2'>"+ CurrencyStatus.getInstance().getCurrencyStatus()
                    +"&nbsp;" + Math.round(book.getPrice()*100.0)/100.0 +
                    "&nbsp; </td><td bgcolor='#ffffaa' rowspan='2'><a href='" +
                    response.encodeURL(contextPath+"/BookCatalog?Id=" + bookId) + 
                    "'> &nbsp;Add to Cart&nbsp;</a></td></tr>" +
                    "<tr><td bgcolor='white'>&nbsp; &nbsp;by&nbsp;<em>" + book.getAuthor()+"</em></td></tr>");
                        
            }
        } catch (BooksNotFoundException ex) {
            response.reset();
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(BookCatalogServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.println("</table></center></body></html>");
        out.close();
    }
    @Override
    public String getServletInfo() {return "Adds books to the user's shopping cart and prints the catalog.";}

    private Double changeCurrency(double currencyDollar, java.lang.String currencyType) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        com.giftx.cx.ChangeCurrencyService port = service.getChangeCurrencyServicePort();
        return port.changeCurrency(currencyDollar, currencyType);
    }
}
