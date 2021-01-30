<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false"%>


<div class="right_col" role="main">
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h3>User List</h3>
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
						
						<form:form modelAttribute="userSearchForm" cssClass="form-horizontal form-label-left" servletRelativeAction="/user/list/1" method="POST">
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">Name </label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="name" cssClass="form-control col-md-7 col-xs-12" />
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
						
						<a href="<c:url value="/user/add"/>" class="btn btn-success btn-lg">
							Add <i class="fa fa-plus"></i> 
						</a>
						
						<div class="table-responsive">
							<table class="table table-striped jambo_table bulk_action">
								<thead>
									<tr class="headings">
										<th class="column-title">#</th>
										<th class="column-title">Id</th>
										<th class="column-title">User Name</th>
										<th class="column-title">Name</th>
										<th class="column-title">Email</th>
										<th class="column-title no-link last text-center" colspan="3"><span class="nobr">Action</span></th>
									</tr>
								</thead>
	
								<tbody>
									<c:forEach items="${users}" var="user" varStatus="loop">
	
										<c:choose>
											<c:when test="${loop.index % 2 == 0 }">
												<tr class="even pointer">
											</c:when>
											<c:otherwise>
												<tr class="odd pointer">
											</c:otherwise>
										</c:choose>
											<td class=" ">${loop.index + 1}</td>
											<td class=" ">${user.id }</td>
											<td class=" ">${user.username }</td>
											<td class=" ">${user.name }</td>
											<td class=" ">${user.email }</td>
											
											<td class="text-center">
												<a href="<c:url value="/user/view/${user.id }"/>" class="btn btn-info">
													View <i class="fa fa-file-text-o"></i> 
												</a>
											</td>
											<td class="text-center">
												<a href="<c:url value="/user/edit/${user.id }"/>" class="btn btn-warning">
													Edit <i class="fa fa-edit"></i> 
												</a>
											</td>
											<td class="text-center">
												<a href="javascript:void(0);" onclick="confirmDelete(${user.id});" class="btn btn-danger">
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
	 function confirmDelete(id){
		 if(confirm('Do you want delete this user?')){
			 window.location.href = '<c:url value="/user/delete/"/>'+id;
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