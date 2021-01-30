<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false" %>


<div class="right_col" role="main">
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h3>Product In Stock List</h3>
			</div>
		</div>

		<div class="clearfix"></div>
		
		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
				
					<div class="x_title">
						<h2>Searching Form</h2>
						<ul class="nav navbar-right panel_toolbox">
							<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
							<li class="dropdown">
								<a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false">
									<i class="fa fa-wrench"></i>
								</a>
								<ul class="dropdown-menu" role="menu">
									<li><a class="dropdown-item" href="#">Settings 1</a></li>
									<li><a class="dropdown-item" href="#">Settings 2</a></li>
								</ul>
							</li>
						</ul>
						<div class="clearfix"></div>
					</div>
	
					<div class="x_content">
						
						<form:form modelAttribute="productInStockSearchForm" servletRelativeAction="/product-in-stock/list" method="POST"
									class="form-horizontal form-label-left" >
									
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12" for="code">Category</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="productInfo.category.name" class="form-control col-md-7 col-xs-12" />
								</div>
							</div>
							
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12" for="code">Code </label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="productInfo.code" class="form-control col-md-7 col-xs-12" />
								</div>
							</div>
							
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">Name </label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="productInfo.name" class="form-control col-md-7 col-xs-12" />
								</div>
							</div>
							
							<div class="form-group">
								<div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
									<button type="submit" class="btn btn-primary">
										Search <i class="fa fa-search"></i>
									</button>
								</div>
							</div> 
						</form:form>
					</div>
						
					<div class="x_content">
						<div class="ln_solid"></div>
					</div>		
					
					<div class="container">
		
						<a href="<c:url value="/product-in-stock/list"/>" class="btn btn-success btn-lg">
							Normal <i class="fa fa-book"></i> 
						</a>
						
						<div class="table-responsive">
							<table class="table table-striped jambo_table bulk_action">
								<thead>
									<tr class="headings">
										<th class="column-title">#</th>
										<th class="column-title">Category</th>
										<th class="column-title">Code</th>
										<th class="column-title">Name</th>
										<th class="column-title">Image</th>
										<th class="column-title">Quantity</th>
										<th class="column-title">Price</th>
									</tr>
								</thead>
	
								<tbody>
									<c:forEach items="${productInStocks}" var="productInStock" varStatus="loop">
	
										<c:choose>
											<c:when test="${loop.index % 2 ==0 }">
												<tr class="even pointer">
											</c:when>
											<c:otherwise>
												<tr class="odd pointer">
											</c:otherwise>
										</c:choose>
											<td class=" ">${pageInfo.offset + loop.index + 1}</td>
											<td class=" ">${productInStock.productInfo.category.name }</td>
											<td class=" ">${productInStock.productInfo.code }</td>
											<td class=" ">${productInStock.productInfo.name }</td>
											<td class=" "><img src="<c:url value="${productInStock.productInfo.imageUrl}"/>" width="100px" height="100px"/></td>
											<td class="">${productInStock.quantity }</td>
											<td class="">${productInStock.price }</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
					
							<!-- For paging -->
							<jsp:include page="/WEB-INF/tiles-view/base-view/paging.jsp"></jsp:include>
						
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

	 function gotoPage(page){
		 window.location.replace("<c:url value="/paging/product-in-stock/list/"/>" + page);
		 //$('#productInfoSearchForm').attr('action','<c:url value="/product-in-stock/list/"/>' + page);
		 //$('#productInfoSearchForm').submit();
	 }
	 
	 $(document).ready(function(){
		 processMessage();
	 });
	 function processMessage(){

		 var msgSuccess = '${msgSuccess}';
		 var msgFailure = '${msgFailure}';
		 
		 if(msgSuccess){
			 new PNotify({
                 title: ' Success',
                 text: msgSuccess,
                 type: 'success',
                 styling: 'bootstrap3'
             });
		 }
		 if(msgFailure){
			 new PNotify({
                 title: ' Failure',
                 text: msgFailure,
                 type: 'error',
                 styling: 'bootstrap3'
             });
		 }
	 }
</script>
