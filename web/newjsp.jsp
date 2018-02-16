<%-- 
    Document   : newjsp
    Created on : Dec 2, 2016, 6:07:58 PM
    Author     : Michael Donny Kusuma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <% String[] nis = new String[3]; 
           nis[0] = "001"; 
           nis[1] = "002";
           nis[2] = "003";
        %>
        <h1>Hello World!</h1>
        <% for (int i = 0; i < nis.length; i++) { %>
        <input type="checkbox" name="nis" value=<%=nis[i]%>>001<br>
        <%}%>
    </body>
</html>
