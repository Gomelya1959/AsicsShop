<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<!DOCTYPE html>
<html>
<head>

    <style>
        body {
            background-image: url("images/5.jpg");
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

<div class="page-title">Product List</div>

<c:forEach items="${listProducts}" var="prodInfo">
    <div class="product-preview-container">
        <ul>
            <li><img class="product-image"
                     src="${pageContext.request.contextPath}/productImage?code=${prodInfo.code}" /></li>
            <li>Code: ${prodInfo.code}</li>
            <li>Name: ${prodInfo.name}</li>
            <li>In stock: ${prodInfo.surplus}</li>
            <li>Price: <fmt:formatNumber value="${prodInfo.price}" type="currency"/></li>
            <c:if test="${prodInfo.discount != 0.0}">
                <li>Discount: ${prodInfo.discount}%</li>
                <li>Old price: <fmt:formatNumber value="${prodInfo.price * 100/(100 - prodInfo.discount)}" type="currency"/></li>
            </c:if>
            <security:authorize  access="hasRole('ROLE_EMPLOYEE')">
            <li><a href="${pageContext.request.contextPath}/buyProduct?code=${prodInfo.code}">
                Buy Now</a></li>
            </security:authorize>
            <security:authorize  access="hasRole('ROLE_STOREKEEPER')">
                <li><a href="${pageContext.request.contextPath}/addProduct?code=${prodInfo.code}">
                    Add Product</a></li>
            </security:authorize>
            <!-- For Manager edit Product -->
            <security:authorize  access="hasRole('ROLE_MANAGER')">
                <li><a style="color:red;"
                       href="${pageContext.request.contextPath}/product?code=${prodInfo.code}">
                    Edit Product</a></li>
            </security:authorize>
            <security:authorize  access="hasRole('ROLE_MANAGER')">
                <li><a style="color:red;"
                       href="${pageContext.request.contextPath}/requestOnDelete?code=${prodInfo.code}">
                    Delete Product</a></li>
            </security:authorize>
            <security:authorize  access="hasRole('ROLE_MANAGER')">
                <li><a style="color:red;"
                       href="${pageContext.request.contextPath}/discounts?code=${prodInfo.code}">
                    Add Discounts</a></li>
            </security:authorize>
            <security:authorize  access="hasRole('ROLE_MANAGER')">
                <li><a style="color:red;"
                       href="${pageContext.request.contextPath}/productList?codeProduct=${prodInfo.code}">
                    Set Discount</a></li>
                <li><a style="color:red;"
                       href="${pageContext.request.contextPath}/productList?deleteDiscountCode=${prodInfo.code}">
                    Delete Discount</a></li>

                <c:if test="${codeProduct == prodInfo.code}">

                    <form method="POST" action="${pageContext.request.contextPath}/productList?codeProduct=${codeProduct}">

                        Set discount for this product:

                        <input type="text" name="discount"/>

                        <input class="button-update-sc" type="submit" value="Confirm Discount" />

                    </form

                </c:if>

            </security:authorize>
        </ul>
    </div>

</c:forEach>
<br/>


<c:if test="${paginationProducts.totalPages > 1}">
    <div class="page-navigator">
        <c:forEach items="${paginationProducts.navigationPages}" var = "page">
            <c:if test="${page != -1 }">
                <a href="productList?page=${page}" class="nav-item">${page}</a>
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