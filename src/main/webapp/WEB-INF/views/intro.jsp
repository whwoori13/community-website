<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>기능 소개</title>
<link href="<c:url value= "/resources/css/site.css" />" rel="stylesheet" />
<script src="<c:url value= "/resources/js/jquery.min.js" />"></script>
<script>
	$(document).ready(function() {
		$('#nav').load('resources/html/nav.jsp');
		$('#header').load('resources/html/header.html');
		$('#footer').load('resources/html/footer.html');

		$(document).on('click', '.showImg1', function() {
			$('.img1').slideDown(500);                                 

		});
		$(document).on('click', '.showImg2', function() {
			$('.img2').slideDown(500);

		});
		$(document).on('click', '.showImg3', function() {
			$('.img3').slideDown(500);

		});
		$(document).on('click', '.showImg4', function() {
			$('.img4').slideDown(500);

		});
		$(document).on('click', '.showImg5', function() {
			$('.img5').slideDown(500);

		});

		$(document).on('click', 'img', function() {
			$(this).slideUp(500);

		});

	});
</script>
<style>
img {
	display: none;
}
</style>
</head>
<body>
	<div class="wrapper">
		<div class="content">
			<div id="header"></div>
			<div id="nav"></div>
			<br /> <br />
			<h2>사용된 기술</h2>
			<hr>
			<p>Spring, JSP, MySQL, MyBatis, Jquery</p>
			<br /> <br />
			<h2>1. 실시간 인기순 홈 화면</h2>
			<hr>
			<p>최근 100개의 전체 게시물에 대하여 각 게시물이 속한 게시판별로 조회수를 합산해서 인기 게시판을 선정하고, 그
				순서대로 홈 화면에 나타냅니다.</p>
			<button class="showImg1">이미지 보기</button>
			<br /> <br /> <img class="img1" src="resources/img/1.png"> <br />
			<br />
			<h2>2. 카테고리, 게시판 등록</h2>
			<hr>
			<p>새로운 카테고리와 게시판을 추가할 수 있습니다.</p>
			<button class="showImg2">이미지 보기</button>
			<br /> <br /> <img class="img2" src="resources/img/2.png"> <br />
			<br />
			<h2>3. 즐겨 찾기</h2>
			<hr>
			<p>새로고침 없이 즐겨찾기를 추가/삭제할 수 있습니다.</p>
			<button class="showImg3">이미지 보기</button>
			<br /> <br /> <img class="img3" src="resources/img/3.png"> <br />
			<br />
			<h2>4. 무한 계층 댓글 & 연쇄 삭제</h2>
			<hr>
			<p>마지막 대댓글 삭제시 연속된 숨김 댓글 체인을 통째로 삭제합니다.</p>
			<button class="showImg4">이미지 보기</button>
			<br /> <br /> <img class="img4" src="resources/img/4.png"> <br />
			<br />
			<h2>5. 실시간 댓글 알림</h2>
			<hr>
			<p>3초 주기로 ajax GET 요청을 보내서 화면 새로고침 없이 실시간으로 댓글 알림을 받을 수 있습니다.</p>
			<button class="showImg5">이미지 보기</button>
			<br /> <br /> <img class="img5" src="resources/img/5.png"> <br />
			<br /> <br />

		</div>
	</div>
	<footer id="footer"></footer>
</body>
</html>
