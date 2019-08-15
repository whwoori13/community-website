<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<title>${article.title}|CommunityWebsite</title>
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
</style>
<script>
	$(document).ready(function() {

		$('#header').load('../resources/html/header.html');
		$('#nav').load('../resources/html/nav.jsp');
		$('#sidebar').load('../resources/html/sidebar.jsp');
		$('#footer').load('../resources/html/footer.html');

		topic = '${currentTopic}';
	});
</script>


<script>
	$(document).ready(function() {
		var yPos = '${yPos}';

		// 댓글 페이지가 넘어갔을때를 가리키도록 컨트롤러에서 -1을 할당함.                                              
		if (yPos == -1) {
			// 댓글 페이지가 넘어갔을때 댓글영역 맨 위로 스크롤 위치.
			location.hash = "repTop";
		}
		// 댓글 페이지가 넘어가지 않았을때 : 스크롤 위치 그대로 유지.
		else
			// 알수없는 이유로 원래 위치에서 조금씩 밑으로 내려가길래 조정해줌.
			$(document).scrollTop(yPos - 107);

	});
</script>


<script>
	// 댓글 작성 버튼 누를시 현재 스크롤 위치를 유지하기 위해 form에 포함하여 전송.
	$(document).on('click', '#submitTextareaRoot', function() {
		//$('#yPos').val($(document).scrollTop());
		$('#yPos').val($(document).scrollTop() | 0);
	});
	// 대댓글 작성 버튼 누를시 현재 스크롤 위치를 유지하기 위해 form에 포함하여 전송.
	$(document).on('click', '.submitTextareaRep', function() {
		//$('.yPos').val($(document).scrollTop());
		$('.yPos').val($(document).scrollTop() | 0);
	});
	// 댓글 삭제 버튼 누를시 현재 스크롤 위치를 유지하기 위해 url에 파라미터 할당
	$(document).on('click', '.thisButton', function() {
		//deleteYPos = $(document).scrollTop();
		deleteYPos = $(document).scrollTop() | 0;
		$('.deleteYPOS').val(deleteYPos)
		return confirm("정말 삭제하시겠습니까?");
	});
</script>
<script>
	// id 속성이 delete인 버튼 클릭 시 confirm창 띄움
	$(document).on('click', '#delete', function() {
		return confirm("정말 삭제하시겠습니까?");
	});

	// 대댓글 버튼 클릭시
	$(document).on('click', '.reReplyButton', function() {

		// 해당 댓글의 id get
		var replyId = $(this).attr('id');

		// 해당 대댓글 입력란이 열려있을때
		if ($("#" + replyId + ".reReplyForm").is(":visible")) {

			$("#" + replyId + ".reReplyForm").hide();
		}

		// 해당 대댓글 입력란이 닫혀있을때
		else {
			// 다른 댓글의 대댓글 입력란 열려있는거 닫음.
			$(".reReplyForm:visible").hide();
			$("#" + replyId + ".reReplyForm").show();
		}

	});
</script>

<script type='text/javaScript'>
	// 대댓글 textarea max rows 제한.
	$(document).ready(function() {

		var lines = 30; // max rows

		$('.countMe').keydown(function(e) {

			// 각 for문에서 form을 div로 묶고 div에 replyNum이 id로 할당되어있다.
			var replyId = $(this).parent().parent().attr('id');
			var linesUsed = $('.' + replyId + ' .linesUsed');

			var newLines = $(this).val().split("\n").length;
			linesUsed.text(newLines);

			if (e.keyCode == 13 && newLines >= lines) {
				linesUsed.css('color', 'red');
				return false;
			} else {
				linesUsed.css('color', '');

			}
		});

		$(document).on('click', '.submitTextarea', function(e) {

			var replyId = $(this).parent().parent().parent().attr('id');

			var reg = /^\s*$/;
			var content = $('.' + replyId + ' .reRepTxt').val();

			if (content == null)
				content = '';

			if (reg.test(content)) {
				e.preventDefault();
				alert('내용을 입력하세요');
			}

			var newLines = $('.' + replyId + ' .countMe').val().split("\n").length;
			if (newLines == null)
				newLines = 0;

			if (newLines > lines) {
				e.preventDefault();
				alert('줄이 너무 많습니다.');
			}
		});
	});
