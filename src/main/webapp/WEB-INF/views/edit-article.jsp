<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>Edit</title>
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

	<form:form action="editArticle" modelAttribute="article" method="POST">

		<div style="width: 40%; margin: auto; min-width: 768px;">
			<form:input type="text" path="title" class="artTit"
				style="width:100%; margin-bottom: 20px" maxlength="45" />
			<form:hidden path="customerId" value="${article.customerId}" />
			<form:hidden path="num" value="${article.num}" />
			<form:hidden path="writeDate" value="${article.writeDate}" />
			<input type="hidden" name="topic" value="${currentTopic}" />

			<form:textarea id="countMe" type="text" path="content" rows="20"
				style="width: 100%; resize: vertical; font-size: 15px;
							margin: auto;"
				maxlength="20000" class="artCont" />
			<span id="linesUsed" style="font-size: 15px;"></span><span
				style="font-size: 15px;">&nbsp;줄 사용됨 (최대 300줄)</span><span><input
				id="submitTextarea" type="submit" value="제출" class="save"
				style="float: right;" /></span>

		</div>
	</form:form>

	<footer id="footer"></footer>
</body>
</html>






<%-- 글 작성 화면 캐싱 비활성화 --%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	response.setDateHeader("Expires", -1);
%>

