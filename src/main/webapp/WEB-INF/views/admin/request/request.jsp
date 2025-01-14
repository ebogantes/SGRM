<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<%@include file="../../plantilla/header.jsp"%>

<!-- Bootstrap Core Css -->
<link
	href="<c:url value='/static/plugins/bootstrap/css/bootstrap.css'/>"
	rel="stylesheet" type="text/css">

<!-- Animation Css -->
<link href="<c:url value='/static/plugins/animate-css/animate.css'/>"
	rel="stylesheet" type="text/css">

<!-- Morris Chart Css-->
<link href="<c:url value='/static/plugins/morrisjs/morris.css'/>"
	rel="stylesheet" type="text/css">

<!-- JQuery DataTable Css -->
<link
	href="<c:url value='/static/plugins/jquery-datatable/skin/bootstrap/css/dataTables.bootstrap.css'/>"
	rel="stylesheet" type="text/css">

<!-- Sweetalert Css -->
<link href="<c:url value='/static/plugins/sweetalert/sweetalert.css'/>"
	rel="stylesheet" />

<!-- Custom Css -->
<link href="<c:url value='/static/css/style.css'/>" rel="stylesheet"
	type="text/css">

<!-- Bootstrap Select Css -->
<link
	href="<c:url value='/static/plugins/bootstrap-select/css/bootstrap-select.css'/>"
	rel="stylesheet" type="text/css">

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">

<!-- TagInput Js -->
<link
	href="<c:url value='/static/plugins/jquery-tag-input/jquery.tagsinput-revisited.css'/>"
	rel="stylesheet" type="text/css">

<!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
<link href="<c:url value='/static/css/themes/all-themes.css'/>"
	rel="stylesheet" type="text/css">

<link
	href="<c:url value='/static/plugins/multi-select/css/multi-select.css'/>"
	rel="stylesheet" type="text/css">

<!-- Custom Css -->
<link
	href="<c:url value='/static/plugins/summernote/css/summernote.min.css'/>"
	rel="stylesheet" type="text/css">

