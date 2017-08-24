
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>


<script
	src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>

<link
	href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />"
	rel="stylesheet"></link>
<script
	src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>
<script
	src="<c:url value="/resources/js/admin/marketing/commonLinks.js" />"></script>

<script type="text/javascript">

	$(document).ready(function() {
		
		$('#sortOrder').numeric();

		if ('${voucher.id}' == '') {
			
			$('#merchant').find('option:first').attr('selected', 'selected');
			$('#voucherForm :input').not(':button,:hidden').each(function() {
				$(this).val('');
			});
			$('#btnSubmit').attr('disabled', true);$('#btnSubmit').attr('disabled', true);
			$('#orderDiscountDiv').show();
			$('#total_discount').show();
			$('#total_discount_percentage').hide();
		
		} else {
			
			   			
			 if ($('#orderDiscountPercentage').val()!='') {
				$('#percentageDiscount').attr('checked', true);
				$('#orderDiscountDiv').show();
				$('#total_discount').hide();
				$('#total_discount_percentage').show();
			} else {
				$('#orderDiscountDiv').show();
				$('#total_discount').show();
				$('#total_discount_percentage').hide();
			} 
					

    	}



		$("#voucherForm :input").on("change keyup paste", function() {
			isValid();
		});
		
		$('#percentageDiscount').change(function() {
			if ($(this).is(":checked")) {
				$('#orderTotalDiscount').val("");	
				$('#total_discount').hide();
				$('#total_discount_percentage').show();
			} else {
				$('#orderDiscountPercentage').val("");
				$('#total_discount').show();
				$('#total_discount_percentage').hide();
			}

		});

		function isValid() {
			if (isFValid()) {
				$('#btnSubmit').attr('disabled', false);
				return true;
			} else {
				$('#btnSubmit').attr('disabled', true);
				return false;
			}
		}
		
		$('#active').change(function() {
			if ($(this).is(":checked")) {
				$('#active').val('true');	
				
			} else {
				$('#active').val('false');	
			}

		});

		function isFValid() {
			var fieldValid;
			$("form#voucherForm :input").each(function() {
				if ($(this).hasClass('highlight')) {

					console.log($(this).attr('id') + ' - ' + $(this).css('display'));

					fieldValid = isFieldValid($(this));
					if (fieldValid) {
						return true;
					} else {
						return false;
					}
				}
			});

			return fieldValid;
		}
		function isFieldValid(field) {
			if (field.is(":hidden")) {
				return true;
			}
			var value = field.val();
			if (!emptyString(value)) {
				field.css('background-color', '#FFF');
				return true;
			} else {
				field.css('background-color', '#FFC');
				return false;
			}
		}

		function emptyString($value) {
			return !$value || !/[^\s]+/.test($value);
		}




	});
</script>

