package servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class BannerServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        output(request, response);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException { 
        output(request, response);
    }
    private void output(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String path= request.getContextPath();
        PrintWriter out = response.getWriter();
        out.println(
            "<table border='1' width='100%'>"+
              "<tr><th width='100'><img src='"+response.encodeURL(path +"/images/logo.gif") +"'/></th>"+
                     "<th>AMAZON.COM</th>"+
                     "<th width='50'><a href='"+response.encodeURL(path +"/Admin")+"'>Admin</a></th>"+
                     "<th width='50'><img src='./images/books.jpg'/></th></tr></table>");
    }
}
