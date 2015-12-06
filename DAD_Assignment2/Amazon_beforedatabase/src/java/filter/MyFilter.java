package filter;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

//@WebFilter(filterName = "myFilter", urlPatterns = {"*.jpg,*.gif"})
public class MyFilter implements Filter {
   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
      //String path = ((HttpServletRequest) request).getServletPath();

      //if (excludeFromFilter(path)) chain.doFilter(request, response);
      //else {}
      String uri = ((HttpServletRequest) request).getRequestURI();
     //if (excludeFromFilter(uri)) {
       //chain.doFilter(request, response); // Goes to container's default servlet.
     //} else {
        //request.getRequestDispatcher(uri).forward(request, response);
     //} 
     //request.getRequestDispatcher(uri).forward(request, response);
   }

   private boolean excludeFromFilter(String path) {
      if (path.endsWith(".jpg")) return true; // add more page to exclude here
      if (path.endsWith(".gif")) return true; // add more page to exclude here
      return false;
   }

    @Override
    public void init(FilterConfig fc) throws ServletException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void destroy() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}