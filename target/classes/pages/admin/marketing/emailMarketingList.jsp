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
						<s:message code="menu.email-list" text="List of Emails Content" />
				</h3>	
				<br/>

				 <!-- Listing grid include -->
				 <c:set value="/admin/emailmarketing/paging.html" var="pagingUrl" scope="request"/>
				 <c:set value="/admin/emailmarketing/remove.html" var="removeUrl" scope="request"/>
				 <c:set value="/admin/emailmarketing/edit.html" var="editUrl" scope="request"/>
				 <c:set value="/admin/emailmarketing/list.html" var="afterRemoveUrl" scope="request"/>
				 <c:set var="entityId" value="id" scope="request"/>
				 <c:set var="componentTitleKey" value="menu.email-list" scope="request"/>
				 <c:set var="gridHeader" value="/pages/admin/marketing/emailMarketing-gridHeader.jsp" scope="request"/>
				 <c:set var="canRemoveEntry" value="true" scope="request"/>

            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
				 <!-- End listing grid include -->

    					</div>  


 					</div>


					</div>

		</div>		      			     