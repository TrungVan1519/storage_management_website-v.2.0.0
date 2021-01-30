<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false" %>


<!-- format number: VD 1000000 ==> 1,000,000 -->
<script src="//cdnjs.cloudflare.com/ajax/libs/numeral.js/2.0.6/numeral.min.js"></script>
    
<div class="right_col" role="main">
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h3>Goods Receipt List</h3>
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
						
						<form:form modelAttribute="invoiceSearchForm" servletRelativeAction="/goods-receipt/list" method="POST"
							class="form-horizontal form-label-left">
							
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12" for="code">Code </label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="code" class="form-control col-md-7 col-xs-12" />
								</div>
							</div>
							
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12" for="fromDate">From Date</label>
								<div class="col-md-6 col-sm-6 col-xs-12 " >
									<div class="input-group date" id='fromDatePicker'>
			                            <form:input path="fromDate" class="form-control" />
			                            <span class="input-group-addon">
			                               <span class="glyphicon glyphicon-calendar"></span>
			                            </span>
		                            </div>
								</div>
							</div>
							
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12" for="toDate">To Date </label>
								<div class="col-md-6 col-sm-6 col-xs-12 " >
									<div class="input-group date" id='toDatePicker'>
									    <form:input path="toDate" class="form-control" />
			                            <span class="input-group-addon">
			                               <span class="glyphicon glyphicon-calendar"></span>
			                            </span>
									</div>
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
					
						<a href="<c:url value="/goods-receipt/add"/>" class="btn btn-success btn-lg">
							Add <i class="fa fa-plus"></i> 
						</a>
						
						<a href="<c:url value="/goods-receipt/export"/>" class="btn btn-success btn-lg">
							Export <i class="fa fa-cloud-download"></i>
						</a>
						
						<div class="table-responsive">
							<table class="table table-striped jambo_table bulk_action">
								<thead>
									<tr class="headings">
										<th class="column-title">#</th>
										<th class="column-title">Code</th>
										<th class="column-title">Quantity</th>
										<th class="column-title">Price</th>
										<th class="column-title">Product </th>
										<th class="column-title">Updated Date</th>
										<th class="column-title no-link last text-center" colspan="3"><span class="nobr">Action</span></th>
									</tr>
								</thead>
	
								<tbody>
									<c:forEach items="${invoices}" var="invoice" varStatus="loop">
											<c:choose>
												<c:when test="${loop.index % 2 == 0 }">
													<tr class="even pointer">
												</c:when>
												<c:otherwise>
													<tr class="odd pointer">
												</c:otherwise>
											</c:choose>
											<td class=" ">${loop.index + 1}</td>
											<td class=" ">${invoice.code }</td>
											<td class=" ">${invoice.quantity }</td>
											<td class="price" style="font-size: 13px;">${invoice.price }</td>
											<td class=" ">${invoice.productInfo.name }</td>
											<td class="date">${invoice.updatedDate}</td>
											
											<td class="text-center">
												<a href="<c:url value="/goods-receipt/view/${invoice.id }"/>" class="btn btn-info">
													View <i class="fa fa-file-text-o"></i> 
												</a>
											</td>
											<td class="text-center">
												<a href="<c:url value="/goods-receipt/edit/${invoice.id }"/>" class="btn btn-warning">
													Edit <i class="fa fa-edit"></i> 
												</a>
											</td>
											<td class="text-center">
												<a href="javascript:void(0);" onclick="confirmDelete(${invoice.id});" class="btn btn-danger">
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
		 if(confirm('Do you want delete this invoice?')){
			 window.location.href = '<c:url value="/goods-receipt/delete/"/>'+id;
		 }
	 }
	 
	 $(document).ready(function(){
		 
		 processMessage();
		 
		 // format date time khi nhap field fromDate va toDate
		 $('#fromDatePicker').datetimepicker({
			 format : 'YYYY-MM-DD HH:mm:ss'
		 });
		 
		 $('#toDatePicker').datetimepicker({
			 format : 'YYYY-MM-DD HH:mm:ss'
		 })
		 
		 // formate number doi voi cac field co class="price"
		  $('.price').each(function(){
			 $(this).text(numeral($(this).text()).format('0,0'));
		 }) 
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