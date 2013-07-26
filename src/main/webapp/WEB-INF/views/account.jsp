<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/inc/page-meta.jsp" %>
<title><c:out value="${ account }"/></title>
</head>
<body>
<%@ include file="/WEB-INF/views/inc/page-nav.jsp" %>
<div class="container-fluid">
<div class="page-header">
<h1>Hello <c:out value="${ account }"/>!  </h1>
</div>
<form id="nlk-form" action="<c:url value="/${ account }/nlk"/>" class="form-inline">
    <input type="text" name="nlk" class="input-xlarge" placeholder="long nick">
    <button type="submit" class="btn">Set long nick</button>
</form>
<div class="btn-group">
    <a href="<c:url value="/${ account }/offline"/>" class="btn btn-danger">Offline</a>
</div>
<h3>Users</h3>
<ul class="media-list">
    <c:forEach items="${qqRobot.context.friendManager.users }" var="entry">
        <li class="media">
            <a><c:out value="${ entry.value }"/></a>
            faith: [<c:out value="${ entry.value.faith }"/>]
            <c:if test="${ !empty entry.value.lastMsg }" >
                <blockquote><c:out value="${ entry.value.lastMsg }"/></blockquote>
            </c:if>
        </li>
    </c:forEach>
</ul>
<hr>
<h3>Groups</h3>
<ul class="media-list">
    <c:forEach items="${qqRobot.context.groupManager.groups }" var="entry">
        <li class="media">
            <a><c:out value="${ entry.value }"/></a>
        </li>
    </c:forEach>
</ul>
</div>
<%@ include file="/WEB-INF/views/inc/scripts.jsp" %>
<script>
$(function(){
	$('#nlk-form').submit(function(){
		var $this = $(this);
		var url = $this.prop('action');
		$.post(url, $this.serialize(), function(response){
			if(response.result) {
				
			} else {
				alert(response.error);
			}
		});
		return false;
	});
});
</script>
</body>
</html>
