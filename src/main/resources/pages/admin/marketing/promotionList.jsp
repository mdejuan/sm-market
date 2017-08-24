<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="true" %>				
				
<script
	src="<c:url value="/resources/js/admin/marketing/commonLinks.js" />"></script>
<div class="tabbable">

					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">

    					<div class="tab-pane active" id="customer-section">



								<div class="sm-ui-component">	
								
								
				<h3>
						<s:message code="menu.promotions-list" text="List of Promotions" />
				</h3>	
				<br/>

				 <!-- Listing grid include -->
				 <c:set value="/admin/promotion/paging.html" var="pagingUrl" scope="request"/>
				 <c:set value="/admin/promotion/remove.html" var="removeUrl" scope="request"/>
				 <c:set value="/admin/promotion/edit.html" var="editUrl" scope="request"/>
				 <c:set value="/admin/promotion/list.html" var="afterRemoveUrl" scope="request"/>
				 <c:set var="entityId" value="id" scope="request"/>
				 <c:set var="componentTitleKey" value="menu.promotion-list" scope="request"/>
				 <c:set var="gridHeader" value="/pages/admin/marketing/promotion-gridHeader.jsp" scope="request"/>
				 <c:set var="canRemoveEntry" value="true" scope="request"/>

            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
				 <!-- End listing grid include -->

    					</div>  


 					</div>


					</div>

		</div>		      			     