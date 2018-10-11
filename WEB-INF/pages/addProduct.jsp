<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add product</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>

<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />

<div class="page-title">Add Product</div>
<br/>

<%--@elvariable id="deleteForm" type=""--%>
<form method="POST" action="${pageContext.request.contextPath}/addProduct?code=${productInfo.code}">
    <table style="text-align:left;">
        <tr>
            <td>Add product ${productInfo.name}, numbers: </td>
        <tr>
            <td><input type="text" name="numbers"/>
        </tr>

        <tr>
            <%--<td>&nbsp;</td>--%>
            <td><input type="submit" value="Add" /> <input type="reset" value="Reset" /></td>
        </tr>

    </table>
</form>




<jsp:include page="_footer.jsp" />

</body>
</html>