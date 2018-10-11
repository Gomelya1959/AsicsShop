<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>RegularCustomerDiscounts</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>

<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />

<div class="page-title">Set Regular Customer Discounts</div>

<c:if test="${not empty errorMessage }">
    <div class="error-message">
            ${errorMessage}
    </div>
</c:if>

<br/>

<form action="${pageContext.request.contextPath}/regularCustomerDiscounts" method="post">

    <table border="1" style="width:100%">
        <tr>
            <th>Total sum</th>
            <th>Value of discount</th>
        </tr>
        <c:forEach items="${listOfDiscounts}" var="discountInfo" varStatus="varStatus">
            <tr>
                <c:if test="${not empty edit}">
                    <td>
                        <input type="text" name="total${varStatus.index}" value="${discountInfo.total}"/>
                    </td>
                    <td>
                        <input type="text" name="value${varStatus.index}" value="${discountInfo.value}"/>
                    </td>
                </c:if>
                <c:if test="${empty edit}">
                    <td>
                            ${discountInfo.total}
                    </td>
                    <td>
                            ${discountInfo.value}
                    </td>
                    <td>
                        <a style="color:#4935ff;"
                           href="${pageContext.request.contextPath}/regularCustomerDiscounts?delete=${discountInfo.id}">
                            Delete Discount</a>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
        <c:if test="${not empty add}">
            <tr>
                <td>
                    <input type="text" name="total_new"/>
                </td>
                <td>
                    <input type="text" name="value_new"/>
                </td>
            </tr>
        </c:if>
    </table>

    <p>
        Confirm discounts <input type="submit" name="confirm" value="CONFIRM" />
    </p>

    <a style="color:#4935ff;"
       href="${pageContext.request.contextPath}/regularCustomerDiscounts?add=YES">
        Add Discount</a>

    <br/>
    <br/>

    <a style="color:#4935ff;"
       href="${pageContext.request.contextPath}/regularCustomerDiscounts?edit=YES">
        Edit Discounts</a>

    <br/>
    <br/>

</form>

<jsp:include page="_footer.jsp" />

</body>
</html>