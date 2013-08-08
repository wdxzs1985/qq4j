<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/inc/page-config.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/inc/page-meta.jsp" %>
<title>Home</title>
</head>
<body>
<%@ include file="/WEB-INF/views/inc/page-nav.jsp" %>
<div class="container">
    <div class="">
        <form id="study-form" action="<c:url value="/${account }/study/analyze"/>" method="post">
            <div class="form-group">
                <input type="text" name="message" class="form-control" placeholder="query" value="<c:out value="${message }"/>">
            </div>
            <button type="submit" class="btn">analyze</button>
        </form>
    </div>
    <c:if test="${!empty wordList }">
        <div>
            <c:forEach var="word" items="${wordList }">
                <span class="badge"><c:out value="${word }"/></span>
            </c:forEach>
        </div>
    </c:if>
</div>
</body>
<%@ include file="/WEB-INF/views/inc/scripts.jsp" %>
<script>
$(function(){
});
</script>
</html>
