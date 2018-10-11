<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Courier</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>

<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />

<div class="page-title">Courier</div>

<c:if test="${not empty errorMessage }">
    <div class="error-message">
            ${errorMessage}
    </div>
</c:if>

<%--@elvariable id="courierForm" type=""--%>
<form:form modelAttribute="courierForm" method="POST">
    <table style="text-align:left;">
        <tr>
            <td>ID *</td>
            <td style="color:red;">
                <c:if test="${not empty courierForm.id}">
                    <%--<form:hidden path="id"/>--%>
                    <%--${courierForm.id}--%>
                </c:if>
                <c:if test="${empty courierForm.id}">
                    <form:input path="id" />
                    <form:hidden path="newCourier" />
                </c:if>
            </td>
            <td><form:errors path="id" class="error-message" /></td>
        </tr>

        <tr>
            <td>Name *</td>
            <td><form:input path="name" /></td>
            <td><form:errors path="name" class="error-message" /></td>
        </tr>

        <tr>
            <td>Phone *</td>
            <td><form:input path="phone" /></td>
            <td><form:errors path="phone" class="error-message" /></td>
        </tr>
        <tr>
            <td>Courier_name *</td>
            <td><form:input path="user_name" /></td>
            <td><form:errors path="user_name" class="error-message" /></td>
        </tr>

        <tr>
            <td>&nbsp;</td>
            <td><input type="submit" value="Submit" /> <input type="reset"
                                                              value="Reset" /></td>
        </tr>
    </table>
</form:form>




<jsp:include page="_footer.jsp" />

</body>
</html>