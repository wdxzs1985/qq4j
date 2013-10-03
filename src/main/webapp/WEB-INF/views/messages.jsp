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
    <div class="page-header">
        <h1><c:out value="${ account }"/></h1>
    </div>
    <table class="table table-bordered">
        <thead>
            <tr>
                <th>message</th>
                <th>answer</th>
                <th>owner</th>
                <th>actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="message" items="${messageList}">
                <tr>
                    <td>
                        <a href="<c:url value="/message/${message.messageId}"/>">
                            <c:out value="${message.message }" />
                        </a>
                    </td>
                    <td><c:out value="${message.answer }" /></td>
                    <td><c:out value="${message.owner }" /></td>
                    <td>
                        <div class="btn-toolbar">
                            <div class="btn-group">
                                <c:if test="${message.privatable eq 0}">
                                    <a class="btn btn-warning disabled">private</a>
                                    <a href="<c:url value="/message/${message.messageId}/private"/>" class="btn btn-success">public</a>
                                </c:if>
                                <c:if test="${message.privatable eq 1}">
                                    <a href="<c:url value="/message/${message.messageId}/public"/>" class="btn btn-warning">private</a>
                                    <a class="btn btn-success disabled">public</a>
                                </c:if>
                            </div>
                            <div class="btn-group">
                                <a href="<c:url value="/message/${message.messageId}"/>" class="btn btn-primary">edit</a>
                                <a href="<c:url value="/message/${message.messageId}"/>/delete" class="btn btn-danger">delete</a>
                            </div>
                            <div class="btn-group">
                                <c:if test="${message.owner ne 0 }">
                                    <a href="<c:url value="/${account }/user/${message.owner }/black"/>" class="btn btn-warning">
                                        <span>block user</span>
                                    </a>
                                </c:if>
                            </div>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <a href="<c:url value="/${account}/study"/>" class="btn btn-default">
        study
    </a>
</div>
<%@ include file="/WEB-INF/views/inc/scripts.jsp" %>
<script>
</script>
</body>
</html>