
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>Test</title>
<link href="<c:url value= "/resources/css/site.css" />" rel="stylesheet" />
<script src="<c:url value= "/resources/js/jquery.min.js" />"></script>
<script>
	$(document).ready(function() {
		$('#nav').load('resources/html/nav.jsp');
		$('#header').load('resources/html/header.html');
		$('#footer').load('resources/html/footer.html');

	});
</script>
<style>
#1 #2 {
	color: red;
}
</style>
</head>
<body>

	<div id="1">
		<div>
			<p id="2">hello</p>
		</div>
	</div>
	<div id="3">
		<div>
			<p id="4">hello</p>
		</div>
	</div>



</body>
</html>
<!-- above : basic tamplate -->





<div style="width: 50%; min-width: 480px; margin: auto;">
	<form:form action="saveReply" modelAttribute="newReply" method="GET">
		<form:hidden path="articleNum" value="${article.num}" />
		<form:hidden path="customerId" value="${sessionScope.customer.id}" />

		<form:textarea id="countMeRoot" type="text" path="content" rows="5"
			placeholder="이곳에 댓글을 입력하세요."
			style="width:100%; resize: none; font-size: 15px" maxlength="300" />
		<span id="linesUsedRoot"></span>&nbsp;줄 사용됨 (최대 300줄) <span> <input
			id="submitTextareaRoot" type="submit" value="댓글 쓰기" class="save"
			style="float: right;" />
	</form:form>
</div>




<form:form action="saveArticle" modelAttribute="article" method="GET">
	<div style="width: 40%; margin: auto;">
		<form:input type="text" path="title"
			style="width:100%; margin-bottom: 20px" maxlength="38"
			placeholder="이곳에 제목을 입력하세요." />

		<form:hidden path="customerId" value="${sessionScope.customer.id}" />



		<form:textarea id="countMe" type="text" path="content" rows="20"
			style="width: 100%; resize: vertical; font-size: 15px;
							margin: auto;"
			maxlength="3000" placeholder="이곳에 내용을 입력하세요." />
		<span id="linesUsed"></span>&nbsp;줄 사용됨 (최대 300줄) <span><input
			id="submitTextarea" type="submit" value="제출" class="save"
			style="float: right;" /></span>
</form:form>








<select id="loadSearchedArticles" parameterType="hashmap"
	resultType="Article"> SELECT * FROM article
	<choose> <when test="searchType == 'title_content'">
	WHERE (title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE
	CONCAT('%', #{keyword}, '%')) AND topic = #{topic} </when> <otherwise>
	WHERE (${searchType} LIKE CONCAT('%', #{keyword}, '%')) AND topic =
	#{topic} </otherwise> </choose> ORDER BY num DESC LIMIT #{limit} OFFSET #{offset};
</select>













$(document).on('click', '.deleteReReplyButton', function() {
$('.yPosParam').val($(document).scrollTop()); }); // 댓글 삭제 버튼 누를시 현재 스크롤
위치를 유지하기 위해 scrollTop 값 얻음. $(document).on('click',
'.deleteReReplyButton', function() { deleteYPos =
$(document).scrollTop()); });




<script>
	document.write('<c:param name="yPos" value="' + $(document).scrollTop() + '" />');
</script>

<%-- <c:param name="yPos" value="${(document).scrollTop()}" />--%>


deleteYPos .thisButton 클릭시 deleteYPOS 에 yPos 넣기






<c:url var="deleteReplyLink" value="/board/deleteReply">
	<%-- 삭제 버튼 URL --%>
	<c:param name="articleNum" value="${reply.articleNum}" />
	<c:param name="currentPage" value="${currentPage}" />
	<c:param name="replyNum" value="${reply.num}" />
	<c:param name="customerId" value="${reply.customerId}" />
	<c:param name="depth" value="${reply.depth}" />
	<c:param name="groupOrder" value="${reply.groupOrder}" />
	<c:param name="groupNum" value="${reply.groupNum}" />
	<c:param name="topic" value="${currentTopic}" />
	<c:param name="yPos" value="999999" />



</c:url>











<dependency> <groupId>com.fasterxml.jackson.core</groupId>
<artifactId>jackson-core</artifactId> <version>2.8.8</version> </dependency>

<dependency> <groupId>com.fasterxml.jackson.core</groupId>
<artifactId>jackson-annotations</artifactId> <version>2.8.8</version> </dependency>

<dependency> <groupId>com.fasterxml.jackson.core</groupId>
<artifactId>jackson-databind</artifactId> <version>2.8.8</version> </dependency>






