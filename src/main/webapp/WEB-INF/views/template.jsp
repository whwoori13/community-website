
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title></title>
<link href="<c:url value= "/resources/css/site.css" />" rel="stylesheet" />
<script src="<c:url value= "/resources/js/jquery.min.js" />"></script>
<script>
	$(document).ready(function() {
		$('#nav').load('resources/html/nav.jsp');
		$('#header').load('resources/html/header.html');
		$('#footer').load('resources/html/footer.html');
	});
</script>
</head>
<body>
	<div id="header"></div>
	<div id="nav"></div>
	<br />
	<br />



	<footer id="footer"></footer>
</body>
</html>
<!-- above : basic tamplate -->


<!-- below : pager tamplate : 링크 적절하게 수정해서 사용할것 -->

<div id="pager">
	<table style="margin: auto">
		<tr>
			<c:choose>
				<c:when test="${currentPage == 1}">
					<td><a style="color: red;">처음</a>&nbsp;</td>
					<td><a style="color: black;">&lt;이전</a>&nbsp;|</td>
				</c:when>
				<c:otherwise>
					<td><a style="color: black;"
						href="/community/board/listPaged?currentPage=1">처음</a>&nbsp;</td>
					<td><a style="color: black;"
						href="/community/board/listPaged?currentPage=${currentPage - 10}">&lt;이전</a>&nbsp;|</td>
				</c:otherwise>
			</c:choose>

			<c:forEach var="i" begin="${startPage}" end="${lastPage}" step="1">

				<c:choose>
					<c:when test="${i != currentPage}">
						<td><a style="color: black;"
							href="/community/board/listPaged?currentPage=${i}">
								&nbsp;${i}&nbsp;</a>|</td>
					</c:when>
					<c:otherwise>
						<td><a style="color: red;">&nbsp;${i}&nbsp;</a>|</td>
					</c:otherwise>
				</c:choose>
			</c:forEach>

			<c:choose>
				<c:when test="${currentPage != endPage}">
					<td>&nbsp;<a style="color: black;"
						href="/community/board/listPaged?currentPage=${currentPage + 10}">다음&gt;</a></td>
					<td>&nbsp;<a style="color: black;"
						href="/community/board/listPaged?currentPage=${endPage}">마지막</a></td>
				</c:when>
				<c:otherwise>
					<td>&nbsp;<a style="color: black;">다음&gt;</a></td>
					<td>&nbsp;<a style="color: red;">마지막</a></td>
				</c:otherwise>
			</c:choose>

		</tr>
	</table>
</div>


<!-- above : pager tamplate : 링크 적절하게 수정해서 사용할것 -->



<div id="table">
	<%-- 댓글 --%>
	<table>
		<tr>
			<th>작성자</th>
			<th>날짜</th>
			<th>삭제</th>
			<th>댓글</th>
		</tr>
		<c:forEach items="${replyList}" var="reply">
			<tr>
				<td>${reply.customerId}</td>
				<td>${reply.writeDate}</td>
				<c:url var="deleteReplyLink" value="/board/deleteReply">
					<c:param name="articleNum" value="${reply.articleNum}" />
					<c:param name="replyNum" value="${reply.num}" />
					<c:param name="customerId" value="${reply.customerId}" />
					<c:param name="currentPage" value="${currentPage}" />
				</c:url>
				<c:url var="reReplyLink" value="/board/deleteReply">
					<%-- 대댓글 URL --%>
					<c:param name="articleNum" value="${reply.articleNum}" />
					<c:param name="replyNum" value="${reply.num}" />
					<c:param name="customerId" value="${reply.customerId}" />
					<c:param name="currentPage" value="${currentPage}" />
				</c:url>
				<td><button>
						<a href="${deleteReplyLink}" class="button" id="delete">삭제</a>
					</button></td>
				<td><button>
						<a href="${reReplyLink}" class="button">댓글</a>
					</button></td>
			</tr>
			<tr>
				<td colspan="4">${reply.content}</td>
			</tr>
		</c:forEach>
	</table>
</div>

<!-- above : 기존 테이블형 댓글박스 백업용, 3->4칼럼으로 바뀐 형태임. -->





<%-- 캐싱 비활성화 --%>
<%        
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setDateHeader("Expires", -1);
%>




















