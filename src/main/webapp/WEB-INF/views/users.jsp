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
                <th>rank</th>
                <th>account</th>
                <th>faith</th>
                <th>black</th>
                <th>actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="user" items="${userList}">
                <tr>
                    <td><c:out value="${user.rank }" /></td>
                    <td><c:out value="${user.account }" /></td>
                    <td><c:out value="${user.faith }" /></td>
                    <td>
                        <c:choose>
                            <c:when test="${user.black eq 0 }"><span class="label label-success">normal</span></c:when>
                            <c:otherwise><span class="label label-danger">blocked</span></c:otherwise>
                        </c:choose>
                    <td>
                        <c:choose>
                            <c:when test="${user.black eq 0 }">
                                <a href="<c:url value="/${account }/user/${user.account }/black"/>" class="btn btn-danger">
                                    block user
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a href="<c:url value="/${account }/user/${user.account }/black"/>" class="btn btn-success">
                                    cancel
                                </a>
                            </c:otherwise>
                        </c:choose>
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