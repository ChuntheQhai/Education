package servlets;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String contextPath = request.getContextPath();
        PrintWriter out = response.getWriter();
        try {
            out.println("<html><head><title>Bookstore Admin Module</title></head><body>");
            getServletContext().getRequestDispatcher("/Banner").include(request, response);
            out.println("<p><strong><a href='"+contextPath+ "/logout.jsp'>Log out</a></strong>");
            out.println("<p><h1>Administrator Options:</h1>");
            out.println("<p><strong><a href='"+contextPath+ "/AdminDelete'>Delete Book</a></strong>");
            out.println("<p><strong><a href='"+contextPath+ "/AdminBookEntry?Mode=New'>Add New Book</a></strong>");
            out.println("<p><strong><a href='"+contextPath+ "/AdminUpdate'>Update Existing Book</a></strong>");
            out.println("</body></html>");
        } finally { 
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
    * Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
    * Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
    * Returns a short description of the servlet.
    */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
