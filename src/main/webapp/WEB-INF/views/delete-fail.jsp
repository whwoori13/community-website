<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>Error Page</title>
<link href="<c:url value= "/resources/css/site.css" />" rel="stylesheet" />
<script src="<c:url value= "/resources/js/jquery.min.js" />"></script>
<script>
	$(document).ready(function() {

		$('#header').load('../resources/html/header.html');

		$('#footer').load('../resources/html/footer.html');
	});
</script>
</head>
<body>
	<div id="header"></div>
	<br />
	<br />
	<br />
	<br />
	<br />
	<p style="color: red; text-align: center;">&nbsp;&nbsp;다른 사람의 글은 수정할
		수 없습니다.</p>
	<br />
	
	<div>
		<span style="margin-left: 60%;">
			<button>
				<a href="javascript:history.back()">뒤로가기</a>
			</button>
		</span>
	</div>
	<footer id="footer"></footer>
</body>
</html>
