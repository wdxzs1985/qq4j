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
<div class="container">
    <div class="row">
        <div class="col-lg-4">
            <div class="page-header">
                <h1><c:out value="${ account }"/></h1>
                <small></small>
            </div>
            <c:if test="${ !qqRobot.context.run }">
                <form id="login-form" action="<c:url value="/${account}/login"/>" method="post" class="">
                    <div class="form-group">
                        <label class="control-label" for="inputPassword">password</label>
                        <input type="password" id="inputPassword" name="password" class="form-control">
                    </div>
                    <div class="form-group">
                        <label class="control-label" for="inputVerifyCode">verify</label>
                        <div class="row">
                            <div class="col-6">
                                <input type="text" id="inputVerifyCode" name="verifyCode" class="form-control" value="<c:out value="${verifyCode}"/>">
                            </div>
                            <div class="col-6">
                                <c:if test="${empty verifyCode}">
                                    <img src="<c:url value="/${account}/verify.jpg"/>"/>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-success btn-block">login</button>
                </form>
            </c:if>
            <c:if test="${ qqRobot.context.run }">
                <form id="status-form" action="<c:url value="/${ account }/status"/>" method="post">
                        <select id="status" name="status" class="form-control">
                            <option value="online" <c:if test="${ qqRobot.context.userManager.self.status eq 'online' }"></c:if>>online</option>
                            <option value="callme" <c:if test="${ qqRobot.context.userManager.self.status eq 'callme' }"></c:if>>callme</option>
                            <option value="away" <c:if test="${ qqRobot.context.userManager.self.status eq 'away' }"></c:if>>away</option>
                            <option value="busy" <c:if test="${ qqRobot.context.userManager.self.status eq 'busy' }"></c:if>>busy</option>
                            <option value="silent" <c:if test="${ qqRobot.context.userManager.self.status eq 'silent' }"></c:if>>silent</option>
                            <option value="hidden" <c:if test="${ qqRobot.context.userManager.self.status eq 'hidden' }"></c:if>>hidden</option>
                            <option value="offline" <c:if test="${ qqRobot.context.userManager.self.status eq 'offline' }"></c:if>>offline</option>
                        </select>
                </form>
                <hr>
                <form id="nlk-form" action="<c:url value="/${ account }/nlk"/>" method="post">
                    <div class="input-group">
                        <input type="text" name="nlk" class="form-control" placeholder="long nick">
                        <span class="input-group-btn">
                            <button class="btn btn-default" type="submit">Set long nick</button>
                        </span>
                    </div><!-- /input-group -->
                </form>
            </c:if>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/inc/scripts.jsp" %>
<script>
$(function(){
	$('#status').change(function(){
		$('#status-form').submit();
	});
});
</script>
</body>
</html>
