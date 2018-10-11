<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>


<div class="menu-container">

    <a href="${pageContext.request.contextPath}/">Home</a>
    |
    <a href="${pageContext.request.contextPath}/productList">
        Product List
    </a>
    |
    <security:authorize  access="hasRole('ROLE_MANAGER')">
        <a href="${pageContext.request.contextPath}/courierList">
            Courier List
        </a>
        |
    </security:authorize>
    <security:authorize  access="hasRole('ROLE_EMPLOYEE')">
        <a href="${pageContext.request.contextPath}/shoppingCart">
            My Cart
        </a>
        |
    </security:authorize>

    <security:authorize  access="hasAnyRole('ROLE_MANAGER', 'ROLE_COURIER')">
        <a href="${pageContext.request.contextPath}/orderList">
            Order List
        </a>
        |
    </security:authorize>

    <security:authorize  access="hasAnyRole('ROLE_EMPLOYEE')">
        <a href="${pageContext.request.contextPath}/myOrders?name_user=${pageContext.request.userPrincipal.name}">
            My Order List
        </a>
        |
    </security:authorize>

    <security:authorize  access="hasRole('ROLE_MANAGER')">
        <a href="${pageContext.request.contextPath}/product">
            Create Product
        </a>
        |
    </security:authorize>

    <security:authorize  access="hasRole('ROLE_MANAGER')">
        <a href="${pageContext.request.contextPath}/courier">
            Add Courier
        </a>
        |
    </security:authorize>
    <security:authorize  access="hasRole('ROLE_MANAGER')">
        <a href="${pageContext.request.contextPath}/discounts">
            Set Discounts
        </a>
        |
    </security:authorize>

    <security:authorize  access="hasRole('ROLE_MANAGER')">
        <a href="${pageContext.request.contextPath}/regularCustomerDiscounts">
            Set Regular Customer Discounts
        </a>
        |
    </security:authorize>

</div>