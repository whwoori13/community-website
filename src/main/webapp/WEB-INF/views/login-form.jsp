<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>Log-in</title>
<link href="<c:url value= "/resources/css/site.css" />" rel="stylesheet" />
<script src="<c:url value= "/resources/js/jquery.min.js" />"></script>
<script>
	$(document).ready(function() {
		$('#header').load('resources/html/header.html');

		$('#footer').load('resources/html/footer.html');
	});
</script>
<style>
#inner {
	width: 25%;
	margin: auto;
}
</style>
</head>                                                                                                                                                                               
<body>
	<div id="header" ></div>
                                                                                                                                      
	<div
		style="width: 50%; min-width: 960px; margin-left: 40%; padding-top: 80px;">                           
		<p>ID와 비밀번호를 입력하세요.</p>                                 
		<form:form action="login" modelAttribute="customer" method="POST">
				<form:input type="text" path="id" size="12" placeholder="ID" />
			<br />
			<form:input type="password" path="pw" size="12"
				style="margin-top: 10px;" placeholder="비밀번호" />
			<br />
			<input type="submit" value="로그인" style="margin-top: 10px;" />
			<input type="hidden" name="referer" value="${referer}"/>
		</form:form>

		<form action="joinForm" method="GET">
			<input type="submit" value="회원가입" />
		</form>
	</div>


	<footer id="footer"></footer>
</body>
</html>