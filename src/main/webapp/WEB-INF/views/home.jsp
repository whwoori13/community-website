<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>Community Website</title>
<link href="<c:url value= "/resources/css/site.css" />" rel="stylesheet" />
<script src="<c:url value= "/resources/js/jquery.min.js" />"></script>
<script>
	$(document).ready(function() {

		$('#header').load('resources/html/header.html');
		$('#nav').load('resources/html/nav.jsp');
		$('#sidebar').load('resources/html/sidebar.jsp');
		$('#footer').load('resources/html/footer.html');

		if (navigator.userAgent.match(/msie/i) || navigator.userAgent.match(/trident/i)) {
			alert('이 웹사이트는 IE 브라우저에 최적화 되어있지 않습니다.');
		}
	});
</script>
<style>
a:link {
	color: black;
} /* unvisited link */
a:visited {
	color: black;
} /* visited link */
a:hover {
	color: black;
} /* mouse over link */
a:active {
	color: black;
} /* selected link */
</style>
</head>
<body>
	<div class="wrapper">
		<div class="content">
			<div id="header"></div>
			<div id="nav"></div>
			<br /> <br />
			<c:forEach items="${topTopicList}" var="topic">

				<div class="homeSection">
					<c:forEach items="${sessionScope.topicList}" var="element">
						<c:if test="${topic.topic == element.code}">
							<a href="/community/board/list?topic=${element.code}"><strong>${element.name}</strong></a>
							<hr>
						</c:if>
					</c:forEach>
					<c:forEach items="${homeArticleList}" var="article">
						<c:set var="dateParts" value="${fn:split(article.writeDate, ' ')}" />
						<c:if test="${topic.topic == article.topic}">
							<span style="display: block; margin-top: 2.5px;"><span
								style="font-size: 12.5px; color: gray;">•&nbsp;${dateParts[0]}&nbsp;</span>
								<a style="font-size: 13.5px;"
								href="/community/board/readArticle?articleNum=${article.num}&topic=${article.topic}">${article.title}</a><span
								style="font-size: 13.5px; color: #FE6700;">&nbsp;(${article.replyCount})</span>
								<br /></span>
						</c:if>
					</c:forEach>
				</div>
			</c:forEach>
			<br /> <br />                             
		</div>
		<div id="sidebar"></div>
	</div>

	<footer id="footer"></footer>
</body>
</html>
