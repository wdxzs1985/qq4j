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
<div class="container">
    <div class="row">
        <div class="col-lg-4">
            <div class="page-header">
                <h1>login</h1>
            </div>
            <form id="login-form" action="<c:url value="/${account}/login"/>" method="post" class="">
                <div class="form-group">
                    <label class="form-label" for="inputAccount">account</label>
                    <input type="text" id="inputAccount" name="account" class="form-control" value="<c:out value="${account}"/>">
                </div>
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
                <div class="form-group">
                <button type="submit" class="btn btn-success btn-block">login</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
