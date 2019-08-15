<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<style>
/* Navbar container */
.navbar {
	overflow: hidden;
	background-color: #333;
	font-family: Arial;
	width: 50%;
	margin: auto;
	min-width: 960px;
}

/* Links inside the navbar */
.navbar a {
	float: left;
	font-size: 16px;
	color: white;
	text-align: center;
	padding: 12px 16px;
	text-decoration: none;
}

/* The dropdown container */
.dropdown {
	float: left;
	overflow: hidden;
}

/* Dropdown button */
.dropdown .dropbtn {
	font-size: 16px;
	border: none;
	outline: none;
	color: white;
	padding: 12px 16px;
	background-color: inherit;
	font-family: inherit;
	/* Important for vertical align on mobile phones */
	margin: 0; /* Important for vertical align on mobile phones */
}

/* Add a red background color to navbar links on hover */
.navbar a:hover, .dropdown:hover .dropbtn {
	background-color: #111;
}

/* Dropdown content (hidden by default) */
.dropdown-content {
	display: none;
	position: absolute;
	background-color: #f9f9f9;
	min-width: 160px;
	box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
	z-index: 1;
}

/* Links inside the dropdown */
.dropdown-content a {
	float: none;
	color: black;
	padding: 12px 16px;
	text-decoration: none;
	display: block;
	text-align: left;
}

/* Add a grey background color to dropdown links on hover */
.dropdown-content a:hover {
	background-color: #ddd;
}

/* Show the dropdown menu on hover */
.dropdown:hover .dropdown-content {
	display: block;
}
/* Show the dropdown menu on hover 
@media screen and (max-width: 770px) {
	div.loggedin {
		width: 100%;
		margin: auto;
	}
	.loggedin span {
		float: right;
	}
}

@media screen and (min-width: 770px) {
	div.loggedin {
		width: 770px;
		margin: auto;
	}
	.loggedin span {
		float: right;
	}
}
*/
</style>
</head>
<body>
	<div class="navbar">
		<a href="/community/intro">기능 소개</a>

		<%--======================================================================== --%>
		<c:forEach items="${sessionScope.categoryList}" var="category">
			<div class="dropdown">
				<button class="dropbtn">${category.name}</button>
				<div class="dropdown-content">
					<c:forEach items="${sessionScope.CTmatchList}" var="CTmatch">
						<c:if test="${category.code == CTmatch.category}">
							<c:forEach items="${sessionScope.topicList}" var="topic">
								<c:if test="${CTmatch.topic == topic.code}">
									<a href="/community/board/list?topic=${CTmatch.topic}">${topic.name}</a>
								</c:if>
							</c:forEach>
						</c:if>
					</c:forEach>
				</div>
			</div>
		</c:forEach>
		<%--======================================================================== --%>

		<c:choose>
			<c:when test="${sessionScope.customer != null}">
				<a style="float: right;" href="/community/logout">로그아웃</a>

				<div style="float: right;" class="dropdown">
					<button class="dropbtn">
						마이 페이지 <i class="fa fa-caret-down"></i>
					</button>
					<div class="dropdown-content">
						<c:if test="${sessionScope.customer.id == 'admin'}">
							<a style="color: red;" href="/community/admin/adminPage"><strong>관리자
									페이지</strong></a>
						</c:if>
						<a href="/community/myInfo">나의 정보</a> <a
							href="/community/board/list?topic=myArticles">내가 쓴 글</a>
					</div>
				</div>


				<c:if test="${fn:length(favList) > 0}">
					<div style="float: right;" class="dropdown">
						<button class="dropbtn">
							즐겨 찾기 <i class="fa fa-caret-down"></i>
						</button>
						<div class="dropdown-content">

							<c:forEach items="${sessionScope.favList}" var="fav">
								<c:forEach items="${sessionScope.topicList}" var="topic">
									<c:if test="${topic.code == fav}">
										<a href="/community/board/list?topic=${fav}">${topic.name}</a>
									</c:if>
								</c:forEach>
							</c:forEach>

						</div>
					</div>
				</c:if>

			</c:when>
			<c:otherwise>
				<a style="float: right;" href="/community/loginForm">로그인 / 회원가입</a>

			</c:otherwise>
		</c:choose>
	</div>
	<%--======================================================================== --%>
	<div style="width: 50%; min-width: 960px; margin: auto;"
		class="loggedin">
		<c:if test="${ sessionScope.customer != null }">
			<span style="float: right;">
				<p
					style="text-align: center; margin-top: 4px; margin-right: 3px; color: red; font-size: 15px;">

					<c:if test="${sessionScope.customer.id != 'admin'}">
							접속중: ${sessionScope.customer.id}
						</c:if>
					<c:if test="${sessionScope.customer.id == 'admin'}">
							접속중: 관리자 계정
						</c:if>
				</p>
			</span>

		</c:if>
	</div>
	<%--======================================================================== --%>

</body>
</html>

