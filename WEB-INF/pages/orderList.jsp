<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>

    <style>
        body {
            background-image: url("images/3.jpg");
        }
    </style>

    <meta charset="UTF-8">
    <title>Product List</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>

<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />

<fmt:setLocale value="en_US" scope="session"/>

<div class="page-title">Order List</div>

<div>Total Order Count: ${paginationResult.totalRecords}</div>

<table border="1" style="width:100%">
    <tr>
        <th>Order Num</th>
        <th>Order Date</th>
        <th>Customer Name</th>
        <th>Customer Address</th>
        <th>Customer Email</th>
        <th>Amount</th>
        <th>Saving</th>
        <th>View</th>
        <security:authorize  access="hasRole('ROLE_MANAGER')">
        <th>Delete</th>
        </security:authorize>
        <th>Delivery</th>
        <th>Return</th>
        <security:authorize  access="hasRole('ROLE_EMPLOYEE')">
            <th>Cancel</th>
            <th>Edit</th>
        </security:authorize>
    </tr>
    <c:forEach items="${paginationResult.list}" var="orderInfo">
        <tr>
            <td>${orderInfo.orderNum}</td>
            <td>
                <fmt:formatDate value="${orderInfo.orderDate}" pattern="dd-MM-yyyy HH:mm"/>
            </td>
            <td>${orderInfo.customerName}</td>
            <td>${orderInfo.customerAddress}</td>
            <td>${orderInfo.customerEmail}</td>
            <td style="color:red;">
                <fmt:formatNumber value="${orderInfo.amount}" type="currency"/>
            </td>
            <td style="color:red;">
                <fmt:formatNumber value="${orderInfo.saving}" type="currency"/>
            </td>
            <td><a href="${pageContext.request.contextPath}/order?orderId=${orderInfo.id}">
                View</a></td>
            <security:authorize  access="hasRole('ROLE_MANAGER')">
            <td><a href="${pageContext.request.contextPath}/deleteOrder?orderId=${orderInfo.id}">
                Delete</a></td>
            </security:authorize>
            <c:if test="${orderInfo.deliveryInfoDate != orderInfo.deliveryInfoTime}">
                <c:choose>
                    <c:when test="${orderInfo.status}">
                        <td>delivered</td>
                    </c:when>
                    <c:otherwise>
                        <td>expect delivery</td>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <c:if test="${orderInfo.partialReturn}">
                <td>Partial Return</td>
            </c:if>
            <c:if test="${orderInfo.back}">
                <td>Return</td>
            </c:if>
            <c:if test="${not orderInfo.partialReturn and not orderInfo.back or orderInfo.delivery}">
                <td> </td>
            </c:if>
            <c:if test="${orderInfo.delivery == 'NO'}">
                <td> </td>
            </c:if>
            <security:authorize  access="hasRole('ROLE_EMPLOYEE')">
                <td><a href="${pageContext.request.contextPath}/deleteOrder?orderId=${orderInfo.id}">Cancel</a></td>
                <td><a href="${pageContext.request.contextPath}/myShoppingCart?orderID=${orderInfo.id}">Edit</a></td>
            </security:authorize>
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




<jsp:include page="_footer.jsp" />

</body>
</html>