<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/inc/page-config.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/inc/page-meta.jsp" %>
<title>Home</title>
<style>
textarea { resize: none; }
.btn-group { margin-bottom: 5px;}
</style>
</head>
<body>
<%@ include file="/WEB-INF/views/inc/page-nav.jsp" %>
<div class="container">
    <form action="<c:url value="/message/${message.messageId }"/>" method="post">
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label class="form-label">message</label>
                    <p class="lead "><c:out value="${message.message}"/></p>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="form-group">
                    <label class="form-label">answer</label>
                    <textarea name="answer" class="form-control" rows="3"><c:out value="${message.answer}"/></textarea>
                </div>
            </div>
        </div>
        <hr>
        <button type="submit" class="btn btn-primary">save</button>
        <a href="<c:url value="/${message.qq }/messages"/>" class="btn btn-default">back</a>
    </form>
</div>
</body>
<%@ include file="/WEB-INF/views/inc/scripts.jsp" %>
<script>
$(function(){
});
</script>
</html>
