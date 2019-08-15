
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>Admin Page</title>
<link href="<c:url value= "/resources/css/site.css" />" rel="stylesheet" />
<script src="<c:url value= "/resources/js/jquery.min.js" />"></script>
<style>
select.match {
	font-size: 15px;
	margin-right: 3px;
	float: left;
	min-width: 80px;
	display: inline-block;
}

div.inputarea {
	width: 100%;
	min-width: 960px;
	padding: 15px 0 10px 15px;
	margin: auto;
	margin-bottom: 35px;
	margin-top: 0px;
	border: 1px solid gray;
}

.form input {
	font-size: 15px;
}
</style>
<style>
.tooltip {
	position: relative;
	display: inline-block;
}

.tooltip .tooltiptext {
	visibility: hidden;
	width: 150px;
	background-color: #555;
	color: #fff;
	text-align: center;
	font-size: 13.5px;
	border-radius: 6px;
	padding: 5px 0;
	position: absolute;
	z-index: 1;
	bottom: 135%;
	left: 15%;
	margin-left: -40px;
	opacity: 0;
	transition: opacity 0.3s;
	border-radius: 6px;
	bottom: 135%;
}

.tooltip .tooltiptext::after {
	content: "";
	position: absolute;
	top: 100%;
	left: 50%;
	margin-left: -5px;
	border-width: 5px;
	border-style: solid;
	border-color: #555 transparent transparent transparent;
}

.tooltip:hover .tooltiptext {
	visibility: visible;
	opacity: 1;
}
</style>
<script>
	$(document).ready(function() {
		$('#nav').load('../resources/html/nav.jsp');
		$('#header').load('../resources/html/header.html');
		$('#footer').load('../resources/html/footer.html');
	});
</script>
<script>
	function checkDup(input, type) {

		$.getJSON("<c:url value='/APValid?input=" + input + "&type=" + type + "'/>", function(data) {
			dup = data[0].num;

			var txt, div;

			////
			if (type == 'checkcName') {
				txt = 'cNametxt';
				div = 'cNametxtDiv';
			}

			if (type == 'checkcCode') {
				txt = 'cCodetxt';
				div = 'cCodetxtDiv';
			}

			if (type == 'checktName') {
				txt = 'tNametxt';
				div = 'tNametxtDiv';
			}

			if (type == 'checktCode') {
				txt = 'tCodetxt';
				div = 'tCodetxtDiv';
			}
			////
			if (dup === 1) {
				$('.' + txt).remove();
				$('.' + div).append('<p style="color: LimeGreen;" class="'+ txt +'">✔</p>');

				if (type == 'checkcName')
					cNameTest = 1;
				if (type == 'checkcCode')
					cCodeTest = 1;
				if (type == 'checktName')
					tNameTest = 1;
				if (type == 'checktCode')
					tCodeTest = 1;
			}

			if (dup === 0) {

				$('.' + txt).remove();
				$('.' + div).append('<p style="color: red;" class="'+ txt +'">중복된 값입니다.</p>');

				if (type == 'checkcName')
					cNameTest = 0;
				if (type == 'checkcCode')
					cCodeTest = 0;
				if (type == 'checktName')
					tNameTest = 0;
				if (type == 'checktCode')
					tCodeTest = 0;
			}
		});
	}
