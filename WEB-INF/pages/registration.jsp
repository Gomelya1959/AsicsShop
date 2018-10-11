<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <title>Registration</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>


<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />

<c:if test="${not empty errorMessage }">
    </br>
    <div style="color: red">
            ${errorMessage}
    </div>
</c:if>


<div class="login-container">

    <h3>Enter name, username and password</h3>

    <form method="POST"
          action="${pageContext.request.contextPath}/registration">
        <table>
            <tr>
                <td>Name</td>
                <td><input name="name" value="${name}"/></td>
            </tr>

            <tr>
                <td>User Name</td>
                <td><input name="userName" value="${userName}"/></td>
            </tr>

            <tr>
                <td>Password (at least 5 characters, including letters and numbers, no more than 20 characters) </td>
                <td><input type="password" name="password" value="${password}"/></td>
            </tr>

            <tr>
                <td>Re - enter your password </td>
                <td><input type="password" name="repeatPassword" value="${repeatPassword}"/></td>
            </tr>

            <tr>
                <td>&nbsp;</td>
                <td><input type="submit" value="Registration" /> <input type="reset"
                                                                 value="Reset" /></td>
            </tr>
        </table>
    </form>


</div>


<jsp:include page="_footer.jsp" />

</body>
</html>