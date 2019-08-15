<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>나의 정보</title>
<link href="<c:url value= "/resources/css/site.css" />" rel="stylesheet" />
<script src="<c:url value= "/resources/js/jquery.min.js" />"></script>
</head>
<script>
	$(document).ready(function() {
		$('#header').load('resources/html/header.html');
		$('#nav').load('resources/html/nav.jsp');
		$('#footer').load('resources/html/footer.html');

		$(document).on('click', '.del', function() {
			return confirm('정말 탈퇴 하시겠습니까?');
		});

	});
</script>
<style>
span p {
	font-size: 18px;
	display: inline-block;
}

label {
	font-size: 15px;
}
</style>
<body>
	<div class="wrapper">
		<div class="content">
			<div id="header"></div>
			<div id="nav"></div>
			<br/>
			<br/>                                            
			<br/>                 
			<div
				style="width: 650px; min-width: 650px; margin: auto; padding-left: 50px;">
				<label>ID: </label><span><p>${sessionScope.customer.id}</p></span><br />
				<label>PW: </label><span><p>${sessionScope.customer.pw}</p></span><br />
				<label>Email: </label><span><p>${sessionScope.customer.email}</p></span><br />
				<label>주소: </label><span><p>${sessionScope.customer.address}</p></span><br />
				<label>가입일: </label><span><p>${sessionScope.customer.joinDate}</p></span>

				<form action="delCust" method="POST">
					<br /> <input class="del" type="submit" value="회원 탈퇴"
						style="font-size: 13.5px;" />
				</form>
			</div>
		</div>
	</div>
	<footer id="footer"></footer>
</body>
</html>



