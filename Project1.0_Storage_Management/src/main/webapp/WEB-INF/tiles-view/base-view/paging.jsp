<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%--  <ul class="pagination">
	<c:forEach begin="1" end="${pageInfo.totalPages}" varStatus="loop">
		<c:choose>
			<c:when test="${pageInfo.indexPage == loop.index}">
				<li class="active"><a href="javascript:void(0);">${loop.index}</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="<c:url value="/paging/category/list/${loop.index }"/>">${loop.index}</a></li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</ul>  --%>
    
<ul class="pagination">
	<c:forEach begin="1" end="${pageInfo.totalPages}" varStatus="loop">
		<c:choose>
			<c:when test="${pageInfo.indexPage == loop.index}">
				<li class="active"><a href="javascript:void(0);">${loop.index}</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="javascript:void(0);" onclick="gotoPage(${loop.index});">${loop.index}</a></li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</ul>