<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Cache-Control", "no-store");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
%>
<%-- 
if (session.getAttribute("token") == null) {
	response.sendRedirect(request.getContextPath() + "/portal/");

} %>
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Loan History</title>

</head>
<body>

	<h1>Loan History</h1>
	<c:if test="${empty loanHistory}">
		<p>No loans availed yet</p>
	</c:if>
	<c:if test="${not empty loanHistory}">
		<c:forEach items="${loanHistory}" var="loan">
			<li>${loan.loanId}</li>
			<li>${loan.accountNumber}</li>
			<li>${loan.type}</li>
			<li>${loan.amount}</li>
			<li>${loan.interestRate}</li>
			<li>${loan.duration}</li>
			<li>${loan.dateApplied}</li>
			<br>
		</c:forEach>
	</c:if>

</body>


</html>