</script>

<script type='text/javaScript'>
	// root 댓글 textarea max rows 제한.
	$(document).ready(function() {

		var lines = 30; // max rows
		var linesUsed = $('#linesUsedRoot');

		$('#countMeRoot').keydown(function(e) {

			newLines = $(this).val().split("\n").length;
			linesUsed.text(newLines);

			if (e.keyCode == 13 && newLines >= lines) {
				linesUsed.css('color', 'red');
				return false;
			} else {
				linesUsed.css('color', '');
			}
		});

		$(document).on('click', '#submitTextareaRoot', function(e) {

			var reg = /^\s*$/;
			var content = $('#countMeRoot').val();

			if (content == null)
				content = '';

			if (reg.test(content)) {
				e.preventDefault();
				alert('내용을 입력하세요');
			}

			if (newLines > lines) {
				e.preventDefault();
				alert('줄이 너무 많습니다.');

			}

		});
	});
</script>
<script>
	//즐찾 추가 버튼     
	$(document).on('click', '.addFav', function(e) {
		$.get("<c:url value='/addFav?topic=" + topic + "'/>", function() {
			$(".addFav").remove();
			$(".favBtnSpn").append("<button class='deFav'></button>");
		});
	});

	// 즐찾 해제 버튼
	$(document).on('click', '.deFav', function(e) {
		$.get("<c:url value='/deFav?topic=" + topic + "'/>", function() {
			$(".deFav").remove();
			$(".favBtnSpn").append("<button class='addFav'></button>");
		});

	});
</script>
<style>
input.button {
	font-size: 13.5px;
}
</style>


