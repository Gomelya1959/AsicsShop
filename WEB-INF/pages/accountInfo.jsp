<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <title>Account Info</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>


<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />

<div class="page-title">Account Info</div>

<div class="account-container">


    <ul>
        <li>User Name: ${pageContext.request.userPrincipal.name}</li>
        <li>Role:
            <ul>
                <c:forEach items="${userDetails.authorities}" var="auth">
                    <li>${auth.authority }</li>
                </c:forEach>
            </ul>
        </li>
    </ul>
</div>

<c:if test="${not empty discount}">

    <div style="color: #34ff1c; margin: 10px 0px;">

        Your Regular Customer Discounts: ${discount}%

    </div>

</c:if>

<security:authorize  access="hasRole('ROLE_EMPLOYEE')">

<c:if test="${empty codeForReservation}">

    <div style="color: #4935ff; margin: 10px 0px;">

        <a href="${pageContext.request.contextPath}/buyReservationProduct">Buy reservation product</a>

    </div>

</c:if>

</security:authorize>

<c:if test="${not empty codeForReservation}">

    <form method="POST" action="${pageContext.request.contextPath}/shoppingCart">

        Enter code from e-mail:

        <input type="text" name="codeForReservation" />    <input type="submit" value="Commit" />

    </form>

</c:if>

<br>

<jsp:include page="_footer.jsp" />

</body>
</html>