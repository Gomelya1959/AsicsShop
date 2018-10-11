<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>

    <style>
        body {
            background-image: url("images/night.jpg");
        }
    </style>

    <meta charset="UTF-8">
    <title>RequestOnDelete</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>

<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />

<div class="page-title">Request on Delete</div>


<%--@elvariable id="deleteForm" type=""--%>
<form:form modelAttribute="deleteForm" method="POST" action="${pageContext.request.contextPath}/delete">
    <table style="text-align:left;">
        <tr>
            <td>Delete ${deleteForm.name} ?</td>
        </tr>
        <td>
            <input type="radio" name="delete" value="1">Yes</input>
            <input type="radio" name="delete" value="0" checked>No</input>
        </td>

        </tr>

        <tr>
            <td>&nbsp;</td>
            <td><input type="submit" value="Submit" />
        </tr>

    </table>
</form:form>




<jsp:include page="_footer.jsp" />

</body>
</html>