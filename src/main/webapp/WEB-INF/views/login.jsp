<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/inc/page-meta.jsp" %>
<title>login</title>
</head>
<body>
<%@ include file="/WEB-INF/views/inc/page-nav.jsp" %>
<div class="container-fluid">
<div class="page-header">
<h1><c:out value="${ account }"/> login  </h1>
</div>
<form id="login-form" action="<c:url value="/${account}/login"/>" method="post" class="">
    <div class="control-group">
        <label class="control-label" for="inputAccount">
            account
        </label>
        <div class="controls">
            <input type="text" id="inputAccount" name="account" class="input-block-level" value="<c:out value="${account}"/>">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="inputPassword">
            password
        </label>
        <div class="controls">
            <input type="password" id="inputPassword" name="password" class="input-block-level">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="inputVerifyCode">
            verify
        </label>
        <div class="controls">
            <input type="text" id="inputVerifyCode" name="verifyCode" class="input-block-level" value="<c:out value="${verifyCode}"/>">
            <c:if test="${empty verifyCode}">
                <span class="help-block"><img src="<c:url value="/${account}/verify.jpg"/>"/></span>
            </c:if>
        </div>
    </div>
    <div class="control-group">
        <div class="controls">
            <button type="submit" class="btn btn-success btn-block">
                login
            </button>
        </div>
    </div>
</form>
</div>
</body>
</html>
