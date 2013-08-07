<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/inc/page-meta.jsp" %>
<link rel="stylesheet" media="screen" type="text/css" href="<c:url value="/resources/colorpicker/css/colorpicker.css"/>" />
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
            <c:if test="${ qqRobot.context.run }">
                <a href="<c:url value="/${ account }/offline"/>" class="btn btn-danger">Offline</a>
            </c:if>
            <c:if test="${ !qqRobot.context.run }">
                <a href="<c:url value="/${ account }/login"/>" class="btn btn-success">Login</a>
            </c:if>
            <hr>
            <form id="nlk-form" action="<c:url value="/${ account }/nlk"/>" method="post">
                <div class="input-group">
                    <input type="text" name="nlk" class="form-control" placeholder="long nick">
                    <span class="input-group-btn">
                        <button class="btn btn-default" type="submit">Set long nick</button>
                    </span>
                </div><!-- /input-group -->
            </form>
        </div>
        <div class="col-lg-4">
            <div class="page-header">
                <h1>Friends <small></small></h1>
            </div>
            <ul class="media-list">
                <c:forEach items="${qqRobot.context.friendManager.users }" var="entry" begin="1" end="5">
                    <li class="media">
                        <a><c:out value="${ entry.value }"/></a>
                        faith: [<c:out value="${ entry.value.faith }"/>]
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="col-lg-4">
            <div class="page-header">
                <h1>Groups</h1>
                <ul class="media-list">
                    <c:forEach items="${qqRobot.context.groupManager.groups }" var="entry" begin="1" end="5">
                        <li class="media">
                            <a><c:out value="${ entry.value }"/></a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/inc/scripts.jsp" %>
<script type="text/javascript" src="<c:url value="/resources/colorpicker/js/colorpicker.js"/>"></script>
<script>
$(function(){
	$('#font_color').ColorPicker({
	    onShow: function (colpkr) {
	        $(colpkr).fadeIn(500);
	        return false;
	    },
	    onHide: function (colpkr) {
	        $(colpkr).fadeOut(500);
	        return false;
	    },
	    onChange: function (hsb, hex, rgb) {
	        $('#colorSelector div').css('backgroundColor', '#' + hex);
	        $('#font_color').val(hex);
	    }
	});
});
</script>
</body>
</html>
