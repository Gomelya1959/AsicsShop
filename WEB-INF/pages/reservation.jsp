<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ReservationProduct</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">

</head>
<body>

<jsp:include page="_header.jsp" />
<jsp:include page="_menu.jsp" />

<div class="page-title">Reservation Product</div>

<div style="color: #4935ff; font-weight: bold">

    <form method="POST" action="${pageContext.request.contextPath}/reservation?code=${productInfo.code}">

        <h2>
            It is possible to reserve goods. Enter the numbers of product ${productInfo.name}:
        </h2>

        <input type="text" name="numbersOfReservedProducts" />

        <input class="button-update-sc" type="submit" value="Confirm Reservation" />

    </form>
</div>
<br>



<jsp:include page="_footer.jsp" />

</body>
</html>