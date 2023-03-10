<%-- 
    Document   : create_employee
    Created on : 12 feb 2023, 18:17:33
    Author     : Agustín Pacheco
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@page import="p2.zoofrontendcrud.auxiliar.TYPE_ENUM"%>
<c:import url="../sessionCheck.jsp"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Crear Empleado</title>
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/form.css"/>
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/style.css"/>
    </head>
    <body>
        <h1>Ingrese los datos del empleado</h1>
        <form method="post" class="create_form">
            <select class="form_input" name="type">
                <c:forEach var="type" items="${TYPE_ENUM.values()}">
                    <option>${type.name}</option>
                </c:forEach>
            </select>
            <input class="form_input" type="text" name="userName" placeholder="Nombre de Usuario" required>
            <input class="form_input" type="password" name="password" placeholder="Contraseña" required>
            <input class="form_input" type="text" name="name" placeholder="Nombre" required>
            <input class="form_input" type="text" name="address" placeholder="Direccion" required>
            <input class="form_input" type="number" name="phone" required>
            <input class="form_input" type="date" name="firstDay" required>
            <input class="form_button" type="submit">
        </form>
        <br/>
        <p>${errorMsg}</p>
    </body>
</html>
