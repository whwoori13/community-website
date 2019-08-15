<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>Write</title>
<link href="<c:url value= "/resources/css/site.css" />" rel="stylesheet" />
<script src="<c:url value= "/resources/js/jquery.min.js" />"></script>
<script>
	$(document).ready(function() {
		$('#header').load('../resources/html/header.html');
		$('#nav').load('../resources/html/nav.jsp');
		$('#footer').load('../resources/html/footer.html');
	});
</script>
<script type='text/javaScript'>
	// textarea max rows 제한.
	$(document).ready(function() {

		var newLines;
		var lines = 300; // max rows
		var linesUsed = $('#linesUsed');

		$('#countMe').keydown(function(e) {

			newLines = $(this).val().split("\n").length;
			linesUsed.text(newLines);

			if (e.keyCode == 13 && newLines >= lines) {
				linesUsed.css('color', 'red');
				return false;
			} else {
				linesUsed.css('color', '');
			}
		});

		$(document).on('click', '#submitTextarea', function(e) {

			var reg = /^\s*$/;
			var title = $('.artTit').val();
			var content = $('.artCont').val();

			if (newLines == null)
				newLines = 0;
			if (title == null)
				title = '';
			if (content == null)
				content = '';

			if (reg.test(title) || reg.test(content)) {
				e.preventDefault();
				alert('제목과 내용을 모두 입력하세요.');
			}

			else if (newLines > lines) {
				e.preventDefault();
				alert('줄이 너무 많습니다.');
			}

		});
	});
</script>

</head>
<body>
	<div id="header"></div>
	<div id="nav"></div>
	<br />
	<br />

	<form:form action="saveArticle" modelAttribute="article" method="POST">
		<div style="width: 40%; min-width: 768px; margin: auto;">
			<form:input class="artTit" type="text" path="title"
				style="width:100%; margin-bottom: 20px" maxlength="45"
				placeholder="이곳에 제목을 입력하세요." />

			<%-- modelAttribute의 path 외에도 글 작성 완료 후 읽기 페이지로 가려면 @RequestParam으로 현재 topic 전달해야함 --%>
			<input type="hidden" name="topic2" value="${currentTopic}" />
			<form:hidden path="customerId" value="${sessionScope.customer.id}" />
			<form:hidden path="topic" value="${currentTopic}" />
			<form:textarea class="artCont" id="countMe" type="text"
				path="content" rows="20"
				style="width: 100%; resize: vertical; font-size: 15px;
							margin: auto;"
				maxlength="20000" placeholder="이곳에 내용을 입력하세요." />
			<span id="linesUsed" style="font-size: 15px;"></span><span
				style="font-size: 15px;">&nbsp;줄 사용됨 (최대 300줄) </span><span><input
				id="submitTextarea" type="submit" value="제출" class="save"
				style="float: right;" /></span>
	</form:form>
	<br />
	<br />
	<br />
	<br />

	<footer id="footer"></footer>
</body>
</html>




<%-- 글 작성 화면 캐싱 비활성화 --%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	response.setDateHeader("Expires", -1);
%>


