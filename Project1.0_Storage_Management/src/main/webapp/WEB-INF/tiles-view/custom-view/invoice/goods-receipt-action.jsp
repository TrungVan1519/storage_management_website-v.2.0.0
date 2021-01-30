<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false" %>


<div class="right_col" role="main">
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h3>Form Action</h3>
			</div>
		</div>
		
		<div class="clearfix"></div>
		
		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
				
					<div class="page-title">
						<div class="title_left">
							<h2>${formTitle}</h2>
						</div>
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
						<form:form modelAttribute="invoiceForm" servletRelativeAction="/goods-receipt/save" method="POST"
									class="form-horizontal form-label-left">
						
							<!-- 
								> Day la 4 field khong cho phep user duoc nhap, vi the ma ta phai de form:hidden/> va phai tu dong nhap truoc cho user.
								> Chu y: Do trong DB ta de createdDate va updatedDate la kieu TimeStamp tuong duong voi kieu Date() trong Java,
									nhung form:hidden/> mac dinh luon tra ve kieu String nen trong @InitBinder ta phai convert tu kieu String
									tra ve tu cac the form:hidden/> nay sang kieu Date() thi moi co the update trong DB duoc
							-->
							<form:hidden path="id"/>
							<form:hidden path="activeFlag"/>
							<form:hidden path="createdDate"/>
							<form:hidden path="updatedDate"/>
							
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12" for="code">
									Code <span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="code" class="form-control col-md-7 col-xs-12" disabled="${viewMode}"/>
									<div class="has-error">
										<form:errors path="code" class="help-block"></form:errors>
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12" for="productId">
									Product <span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<c:choose>
										<c:when test="${!viewMode}">
											<form:select path="productInfo.id" class="form-control">
												<form:options items="${mapProduct}"/>
											</form:select>
											<div class="has-error">
												<form:errors path="productInfo.name" class="help-block"></form:errors>
											</div>
										</c:when>
										<c:otherwise>
											<form:input path="productInfo.name" disabled="true" class="form-control col-md-7 col-xs-12"/>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12" for="quantity">
									Quantity <span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="quantity" class="form-control col-md-7 col-xs-12" disabled="${viewMode}"/>
									<div class="has-error">
										<form:errors path="quantity" class="help-block"></form:errors>
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label for="description" class="control-label col-md-3 col-sm-3 col-xs-12">Price</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="price" class="form-control col-md-7 col-xs-12" disabled="${viewMode}"/>
									<div class="has-error">
										<form:errors path="price" class="help-block"></form:errors>
									</div>
								</div>
							</div>
							
							<div class="ln_solid"></div>
							
							<div class="form-group">
								<div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
									<button class="btn btn-primary" type="button" onclick="cancel();">
											Cancel <i class="fa fa-close"></i>
										</button>
								
									<c:if test="${!viewMode }">
										<button class="btn btn-primary" type="reset">Reset</button>
										<button type="submit" class="btn btn-success">
											Submit <i class="fa fa-check"></i>
										</button>
									</c:if>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


<script type="text/javascript">

	$(document).ready(
			function() {
				$('#goods-receiptlistId').addClass('current-page')
										 .siblings().removeClass('current-page');
				
				var parent = $('#goods-receiptlistId').parents('li');
				parent.addClass('active')
					  .siblings().removeClass('active');
				
				$('#goods-receiptlistId').parents().show();
			});
	
	function cancel() {
		window.location.href = '<c:url value="/goods-receipt/list"/>'
	}
</script>