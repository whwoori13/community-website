<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script>
	alert('존재하지 않는 게시물 입니다.');
	
	var topic = '${currentTopic}';

	location.href = "/community/board/list?topic=" + topic;
	
	
	
</script>