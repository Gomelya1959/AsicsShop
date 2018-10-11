<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Courier List</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>

<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />

<fmt:setLocale value="en_US" scope="session"/>

<div class="page-title">Courier List</div>



<c:forEach items="${paginationCourier.list}" var="courierInfo">
    <div class="courier-preview-container">
        <ul>
            <li>Id: ${courierInfo.id}</li>
            <li>Name: ${courierInfo.name}</li>
            <li>Phone: ${courierInfo.phone}</li>
            <li>Courier_name: ${courierInfo.user_name}</li>
            <!-- For Manager edit Product -->
            <security:authorize  access="hasRole('ROLE_MANAGER')">
                <li><a style="color:red;"
                       href="${pageContext.request.contextPath}/courier?id=${courierInfo.id}">
                    Edit Courier</a></li>
            </security:authorize>
            <security:authorize  access="hasRole('ROLE_MANAGER')">
                <li><a style="color:red;"
                       href="${pageContext.request.contextPath}/requestOnDeleteCourier?id=${courierInfo.id}">
                    Delete Courier</a></li>
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