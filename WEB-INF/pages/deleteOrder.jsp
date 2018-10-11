<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>DeleteOrder</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>

<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />

<div class="page-title">Request on Delete</div>


<%--@elvariable id="deleteForm" type=""--%>
<form method="POST" action="${pageContext.request.contextPath}/deleteOrder?orderId=${orderForDelete.id}">
    <table style="text-align:left;">
        <tr>
            <td>Delete order ${orderForDelete.orderNum}?</td>

        <tr>
            <td>&nbsp;</td>
            <td><input type="submit" value="OK" name="delete"/>
            <td><input type="submit" value="NO" name="delete"/>
        </tr>

    </table>
</form>




<jsp:include page="_footer.jsp" />

</body>
</html>