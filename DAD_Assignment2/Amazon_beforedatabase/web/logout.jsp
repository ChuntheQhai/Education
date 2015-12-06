<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html xmlns="[http://www.w3.org/1999/xhtml]" xml:lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Amazon Admin. logout page</title>
    </head>
    <body>
        <% 
             getServletContext().getRequestDispatcher("/Banner").include(request, response);    
             //request.getSegetSession(true).invalidate();
             request.logout();
        %>
        <h1>You have been logged out</h1>
	<p>
                    <a href="/Admin">Log in</a> again.
	</p>
    </body>
</html>