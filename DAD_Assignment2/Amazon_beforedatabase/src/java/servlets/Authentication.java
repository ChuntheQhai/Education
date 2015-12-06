package servlets;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Authentication extends HttpServlet{
  private ServletConfig config;
  
  @Override
  public void init(ServletConfig config) throws ServletException{
    this.config=config;
  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException,IOException{
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession(); 
    Connection connection=null;
    response.setContentType("text/html");
    try {
        // Load the database driver
        Class.forName("com.mysql.jdbc.Driver");
 
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amazon", "root", "root123"); 
        PreparedStatement prepStmt = connection.prepareStatement("select password from usertable where userid = ? ");
        String userName = request.getParameter("user");
        if (userName==null) userName="";
        prepStmt.setString(1, userName);
        ResultSet rs = prepStmt.executeQuery();
        if (rs.next()){
            String password=rs.getString(1);
            if(password.equals(request.getParameter("password"))){
                prepStmt = connection.prepareStatement("select groupid from grouptable where userid = ? ");
                prepStmt.setString(1, userName);
                //ResultSet rsG = prepStmt.executeQuery();
                //String groups="";
                //while (rsG.next()){
                //    if(groups.length()>0) groups += ",";
                //    groups += rsG.getString(1);
                //}
                //out.println("User Authenticated under Groups:"+groups);
                //rsG.close();
                session.setAttribute("user",userName);
                response.sendRedirect(request.getRequestURI());       
            }
            else{
                out.println("Wrong password for user '"+userName+"'!");
            }
        }
        else{
            out.println("User '"+userName+"' not found!");
        }
        rs.close ();
    }
    catch(Exception e){
        out.println("Exception is ;"+e);
    }
  }
} 