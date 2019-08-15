<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>${currentTopic} | CommunityWebsite</title>
<link href="<c:url value= "/resources/css/site.css" />" rel="stylesheet" />
<script src="<c:url value= "/resources/js/jquery.min.js" />"></script>
<style>
.deFav {
	background: url(../resources/img/addFav.png) no-repeat;
	cursor: pointer;
	border: none;
	width: 21px;
	height: 20px;
}

.addFav {
	background: url(../resources/img/deFav.png) no-repeat;
	cursor: pointer;
	border: none;
	width: 21px;
	height: 20px;
}

#table {
	
}
</style>
<script>
	$(document).ready(function() {
		$('#header').load('../resources/html/header.html');
		$('#nav').load('../resources/html/nav.jsp');
		$('#sidebar').load('../resources/html/sidebar.jsp');
		$('#footer').load('../resources/html/footer.html');
		topic = '${currentTopic}';
	});

	$(document).on('click', '#searchButton', function(e) {

		var reg = /^\s*$/;
		var keyword = $('#keyword').val();

		if (keyword == null)
			keyword = '';

		if (reg.test(keyword)) {
			e.preventDefault();
			alert('검색어를 입력하세요.');                                               

		} else {
			e.preventDefault();
			var url = "/community/board/searchList";
			url = url + "?searchType=" + $('#searchType').val();
			url = url + "&keyword=" + $('#keyword').val().trim();
			url = url + "&topic=" + topic;
			location.href = url;
		}
	});                                

	// 즐찾 추가 버튼                                                                               
	$(document).on('click', '.addFav', function() {
		$.get("<c:url value='/addFav?topic=" + topic + "'/>", function() {
			$(".addFav").remove();
			$(".favBtnSpn").append("<button class='deFav'></button>");
		});
	});

	// 즐찾 해제 버튼
	$(document).on('click', '.deFav', function() {
		$.get("<c:url value='/deFav?topic=" + topic + "'/>", function() {
			$(".deFav").remove();
			$(".favBtnSpn").append("<button class='addFav'></button>");
		});

	});
