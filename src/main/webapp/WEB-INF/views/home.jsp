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
<div class="container-fluid">
    <div class="">
        <c:if test="${ empty qqRobotList }">
        <div class="alert">
            no data!
        </div>
        </c:if>
        <c:if test="${ !empty qqRobotList }">
        <table class="table table-striped">
            <tr>
                <th>qq</th>
                <th>running</th>
            </tr>
            <c:forEach var="qqRobot" items="${qqRobotList}" varStatus="stat">
                <tr>
                    <td>
                        <a href="<c:url value="/${qqRobot.context.userManager.self.account}"/>">
                            <c:out value="${qqRobot.context.userManager.self.account}"/>
                        </a>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${qqRobot.context.run}">○</c:when>
                            <c:otherwise>×</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
        </c:if>
    </div>
</div>
</body>
<%@ include file="/WEB-INF/views/inc/scripts.jsp" %>
</html>