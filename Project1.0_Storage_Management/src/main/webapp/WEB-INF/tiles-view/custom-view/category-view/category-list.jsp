<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<div class="right_col" role="main">
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h3>Category List</h3>
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
						<br/>
						
						<form:form modelAttribute="categorySearchForm" servletRelativeAction="/category/list" method="POST"
									class="form-horizontal form-label-left">
									
							<%-- > Day la Form search nen khong can cac field nay
							
								<form:input path="description"/>
							 	<form:hidden path="activeFlag"/>
								<form:hidden path="createdDate"/> 
								<form:hidden path="updatedDate"	/> 
							--%>
							
							<div class="form-group">
								<label for="id" class="control-label col-md-3 col-sm-3 col-xs-12">ID:</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="id" class="form-control col-md-7 col-xs-12"/>
								</div>
							</div>
							
							<div class="form-group">
								<label for="code" class="control-label col-md-3 col-sm-3 col-xs-12">Code:</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="code" class="form-control col-md-7 col-xs-12"/>
								</div>
							</div>
							
							<div class="form-group">
								<label for="name" class="control-label col-md-3 col-sm-3 col-xs-12">Name:</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="name" class="form-control col-md-7 col-xs-12"/>
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
					
						<a href="<c:url value="/category/add"/>" class="btn btn-success btn-lg">
							Add <i class="fa fa-plus"></i> 
						</a>
					
						<a href="<c:url value="/paging/category/list"/>" class="btn btn-success btn-lg">
							Paging <i class="fa fa-book"></i> 
						</a>
						
						<div class="table-responsive">
							<table class="table table-striped jambo_table bulk_action">
								<thead>
									<tr class="headings">
										<th class="column-title">#</th>
										<th class="column-title">Id</th>
										<th class="column-title">Code</th>
										<th class="column-title">Name</th>
										<th class="column-title">Description</th>
										<th class="column-title no-link last text-center" colspan="3"><span
											class="nobr">Action</span></th>
									</tr>
								</thead>
	
								<tbody>
									<c:forEach items="${categories}" var="category" varStatus="loop">
	
											<c:choose>
												<c:when test="${loop.index % 2 == 0 }">
													<tr class="even pointer">
												</c:when>
												<c:otherwise>
													<tr class="odd pointer">
												</c:otherwise>
											</c:choose>
											
											<td class=" ">${loop.index + 1}</td>
											<td class=" ">${category.id }</td>
											<td class=" ">${category.code }</td>
											<td class=" ">${category.name }</td>
											<td class=" ">${category.description }</td>
											
											<td class="text-center">
												<a href="<c:url value="/category/view/${category.id }"/>" class="btn btn-info">
													View <i class="fa fa-file-text-o"></i> 
												</a>
											</td>
											<td class="text-center">
												<a href="<c:url value="/category/edit/${category.id }"/>" class="btn btn-warning">
													Edit <i class="fa fa-edit"></i> 
												</a>
											</td>
											<td class="text-center">
												<%-- javascript:void(0); co nghia disable chuyen huong cua the <a --%>
												<a href="javascript:void(0);" onclick="confirmDelete(${category.id});" class="btn btn-danger">
													Delete <i class="fa fa-remove"></i> 
												</a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

	 function confirmDelete(categoryId){
		 if(confirm('Do you want delete this category?')){
			 window.location.href = '<c:url value="/category/delete/"/>' + categoryId;
		 }
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