<div id="tabbable" class="tabbable">

	<jsp:include page="/common/adminTabs.jsp" />

	<div class="tab-content">

		<div class="tab-pane active" id="customer-section">



			<div class="sm-ui-component">


				<h3>
					<s:message code="menu.vouchers" text="Vouchers management" />
				</h3>


				<h3>
					<c:choose>
						<c:when test="${voucher.id!=null && voucher.id>0}">
							<s:message code="label.voucher.edit" text="Edit voucher" />
						</c:when>
						<c:otherwise>
							<s:message code="label.voucher.create" text="Create voucher" />
						</c:otherwise>
					</c:choose>

				</h3>
				<br />

				<c:url var="voucherSave" value="/admin/voucher/save.html" />


				<form:form method="POST" commandName="voucher"
					action="${voucherSave}" id="voucherForm">


					<form:errors path="*" cssClass="alert alert-error" element="div" />
					<div id="store.success" class="alert alert-success"
						style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
						<s:message code="message.success" text="Request successfull" />
					</div>


					<form:hidden path="id" id="idVoucher" />
					
					<div id="commonPromo">
						<div class="control-group" >
							<label class="required"><s:message
									code="label.voucher.description" text="Description" /></label>
							<div class="controls">
								<form:input cssClass="input-large highlight" id="description"
									path="description" />
								<span class="help-inline"><form:errors path="description"
										cssClass="error" /></span>

							</div>
						</div>
						<div class="control-group" >
							<label class="required"><s:message
									code="label.voucher.code" text="Code" /></label>
							<div class="controls">
								<form:input cssClass="input-large highlight" id="code"
									path="code" />
								<span class="help-inline"><form:errors path="code"
										cssClass="error" /></span>

							</div>
						</div>
						<div class="control-group" >
							<label class="required"><s:message
									code="label.customer.optin.merchant" text="Store Name" /></label>
							<div class="controls">

								<form:select cssClass="highlight" path="merchant.id"
									id="merchant">
									<form:option value="" label="*** Select Option ***" />
									<form:options items="${merchants}" itemValue="id"
										itemLabel="storename" />
								</form:select>
								<span class="help-inline"><form:errors
										path="merchant.code" cssClass="error" /></span>


							</div>
						</div>

						
						<div class="control-group">
							<label class="required"><s:message
									code="label.voucher.startDate" text="Start Date" /></label>
							<div class="controls">

								<form:input cssClass="input-large" readonly="true"
									path="startDate" class="small" type="date"
									data-date-format="<%=com.salesmanager.core.business.constants.Constants.DEFAULT_DATE_FORMAT%>" />
								<script type="text/javascript">
									$('#startDate').datepicker();
								</script>
								<span class="help-inline"><form:errors path="startDate"
										cssClass="error" /></span>

							</div>
						</div>

						<div class="control-group">
							<label class="required"><s:message
									code="label.voucher.endDate " text="End Date" /></label>
							<div class="controls">

								<form:input cssClass="input-large" path="endDate"
									readonly="true" class="small" type="date"
									data-date-format="<%=com.salesmanager.core.business.constants.Constants.DEFAULT_DATE_FORMAT%>" />
								<script type="text/javascript">
									$('#endDate').datepicker();
								</script>
								<span class="help-inline"><form:errors path="endDate"
										cssClass="error" /></span>

							</div>
						</div>
						
						<div class="control-group" id="lastOrderDiv">
							<label class="required"><s:message
									code="label.voucher.customerLastOrder"
									text="Customer Last Order" /></label>
							<div class="controls">

								<form:input cssClass="input-large" path="custLastOrder" id="custLastOrder" 
									readonly="true" class="small" type="date"
									data-date-format="<%=com.salesmanager.core.business.constants.Constants.DEFAULT_DATE_FORMAT%>" />
								<script type="text/javascript">
									$('#custLastOrder').datepicker();
								</script>
								<span class="help-inline"><form:errors
										path="custLastOrder" cssClass="error" /></span>

							</div>
						</div>
						<div class="control-group" id="minQtyDiv">
							<label class="required"><s:message
									code="label.voucher.redemed" text="Max Redeemed" /></label>
							<div class="controls">
								<form:input
									onkeyup="if (/\D/g.test(this.value)) this.value = this.value.replace(/\D/g,'')"
									type="text" id="maxRedeemed" path="maxRedeemed" />
								<span class="help-inline"><form:errors path="maxRedeemed"
										cssClass="error" /></span>

							</div>
						</div>

						<div class="control-group" id="minQtyDiv">
							<label class="required"><s:message
									code="label.voucher.qtyminy" text="Min Quantity" /></label>
							<div class="controls">
								<form:input
									onkeyup="if (/\D/g.test(this.value)) this.value = this.value.replace(/\D/g,'')"
									type="text" id="qtymin" path="qtymin" />
								<span class="help-inline"><form:errors path="qtymin"
										cssClass="error" /></span>

							</div>
						</div>
						<div class="control-group">
							<label class="required"><s:message
									code="label.voucher.qtymax" text="Max Quantity" /></label>
							<div class="controls">
								<form:input
									onkeyup="if (/\D/g.test(this.value)) this.value = this.value.replace(/\D/g,'')"
									type="text" id="qtymax" path="qtymax" />
								<span class="help-inline"><form:errors path="qtymax"
										cssClass="error" /></span>

							</div>
						</div>
						
						<div id="orderDiscountDiv">
							<div class="control-group">
								<label class="required"><s:message
										code="label.voucher.percentage"
										text="Is percentage discount?" /></label>
								<div class="controls">
									<input type="checkbox" id="percentageDiscount" />
								</div>
							</div>
							<div class="control-group" id="total_discount">
								<label class="required"><s:message
										code="label.voucher.total_discount" text="Total Discount" /></label>
								<div class="controls">
									<form:input cssClass="input-large highlight"
										path="orderTotalDiscount" id="orderTotalDiscount"
										onkeyup="if (/\D/g.test(this.value)) this.value = this.value.replace(/\D/g,'')"
										type="text" />
									<span class="help-inline"><form:errors
											path="orderTotalDiscount" cssClass="error" /></span>

								</div>
							</div>
							<div class="control-group" id="total_discount_percentage">
								<label class="required"><s:message
										code="label.voucher.total_discount_percentage"
										text="Total Discount Percentage" /></label>
								<div class="controls">
									<form:input cssClass="input-large highlight"
										path="orderDiscountPercentage" id="orderDiscountPercentage"
										onkeyup="if (/\D/g.test(this.value)) this.value = this.value.replace(/\D/g,'')"
										type="text" />
									<span class="help-inline"><form:errors
											path="orderDiscountPercentage" cssClass="error" /></span>

								</div>
							</div>
						</div>
						<div class="control-group">
								<label ><s:message
										code="label.promotion.active"
										text="Is active?" /></label>
								<div class="controls">
									<form:checkbox path="active" id="active"/>
								</div>
						</div>
					</div>


					<div class="form-actions">

						<div class="pull-right">

							<button type="submit" class="btn btn-success" id="btnSubmit">
								<s:message code="button.label.submit" text="Submit" />
							</button>


						</div>

					</div>

				</form:form>


				<br />

				
			</div>


		</div>


	</div>

</div>
