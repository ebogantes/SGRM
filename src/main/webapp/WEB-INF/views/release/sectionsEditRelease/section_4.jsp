<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.soin.sgrm.model.ButtonCommand"%>

<div id="empty_4" style="display: none;">
	<%@include file="../../plantilla/emptySection.jsp"%>
</div>
<c:if test="${systemConfiguration.attachmentFiles}">
	<div class="button-demo flr">
		<button type="button" class="btn btn-primary setIcon"
			onclick="openAddFileModal()">
			<span>AGREGAR</span><span><i class="material-icons m-t--2 ">add</i></span>
		</button>
	</div>
	<div class="row clearfix activeSection">
		<div class="col-sm-12">
			<h5 class="titulares">Archivos Adjuntos</h5>
		</div>
	</div>

	<div class="row clearfix">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="table-responsive"
				style="margin-top: 20px; margin-bottom: 20px;">
				<table
					class="table table-bordered table-striped table-hover dataTable"
					id="attachedFilesTable">
					<thead>
						<tr>
							<th>Nombre</th>
							<th>Fecha Carga</th>
							<th class="actCol"
								style="text-align: center; padding-left: 0px; padding-right: 0px;">Acciones</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${docs}" var="doc">
							<tr>
								<td>${doc.name}</td>
								<td></td>
								<td>
									<div style="text-align: center">
										<div class="iconLine">
											<a href="#" class="" style="visibility: hidden;"> <i
												class="material-icons gris" style="font-size: 30px;">delete</i>
											</a> <a class=""> <i class="material-icons gris"
												style="font-size: 30px;">cloud_download</i>
											</a>
										</div>
									</div>
							</tr>
						</c:forEach>
						<tr>
							<td>OBJETOS IMPACTADOS</td>
							<td></td>
							<td>
								<div style="text-align: center">
									<div class="iconLine">
										<a href="#" class="" style="visibility: hidden;"> <i
											class="material-icons gris" style="font-size: 30px;">delete</i>
										</a> <a href="<c:url value='/file/impactObject-${release.id}'/>"
											download class=""> <i class="material-icons gris"
											style="font-size: 30px;">cloud_download</i>
										</a>
									</div>
								</div>
							</td>
						</tr>

						<c:forEach items="${release.files}" var="fileRelease">
							<tr id="${fileRelease.id}">
								<td>${fileRelease.name}</td>
								<td><fmt:formatDate value="${fileRelease.revisionDate}"
										type="both" /></td>
								<td>
									<div style="text-align: center">
										<div class="iconLine">
											<a onclick="deleteReleaseFile(${fileRelease.id})" download
												class=""> <i class="material-icons gris"
												style="font-size: 30px;">delete</i>
											</a> <a
												href="<c:url value='/file/singleDownload-${fileRelease.id}'/>"
												download class=""> <i class="material-icons gris"
												style="font-size: 30px;">cloud_download</i>
											</a>
										</div>
									</div>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</c:if>