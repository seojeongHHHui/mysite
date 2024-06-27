<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link th:href="@{/assets/css/main.css}" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<div id="wrapper">
			<div id="content">
				<div id="site-introduction">
					<img id="profile" th:src="@{(${servletContext.getAttribute('sitevo').profile) }" style='width: 250px'>
					<h2 th:text="${servletContext.getAttribute('sitevo').welcome }"></h2>
					<p>
						<th:block th:text="${#strings.replace(servletContext.getAttribute('sitevo').description, '\n', '<br>') }"/>
						<br><br>
						<a th:href="@{/guestbook}">방명록</a>에 글 남기기<br>
					</p>
				</div>
			</div>
		</div>
	</div>
</body>
</html>