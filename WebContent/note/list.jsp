<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="col-md-9">
	<div class="data_list">
		<div class="data_list_title">
			<span class="glyphicon glyphicon glyphicon-th-list"></span>&nbsp;云记列表
		</div>
		<c:if test="${resultInfo.code !=1 }">
			<h2>${resultInfo.msg }</h2>
		</c:if>
		<c:if test="${resultInfo.code==1 }">
			<div class="note_datas">
				<ul>
					<c:forEach items="${resultInfo.result.dataList }" var="item">
						<fmt:formatDate value="${item.pubTime }" pattern="yyyy-MM-dd"
							var="noteDate" />
						<li>『 ${noteDate }』&nbsp;&nbsp;<a
							href="note?actionName=
						detail&noteId=${item.noteId }">${item.title }</a>
						</li>
					</c:forEach>
				</ul>
			</div>

			<!-- 分页导航栏begin -->
			<nav style="text-align: center">
				<ul class="pagination   center">
					<c:if test="${resultInfo.result.currentPage != 1}">
						<li><a
							href="index?currentPage=${resultInfo.result.currentPage-1 }"
								<c:if test="${!empty actionName }">&actionName=${actionName }
							</c:if>
							 <c:if test="${!empty title }">&title=${title}</c:if>
							  <c:if test="${!empty date }">&date=${date}</c:if> 
							  <c:if test="${!empty typeId }">&typeId=${typeId}</c:if>">
							  <span>上一页</span>
						</a></li>
					</c:if>

					<!-- 页码数组 -->
					<c:forEach items="${resultInfo.result.navPageNumbers }" var="p">
						<li
							<c:if test="${resultInfo.result.currentPage == p}"> class="active"
						</c:if>>
							<a
							href="index?currentPage=${p }<c:if test="${!empty actionName }">&actionName=${actionName
							}</c:if><c:if test="${!empty title }">&title=${title}
							</c:if><c:if test="${!empty date }">&date=${date}</c:if>
							<c:if test="${!empty typeId }">&typeId=${typeId}</c:if>">
								${p }</a>
						</li>
					</c:forEach>

					<!-- 下一页  -->
					<c:if
						test="${resultInfo.result.currentPage!=resultInfo.result.totalpages }">
						<li><a
							href="index?currentPage=${resultInfo.result.currentPage+1 }<c:if test="${!empty actionName }">&actionName=
							${actionName }</c:if><c:if test="${!empty title }">
							&title=${title}</c:if><c:if test="${!empty date }">
							&date=${date}</c:if><c:if test="${!empty typeId }">&typeId
							=${typeId}</c:if>"><span>下一页</span></a>
						</li>
					</c:if>
				</ul>
			</nav>
			<!-- 分页导航栏end -->
		</c:if>
	</div>
</div>
