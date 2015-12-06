package servlets;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import database.*;
import cart.*;
import config.Config;
import config.CurrencyStatus;
import couponcodeinterface.CouponCode;
import exception.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookShowCartServlet extends HttpServlet {
    private BookDBAO bookDB;
    private boolean isCouponCodeUsed;
    private CouponCode couponRMI;
    
    @Override
    public void init() throws ServletException {
        bookDB = (BookDBAO) getServletContext().getAttribute("bookDB");
        if (bookDB == null) throw new UnavailableException("Couldn't get database.");
    }
    @Override
    public void destroy() {bookDB = null;}
    
    private String getParamWithoutNull(HttpServletRequest request,String param){
         String s=request.getParameter(param);
         return (s==null)?"":s;
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        
        Registry registry = LocateRegistry.getRegistry("localhost",1099);
        
        try {      
          couponRMI = (CouponCode) registry.lookup(Config.RMI_ID);
        }
        catch (RemoteException ex) {
          System.err.println("Remote object threw exception " + ex);
        }
        catch (NotBoundException ex) {
          System.err.println("Could not find the requested remote object on the server");
        }
        
        
        
        String sCouponCode = getParamWithoutNull(request,"couponCode");
        
        if(sCouponCode.equals("")){
            isCouponCodeUsed = false;
        }else{
            isCouponCodeUsed = true;
            if(couponRMI.isCouponCodeVerified(sCouponCode)){
                System.out.println("Coupon is working and discount rate: "+couponRMI.getDiscountRate(sCouponCode));
            }else{
              System.out.println("Coupon "+sCouponCode+" is not working.");
            }
        }
        
       
        
        response.setContentType("text/html");
        response.setBufferSize(8192);
        String contextPath = request.getContextPath();
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Shopping Cart</title></head>");
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
        String bookId = request.getParameter("Remove");
        BookDetails bd;
        if (bookId != null) {
            try {
                bd = bookDB.getBookDetails(bookId);
                cart.remove(bookId);
                out.println("<font color='red' size='+2'>You just removed <strong>" +
                    bd.getTitle() + "</strong> <br> &nbsp; <br></font>");
            } catch (BookNotFoundException ex) {
                response.reset();
                throw new ServletException(ex);
            }
        } else if (request.getParameter("Clear") != null) {
            cart.clear();
            out.println("<font color='red' size='+2'><strong>You just cleared your shopping cart!</strong> <br>&nbsp; <br> </font>");
        }

        // Print a summary of the shopping cart
        int num = cart.getNumberOfItems();
        if (num > 0) {
            out.println("<font size='+2'>Your shopping cart contains " + num +
                ((num == 1) ? " item":" items") + "</font><br>&nbsp;");

            // Return the Shopping Cart
            out.println("<table summary='layout'><tr>" +
                "<th align='left'>Quantity</th>" + 
                "<th align='left'>Title</th>" + 
                "<th align='left'>Price</th></tr>");

            Iterator i = cart.getItems().iterator();
            while (i.hasNext()) {
                ShoppingCartItem item = (ShoppingCartItem) i.next();
                bd = (BookDetails) item.getItem();
                try {
                    out.println("<tr>" +
                            "<td align='right' bgcolor='white'>" + item.getQuantity() + "</td>" +
                            "<td bgcolor='#ffffaa'><strong><a href='" +
                            response.encodeURL(contextPath+"/BookDetails?Id=" + bd.getId()) + "'>" +
                            bd.getTitle() + "</a></strong></td>" +
                            "<td bgcolor='#ffffaa' align='right'>"+CurrencyStatus.getInstance().getCurrencyStatus()+"&nbsp;" + bd.getPrice() +"</td>" +
                            "<td bgcolor='#ffffaa'><strong><a href='" + response.encodeURL(contextPath+
                                    "/BookShowCart?Remove=" + bd.getId()) + "'>Remove Item</a></strong></td></tr>");
                } catch (Exception ex) {
                    Logger.getLogger(BookShowCartServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            
            if(isCouponCodeUsed && couponRMI.isCouponCodeVerified(sCouponCode)){
                try {
                    // Print the total at the bottom of the table
                    out.println("<tr>"
                                    + "<td colspan='5' bgcolor='white'><br></td>"
                              + "</tr>" 
                            + "<tr>"
                                    + "<td colspan='2' align='right' bgcolor='white'>Saved</td>"
                                    +"<td bgcolor='#ffffaa' align='right' style='color:green'>"+CurrencyStatus.getInstance().getCurrencyStatus()+"&nbsp;" + Math.round((cart.getTotal() - cart.getTotal()*couponRMI.getDiscountRate(sCouponCode))*100.0)/100.0 + "</td>" 
                            + "</tr>"
                               + "<tr>"
                                    + "<td colspan='2' align='right' bgcolor='white'>Subtotal</td>"
                                    +"<td bgcolor='#ffffaa' align='right''>"+CurrencyStatus.getInstance().getCurrencyStatus()+"&nbsp;" + Math.round(cart.getTotal()*couponRMI.getDiscountRate(sCouponCode)*100.0)/100.0 + "</td>"
                            +"</td><td><br></td></tr></table>");
                } catch (Exception ex) {
                    Logger.getLogger(BookShowCartServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }else {
                 try {
                // Print the total at the bottom of the table
                out.println("<tr><td colspan='5' bgcolor='white'><br></td></tr>" +
                        "<tr><td colspan='2' align='right' bgcolor='white'>Subtotal</td>" +
                        "<td bgcolor='#ffffaa' align='right'>"+CurrencyStatus.getInstance().getCurrencyStatus()+"&nbsp;" + cart.getTotal() + "</td>" +
                        "</td><td><br></td></tr></table>");
                } catch (Exception ex) {
                    Logger.getLogger(BookShowCartServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           
            
            
            if(!isCouponCodeUsed){
                 String couponCode = request.getParameter("newCouponCode");

                if(couponCode != null){
                    response.sendRedirect(response.encodeURL(contextPath+"/BookShowCart?couponCode="+couponCode));
                }
            }
           
            
            // Discount table
            if(isCouponCodeUsed && couponRMI.isCouponCodeVerified(sCouponCode)){
                out.println("<tr><td colspan='5' bgcolor='white'><br></td></tr>" + 
                    "<tr><td colspan='2' align='right' bgcolor='white'>Discount Code : </td>" +
                           "<td bgcolor='#ffffaa' align='right'><input align='right' type='text' value="+sCouponCode+" disabled></input>"
                    + "<a href="+ response.encodeURL(contextPath+"/BookShowCart") + ">Cancel</a>"
                          + "</td><td><br></td></tr></table>");
            }else if (isCouponCodeUsed && !couponRMI.isCouponCodeVerified(sCouponCode)){
                out.println("<form action="+response.encodeURL(contextPath+"/BookShowCart")+" method='GET'><tr><td colspan='5' bgcolor='white'><br></td></tr>" + 
                    "<tr><td colspan='2' align='right' bgcolor='white'>Discount Code : </td>" +
                           "<td bgcolor='#ffffaa' align='right'><input name='newCouponCode' align='right' type='text'></input>"
                    + "<input type='submit' value='Apply'/><h4 style='color:red'>Coupon '"+sCouponCode+"' is not working!, please try another.</h4></td>" + 
                           "</td><td><br></td></tr></form></table>");
            }
            else{     
                out.println("<form action="+response.encodeURL(contextPath+"/BookShowCart")+" method='GET'><tr><td colspan='5' bgcolor='white'><br></td></tr>" + 
                    "<tr><td colspan='2' align='right' bgcolor='white'>Discount Code : </td>" +
                           "<td bgcolor='#ffffaa' align='right'><input name='newCouponCode' align='right' type='text'></input>"
                    + "<input type='submit' value='Apply'/></td>" + 
                           "</td><td><br></td></tr></form></table>");
            }
            

            // Where to go and what to do next
            out.println("<p> &nbsp; <p><strong><a href='" +
                response.encodeURL(contextPath+ "/BookCatalog") +
                "'>Continue Shopping</a> &nbsp; &nbsp; &nbsp;<a href='" +
                response.encodeURL(contextPath+ "/BookCashier") +
                "'>Check Out</a> &nbsp; &nbsp; &nbsp;" + "<a href='" +
                response.encodeURL(contextPath+"/BookShowCart?Clear=clear") + 
                "'>Clear Cart</a></strong>");
        } else {// Shopping cart is empty!
            out.println("<font size='+2'>Your cart is empty.</font><br> &nbsp; <br><center><a href='" +
                response.encodeURL(contextPath+ "/BookCatalog") +"'>Back to the Catalog</a></center>");
        }
        out.println("</body> </html>");
        out.close();
    }

   
    
    
    @Override
    public String getServletInfo() {
        return "Returns information about the books that the user is in the process of ordering.";
    }
}