</head>
<body>
	<div class="wrapper">
		<div class="content">
			<div id="header"></div>
			<div id="nav"></div>
			<br /> <br />
			<div id="table">

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
				<table>
					<tr>
						<th>Num</th>
						<th>제목</th>
						<th>날짜</th>
						<th>작성자</th>
						<th>조회수</th>
					</tr>
					<tr>
						<td><span style="font-size: 13.5px;">${article.num}</span></td>
						<td width="550"><span style="font-size: 15px;">${article.title}</span></td>
						<td><span style="font-size: 13.5px;">${article.writeDate}</span></td>
						<td><span style="font-size: 13.5px;">${article.customerId}</span></td>
						<td><span style="font-size: 13.5px;">${article.viewCount}</span></td>
					</tr>
				</table>
			</div>
			<div
				<%-- 게시글 내용 영역 --%>
		style="border: 1px ridge lightgray; width: 50%; min-width: 960px; min-height: 130px; margin: auto; margin-top: 20px; margin-bottom: 20px; padding-left: 20px; padding-right: 20px; padding-top: 20px; padding-bottom: 20px; word-wrap: break-word; background-color: white;">

				<p style="font-size: 15px;">${article.content}</p>

			</div>



			<c:if test="${ sessionScope.customer.id == article.customerId }">

				<c:url var="deleteLink" value="/board/deleteArticle">
					<c:param name="articleNum" value="${article.num}" />
					<c:param name="customerId" value="${article.customerId}" />
					<c:param name="topic" value="${currentTopic}" />
				</c:url>
				<c:url var="editLink" value="/board/editArticleForm">
					<c:param name="articleNum" value="${article.num}" />
					<c:param name="currentPage" value="${currentPage}" />
					<c:param name="topic" value="${currentTopic}" />
				</c:url>
				<div style="width: 50%; min-width: 960px; margin: auto;">
					<span style="float: right;"> <a href="${deleteLink}"
						id="delete"><input type="button" class="button" value="삭제" /></a>



					</span> <span style="float: right; margin-right: 5px;"> <a
						href="${editLink}"><input type="button" class="button"
							value="수정" /></a>
					</span>
				</div>
			</c:if>





			<br /> <br /> <br />

			<div id=repTop>
				<c:choose>
					<c:when test="${replyList != null}">
						<%-- 댓글이 1개 이상 있을때 --%>

						<div
							style="border: 1px ridge silver; width: 50%; min-width: 960px; margin: auto;">
							<%-- 댓글 영역 --%>
							<c:forEach items="${replyList}" var="reply">

								<%-- 각 댓글 상자 --%>
								<div id="root_${reply.num}"
									style="border: 1px solid lightgray; margin-top: 5px; margin-left: ${(1 + reply.depth * 3 <= 49)?(1 + reply.depth * 3) : 49}%; <%-- 대댓글 최대 indent 제한 --%>
							margin-bottom: 5px; margin-right: 1%; background-color: ${(reply.depth > 0) ? 'rgb(245,245,245)' : 'white' }; word-wrap:break-word;">

									<%-- 숨김 처리가 아닐때 댓글--%>
									<c:if test="${reply.isDeleted == 0}">

										<c:if test="${reply.depth > 0}">
											<span style="margin-left: 5px; float: left;">┖</span>
										</c:if>
										<span style="margin-left: 10px; float: left; font-size: 14px">
											<strong> ${reply.customerId}</strong>
										</span>
										<c:if test="${ sessionScope.customer != null }">
											<span
												style="margin-right: 1px; margin-top: 1px; float: right">
												<button id="${reply.num}" class="reReplyButton">댓글</button>
											</span>
										</c:if>
										<c:if test="${ sessionScope.customer.id == reply.customerId }">
											<span
												style="margin-right: 5px; margin-top: 1px; float: right">

												<form action="deleteReply" method="GET" name="myForm">
													<input type="hidden" name="articleNum"
														value="${reply.articleNum}" /> <input type="hidden"
														name="currentPage" value="${currentPage}" /> <input
														type="hidden" name="replyNum" value="${reply.num}" /> <input
														type="hidden" name="customerId"
														value="${reply.customerId}" /> <input type="hidden"
														name="depth" value="${reply.depth}" /> <input
														type="hidden" name="groupOrder"
														value="${reply.groupOrder}" /> <input type="hidden"
														name="groupNum" value="${reply.groupNum}" /> <input
														type="hidden" name="yPos" value="" class="deleteYPOS" />
													<input type="hidden" name="topic" value="${currentTopic}" />
													<input type="submit" class="thisButton" value="삭제"
														style="font-size: 13.5px;" />
												</form>

											</span>

										</c:if>

										<c:set var="dateParts"
											value="${fn:split(reply.writeDate, ' ')}" />
										<span
											style="margin-right: 5px; float: right; font-size: 12.5px;"><strong>${dateParts[0]}</strong>
											${dateParts[1]}</span>

										<p
											style="margin-left: 10px; margin-top: 30px; font-size: 15px;">${reply.content}</p>

									</c:if>

									<%-- 숨김 처리 : 대댓글이 있는 삭제된 댓글 --%>
									<c:if test="${reply.isDeleted == 1}">
										<c:if test="${reply.depth > 0}">
											<span style="margin-left: 5px; float: left; color: silver;">┖</span>
										</c:if>
										<span
											style="margin-left: 10px; float: left; color: silver; font-size: 13.5px;">익명</span>

										<span style="margin-right: 5px; margin-top: 1px; float: right">

										</span>
										<c:set var="dateParts"
											value="${fn:split(reply.writeDate, ' ')}" />

										<span
											style="margin-right: 5px; float: right; font-size: 12.5px; color: silver;"><strong>${dateParts[0]}</strong>
											${dateParts[1]}</span>

										<p
											style="margin-left: 10px; margin-top: 30px; color: silver; font-size: 15px;">삭제된
											댓글입니다.</p>



									</c:if>
								</div>
								<%-- 각 댓글 상자 --%>

								<c:if test="${ sessionScope.customer != null }">
									<%-- 숨겨진 대댓글 입력란 --%>
									<div style="margin-left: 1%; margin-right: 1%; display: none;"
										class="reReplyForm ${reply.num}" id="${reply.num}">

										<form action="saveReReply" method="GET" name="myForm">

											<input type="hidden" name="parentGrpOrd"
												value="${(reply.groupOrder)}"> <input type="hidden"
												name="depth" value="${(reply.depth) + 1}"> <input
												type="hidden" name="currentPage" value="${currentPage}">
											<input type="hidden" name="articleNum"
												value="${reply.articleNum}"> <input type="hidden"
												name="groupNum" value="${reply.groupNum}"> <input
												type="hidden" name="topic" value="${currentTopic}">
											<input type="hidden" class="yPos" name="yPos" value="0">


											<textarea id="myForm" class="text countMe reRepTxt" rows="5"
												name="content" placeholder="이곳에 댓글을 입력하세요." maxlength="3000"
												style="width: 100%; resize: none; font-size: 15px; background-color: rgb(245, 245, 245);"></textarea>
											<span class="linesUsed" style="font-size: 15px;"></span><span
												style="font-size: 15px;">줄 사용됨 (최대 30줄)</span> <span
												style="font-size: 15px important!;"><input
												id="submitTextarea${reply.num}"
												class="submitTextarea submitTextareaRep reRepSub"
												type="submit" value="작성" class="save" style="float: right;" /></span>
										</form>


										<br />

									</div>
									<%-- 숨겨진 대댓글 입력란 --%>
								</c:if>


							</c:forEach>
						</div>
						<%-- 댓글 영역 --%>

						<div id="pager">
							<%-- 페이징 바 --%>
							<table style="margin: auto">
								<tr>
									<c:choose>
										<c:when test="${currentPage == 1}">
											<td><a style="color: red;">처음</a>&nbsp;</td>
											<td><a style="color: black;">&#9664;이전</a>&nbsp;|</td>
										</c:when>
										<c:otherwise>
											<td><a style="color: black;"
												href="/community/board/readArticle?currentPage=1&articleNum=${article.num}&topic=${currentTopic}#repTop">처음</a>&nbsp;</td>
											<td><a style="color: black;"
												href="/community/board/readArticle?currentPage=${currentPage - 1}&articleNum=${article.num}&topic=${currentTopic}#repTop">&#9664;이전</a>&nbsp;|</td>
										</c:otherwise>
									</c:choose>

									<c:forEach var="i" begin="${startPage}" end="${lastPage}"
										step="1">

										<c:choose>
											<c:when test="${i != currentPage}">
												<td><a style="color: black;"
													href="/community/board/readArticle?currentPage=${i}&articleNum=${article.num}&topic=${currentTopic}#repTop">
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
												href="/community/board/readArticle?currentPage=${currentPage + 1}&articleNum=${article.num}&topic=${currentTopic}#repTop">다음&#9654;</a></td>
											<td>&nbsp;<a style="color: black;"
												href="/community/board/readArticle?currentPage=${endPage}&articleNum=${article.num}&topic=${currentTopic}#repTop">끝</a></td>
										</c:when>
										<c:otherwise>
											<td>&nbsp;<a style="color: black;">다음&#9654;</a></td>
											<td>&nbsp;<a style="color: red;">끝</a></td>
										</c:otherwise>
									</c:choose>

								</tr>
							</table>
						</div>

					</c:when>
					<%-- 댓글이 1개 이상 있을때 --%>
				</c:choose>

				<br />

				<c:choose>
					<%-- 대댓글이 아닌 최초 댓글 작성 박스 --%>

					<c:when test="${sessionScope.customer == null}">
						<%-- 비로그인시 댓글작성 비활성화 --%>
						<br />
						<table style="margin: auto;">
							<tr>
								<td><p style="color: gray; font-size: 17px;">댓글을 쓰려면
										로그인 하십시오.</p></td>
							</tr>
						</table>
					</c:when>

					<c:otherwise>
						<%-- 로그인시 댓글작성 활성화 --%>
						<div style="width: 50%; min-width: 960px; margin: auto;">
							<form:form action="saveReply" modelAttribute="newReply"
								method="GET">
								<form:hidden path="articleNum" value="${article.num}" />
								<form:hidden path="customerId"
									value="${sessionScope.customer.id}" />
								<input type="hidden" name="topic" value="${currentTopic}">
								<input id="yPos" type="hidden" name="yPos" value="">

								<form:textarea id="countMeRoot" type="text" path="content"
									rows="5" placeholder="이곳에 댓글을 입력하세요."
									style="width:100%; resize: none; font-size: 15px"
									maxlength="3000" />
								<span id="linesUsedRoot" style="font-size: 15px;"></span>
								<span style="font-size: 15px;">줄 사용됨 (최대 30줄)</span>
								<span> <input id="submitTextareaRoot" type="submit"
									value="작성" class="save" style="float: right;" />
								</span>
							</form:form>
						</div>

					</c:otherwise>

				</c:choose>

			</div>

			<br /> <br /> <br /> <br /> <br />
			<div id="table">
				<table>
					<thead>
						<tr>
							<th>Num</th>
							<th>제목</th>
							<th>날짜</th>
							<th>작성자</th>
							<th>조회수</th>

						</tr>
					</thead>
					<tbody>
						<c:forEach items="${articleList}" var="articleFromList">
							<c:url var="readArticleLink" value="/board/readArticle">
								<c:param name="articleNum" value="${articleFromList.num}" />
								<c:param name="topic" value="${currentTopic}" />
							</c:url>
							<c:url var="readArticleSearchedLink" value="/board/readArticle">
								<c:param name="articleNum" value="${articleFromList.num}" />
								<c:param name="topic" value="${currentTopic}" />
								<c:param name="keyword" value="${keyword}" />
								<c:param name="searchType" value="${searchType}" />
							</c:url>

							<%-- 현재 글 표시 이펙트 --%>
							<c:choose>
								<c:when test="${article.num == articleFromList.num}">
									<tr style="background-color: rgb(218, 237, 224);">
								</c:when>
								<c:otherwise>
									<tr>
								</c:otherwise>
							</c:choose>
							<td><span style="font-size: 13.5px;">${articleFromList.num}</span></td>
							<%-- 현재 글 표시 이펙트 --%>
							<c:choose>
								<c:when test="${article.num == articleFromList.num}">
									<td width="625"><span style="font-size: 15px;"><a
											style="color: #6644DD;" href="#">${articleFromList.title}</a>&nbsp;(${articleFromList.replyCount})</span></td>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${keyword == null}">
											<td><span style="font-size: 15px;"><a
													href="${readArticleLink}">${articleFromList.title}</a>&nbsp;(${articleFromList.replyCount})</span></td>
										</c:when>
										<c:otherwise>
											<td><span style="font-size: 15px;"><a
													href="${readArticleSearchedLink}">${articleFromList.title}</a>&nbsp;(${articleFromList.replyCount})</span></td>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>

							<jsp:useBean id="now" class="java.util.Date" />
							<c:set var="dateParts"
								value="${fn:split(articleFromList.writeDate, ' ')}" />
							<fmt:formatDate pattern="yyyy-MM-dd" value="${now}" var="today" />

							<c:choose>
								<c:when test="${dateParts[0] == today}">
									<td><span style="font-size: 13.5px;">${dateParts[1]}</span></td>
								</c:when>
								<c:otherwise>
									<td><span style="font-size: 13.5px;">${dateParts[0]}</span></td>
								</c:otherwise>
							</c:choose>

							<td><span style="font-size: 13.5px;">${articleFromList.customerId}</span></td>
							<td><span style="font-size: 13.5px;">${articleFromList.viewCount}</span></td>
							</tr>

						</c:forEach>
					</tbody>
				</table>

				<div
					style="width: 50%; min-width: 960px; margin-top: 10px; margin-bottom: 0px; margin-left: auto; margin-right: auto;">
					<span style="float: righnt;"> <c:if
							test="${keyword == null}">
							<a href="/community/board/list?topic=${currentTopic}">
						</c:if> <c:if test="${keyword != null}">
							<a
								href="/community/board/searchList?topic=${currentTopic}&searchType=${searchType}&keyword=${keyword}">
						</c:if> <input style="font-size: 18px;" type="button" class="button"
						value="목록" /></a></span>
				</div>
			</div>
		</div>
		<div id="sidebar"></div>
	</div>


	<br />
	<br />
	<br />
	<br />
	<br />
	<br />

	<footer id="footer"></footer>
</body>
</html>

