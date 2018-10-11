<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>

    <style>
        body {
            background: #c7b39b url("images/1.jpg");
        }
    </style>

    <meta charset="UTF-8">

    <title>Login</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>


<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />



<div class="page-title">Login (For Employee, Manager, Courier)</div>

<div class="login-container">

    <h3>Enter username and password</h3>
    <!-- /login?error=true -->
    <c:if test="${param.error == 'true'}">
        <div style="color: red; margin: 10px 0px;">

            Login Failed!!!<br /> Reason :
                ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}

        </div>
    </c:if>

    <div style="color: #4935ff; margin: 10px 0px;">

        <a href="${pageContext.request.contextPath}/registration">Registration</a>

    </div>

    <form method="POST"
          action="${pageContext.request.contextPath}/j_spring_security_check">
        <table>
            <tr>
                <td>User Name *</td>
                <td><input name="userName" value="${userName}"/></td>
            </tr>

            <tr>
                <td>Password *</td>
                <td><input type="password" name="password" /></td>
            </tr>

            <tr>
                <td>&nbsp;</td>
                <td><input type="submit" value="Login" /> <input type="reset"
                                                                 value="Reset" /></td>
            </tr>
        </table>
    </form>

    <span class="error-message">${error }</span>

</div>


<jsp:include page="_footer.jsp" />

</body>
</html>