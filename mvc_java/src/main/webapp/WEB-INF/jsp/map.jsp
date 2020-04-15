<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:import url="/WEB-INF/jsp/header.jsp" />

<script src="https://maps.googleapis.com/maps/api/js?key=${request}"></script>
<script>
var map;
function initialize() {
	
	var mapOptions = {
		zoom : 6.5,
		center : new google.maps.LatLng(44.5, -86)
	};
	var map = new google.maps.Map(document.getElementById('map-canvas'),
			mapOptions);
	

	<c:forEach var="course" items="${courses}">
		var myLatLng = {lat: ${course.latitude}, lng: ${course.longitude}};
		var name = "${course.name}" 
		var image = {
				url: 'https://upload.wikimedia.org/wikipedia/commons/b/bc/Map_symbol_golf_course_02.png',
				scaledSize: new google.maps.Size(30,30)
		};
		
    	var marker = new google.maps.Marker({
    		position: myLatLng,
    		type:'info',
    		map: map,
    		title: name,
    		icon: image
   	});

    </c:forEach>
    
};
google.maps.event.addDomListener(window, 'load', initialize);
</script>

<script>
!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src='https://weatherwidget.io/js/widget.min.js';fjs.parentNode.insertBefore(js,fjs);}}(document,'script','weatherwidget-io-js');
</script>



</head>
</head>
<body>
<a class="weatherwidget-io" href="https://forecast7.com/en/42d33n83d05/detroit/?unit=us" data-label_1="DETROIT" data-label_2="WEATHER" data-icons="Climacons Animated" data-days="5" data-theme="weather_one" >DETROIT WEATHER</a>

<div id="wrapper">
  <h1>Golfing in Michigan</h1>
  <input type="hidden" name="CSRF_TOKEN" value="${CSRF_TOKEN}" />
  <div id="map-canvas" style="height: 500px;; width: auto"></div>
</div>
</body>
</html>
<c:import url="/WEB-INF/jsp/footer.jsp" />