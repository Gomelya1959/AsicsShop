<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>

    <style>
        body {
            background-image: url("images/2.jpg");
        }
    </style>

    <meta charset="UTF-8">
    <title>Order</title>

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
    <h3>Order Summary:</h3>
    <ul>
        <li>Total:
            <span class="total">
           <fmt:formatNumber value="${orderInfo.amount}" type="currency"/>
           </span></li>
    </ul>
    <ul>
        <li>Delivery: ${orderInfo.delivery}</li>
    </ul>
    <c:if test="${orderInfo.deliveryInfoDate != orderInfo.deliveryInfoTime}">
        <c:choose>
            <c:when test="${orderInfo.status == true}">
                <ul>
                    <li>Status of delivery: The order is delivered!</li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul>
                    <li>Status of delivery: Expect delivery</li>
                </ul>
            </c:otherwise>
        </c:choose>

        <c:if test="${orderInfo.partialReturn}">
            <ul><li>Status of return: Partial Return</li></ul>
        </c:if>

        <c:if test="${orderInfo.back}">
            <ul><li>Status of return: All returned</li></ul>
        </c:if>

        <ul>
            <li>Date of delivery: ${orderInfo.deliveryInfoDate}</li>
        </ul>
        <ul>
            <li>Time of delivery: ${orderInfo.deliveryInfoTime}</li>
        </ul>
        <ul>
            <li>Order deliver: ${orderInfo.courierName}</li>
        </ul>
        <ul>
            <li>Phone of courier: ${orderInfo.courierPhone}</li>
        </ul>
    </c:if>
</div>

<br/>

<table border="1" style="width:100%">
    <tr>
        <th>Product Code</th>
        <th>Product Name</th>
        <th>Quantity</th>
        <th>Price</th>
        <th>Amount</th>
        <th>Saving</th>
    </tr>
    <c:forEach items="${orderInfo.details}" var="orderDetailInfo">
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
        </tr>
    </c:forEach>
</table>
<c:if test="${paginationResult.totalPages > 1}">
    <div class="page-navigator">
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

<security:authorize  access="hasRole('ROLE_COURIER')">

    <p><b>It is delivered?</b></p>
    <a class="button-send-sc" href="${pageContext.request.contextPath}/delivery?orderID=${orderInfo.id}">Confirm delivery</a>
    <p> </p>

    <p><b>It is returned?</b></p>
    <a class="button-send-sc" href="${pageContext.request.contextPath}/return?orderID=${orderInfo.id}">To make a return</a>
    <p> </p>
</security:authorize>

<jsp:include page="_footer.jsp" />

</body>
</html>