<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
body {
	font-family: "Lato", sans-serif;
}

.sidenav {
	width: 200px;
	height: 300px;
	position: absolute;
	display: inline-block;                                               
	z-index: 1;                        
	margin: 185px 0 0 5px;                                                                                                                     
	background: #eee;                  
	overflow-x: hidden;                                                                     
	padding: 10px 5px 5px 5px;                                                                                                                                                        
	float: left;                        	
	                                                                                                                             
}                                                                                         
                                            
.sidenav a {
	padding: 5px 3px 5px 5px;
	text-decoration: none;
	font-size: 13.5px;
	color: #2196F3;
	display: block;
}

.sidenav a:hover {
	color: #064579;
}
</style>
<script>
	// n개의 새로운 댓글 알림에서 n을 불러오는 함수
	$.getAlertCnt = (function() {

		$.getJSON("<c:url value='/getAlertCnt' />", function(data) {
			var newAlert = data[0].newAlert;

			if (newAlert > 0)
				$(".sidenav").fadeIn(300);

			$(".count").remove();
			$(".countDiv").append(
					"<span class='count'><p style='font-size: 15px; text-align: center;'><span style='color: red;'>" + newAlert
							+ "</span>개의 새로운 댓글 알림  </p></span>");
		});
	});

	// 최근 20개의 알림 오브젝트를 불러오는 함수
	$.getAlert = (function() {

		$.getJSON("<c:url value='/getAlert' />", function(data) {
			$(".alerts").remove();
			$.each(data, function(index) {
				var content = data[index].content;
				var replyNum = data[index].replyNum;
				var author = data[index].author;
				if (author == '${sessionScope.customer.id}')
					author = '본인';
				$(".alertsDiv").append(
						"<div class='alerts'><a href='/community/board/locate?replyNum=" + replyNum + "'> <span style='color: black;'><strong>"
								+ author + "</strong><span/> " + "<span style='color: #2196F3;'>" + content + "</span></a></div>");
			});
		});

	});

	// Polling
	function doPoll() {
		$.getAlertCnt();
		$.getAlert();
		setTimeout(doPoll, 3000);
	}

	// 모두 삭제 버튼
	$(".flushBtn").click(function() {
		$.get("<c:url value='/flushAlert' />", function() {
			$.getAlertCnt();
			$.getAlert();
			$(".sidenav").fadeOut(700);

		});
	});

	$(document).ready(function() {

		doPoll();
	});
</script>
</head>
<body>
	<div class="sidenav" style="display: none;">

		<div class="countDiv">
			<span class="count"></span>
		</div>
		<button class="flushBtn"
			style="float: right; display: inline; margin-right: 8px;">모두
			삭제</button>
		<br />                                                                 

		<div class="alertsDiv" style="margin: 10px 0 0 0;">                          
			<div class="alerts"></div>
		</div>
	</div>
</body>
</html>












