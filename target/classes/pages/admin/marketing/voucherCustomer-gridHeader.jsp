<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>	
{title:"<s:message code="label.customer.optin.id" text="Id"/>", name:"id",canFilter:false},
{title:"<s:message code="label.voucherCustomer.idCust" text="Id Customer"/>", name:"idCustomer",canFilter:true},
{title:"<s:message code="label.voucherCustomer.voucherId" text="Id Voucher"/>", name:"voucher.id",canFilter:true},
{title:"<s:message code="label.voucherCustomer.date" text="Date"/>", name:"date",canFilter:true},
{title:"<s:message code="label.voucher.merchant" text="Store"/>", name: "merchant", align: "center",canFilter:true},


