<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>

    <style>
        body {
            background-image: url("images/tiger.jpg");
        }
    </style>

    <meta charset="UTF-8">

    <title>Shopping Cart Confirmation</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>
<jsp:include page="_header.jsp" />

<jsp:include page="_menu.jsp" />

<fmt:setLocale value="en_US" scope="session"/>

<div class="page-title">Confirmation</div>



<div class="customer-info-container">
    <h3>Customer Information:</h3>
    <ul>
        <li>Name: ${myCart.customerInfo.name}</li>
        <li>Email: ${myCart.customerInfo.email}</li>
        <li>Phone: ${myCart.customerInfo.phone}</li>
        <li>Address: ${myCart.customerInfo.address}</li>
    </ul>
    <h3>Cart Summary:</h3>
    <ul>
        <li>Quantity: ${myCart.quantityTotal}</li>
        <li>Total:
            <span class="total">
            <fmt:formatNumber value="${myCart.amountTotal * (1 - discount/100)}" type="currency"/>
          </span></li>
    </ul>
</div>

<form method="POST"
      action="${pageContext.request.contextPath}/shoppingCartConfirmation">

    <!-- Edit Cart -->
    <a class="navi-item"
       href="${pageContext.request.contextPath}/shoppingCart">Edit Cart</a>

    <!-- Edit Customer Info -->
    <a class="navi-item"
       href="${pageContext.request.contextPath}/shoppingCartCustomer">Edit
        Customer Info</a>

    <c:if test="${empty deliveryYes}">

        <p><b>Cost of delivery is 10$</b></p>
        <input type="checkbox" name="delivery" value="1">Delivery<br>
        <input type="submit" value="To count the total amount including shipping" class="button-send-sc" />

    </c:if>

    <c:if test="${not empty DetailsOfDelivery}">

        <p><b>Your goods will be delivered by courier</b></p>

        <p><b>Choose the time and date of delivery</b></p>

        <select name="time">
            <option value="10:00 - 13:00">10:00 - 13:00</option>
            <option value="13:00 - 16:00">13:00 - 16:00</option>
            <option value="16:00 - 19:00">16:00 - 19:00</option>
            <option value="19:00 - 22:00">19:00 - 22:00</option>
        </select>

        <select name="date">
            <option value="15.09">15.09</option>
            <option value="16.09">16.09</option>
            <option value="17.09">17.09</option>
            <option value="18.09">18.09</option>
        </select>

        <input type="checkbox" name="deliveryYes" value="1">Confirm the details of delivery<br>

    </c:if>

    <!-- Send/Save -->
    <input type="submit" value="Send" class="button-send-sc" />
</form>

<div class="container">

    <c:forEach items="${myCart.cartLines}" var="cartLineInfo">
        <div class="product-preview-container">
            <ul>
                <li><img class="product-image"
                         src="${pageContext.request.contextPath}/productImage?code=${cartLineInfo.productInfo.code}" /></li>
                <li>Code: ${cartLineInfo.productInfo.code} <input
                        type="hidden" name="code" value="${cartLineInfo.productInfo.code}" />
                </li>
                <li>Name: ${cartLineInfo.productInfo.name}</li>
                <li>Price: <span class="price">
                     <fmt:formatNumber value="${cartLineInfo.productInfo.price}" type="currency"/>
                  </span>
                </li>
                <li>Quantity: ${cartLineInfo.quantity}</li>
                <li>Subtotal:
                    <span class="subtotal">
                       <fmt:formatNumber value="${cartLineInfo.amount * (1 - discount/100)}" type="currency"/>
                    </span>
                </li>
                <li>Saving:
                    <span class="saving">
                       <fmt:formatNumber value="${cartLineInfo.saving * (1 - discount/100)}" type="currency"/>
                    </span>
                </li>
            </ul>

        </div>
    </c:forEach>

</div>

<jsp:include page="_footer.jsp" />

</body>
</html>