</script>
</head>
<body>
	<div class="wrapper">
		<div class="content">
			<div id="header"></div>
			<div id="nav"></div>

			<br /> <br />


			<div id="table">
				<%-- 게시글 목록 테이블 --%>

				<c:choose>
					<c:when test="${currentTopic != 'myArticles'}">
						<div class="banner"
							style="width: 50%; min-width: 960px; margin: auto; margin-bottom: 2px;">
							<a style="font-size: 25px;"
								href="/community/board/list?topic=${currentTopic}"><strong>${topicName}
									게시판 </strong></a>
							<c:set var="inFav" value="false" />
							<c:forEach items="${sessionScope.favList}" var="fav">
								<c:if test="${fav == currentTopic}">
									<c:set var="inFav" value="true" />
								</c:if>
							</c:forEach>

							<c:if test="${sessionScope.customer != null}">
								<span class="favBtnSpn"> <c:if test="${inFav == false}">
										<button class="addFav"></button>
									</c:if> <c:if test="${inFav == true}">
										<button class="deFav"></button>
									</c:if>
								</span>
							</c:if>
						</div>
					</c:when>
					<c:otherwise>
						<h2 style="margin-left: 5px;">
							<strong>내가 쓴 글</strong>
						</h2>
					</c:otherwise>
				</c:choose>


				<c:choose>
					<c:when test="${articleList.size() == 0}">

						<c:choose>

							<c:when test="${keyword == null}">
								<span style="margin: auto;"><p
										style="color: gray; text-align: center;">작성된 글이 없습니다.</p></span>
							</c:when>
							<c:otherwise>
								<span style="margin: auto;"><p
										style="color: gray; text-align: center;">검색 결과가 없습니다.</p></span>
							</c:otherwise>
						</c:choose>

					</c:when>


					<c:otherwise>
						<table>
							<thead>
								<tr>
									<th>Num</th>
									<th>제목</th>
									<th>날짜</th>
									<th>작성자</th>
									<th>조회수</th>

									<c:if test="${currentTopic == 'myArticles'}">
										<th>게시판</th>
									</c:if>

								</tr>
							</thead>
							<tbody>
								<c:forEach items="${articleList}" var="article">
									<c:url var="readArticleLink" value="/board/readArticle">
										<c:param name="articleNum" value="${article.num}" />
										<c:param name="topic" value="${article.topic}" />
									</c:url>
									<c:url var="readArticleSearchedLink" value="/board/readArticle">
										<c:param name="articleNum" value="${article.num}" />
										<c:param name="topic" value="${article.topic}" />
										<c:param name="searchType" value="${searchType}" />
										<c:param name="keyword" value="${keyword}" />
									</c:url>
									<tr>
										<td><span style="font-size: 13.5px;">${article.num}</span></td>
										<c:choose>
											<c:when test="${keyword == null}">
												<td width="${currentTopic == 'myArticles' ? 550 : 625}"><span
													style="font-size: 15px;"><a
														href="${readArticleLink}">${article.title}</a><span
														style="color: #FE6700;">&nbsp;(${article.replyCount})</span></span></td>
											</c:when>
											<c:otherwise>
												<td width="${currentTopic == 'myArticles' ? 550 : 625}"><span
													style="font-size: 15px;"><a
														href="${readArticleSearchedLink}">${article.title}</a><span
														style="color: #FE6700;">&nbsp;(${article.replyCount})</span></span></td>
											</c:otherwise>
										</c:choose>


										<jsp:useBean id="now" class="java.util.Date" />
										<c:set var="dateParts"
											value="${fn:split(article.writeDate, ' ')}" />
										<fmt:formatDate pattern="yyyy-MM-dd" value="${now}"
											var="today" />


										<c:choose>
											<c:when test="${dateParts[0] == today}">
												<td><span style="font-size: 13.5px;">${dateParts[1]}</span></td>
											</c:when>
											<c:otherwise>
												<td><span style="font-size: 13.5px;">${dateParts[0]}</span></td>
											</c:otherwise>
										</c:choose>


										<td><span style="font-size: 13.5px;">${article.customerId}</span></td>
										<td><span style="font-size: 13.5px;">${article.viewCount}</span></td>

										<c:if test="${currentTopic == 'myArticles'}">
											<c:forEach items="${topicList}" var="topic">
												<c:if test="${topic.code == article.topic}">
													<td><span style="font-size: 13.5px;">${topic.name}</span></td>
												</c:if>
											</c:forEach>
										</c:if>
									</tr>
								</c:forEach>

							</tbody>
						</table>
					</c:otherwise>
				</c:choose>


				<c:if
					test="${ sessionScope.customer != null && currentTopic != 'myArticles' }">

					<div
						style="width: 50%; min-width: 960px; margin-top: 10px; margin-bottom: 0px; margin-left: auto; margin-right: auto;">
						<form:form action="writeArticle" method="GET">

							<input type="hidden" name="topic" value="${currentTopic}" />
							<input style="float: right;" type="submit" value="글쓰기" />

						</form:form>
					</div>
					<br />
					<br />
				</c:if>
				<br />

			</div>












			<div id="pager">

				<table style="margin: auto">
					<tr>
						<c:choose>
							<c:when test="${currentPage == 1}">
								<%-- 1페이지일때 처음, 이전 버튼 비활성화 --%>
								<td><a style="color: red;">처음</a>&nbsp;</td>
								<td><a style="color: black;">&#9664;이전</a>&nbsp;|</td>
							</c:when>
							<c:otherwise>

								<c:choose>
									<%-- 검색한 상태인지 아닌지에 따라 url을 다르게 처리 --%>
									<c:when test="${keyword == null}">
										<%-- 검색한 상태가 아닐때  --%>
										<td><a style="color: black;"
											href="/community/board/list?currentPage=1&topic=${currentTopic}">처음</a>&nbsp;</td>
										<td><a style="color: black;"
											href="/community/board/list?currentPage=${currentPage - 10}&topic=${currentTopic}">&#9664;이전</a>&nbsp;|</td>
									</c:when>
									<c:otherwise>
										<%-- 검색한 상태일때 --%>
										<td><a style="color: black;"
											href="/community/board/searchList?currentPage=1&searchType=${searchType}&keyword=${keyword}&topic=${currentTopic}">처음</a>&nbsp;</td>
										<td><a style="color: black;"
											href="/community/board/searchList?currentPage=${currentPage - 10}&searchType=${searchType}&keyword=${keyword}&topic=${currentTopic}">&#9664;이전</a>&nbsp;|</td>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>

						<c:forEach var="i" begin="${startPage}" end="${lastPage}" step="1">

							<c:choose>
								<c:when test="${i != currentPage}">
									<c:choose>
										<%-- 검색한 상태인지 아닌지에 따라 url을 다르게 처리 --%>
										<c:when test="${keyword == null}">
											<%-- 검색한 상태가 아닐때 --%>
											<td><a style="color: black;"
												href="/community/board/list?currentPage=${i}&topic=${currentTopic}">
													&nbsp;${i}&nbsp;</a>|</td>
										</c:when>
										<c:otherwise>
											<%-- 검색한 상태일때 --%>
											<td><a style="color: black;"
												href="/community/board/searchList?currentPage=${i}&searchType=${searchType}&keyword=${keyword}&topic=${currentTopic}">
													&nbsp;${i}&nbsp;</a>|</td>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<td><a style="color: red;">&nbsp;${i}&nbsp;</a>|</td>
								</c:otherwise>
							</c:choose>
						</c:forEach>

						<c:choose>
							<c:when test="${currentPage != endPage}">
								<c:choose>
									<%--검색한 상태인지 아닌지에 따라 url을 다르게 처리 --%>
									<c:when test="${keyword == null}">
										<%--검색한 상태가 아닐때 --%>
										<td>&nbsp;<a style="color: black;"
											href="/community/board/list?currentPage=${currentPage + 10}&topic=${currentTopic}">다음&#9654;</a></td>
										<td>&nbsp;<a style="color: black;"
											href="/community/board/list?currentPage=${endPage}&topic=${currentTopic}">끝</a></td>
									</c:when>

									<c:otherwise>
										<%--검색한 상태일때--%>
										<td>&nbsp;<a style="color: black;"
											href="/community/board/searchList?currentPage=${currentPage + 10}&searchType=${searchType}&keyword=${keyword}&topic=${currentTopic}">다음&#9654;</a></td>
										<td>&nbsp;<a style="color: black;"
											href="/community/board/searchList?currentPage=${endPage}&searchType=${searchType}&keyword=${keyword}&topic=${currentTopic}">끝</a></td>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<%--마지막 페이지일때 다음, 마지막 버튼 비활성화 --%>
								<td>&nbsp;<a style="color: black;">다음&#9654;</a></td>
								<td>&nbsp;<a style="color: red;">끝</a></td>
							</c:otherwise>
						</c:choose>

					</tr>
				</table>
			</div>
			<br />

			<c:if test="${currentTopic != 'myArticles'}">
				<div id="searchBox">
					<c:choose>
						<c:when test="${keyword != null}">
							<%-- 검색한 상태일때 select 메뉴와 input text의 default value를 지정하기 위해 --%>
							<table style="margin: auto;">
								<tr>
									<td><select id="searchType" style="font-size: 14px">
											<option value="title"
												${(searchType == 'title') ? 'selected="selected"' : '' }>제목</option>
											<option value="content"
												${(searchType == 'content') ? 'selected="selected"' : '' }>내용</option>
											<option value="title_content"
												${(searchType == 'title_content') ? 'selected="selected"' : '' }>제목+내용</option>
											<option value="customer_id"
												${(searchType == 'customer_id') ? 'selected="selected"' : '' }>작성자</option>
									</select></td>
									<td><input type="text" id="keyword" maxlength="20"
										size="10" style="font-size: 15px" value="${keyword}"></td>
									<td><button id="searchButton">검색</td>
								</tr>
							</table>
						</c:when>
						<c:otherwise>
							<table style="margin: auto;">
								<tr>
									<td><select id="searchType" style="font-size: 14px">

											<option value="title">제목</option>
											<option value="content">내용</option>
											<option value="title_content">제목+내용</option>
											<option value="customer_id">작성자</option>

									</select></td>
									<td><input type="text" id="keyword" maxlength="20"
										size="10" style="font-size: 15px"></td>
									<td><button id="searchButton">검색</td>
								</tr>
							</table>
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>


			<!-- 내가 쓴 글일때 내가 쓴 댓글 불러옴. -->
			<c:if test="${currentTopic == 'myArticles'}">
				<br />
				<div id="table2">
					<h2 style="margin-left: 5px;">
						<strong>내가 쓴 댓글</strong>
					</h2>
					<c:choose>
						<c:when test="${replyList.size() == 0}">
							<span style="margin: auto;">
								<p style="color: gray; text-align: center;">작성된 댓글이 없습니다.</p>
							</span>


						</c:when>

						<c:otherwise>
							<table>
								<thead>
									<tr>
										<th>Num</th>
										<th>댓글</th>
										<th>날짜</th>
										<th>작성자</th>
										<th>게시판</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${replyList}" var="reply">
										<tr>
											<td><span style="font-size: 13.5px;">${reply.num}</span></td>
											<td width="600"><span style="font-size: 15px;"><a
													href="/community/board/locate?replyNum=${reply.num}&fromMyRep=1">${reply.content}</a><span
													style="color: #FE6700;"></span></span></td>



											<c:set var="dateParts"
												value="${fn:split(reply.writeDate, ' ')}" />

											<c:choose>
												<c:when test="${dateParts[0] == today}">
													<td><span style="font-size: 13.5px;">${dateParts[1]}</span></td>
												</c:when>
												<c:otherwise>
													<td><span style="font-size: 13.5px;">${dateParts[0]}</span></td>
												</c:otherwise>
											</c:choose>

											<td><span style="font-size: 13.5px;">${reply.customerId}</span></td>


											<c:forEach items="${topicList}" var="topic">
												<c:if test="${topic.code == reply.articleTopic}">
													<td><span style="font-size: 13.5px;">${topic.name}</span></td>
												</c:if>
											</c:forEach>



										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:otherwise>
					</c:choose>
				</div>

				<br />


				<div id="pager2">
					<table style="margin: auto">
						<tr>
							<c:choose>
								<c:when test="${currentPage2 == 1}">
									<%-- 1페이지일때 처음, 이전 버튼 비활성화 --%>
									<td><a style="color: red;">처음</a>&nbsp;</td>
									<td><a style="color: black;">&#9664;이전</a>&nbsp;|</td>
								</c:when>
								<c:otherwise>
									<td><a style="color: black;"
										href="/community/board/list?currentPage2=1&topic=${currentTopic}#table2">처음</a>&nbsp;</td>
									<td><a style="color: black;"
										href="/community/board/list?currentPage2=${currentPage2 - 1}&topic=${currentTopic}#table2">&#9664;이전</a>&nbsp;|</td>
								</c:otherwise>
							</c:choose>

							<c:forEach var="i" begin="${startPage2}" end="${lastPage2}"
								step="1">

								<c:choose>
									<c:when test="${i != currentPage2}">
										<td><a style="color: black;"
											href="/community/board/list?currentPage2=${i}&topic=${currentTopic}#table2">
												&nbsp;${i}&nbsp;</a>|</td>
									</c:when>
									<c:otherwise>
										<td><a style="color: red;">&nbsp;${i}&nbsp;</a>|</td>
									</c:otherwise>
								</c:choose>
							</c:forEach>

							<c:choose>
								<c:when test="${currentPage2 != endPage2}">
									<td>&nbsp;<a style="color: black;"
										href="/community/board/list?currentPage2=${currentPage2 + 1}&topic=${currentTopic}#table2">다음&#9654;</a></td>
									<td>&nbsp;<a style="color: black;"
										href="/community/board/list?currentPage2=${endPage2}&topic=${currentTopic}#table2">끝</a></td>
								</c:when>
								<c:otherwise>
									<td>&nbsp;<a style="color: black;">다음&#9654;</a></td>
									<td>&nbsp;<a style="color: red;">끝</a></td>
								</c:otherwise>
							</c:choose>
						</tr>
					</table>
				</div>
			</c:if>
			<!-- 위 : 내가 쓴 글일때 내가 쓴 댓글 불러옴. -->

		</div>
		<div id="sidebar"></div>
	</div>



	<footer id="footer"></footer>
</body>
</html>