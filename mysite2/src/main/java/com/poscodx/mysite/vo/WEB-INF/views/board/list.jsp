<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<form id="search_form" action="" method="post">      <!-- search action -->
					<input type="text" id="kwd" name="kwd" value="">
					<input type="submit" value="찾기">
				</form>
				
				<c:set var="count" value="${fn:length(list) }" />
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
					
					<c:forEach items='${list }' var='vo' varStatus="status">
					<tr>
						<td>${count-status.index}</td>
						<td style="text-align:left; padding-left:${20*vo.depth }px">
							<c:if test='${vo.depth > 0 }'>
								<img src='${pageContext.request.contextPath }/assets/images/reply.png' />	
							</c:if>
							<a href="${pageContext.request.contextPath }/board?a=view&no=${vo.no }">${vo.title }</a>
						</td>
						<td>${vo.userName }</td>
						<td>${vo.hit }</td>
						<td>${vo.regDate }</td>
						
						<%-- 로그인 시, 본인에게만 보기에 --%>
						<c:if test='${not empty authUser and authUser.getNo()==vo.userNo}'>
							<td><a href="${pageContext.request.contextPath }/board?a=delete&no=${vo.no }" class="del">삭제</a></td>
						</c:if>
						
					</tr>
					</c:forEach>
				</table>
				
				<%-- pager 추가 --%>
				<c:set var="pageSection" value="${pageSection }" />
				<c:set var="sectionSize" value="${sectionSize }" />
				<c:set var="selected" value="${selectedPage }" />
				<c:set var="maxPage" value="${maxPage }" />
				<c:set var="maxSection" value="${maxSection }" />
				
				<div class="pager">
					<ul>
						<li>
							<c:if test='${pageSection > 1 }'>
								<a href="${pageContext.request.contextPath }/board?currentPageSection=${pageSection }&sectionDown=true"">◀</a>
							</c:if>
						</li>
						<c:forEach var="page" begin="${(pageSection-1)*sectionSize+1 }" end="${pageSection*sectionSize }">
							<c:choose>
								<c:when test='${page eq selected }'>
									<li class="selected">${page }</li>
								</c:when>
								<c:otherwise>
								
									<li>
										<c:choose>
											<c:when test='${page <= maxPage }'>
												<a href="${pageContext.request.contextPath }/board?page=${page }">${page }</a>
											</c:when>
											<c:otherwise>
												${page }
											</c:otherwise>
										</c:choose>
									</li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<li>
							<c:if test='${pageSection < maxSection }'>
								<a href="${pageContext.request.contextPath }/board?currentPageSection=${pageSection }&sectionUp=true">▶</a>
							</c:if>
						</li>
					</ul>
				</div>
				<%-- pager 추가 --%>
				
				<div class="bottom">
					<c:if test='${not empty authUser}'>
						<a href="${pageContext.request.contextPath }/board?a=writeView" id="new-book">글쓰기</a>
					</c:if>
				</div>				
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>