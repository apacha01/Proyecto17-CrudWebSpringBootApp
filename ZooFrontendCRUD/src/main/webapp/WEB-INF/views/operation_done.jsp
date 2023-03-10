<%-- 
    Document   : operation_done
    Created on : 12 feb 2023, 18:29:49
    Author     : Agustín Pacheco
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Listo</title>
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/style.css"/>
    </head>
    <body>
        <h1>¡Operacion realizada con éxito!</h1>
        <c:if test="${not empty msgs}">
            <h4>Aclaraciones/Advertencias:</h4>
            <ul>
                <c:forEach var="msg" items="${msgs}">
                    <li>
                        ${msg}
                    </li>
                </c:forEach>
            </ul>
        </c:if>
        <a class="simple-link" href="/menu_admin">Volver al menu</a>
    </body>
</html>
