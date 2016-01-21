<html>
    <head>
		<title>EMA Avails Web Validator Results</title>
		
		<script type="text/javascript" src="//www.gstatic.com/feedback/api.js"></script>
		
		
		<script>
			function selectAll() {
				content.getSelection().selectAllChildren(content.document.body);
			}
		</script>

    </head>
    
    <body>
            <center>
            
<%--                <div>  --%>
<%--                    	<H1><a href="" onclick="userfeedback.api.startFeedback({'enableAnonymousFeedback' : true, 'productId': '109139', 'productVersion': 'emav4.1' }); return false;">Submit Feedback / Questions</a></H1>  --%>
<%--                </div>  --%>
                
	            <div>
	            <c:if test="${not empty message}">
			    <h1>${message}</h1>
				</c:if>
				</div>
			</center>
			
			<div>
				<pre>
				<c:if test="${not empty errorOutput}">
				${errorOutput}
				</c:if>
				</pre>
			</div>
    </body>
</html>