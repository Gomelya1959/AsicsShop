<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>

    <style>
        body {
            background-image: url("images/sunset.jpg");
        }
    </style>

    <meta charset="UTF-8">
    <title>Return</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>

<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />

<fmt:setLocale value="en_US" scope="session"/>

<div class="page-title">Order Info</div>

<div class="customer-info-container">
    <h3>Customer Information:</h3>
    <ul>
        <li>Name: ${orderInfo.customerName}</li>
        <li>Email: ${orderInfo.customerEmail}</li>
        <li>Phone: ${orderInfo.customerPhone}</li>
        <li>Address: ${orderInfo.customerAddress}</li>
    </ul>
</div>

<br/>

<form action="${pageContext.request.contextPath}/return?orderID=${orderInfo.id}" method="post">

<table border="1" style="width:100%">
    <tr>
        <th>Product Code</th>
        <th>Product Name</th>
        <th>Quantity</th>
        <th>Price</th>
        <th>Amount</th>
        <th>Saving</th>
        <th>Number Of Products Returned</th>
    </tr>
    <c:forEach items="${orderInfo.details}" var="orderDetailInfo" varStatus="varStatus">
        <tr>
            <td>${orderDetailInfo.productCode}</td>
            <td>${orderDetailInfo.productName}</td>
            <td>${orderDetailInfo.quanity}</td>
            <td>
                <fmt:formatNumber value="${orderDetailInfo.price}" type="currency"/>
            </td>
            <td>
                <fmt:formatNumber value="${orderDetailInfo.amount}" type="currency"/>
            </td>
            <td>
                <fmt:formatNumber value="${orderDetailInfo.saving}" type="currency"/>
            </td>
            <td>
                <input type="text" name="product${varStatus.index}" />
            </td>
        </tr>
    </c:forEach>
</table>
<c:if test="${paginationResult.totalPages > 1}">    <div class="page-navigator">
        <c:forEach items="${paginationResult.navigationPages}" var = "page">
            <c:if test="${page != -1 }">
                <a href="orderList?page=${page}" class="nav-item">${page}</a>
            </c:if>
            <c:if test="${page == -1 }">
                <span class="nav-item"> ... </span>
            </c:if>
        </c:forEach>

    </div>
</c:if>

    <p>
        Confirm return <input type="submit" name="confirm" value="CONFIRM" />
    </p>

</form>

<jsp:include page="_footer.jsp" />

</body>
</html>