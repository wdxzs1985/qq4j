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
                <th>status</th>
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
                         <c:if test="${message.unknown eq 0}">
                             <span class="label label-success">known</span>
                         </c:if>
                         <c:if test="${message.unknown eq 1}">
                             <span class="label label-warning">unknown</span>
                         </c:if>
                    </td>
                    <td>
                        <a href="<c:url value="/message/${message.messageId}"/>">
                            <c:out value="${message.message }" />
                        </a>
                    </td>
                    <td><c:out value="${message.answer }" /></td>
                    <td>
                        <c:out value="${message.owner }" />
                    </td>
                    <td>
                        <div class="btn-toolbar">
                            <div class="btn-group">
                                <c:if test="${message.privatable eq 0}">
                                    <button class="btn btn-success">public</button>
                                </c:if>
                                <c:if test="${message.privatable eq 1}">
                                    <button class="btn btn-warning">private</button>
                                </c:if>
                            </div>
                            <div class="btn-group">
                                <a href="<c:url value="/message/${message.messageId}"/>" class="btn btn-primary">edit</a>
                                <a href="" class="btn btn-danger">delete</a>
                            </div>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
<%@ include file="/WEB-INF/views/inc/scripts.jsp" %>
<script>
</script>
</body>
</html>