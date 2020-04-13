<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:import url="/WEB-INF/jsp/header.jsp" />

<title>Schedule a Tee Time</title>
</head>

<body>
<div id="wrapper">
	<div class="row">
	<div class="col-sm-4"></div>
	<div class="col-sm-4">	
	<h3>Schedule a Tee Time</h3>

	<form method="POST" action="${newScoreSubmitVar}">
	<input type="hidden" name="CSRF_TOKEN" value="${CSRF_TOKEN}"/>

			<div class="form-group">
				<label for="name">Course: </label> 
				<select name="name">
					<c:forEach items="${allCourses}" var="course">
						<option value="name">${course.name}</option>			
					</c:forEach>
				</select>
			</div>

			<div class="form-group">
				<label for="score">Score:</label> 
				<input name="score" placeHolder="Score"/>
			</div>
			<hr>
			<br>
			<label for="leagueBool" id="leagueBool"> Was this part of a match:</label>
				<select>
					<option>Select...</option>
					<option value="true">Yes</option>
					<option value="false">No</option>
				</select>
			<c:if test="{leagueBool == true}">
				<div id="leagueTrue">
				<br>
					<div class="form-group">
						<label for="league">League:</label> 
						<input name="league" placeHolder="Score"/>
					</div>
					<div class="form-group">
						<label for="match">Match:</label> 
						<input name="match" placeHolder="Match"/>
					</div>	
				</div>			
				</c:if>
			<br><br>
			<button type="submit" class="btn btn-primary" id="btnSaveScore">Submit</button>
			</form>
		</div>
	</div>
</div>
		
</body>
</html>