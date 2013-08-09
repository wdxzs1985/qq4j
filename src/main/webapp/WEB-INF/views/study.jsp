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
    <c:if test="${empty message }">
        <form action="<c:url value="/${account }/study"/>" method="get">
            <h3>Query:</h3>
            <div class="form-group">
                <textarea name="message" class="form-control" rows="3"><c:out value="${message }"/></textarea>
            </div>
            <button type="submit" class="btn btn-primary">analyze</button>
        </form>
    </c:if>
    <c:if test="${!empty message }">
        <a href="<c:url value="/${account }/study"/>" class="btn btn-default">back</a>
        <form action="<c:url value="/${account }/answer"/>" method="post">
            <hr>
            <input type="hidden" name="message" value="<c:out value="${message}"/>">
            <p class="lead"><c:out value="${message }"/></p>
            <c:if test="${!empty wordList }">
                <hr>
                <div>
                    <c:forEach var="word" items="${wordList }">
                        <div class="btn-group">
                            <input type="hidden" name="word[]" value="<c:out value="${word}"/>">
                            <button class="btn btn-default btn-xs"><c:out value="${word}"/></button>
                            <button class="btn btn-danger btn-xs"><i class="glyphicon glyphicon-remove"></i></button>
                        </div>
                    </c:forEach>
                </div>
                <hr>
            </c:if>
            <div class="form-group">
                <textarea name="answer" class="form-control" rows="3"><c:out value="${answer }"/></textarea>
            </div>
            <button type="submit" class="btn btn-primary">answer</button>
            <c:if test="${!empty answerList }">
                <hr>
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>answer</th>
                            <th>owner</th>
                            <th>actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="message" items="${answerList}">
                            <tr>
                               <td><c:out value="${message.answer }"/></td>
                               <td><c:out value="${message.owner }"/></td>
                               <td><c:out value="${message.privatable }"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </form>
    </c:if>
</div>
</body>
<%@ include file="/WEB-INF/views/inc/scripts.jsp" %>
<script>
$(function(){
});
</script>
</html>