</script>
<script>
	$(document).ready(function() {

		$(document).on('click', '.delBtn', function() {
			return confirm("정말 삭제하시겠습니까?");
		});
		///////////////////////////////////////////////////////////////

		///////////////////////////////////////////////////////////////
		cNameTest = -1;
		cCodeTest = -1;
		tNameTest = -1;
		tCodeTest = -1;

		$('.cName').focusout(function() {

			var reg = /^[가-힣]{1,10}$/; // 한글
			var cNameIpt = $('.cName').val();

			if (reg.test(cNameIpt)) {
				checkDup(cNameIpt, 'checkcName');
			} else {
				cNameTest = 0;
				$('.cNametxt').remove();
				$('.cNametxtDiv').append('<p style="color: red;" class="cNametxt">한글 10자 이내</p>');
			}
		});

		$('.cCode').focusout(function() {
			var reg = /^[a-zA-Z]{1,30}$/; // 영문                  
			var cCodeIpt = $('.cCode').val();

			if (reg.test(cCodeIpt)) {
				checkDup(cCodeIpt, 'checkcCode');
			} else {
				cCodeTest = 0;
				$('.cCodetxt').remove();
				$('.cCodetxtDiv').append('<p style="color: red;" class="cCodetxt">영문 30자 이내</p>');
			}
		});

		$('.tName').focusout(function() {
			var reg = /^[가-힣]{1,10}$/; // 한글
			var tNameIpt = $('.tName').val();

			if (reg.test(tNameIpt)) {
				checkDup(tNameIpt, 'checktName');
			} else {
				tNameTest = 0;
				$('.tNametxt').remove();
				$('.tNametxtDiv').append('<p style="color: red;" class="tNametxt">한글 10자 이내</p>');
			}
		});

		$('.tCode').focusout(function() {
			var reg = /^[a-zA-Z]{1,30}$/; // 영문                  
			var tCodeIpt = $('.tCode').val();

			if (reg.test(tCodeIpt)) {
				checkDup(tCodeIpt, 'checktCode');
			} else {
				tCodeTest = 0;
				$('.tCodetxt').remove();
				$('.tCodetxtDiv').append('<p style="color: red;" class="tCodetxt">영문 30자 이내</p>');
			}
		});

		$(document).on('click', '.cSubmit', function() {
			if (!(cNameTest === 1 && cCodeTest === 1)) {
				return false;
			}
		});

		$(document).on('click', '.tSubmit', function() {
			if (!(tNameTest === 1 && tCodeTest === 1)) {
				return false;
			}
		});

		$(document).on('click', '.matchBtn', function() {
			var cond1 = $('.cSel option:selected').val()
			var cond2 = $('.tSel option:selected').val()
			if (!(cond1 != null && cond2 != null)) {
				alert('선택되지 않은 항목이 있습니다.');
				return false;
			}
		});

		$(document).on('click', '.unmatchBtn', function() {
			var cond = $('.mSel option:selected').val()
			if (cond == null) {
				alert('선택되지 않은 항목이 있습니다.');
				return false;
			}
		});

	});
</script>

