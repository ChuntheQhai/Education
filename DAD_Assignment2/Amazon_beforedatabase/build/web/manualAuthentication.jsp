      
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" %>
<html xmlns="[http://www.w3.org/1999/xhtml]" xml:lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Manual Amazon Admin. Login Page</title>
    </head>
    <body>
      <% getServletContext().getRequestDispatcher("/Banner").include(request, response); %>
      Please enter your credential here:
      <br>
      <br>
      <form name="frm" action="./Authentication" method="Post" >
        Name:&nbsp;&nbsp;&nbsp;<input type="text" name="user" value=""/>
        Password:<input type="text" name="password" value=""/>
        &nbsp;&nbsp;&nbsp;<input type="submit" value="Submit" />
      </form>
    </body>
</html>
     