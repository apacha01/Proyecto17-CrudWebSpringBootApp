<%-- 
    Document   : adminMenu
    Created on : 9 feb 2023, 19:17:10
    Author     : Agustín Pacheco
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<c:import url="../sessionCheck.jsp"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Menu de Opciones (Administrador)</title>
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/style.css"/>
    </head>
    <body>
        <h1>Hola <% out.print(session.getAttribute("employeeUserName"));%>, bienvenido al sistema.</h1>
        <div class="link-container">
            <a class="simple-link" href='/login?cerrar=true'>Cerra Sesion</a>
            <a class="simple-link" href="/empleado/empleados">Empleados</a>
            <a class="simple-link" href="/especie/especies">Especies</a>
            <a class="simple-link" href="/zona/zonas">Zonas</a>
            <a class="simple-link" href="/habitat/habitats">Habitats</a>
            <a class="simple-link" href="/itinerario/itinerarios">Itinerarios</a>
        </div>
    </body>
</html>