</head>
<body>
	<div id="header"></div>
	<div id="nav"></div>



	<!-- ================================================================================================ -->
	<div
		style="width: 50%; min-width: 960px; margin: auto; margin-top: 40px;">
		<!-- 카테고리 등록 / 삭제 -->
		<p style="margin-bottom: 3px;">
			카테고리 추가/삭제&nbsp;&nbsp;<span style="font-size: 13.5px;">(삭제
				방지됨: 게임, 운동)</span>
		</p>
		<div class="inputarea">
			<form class="form" action="insertCategory" method="POST">
				<div>
					<div class="tooltip">
						<input class="cName" type="text" name="name" size="12"
							placeholder="카테고리 이름" /> <span class="tooltiptext">사용자에게
							보여질 <br />카테고리의 <span style="color: yellow;">한글 </span>이름
						</span>
						<div class="cNametxtDiv">
							<p class="cNametxt">&nbsp;</p>
						</div>
					</div>

					<div class="tooltip">
						<input class="cCode" type="text" name="code" size="12"
							placeholder="카테고리 코드" /> <span class="tooltiptext">카테고리를
							구분하기<br /> 위한 <span style="color: yellow;">영문</span> 코드
						</span>
						<div class="cCodetxtDiv">
							<p class="cCodetxt">&nbsp;</p>
						</div>
					</div>
					<div style="display: inline-block;">
						<input class="cSubmit" type="submit" value="추가" />
						<p>&nbsp;</p>
					</div>
				</div>
			</form>



			<c:forEach items="${sessionScope.categoryList}" var="category"
				varStatus="status">
				<span><strong><a class="delBtn"
						style="display: inline-block; margin-right: 10px; padding-bottom: 10px;"
						href="/community/admin/deleteCategory?code=${category.code}">${category.name}</a></strong></span>
				<c:if test="${(status.index + 1) % 10 == 0}">
					<br />
				</c:if>


			</c:forEach>
		</div>

		<!-- 게시판 등록 / 삭제 -->
		<p style="margin-bottom: 3px;">
			게시판 추가/삭제&nbsp;&nbsp;<span style="font-size: 13.5px;">(삭제 방지됨:
				농구, 리그오브레전드, 수영, 스타크래프트)</span>
		</p>
		<div class="inputarea">
			<form class="form" action="insertTopic" method="POST">
				<div>
					<div class="tooltip">
						<input class="tName" type="text" name="name" size="12"
							placeholder="게시판 이름" /><span class="tooltiptext">사용자에게
							보여질<br /> 게시판의 <span style="color: yellow;">한글</span> 이름
						</span>
						<div class="tNametxtDiv">
							<p class="tNametxt">&nbsp;</p>
						</div>
					</div>

					<div class="tooltip">
						<input class="tCode" type="text" name="code" size="12"
							placeholder="게시판 코드" /> <span class="tooltiptext">게시판을
							구분하기<br /> 위한 <span style="color: yellow;">영문</span> 코드
						</span>
						<div class="tCodetxtDiv">
							<p class="tCodetxt">&nbsp;</p>
						</div>
					</div>



					<div style="display: inline-block;">
						<input class="tSubmit" type="submit" value="추가" />
						<p>&nbsp;</p>
					</div>
				</div>
			</form>



			<c:forEach items="${sessionScope.topicList}" var="topic"
				varStatus="status">
				<span><strong><a class="delBtn"
						style="display: inline-block; margin-right: 10px; padding-bottom: 10px;"
						href="/community/admin/deleteTopic?code=${topic.code}">${topic.name}</a></strong></span>
				<c:if test="${(status.index + 1) % 10 == 0}">
					<br />
				</c:if>

			</c:forEach>

		</div>

		<!-- 매칭 등록 / 삭제 -->
		<p style="margin-bottom: 3px;">카테고리&게시판 매칭/해제</p>
		<div class="inputarea" style="overflow: auto;">

			<select size="10" class="match cSel" name="category" form="matchForm">

				<c:forEach items="${sessionScope.categoryList}" var="category">
					<option value="${category.code}">${category.name}</option>
				</c:forEach>

			</select> <select size="10" class="match tSel" name="topic" form="matchForm">

				<c:forEach items="${sessionScope.topicList}" var="topic">
					<c:set var="show" value="true" />
					<c:forEach items="${sessionScope.CTmatchList}" var="match">
						<c:if test="${match.topic == topic.code}">
							<c:set var="show" value="false" />
						</c:if>
					</c:forEach>
					<c:if test="${show == true}">
						<option value="${topic.code}">${topic.name}</option>
					</c:if>
				</c:forEach>

			</select>

			<form action="match" id="matchForm" method="POST"
				style="display: inline-block; float: left;">
				<input class="matchBtn" style="font-size: 15px;" type="submit"
					value="매칭">
			</form>




			<select size="10" class="match mSel" name="match" form="unmatchForm"
				style="margin-left: 50px;">

				<c:forEach items="${sessionScope.CTmatchList}" var="match">

					<option value="${match.category}-${match.topic}">
						<c:forEach items="${sessionScope.categoryList}" var="category">
							<c:if test="${match.category == category.code}">
								${category.name}
							</c:if>
						</c:forEach>&nbsp;-&nbsp;
						<c:forEach items="${sessionScope.topicList}" var="topic">
							<c:if test="${match.topic == topic.code}">
								${topic.name}
							</c:if>
						</c:forEach>
					</option>
				</c:forEach>
			</select>
			<form action="unmatch" id="unmatchForm" method="POST"
				style="display: inline-block; float: left;">
				<input class="unmatchBtn" style="font-size: 15px;" type="submit"
					value="해제">
			</form>



		</div>

	</div>
	<!-- ================================================================================================ -->



	<footer id="footer"></footer>
</body>
</html>

