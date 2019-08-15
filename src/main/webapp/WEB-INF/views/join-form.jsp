<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Join Form</title>
<link href="<c:url value= "/resources/css/site.css" />" rel="stylesheet" />
<script src="<c:url value= "/resources/js/jquery.min.js" />"></script>
<script>
	$(document).ready(function() {
		$('#header').load('resources/html/header.html');
		$('#footer').load('resources/html/footer.html');

		idDup = -1;
		idReg = -1;
		pwRpt = -1;
		pwReg = -1;
		email = 1;
	});
</script>
<script>
	$.checkIdDup = (function() {

		$.getJSON("<c:url value='/checkIdDup?inputId=" + inputId + "' />", function(data) {
			idDup = data[0].num;

			if (idDup === 1) {
				$(".idtxt").remove();
				$(".idtxtSpn").append('<p style="color: LimeGreen;"class="idtxt">✔</p>');
			}

			if (idDup === 0) {
				$(".idtxt").remove();
				$(".idtxtSpn").append('<p style="color: red;"class="idtxt">중복된 ID 입니다.</p>');
			}
		});
	});
</script>
<script>
	$(document).ready(function() {
		$('.id').focusout(function(e) {

			inputId = $('.id').val();

			// regex 먼저 테스트
			var reg = /^[a-z0-9_-]{3,16}$/;
			if (reg.test(inputId)) {
				idReg = 1;
				$(".idtxt").remove();
				$.checkIdDup();
			} else {
				idReg = 0;
				$(".idtxt").remove();
				$(".idtxtSpn").append('<p style="color: red;"class="idtxt">ID: 영문 소문자 or 숫자  3~16자리</p>');

			}

		});
		$('.pw').focusout(function(e) {
			pw = $('.pw').val();

			var reg = /^[A-Za-z\d$@$!%*#?&]{3,16}$/;
			if (reg.test(pw)) {
				pwReg = 1;
				$(".pwtxt").remove();

				if (rpt == pw) {
					pwRpt = 1;
					$(".pwtxt").remove();
					$(".pwtxtSpn").append('<p style="color: LimeGreen;"class="pwtxt">✔</p>');
				} else {
					pwRpt = 0;
					$(".pwtxt").remove();
					$(".pwtxtSpn").append('<p style="color: red;"class="pwtxt">비밀번호가 다릅니다.</p>');
				}

			} else {
				pwReg = 0;
				$(".pwtxt").remove();
				$(".pwtxtSpn").append('<p style="color: red;"class="pwtxt">PW: 영문 소문자 or 숫자 or 특수문자  3~16자리</p>');

			}

		});
		$('.rpt').focusout(function(e) {
			rpt = $('.rpt').val();

			var reg = /^[A-Za-z\d$@$!%*#?&]{3,16}$/;
			if (reg.test(rpt)) {
				pwReg = 1;
				$(".pwtxt").remove();

				if (rpt == pw) {

					pwRpt = 1;
					$(".pwtxt").remove();
					$(".pwtxtSpn").append('<p style="color: LimeGreen;"class="pwtxt">✔</p>');
				} else {
					pwRpt = 0;
					$(".pwtxt").remove();
					$(".pwtxtSpn").append('<p style="color: red;"class="pwtxt">비밀번호가 다릅니다.</p>');

				}

			} else {
				pwReg = 0;
				$(".pwtxt").remove();
				$(".pwtxtSpn").append('<p style="color: red;"class="pwtxt">PW: 영문 소문자 or 숫자 or 특수문자  3~16자리</p>');

			}
			//    /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/
		});
		$('.email').focusout(function(e) {
			emailipt = $('.email').val();
			var reg = /(^$|^(?!\s*$).+@(?!\s*$).+\.(?!\s*$).+$)/;            
			
			if (reg.test(emailipt)) {
				email = 1;                                                                                                                   
				$(".emailtxt").remove();                                    
				if (emailipt === '')
					$(".emailtxtSpn").append('<p style="color: LimeGreen;"class="emailtxt"></p>');
				else
					$(".emailtxtSpn").append('<p style="color: LimeGreen;"class="emailtxt">✔</p>');
			} else {
				email = 0;
				$(".emailtxt").remove();
				$(".emailtxtSpn").append('<p style="color: red;"class="emailtxt">Email: 공란 or 이메일 형식</p>');
			}

		});

		$(document).on('click', '.submit', function() {
			if (!(idDup === 1 && idReg === 1 && pwRpt === 1 && pwReg === 1 && email === 1)) {
				alert('잘못 기입된 부분이 있습니다.');
				return false;
			}

		});
	});
</script>
</head>
<style>
#inner {
	width: 25%;
	margin: 0 auto;
}
</style>
<body>
	<div id="header"></div>
	<br />
	<br />
	<br />
	<br />
	<br />
	<div id="inner">
		<form:form action="insertCustomer" modelAttribute="customer"
			method="GET">
			<table>
				<tr>
					<td><label>ID</label></td>
					<td><form:input class="id" path="id" size="12" /> <span
						class="idtxtSpn">
							<p class="idtxt"></p>
					</span></td>

				</tr>
				<tr>
					<td><label>PW</label></td>
					<td><form:input class="pw" type="password" path="pw" size="12" /></td>
				</tr>
				<tr>
					<td><label>반복</label></td>
					<td><input class="rpt" type="password" size="12" /> <span
						class="pwtxtSpn">
							<p class="pwtxt"></p>
					</span></td>
				</tr>
				<tr>
					<td><label>Email</label></td>
					<td><form:input class="email" path="email" size="20"
							placeholder="선택" /> <span class="emailtxtSpn">
							<p class="emailtxt"></p>
					</span></td>
				</tr>
				<tr>
					<td><label>주소</label></td>
					<td><form:input path="address" size="20" placeholder="선택" /></td>
				</tr>
				<tr>
					<td>&nbsp</td>
				</tr>
				<tr>
					<td><input type="submit" value="제출" class="submit" /></td>
				</tr>
			</table>
		</form:form>
	</div>
	<footer id="footer"></footer>
</body>
</html>


<%-- 캐싱 비활성화 --%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	response.setDateHeader("Expires", -1);
%>