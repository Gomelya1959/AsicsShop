<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>

    <style>
        body {
            background-image: url("images/surf.jpg");
        }
    </style>

    <meta charset="UTF-8">

    <title>Shopping Cart</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>
<jsp:include page="_header.jsp" />

<jsp:include page="_menu.jsp" />

<fmt:setLocale value="en_US" scope="session"/>

<div class="page-title">My Cart</div>

<c:if test="${not empty listNotProduct}">
    <c:forEach items="${listNotProduct}" var="productInfo">
<div style="color: red; font-weight: bold">
        <h2>
            Sorry, ${productInfo.name} remained ${productInfo.surplus} pieces...
        </h2>
</div>
    </c:forEach>

</c:if>

<c:if test="${empty cartForm or empty cartForm.cartLines}">
    <h2>There is no items in Cart</h2>
    <a href="${pageContext.request.contextPath}/productList">Show
        Product List</a>
</c:if>

<c:if test="${not empty cartForm and not empty cartForm.cartLines   }">
    <%--@elvariable id="cartForm" type=""--%>
    <form:form method="POST" modelAttribute="cartForm" action="${pageContext.request.contextPath}/shoppingCart">
        <c:forEach items="${cartForm.cartLines}" var="cartLineInfo"
                   varStatus="varStatus">
            <c:if test="${not empty cartLineInfo.discountInfoList}">
                    <div style="color: red; font-weight: bold">
                    <table border="1" style="width:25%">
                        <tr>
                            <th>Quantity Products</th>
                            <th>Value of discount</th>
                        </tr>
                        <c:forEach items="${cartLineInfo.discountInfoList}" var="discountInfo">
                        <tr>
                            <td>
                                    ${discountInfo.quantity}
                            </td>
                            <td>
                                    ${discountInfo.value}%
                            </td>
                        </tr>
                        </c:forEach>
                    </table>
                    </div>
            </c:if>
            <div class="product-preview-container">
                <ul>
                    <li><img class="product-image"
                             src="${pageContext.request.contextPath}/productImage?code=${cartLineInfo.productInfo.code}" />
                    </li>
                    <li>Code: ${cartLineInfo.productInfo.code} <form:hidden
                            path="cartLines[${varStatus.index}].productInfo.code" />

                    </li>
                    <li>Name: ${cartLineInfo.productInfo.name}</li>
                    <li>Price: <span class="price">

                         <fmt:formatNumber value="${cartLineInfo.productInfo.price}" type="currency"/>

                       </span></li>
                    <li>Surplus: ${cartLineInfo.productInfo.surplus}</li>
                    <li>Quantity: <form:input
                            path="cartLines[${varStatus.index}].quantity" /></li>
                    <li>Subtotal:
                        <span class="subtotal">

                            <fmt:formatNumber value="${cartLineInfo.amount * (1 - discount/100)}" type="currency"/>

                         </span>
                    </li>
                    <li>Saving:

                        <span class="saving">

                         <fmt:formatNumber value="${cartLineInfo.saving  * (1 - discount/100)}" type="currency"/>

                        </span>

                    </li>
                    <li><a
                            href="${pageContext.request.contextPath}/shoppingCartRemoveProduct?code=${cartLineInfo.productInfo.code}">
                        Delete </a></li>
                </ul>
            </div>
            <c:if test="${not empty listNotProduct}">
                <div style="color: #4935ff;">
                    <a href="${pageContext.request.contextPath}/reservation?code=${cartLineInfo.productInfo.code}">To reserve a product</a>
                </div>
            </c:if>
        </c:forEach>
        <div style="clear: both"></div>

        <br>

        <input class="button-update-sc" type="submit" value="Confirm Quantity" />
        <a class="navi-item"
           href="${pageContext.request.contextPath}/shoppingCartCustomer">Enter
            Customer Info</a>
        <a class="navi-item"
           href="${pageContext.request.contextPath}/productList">Continue
            Buy</a>
    </form:form>

</c:if>

<br>
<jsp:include page="_footer.jsp" />

</body>
</html>