</head>
<body class="theme-grey">
	<input type="hidden" id="postMSG" name="postMSG" value="${data}">
	<!-- Page Loader -->
	<%@include file="../../plantilla/pageLoader.jsp"%>
	<!-- #END# Page Loader -->

	<!-- Overlay For Sidebars -->
	<div class="overlay"></div>
	<!-- #END# Overlay For Sidebars -->

	<!-- Top Bar -->
	<%@include file="../../plantilla/admin/topbar.jsp"%>
	<!-- #Top Bar -->

	<section>
		<!-- EmailModal -->
		<%@include file="../../admin/request/requestModal.jsp"%>
		<!-- #END# EmailModal -->
	</section>

	<section>
		<!-- Left Sidebar -->
		<%@include file="../../plantilla/admin/leftbar.jsp"%>
		<!-- #END# Left Sidebar -->
	</section>
	<section class="content m-t-90I">
		<div class="container-fluid">
			<div class="row clearfix">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="" style="padding-top: -5pc;">
						<h2 class="title-Adm m-t-0">REQUERIMIENTOS</h2>
					</div>
					<hr>
				</div>
			</div>
			<div class="row clearfix">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="button-demo flr">
										<button type="button" class="btn btn-primary setIcon"
											onclick="openExcelSync()">
											<span>SINCRONIZAR EXCEL</span><span><i
												class="material-icons m-t--2 ">update</i></span>
										</button>
									</div>
								</div>
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label>Proyecto</label>
					<div class="form-group">
						<select id="proyectFilter"
							class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="">-- Seleccione una opci&oacute;n --</option>
							<c:forEach items="${projects}" var="project">
								<option id="${project.id }" value="${project.code }">${project.code }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
					<label>Tipo Requerimiento</label>
					<div class="form-group">
						<select id="typeRequestFilter"
							class="form-control show-tick selectpicker"
							data-live-search="true">
							<option value="">-- Seleccione una opci&oacute;n --</option>
							<c:forEach items="${typeRequests}" var="typeRequest">
								<option id="${typeRequest.id }" value="${typeRequest.code }">${typeRequest.code }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="table-responsive m-b-20">
						<table
							class="table table-bordered table-striped table-hover dataTable"
							id="requestTable">
							<thead>
								<tr>
									<th>C�digo Soin</th>
									<th>C�digo ICE</th>
									<th>Descripci�n</th>
									<th>Proyecto</th>
									<th>Tipo requerimiento</th>
									<th class="actCol" style="text-align: center;">Activo</th>
									<th class="actCol" style="text-align: center;">Acciones</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${requests}" var="request">
									<tr id="${request.id}">
										<td>${request.code_soin}</td>
										<td>${request.code_ice}</td>
										<td>${request.description}</td>
										<td>${request.proyect.code}</td>
										<td>${request.typeRequest.code}</td>
										<td><div class="iconLine align-center">
												<c:if test="${!request.active}">
													<i id="softDeleteRequest_${request.id}"
														onclick="softDeleteRequest(${request.id}, ${request.active})"
														class="material-icons gris" style="font-size: 30px;">cancel</i>
												</c:if>
												<c:if test="${request.active}">
													<i id="softDeleteRequest_${request.id}"
														onclick="softDeleteRequest(${request.id},${request.active})"
														class="material-icons gris" style="font-size: 30px;">check_circle</i>
												</c:if>
											</div></td>
										<td><div class="iconLineC">
												<a onclick="updateRequestModal(${request.id})"><i
													class="material-icons gris" style="font-size: 30px;">mode_edit</i></a>

												<a onclick="confirmDeleteRequest(${request.id})"><i
													class="material-icons gris" style="font-size: 30px;">delete</i></a>
											</div></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<a id="buttonAddRequest" type="button"
			class="btn btn-primary btn-fab waves-effect fixedDown"
			onclick="openRequestModal()"> <i class="material-icons lh-1-8">add</i>
		</a>
	</section>

	<!-- Jquery Core Js -->
	<script src="<c:url value='/static/plugins/jquery/jquery.min.js'/>"></script>
	<script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

	<script type="text/javascript"
		src="//cdn.datatables.net/1.10.0/js/jquery.dataTables.js"></script>

	<!-- Bootstrap Core Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap/js/bootstrap.js'/>"></script>

	<!-- Select Plugin Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap-select/js/bootstrap-select.js'/>"></script>

	<!-- Slimscroll Plugin Js -->
	<script
		src="<c:url value='/static/plugins/jquery-slimscroll/jquery.slimscroll.js'/>"></script>

	<!-- Jquery CountTo Plugin Js -->
	<script
		src="<c:url value='/static/plugins/jquery-countto/jquery.countTo.js'/>"></script>

	<!-- Autosize Plugin Js -->
	<script src="<c:url value='/static/plugins/autosize/autosize.js'/>"></script>

	<!-- Jquery DataTable Plugin Js -->
	<script
		src="<c:url value='/static/plugins/jquery-datatable/jquery.dataTables.js'/>"></script>
	<script
		src="<c:url value='/static/plugins/jquery-datatable/skin/bootstrap/js/dataTables.bootstrap.js'/>"></script>

	<!-- Bootstrap Notify Plugin Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap-notify/bootstrap-notify.js'/>"></script>

	<!-- SweetAlert Plugin Js -->
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@9"></script>

	<!-- Moment Plugin Js -->
	<script src="<c:url value='/static/plugins/momentjs/moment.js'/>"></script>

	<!-- Bootstrap Material Datetime Picker Plugin Js -->
	<script
		src="<c:url value='/static/plugins/bootstrap-material-datetimepicker/js/bootstrap-material-datetimepicker.js'/>"></script>

	<!-- summernote -->
	<script
		src="<c:url value='/static/plugins/summernote/summernote.min.js'/>"></script>

	<!-- summernote -->
	<script
		src="<c:url value='/static/plugins/summernote/lang/summernote-es-ES.js'/>"></script>

	<!-- TagInput Js -->
	<script
		src="<c:url value='/static/plugins/jquery-tag-input/jquery.tagsinput-revisited.js'/>"></script>

	<!-- Custom Js -->
	<script src="<c:url value='/static/js/admin.js?v=${jsVersion}'/>"></script>
	<script
		src="<c:url value='/static/js/admin/request.js?v=${jsVersion}'/>"></script>
	<script
		src="<c:url value='/static/js/request/excelSync.js?v=${jsVersion}'/>"></script>
	<script src="<c:url value='/static/js/pages/index.js'/>"></script>
	<script src="<c:url value='/static/js/pages/ui/modals.js'/>"></script>

	<script
		src="<c:url value='/static/plugins/multi-select/js/jquery.multi-select.js'/>"></script>
</body>

</html>