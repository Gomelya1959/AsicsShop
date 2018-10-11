<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SetDiscounts</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>

<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />

<div class="page-title">Set Discounts</div>
<br/>
Product : ${productInfo.name}
<br/>
Price : ${productInfo.price}
<br/>
<c:if test="${not empty errorMessage }">
    <div class="error-message">
            ${errorMessage}
    </div>
</c:if>

<br/>

<form action="${pageContext.request.contextPath}/discounts?code=${productInfo.code}" method="post">

    <table border="1" style="width:100%">
        <tr>
            <th>Quantity Products</th>
            <th>Value of discount</th>
        </tr>
        <c:forEach items="${listOfDiscounts}" var="discountInfo" varStatus="varStatus">
            <tr>
                <c:if test="${not empty edit}">
                <td>
                    <input type="text" name="quantity${varStatus.index}" value="${discountInfo.quantity}"/>
                </td>
                <td>
                    <input type="text" name="value${varStatus.index}" value="${discountInfo.value}"/>
                </td>
                </c:if>
                <c:if test="${empty edit}">
                    <td>
                        ${discountInfo.quantity}
                    </td>
                    <td>
                        ${discountInfo.value}
                    </td>
                    <td>
                        <a style="color:#4935ff;"
                           href="${pageContext.request.contextPath}/discounts?code=${productInfo.code}&delete=${discountInfo.id}">
                            Delete Discount</a>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
        <c:if test="${not empty add}">
            <tr>
                <td>
                    <input type="text" name="quantity_new"/>
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
           href="${pageContext.request.contextPath}/discounts?code=${productInfo.code}&add=YES">
        Add Discount</a>

    <br/>
    <br/>

    <a style="color:#4935ff;"
       href="${pageContext.request.contextPath}/discounts?code=${productInfo.code}&edit=YES">
        Edit Discounts</a>

    <br/>
    <br/>

</form>

<jsp:include page="_footer.jsp" />

</body>
</html>