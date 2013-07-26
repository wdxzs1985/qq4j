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
        <form id="study-form" class="form-inline">
            <input type="text" name="q" class="input-xlarge" placeholder="query">
            <button type="submit" class="btn">analyze</button>
        </form>
    </div>
</div>
</body>
<%@ include file="/WEB-INF/views/inc/scripts.jsp" %>
<script>
$(function(){
	$('#study-form').submit(function(){
		var $form = $(this);
		var query = $form.find('input[name="q"]').val();
		if(!query){
			alert('query is empty');
			return false;
		}
		var url = 'http://ajax.googleapis.com/ajax/services/search/web';
		$.get(url, {
			v: '1.0',
	        q: query
		}, function(response){
			if(reponse.responseStatus == 200) {
				$.foreach(response.responseData.results, function(index, element){
					console.log(element.unescapedUrl);
				});
			} else {
				alert(reponse.responseDetails);
			}
		});
		return false;
	});
});
</script>
</html>
