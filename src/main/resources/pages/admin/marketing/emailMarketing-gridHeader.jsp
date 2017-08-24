<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>	
{title:"<s:message code="label.customer.optin.id" text="Id"/>", name:"id",canFilter:false},
{title:"<s:message code="label.email.description" text="Description"/>", name:"description",canFilter:false},
{title:"<s:message code="label.promotion.merchant" text="Store"/>", name: "merchant", align: "center",canFilter:false},
{title:"<s:message code="label.email.date" text="Start Date"/>", name:"date",canFilter:true},
{title:"<s:message code="label.entity.details" text="Details"/>", name: "buttonField", align: "center",canFilter:false,canSort:false, canReorder:false}

