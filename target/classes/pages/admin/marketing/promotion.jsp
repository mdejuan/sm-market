
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>

<script
	src="<c:url value="/resources/js/admin/marketing/jquery.multi-select.js" />"></script>
<script
	src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>
<script
	src="<c:url value="/resources/js/admin/marketing/jquery.quicksearch.js" />"></script>
<link
	href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />"
	rel="stylesheet"></link>
<script
	src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>
<link
	href="<c:url value="/resources/css/admin/marketing/multi-select.css" />"
	rel="stylesheet"></link>
<script
	src="<c:url value="/resources/js/admin/marketing/commonLinks.js" />"></script>





<script type="text/javascript">

	$(document).ready(function() {
	
		$('#categories-select').multiSelect();
		$('#products-select').multiSelect();
		$('#products-select-free').multiSelect();
		$('#manufacturers-select').multiSelect();	
		$('#sortOrder').numeric();

		$('#orderDiscountDiv').hide();

		$('#01div').hide();

		if ('${promotion.id}' == '') {
			$('#promotionType').val('');
			$('#promotionType').find('option:first').attr('selected', 'selected');
			$('#merchant').find('option:first').attr('selected', 'selected');
			$('#promotionForm :input').not(':button,:hidden').each(function() {
				$(this).val('');
			});
			$('#btnSubmit').attr('disabled', true);$('#btnSubmit').attr('disabled', true);
			$('#commonPromo').hide();
			$('#productsDiv').hide();
			$('#productsDivFree').hide();
			$('#manufacturersDiv').hide();
			$('#categoriesDiv').hide();
			$('#thresholdDiv').hide();
			$('#slotDiv').hide();
			$('#moneyBackDiv').hide();
			$('#couponDiv').hide();
			$('#minMax').hide();
			
		} else {
			$('#promotionType').find('option:first').val('${promotionType}');
			
			$('#categoriesDiv').hide();
			$('#productsDiv').hide();
			$('#productsDivFree').hide();
			$('#manufacturersDiv').hide();
			$('#thresholdDiv').hide();
			$('#orderDiscountDiv').hide();
			$('#slotDiv').hide();
			$('#moneyBackDiv').hide();
			$('#couponDiv').hide();
			var option = $('#promotionType').find('option:selected').val();
			switch (option){
				case "Order_Discount": 
					$('#orderDiscountDiv').show();
					$('#total_discount').show();
					$('#minMax').show();
					break;
				case "Product_Discount": 
					getProducts("p1");
					$('#productsDiv').show();	
					$('#orderDiscountDiv').show();
					$('#minMax').hide();
					$('#acumulativeId').hide();
					$('#couponDiv').hide();
					break;
				case "Category_Discount": 
					getCategories();
					$('#categoriesDiv').show();	
					$('#orderDiscountDiv').show();
					$('#minMax').hide();
					break;
				case "Manufacturer_Discount": 
					getManufacturers();
					$('#manufacturersDiv').show();	
					$('#orderDiscountDiv').show();
					$('#minMax').hide();
					break;
				case "Buy_X_get_Y_Free": 
					getProducts("p2");
					$('#productsDiv').show();
					$('#productsDivFree').show();	
					
					break;
				case "Free_Shipping": 
					getProducts("p1");
					getCategories();
					$('#productsDiv').show();
					$('#categoriesDiv').show();		
					$('#minMax').show();
					break;
				case "Threshold": 
					$('#thresholdDiv').show();
					$('#commonPromo').show();
					$('#orderDiscountDiv').hide();
					var option = $('#thresholdType').find('option:selected').val();
					if(option=="Free_Product")
					{
						getProducts("p1");
						$('#productsDiv').show();
					}
					if(option=="Coupon")
					{
						$('#couponDiv').show();
						var optionCoupon = $('#couponType').find('option:selected').val();
						switch (optionCoupon){
							case "Money_Back": 	
								$('#moneyBackDiv').show();	
								break;
							case "Order_Discount": 
								$('#orderDiscountDiv').show();	
								$('#total_discount').show();
								break;
							case "Free_Product": 
								getProducts("p1");
								$('#productsDiv').show();
								break;
							case "Discount_Product": 
								getProducts("p1");
								$('#orderDiscountDiv').show();
								$('#total_discount').show();
								$('#productsDiv').show();
								break;
							case "Discount_Category": 
								getCategories();
								$('#orderDiscountDiv').show();
								$('#total_discount').show();							
								$('#categoriesDiv').show();
								break;
							case "Free_Shipping": 
								getProducts("p1");
								getCategories();
								$('#productsDiv').show();
								$('#categoriesDiv').show();	
								break;
							case "Slot": 
								$('#slotDiv').show();
								break;
						 }
						
					}
					break;
			
			   }			
			if ('${promotion.discountPercentage}'=='true') {
				$('#orderDiscountDiv').show();
				$('#total_discount').hide();
				$('#total_discount_percentage').show();
			} else {
				$('#orderDiscountDiv').show();
				$('#total_discount').show();
				$('#total_discount_percentage').hide();
			}
			
			
			

    	}



		$("#promotionForm :input").on("change keyup paste", function() {
			isValid();
		});

		$('#promotionType').change(function() {
			$('#total_discount').hide();
			$('#merchant').find('option:first').attr('selected', 'selected');
			$('#cats').val("");
			$('#products2').val("");
			$('#productsFree').val("");
			$('#manufacturers').val("");
			$('#idPromo').val("");
			$('#percentageDiscount').attr('checked', false);
			$('#orderDiscountPercentage').val("");
			$('#orderTotalDiscount').val("");
			$('#slotDiv').hide();
			$('#moneyBackDiv').hide();
			$('#couponDiv').hide();
			$('#minMax').hide();
			var option = $(this).find('option:selected').val();
			if (option != "") {
				$('#commonPromo').show();
				$('#products-select').empty();
				$('#products-select-free').empty();
				$('#categories-select').empty();
				$('#manufacturers-select').empty();	
				$('#categories-select').multiSelect('refresh');
				$('#products-select').multiSelect('refresh');
				$('#products-select-free').multiSelect('refresh');
				$('#manufacturers-select').multiSelect('refresh');
				$('#categoriesDiv').hide();	
				$('#productsDivFree').hide();
				$('#productsDiv').hide();
				$('#manufacturersDiv').hide();
				$('#total_discount_percentage').hide();
				$('#orderDiscountDiv').hide();
				$('#total_discount').hide();
				$('#thresholdDiv').hide();
				$('#minQtyDiv').show();
			  switch (option){
				case "Order_Discount": 
					$('#orderDiscountDiv').show();
					$('#total_discount').show();
					$('#minMax').show();
					break;
				case "Category_Discount":
					$('#orderDiscountDiv').show();
					$('#total_discount').show();							
					$('#categoriesDiv').show();
					break;
				case "Product_Discount":
					$('#orderDiscountDiv').show();
					$('#total_discount').show();				
					$('#productsDiv').show();				
					break;
				case "Manufacturer_Discount":
					$('#orderDiscountDiv').show();
					$('#total_discount').show();				
					$('#manufacturersDiv').show();				
					break;
				case "Buy_X_get_Y_Free":								
					$('#productsDiv').show();
					$('#productsDivFree').show();
					break;
				case "Free_Shipping":	
					$('#categoriesDiv').show();			
					$('#productsDiv').show();	
					$('#minMax').show();
					break;
				case "Threshold":
					$('#thresholdDiv').show();	
					$('#commonPromo').show();
					$('#orderDiscountDiv').hide();
					$('#minQtyDiv').hide();				
					break;
			  }
			} else {

				$('#commonPromo').hide();

			}
		});

		$('#merchant').change(function() {
			
			var option = $('#promotionType').find('option:selected').val();
			 switch (option){
				case "Category_Discount": 
					getCategories();				
					break;
				case "Product_Discount": 
					getProducts("p1");				
					break;
				case "Manufacturer_Discount": 
					getManufacturers();				
					break;
				case "Buy_X_get_Y_Free": 
					getProducts("p2");			
					break;
				case "Free_Shipping": 
					getCategories();
					getProducts("p1");		
					break;
				case "Threshold":
					getProducts("p1");		
					break;
			 }
		
		});
		
		$('#thresholdType').change(function() {
			$('#productsDiv').hide();	
			$('#orderDiscountDiv').hide();
			$('#total_discount').hide();
			$('#productsDiv').hide();	
			$('#orderDiscountDiv').hide();
			$('#total_discount').hide();
			$('#categoriesDiv').hide();
			$('#slotDiv').hide();
			$('#moneyBackDiv').hide();
			$('#couponDiv').hide();
			var option = $('#thresholdType').find('option:selected').val();
			 switch (option){
				case "Free_Shipping": 					
					break;
				case "Order_Discount": 
					$('#orderDiscountDiv').show();
					$('#total_discount').show();				
					break;
				case "Free_Product": 
					$('#productsDiv').show();	
					break;
				case "Coupon": 
					$('#couponDiv').show();
					break;
			 }
		
		});
		
		$('#couponType').change(function() {
			$('#productsDiv').hide();	
			$('#orderDiscountDiv').hide();
			$('#total_discount').hide();
			$('#categoriesDiv').hide();
			$('#slotDiv').hide();
			$('#moneyBackDiv').hide();
			var option = $('#couponType').find('option:selected').val();
			 switch (option){
				case "Money_Back": 	
					$('#moneyBackDiv').show();	
					break;
				case "Order_Discount": 
					$('#orderDiscountDiv').show();	
					$('#total_discount').show();
					break;
				case "Free_Product": 
					$('#productsDiv').show()
					break;
				case "Discount_Product": 
					$('#orderDiscountDiv').show();
					$('#total_discount').show();	
					$('#productsDiv').show();
					break;
				case "Discount_Category": 
					getCategories();
					$('#orderDiscountDiv').show();
					$('#total_discount').show();							
					$('#categoriesDiv').show();
					break;
				case "Free_Shipping":
					getCategories();
					$('#productsDiv').show();
					$('#categoriesDiv').show();
					break;
				case "Slot": 
					$('#slotDiv').show();
					break;
			 }
		
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
				$('#amount').val($('#orderTotalDiscount').val());
			}

		});
		
		$('#active').change(function() {
			if ($(this).is(":checked")) {
				$('#active').val('true');	
				
			} else {
				$('#active').val('false');	
			}

		});
		
		$('#acumulative').change(function() {
			if ($(this).is(":checked")) {
				$('#acumulative').val('true');	
				
			} else {
				$('#acumulative').val('false');	
			}

		});
		
		$('#newCustomer').change(function() {
			if ($(this).is(":checked")) {
				$('#newCustomer').val('true');	
				
			} else {
				$('#newCustomer').val('false');	
			}

		});

		function getProducts(combos) {
			var combo = combos;
			$.ajax({
				type : 'GET',
				url : '<c:url value="/admin/promotion/products.html"/>',
				data : 'merchant=' + $('#merchant').find('option:selected').val(),
				dataType : 'json',
				success : function(response) {

					var status = isc.XMLTools.selectObjects(response, "/response/status");
					if (status == 0 || status == 9999) {

						var data = isc.XMLTools.selectObjects(response, "/response/data");
						if (data && data.length > 0) {

							var html = '';
							var len = data.length;
							for (var i = 0; i < len; i++) {
								html += '<option value="' + data[i].id + '">' + data[i].description + '</option>';
							}
							$('#products-select').append(html);
							$('#products-select').multiSelect('refresh');
							if(combo == "p2")
							{
								$('#products-select-free').append(html);
								$('#products-select-free').multiSelect('refresh');
							}
							if ('${promotion.id}' != '') {
								$("#products-select > option").each(function(index1, element1) {

									$("#01product > option").each(function(index2, element2) {

										if (element1.value == element2.value) {
											$('#products-select').multiSelect('select', element2.value);
										}
									});


								});
								if(combo == "p2")
								{
									$("#products-select-free > option").each(function(index1, element1) {

										$("#02product > option").each(function(index2, element2) {

											if (element1.value == element2.value) {
												$('#products-select-free').multiSelect('select', element2.value);
											}
										});


									});
								}
							}

						} else {

						}
					} else {

					}


				},
				error : function(xhr, textStatus, errorThrown) {
					alert('error ' + errorThrown);
				}
			});
		}

		function getCategories() {
			$.ajax({
				type : 'GET',
				url : '<c:url value="/admin/promotion/categories.html"/>',
				data : 'merchant=' + $('#merchant').find('option:selected').val(),
				dataType : 'json',
				success : function(response) {

					var status = isc.XMLTools.selectObjects(response, "/response/status");
					if (status == 0 || status == 9999) {

						var data = isc.XMLTools.selectObjects(response, "/response/data");
						if (data && data.length > 0) {

							var html = '';
							var len = data.length;
							for (var i = 0; i < len; i++) {
								html += '<option value="' + data[i].id + '">' + data[i].code + '</option>';
							}
							$('#categories-select').append(html);
							$('#categories-select').multiSelect('refresh');
							if ('${promotion.id}' != '') {
								$("#categories-select > option").each(function(index1, element1) {

									$("#01category > option").each(function(index2, element2) {

										if (element1.value == element2.value) {
											$('#categories-select').multiSelect('select', element2.value);
										}
									});


								});
							}


						} else {

						}
					} else {

					}


				},
				error : function(xhr, textStatus, errorThrown) {
					alert('error ' + errorThrown);
				}
			});
		}
		
		function getManufacturers() {
			$.ajax({
				type : 'GET',
				url : '<c:url value="/admin/promotion/manufacturers.html"/>',
				data : 'merchant=' + $('#merchant').find('option:selected').val(),
				dataType : 'json',
				success : function(response) {

					var status = isc.XMLTools.selectObjects(response, "/response/status");
					if (status == 0 || status == 9999) {

						var data = isc.XMLTools.selectObjects(response, "/response/data");
						if (data && data.length > 0) {

							var html = '';
							var len = data.length;
							for (var i = 0; i < len; i++) {
								html += '<option value="' + data[i].id + '">' + data[i].code + '</option>';
							}
							$('#manufacturers-select').append(html);
							$('#manufacturers-select').multiSelect('refresh');
							if ('${promotion.id}' != '') {
								$("#manufacturers-select > option").each(function(index1, element1) {

									$("#01manufacturer > option").each(function(index2, element2) {

										if (element1.value == element2.value) {
											$('#manufacturers-select').multiSelect('select', element2.value);
										}
									});


								});
							}


						} else {

						}
					} else {

					}


				},
				error : function(xhr, textStatus, errorThrown) {
					alert('error ' + errorThrown);
				}
			});
		}
		function isValid() {
			if (isFValid()) {
				$('#btnSubmit').attr('disabled', false);
				$('#amount').val($('#orderTotalDiscount').val());
				return true;
			} else {
				$('#btnSubmit').attr('disabled', true);
				return false;
			}
		}

		function isFValid() {
			var fieldValid;
			$("form#promotionForm :input").each(function() {
				if ($(this).hasClass('highlight')) {

					//console.log($(this).attr('id') + ' - ' + $(this).css('display'));

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
					<s:message code="menu.promotions" text="Promotions management" />
				</h3>


				<h3>
					<c:choose>
						<c:when test="${promotion.id!=null && promotion.id>0}">
							<s:message code="label.promotion.edit" text="Edit promotion" />
						</c:when>
						<c:otherwise>
							<s:message code="label.promotion.create" text="Create promotion" />
						</c:otherwise>
					</c:choose>

				</h3>
				<br />

				<c:url var="promotionSave" value="/admin/promotion/save.html" />


				<form:form method="POST" commandName="promotion"
					action="${promotionSave}" id="promotionForm">


					<form:errors path="*" cssClass="alert alert-error" element="div" />
					<div id="store.success" class="alert alert-success"
						style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
						<s:message code="message.success" text="Request successfull" />
					</div>


					<form:hidden path="id" id="idPromo" />
					<input type="hidden" id="cats" name="cats" />
					<input type="hidden" id="products2" name="products2" />
					<input type="hidden" id="productsFree" name="productsFree" />
					<input type="hidden" id="manufacturers" name="manufacturers" />
					<input type="hidden" id="amount" name="amount" />
					<div class="control-group">
						<label><s:message code="label.promotion.type" text="Type" /></label>
						<div class="controls">
							<form:select path="promotionType" id="promotionType">
								<form:option value="" label="*** Select Option ***" />
								<form:options items="${promotionTypes}" />
							</form:select>
							<span class="help-inline"><form:errors
									path="promotionType" cssClass="error" /></span>

						</div>
					</div>
					
					<div id="commonPromo">
						<div class="control-group" >
							<label class="required"><s:message
									code="label.promotion.code" text="Code" /></label>
							<div class="controls">
								<form:input cssClass="input-large highlight" id="codePromo"
									path="codePromo" />
								<span class="help-inline"><form:errors path="codePromo"
										cssClass="error" /></span>

							</div>
						</div>
						<div class="control-group" >
							<label class="required"><s:message
									code="label.promotion.description" text="Description" /></label>
							<div class="controls">
								<form:input cssClass="input-large highlight" id="description"
									path="description" />
								<span class="help-inline"><form:errors path="description"
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
						<div id="thresholdDiv">
								<div class="control-group" >
									<label class="required"><s:message
											code="label.promotion.threshold" text="Threshold Qty" /></label>
									<div class="controls">
										<form:input cssClass="highlight"
											onkeyup="if (/\D/g.test(this.value)) this.value = this.value.replace(/\D/g,'')"
											type="text" path="threshold" />
										<span class="help-inline"><form:errors path="threshold"
												cssClass="error" /></span>
		
									</div>
								</div>
								<div class="control-group">
									<label><s:message code="label.promotion.thresholdType" text="Type" /></label>
									<div class="controls">
										<form:select cssClass="highlight" path="thresholdType" id="thresholdType">
											<form:option value="" label="*** Select Option ***" />
											<form:options items="${thresholdTypes}" />
										</form:select>
										<span class="help-inline"><form:errors
												path="thresholdType" cssClass="error" /></span>
			
									</div>
								</div>

						</div>
						
						<div class="control-group">
							<label class="required"><s:message
									code="label.promotion.startDate" text="Start Date" /></label>
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
									code="label.promotion.endDate " text="End Date" /></label>
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

						<div id="minMax">
							<div class="control-group" id="minQtyDiv">
								<label class="required"><s:message
										code="label.promotion.qtyminy" text="Min Quantity" /></label>
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
										code="label.promotion.qtymax" text="Max Quantity" /></label>
								<div class="controls">
									<form:input
										onkeyup="if (/\D/g.test(this.value)) this.value = this.value.replace(/\D/g,'')"
										type="text" id="qtymax" path="qtymax" />
									<span class="help-inline"><form:errors path="qtymax"
											cssClass="error" /></span>
	
								</div>
							</div>
						</div>
						<div class="control-group" id="couponDiv">
							<label><s:message code="label.promotion.coupon" text="Coupon" /></label>
							<div class="controls">
								<form:select cssClass="highlight" path="couponType" id="couponType">
									<form:option value="" label="*** Select Option ***" />
									<form:options items="${couponType}" />
								</form:select>
								<span class="help-inline"><form:errors
										path="couponType" cssClass="error" /></span>
	
							</div>
						</div>
						
						<div class="control-group" id="moneyBackDiv">
							<label><s:message code="label.promotion.moneyBack" text="Money Back" /></label>
							<div class="controls">
								<div class="controls">
									<form:input cssClass="input-large highlight"
										path="moneyBack" id="moneyBack"
										onkeyup="if (/\D/g.test(this.value)) this.value = this.value.replace(/\D/g,'')"
										type="text" />
									<span class="help-inline"><form:errors
											path="moneyBack" cssClass="error" /></span>

								</div>
	
							</div>
						</div>
						
						<div class="control-group" id="slotDiv">
							<label><s:message code="label.promotion.couponSlot" text="Slot" /></label>
							<div class="controls">
								<div class="controls">
									<form:input cssClass="input-large highlight"
										path="couponSlot" id="couponSlot"
										onkeyup="if (/\D/g.test(this.value)) this.value = this.value.replace(/\D/g,'')"
										type="text" />
									<span class="help-inline"><form:errors
											path="couponSlot" cssClass="error" /></span>

								</div>
	
							</div>
						</div>
						
						<div id="orderDiscountDiv">
							<div class="control-group">
								<label class="required"><s:message
										code="label.promotion.percentage"
										text="Is percentage discount?" /></label>
								<div class="controls">
									<form:checkbox path="discountPercentage"
										id="percentageDiscount" />
								</div>
							</div>
							<div class="control-group" id="total_discount">
								<label class="required"><s:message
										code="label.promotion.total_discount" text="Total Discount" /></label>
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
										code="label.promotion.total_discount_percentage"
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
					</div>
					<div id="categoriesDiv">
						<div class="control-group" id="categories">
							<label class="required"><s:message
									code="label.promotion.categories"
									text="Categories in promotion" /></label>
							<div class="controls">
								<select multiple="multiple" id="categories-select">
								</select>
							</div>
						</div>

					</div>
					<div id="productsDiv">
						<div class="control-group" id="products">
							<label class="required"><s:message
									code="label.promotion.products" text="Products" /></label>
							<div class="controls">
								<select multiple="multiple" id="products-select">
								</select>
							</div>
						</div>

					</div>
					<div id="productsDivFree">
						<div class="control-group" id="productsFree">
							<label class="required"><s:message
									code="label.promotion.products.free" text="Products for Free" /></label>
							<div class="controls">
								<select multiple="multiple" id="products-select-free">
								</select>
							</div>
						</div>

					</div>
					
					<div id="manufacturersDiv">
						<div class="control-group" id="manufacturers">
							<label class="required"><s:message
									code="label.promotion.manufacturers" text="Manufacturers" /></label>
							<div class="controls">
								<select multiple="multiple" id="manufacturers-select">
								</select>
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
					<div class="form-actions">

						<div class="pull-right">

							<button type="submit" class="btn btn-success" id="btnSubmit">
								<s:message code="button.label.submit" text="Submit" />
							</button>


						</div>

					</div>

				</form:form>


				<br />

				<div id="01div">

					<form:select path="promotion.products" id="01product">

						<form:options items="${promotion.products}" itemValue="id"
							itemLabel="id" />
					</form:select>

					<form:select path="promotion.freeProducts" id="02product">

						<form:options items="${promotion.freeProducts}" itemValue="id"
							itemLabel="id" />
					</form:select>

					<form:select path="promotion.categories" id="01category">

						<form:options items="${promotion.categories}" itemValue="id"
							itemLabel="id" />
					</form:select>
					<form:select path="promotion.productManufacturer" id="01manufacturer">

						<form:options items="${promotion.productManufacturer}" itemValue="id"
							itemLabel="id" />
					</form:select>
				</div>


			</div>


		</div>


	</div>

</div>
<script type="text/javascript">
	var catg = "";
	var prods = "";
	var prodsFree = "";
	var manufacturers = "";
	$('#categories-select').multiSelect({
		afterSelect : function(values) {
			catg += values + "|";
			$('#cats').val(catg);
		},
		afterDeselect : function(values) {
			catg = catg.replace(values + "|", "");
			$('#cats').val(catg);
		}
	});


	$('#products-select').multiSelect({
		selectableHeader : "<input type='text' id='selectableSearch' size='20px' class='search-input' autocomplete='off' placeholder='Filter'>",
		selectionHeader : "<input type='text' id='selectionSearch' class='search-input' autocomplete='off' placeholder='Filter'>",
		afterInit : function(ms) {
			var that = this,
				$selectableSearch = that.$selectableUl.prev(),
				$selectionSearch = that.$selectionUl.prev(),
				selectableSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selectable:not(.ms-selected)',
				selectionSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selection.ms-selected';

			that.qs1 = $('#selectableSearch').quicksearch(selectableSearchString)
				.on('keydown', function(e) {
					if (e.which === 40) {
						that.$selectableUl.focus();
						return false;
					}
				});

			that.qs2 = $('#selectionSearch').quicksearch(selectionSearchString)
				.on('keydown', function(e) {
					if (e.which == 40) {
						that.$selectionUl.focus();
						return false;
					}
				});
		},
		afterSelect : function(values) {
			this.qs1.cache();
			this.qs2.cache();
			prods += values + "|";
			$('#products2').val(prods);
		},
		afterDeselect : function(values) {
			this.qs1.cache();
			this.qs2.cache();
			prods = prods.replace(values + "|", "");
			$('#products2').val(prods);
		}
	});
	$('#products-select-free').multiSelect({
		selectableHeader : "<input type='text' id='selectableSearchFree' size='20px' class='search-input' autocomplete='off' placeholder='Filter'>",
		selectionHeader : "<input type='text' id='selectionSearchFree' class='search-input' autocomplete='off' placeholder='Filter'>",
		afterInit : function(ms) {
			var that = this,
				$selectableSearch = that.$selectableUl.prev(),
				$selectionSearch = that.$selectionUl.prev(),
				selectableSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selectable:not(.ms-selected)',
				selectionSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selection.ms-selected';

			that.qs1 = $('#selectableSearchFree').quicksearch(selectableSearchString)
				.on('keydown', function(e) {
					if (e.which === 40) {
						that.$selectableUl.focus();
						return false;
					}
				});

			that.qs2 = $('#selectionSearchFree').quicksearch(selectionSearchString)
				.on('keydown', function(e) {
					if (e.which == 40) {
						that.$selectionUl.focus();
						return false;
					}
				});
		},
		afterSelect : function(values) {
			this.qs1.cache();
			this.qs2.cache();
			prodsFree += values + "|";
			$('#productsFree').val(prodsFree);
		},
		afterDeselect : function(values) {
			this.qs1.cache();
			this.qs2.cache();
			prodsFree = prods.replace(values + "|", "");
			$('#productsFree').val(prodsFree);
		}
	});
	
	$('#manufacturers-select').multiSelect({
		selectableHeader : "<input type='text' id='selectableSearchFree' size='20px' class='search-input' autocomplete='off' placeholder='Filter'>",
		selectionHeader : "<input type='text' id='selectionSearchFree' class='search-input' autocomplete='off' placeholder='Filter'>",
		afterInit : function(ms) {
			var that = this,
				$selectableSearch = that.$selectableUl.prev(),
				$selectionSearch = that.$selectionUl.prev(),
				selectableSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selectable:not(.ms-selected)',
				selectionSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selection.ms-selected';

			that.qs1 = $('#selectableSearchFree').quicksearch(selectableSearchString)
				.on('keydown', function(e) {
					if (e.which === 40) {
						that.$selectableUl.focus();
						return false;
					}
				});

			that.qs2 = $('#selectionSearchFree').quicksearch(selectionSearchString)
				.on('keydown', function(e) {
					if (e.which == 40) {
						that.$selectionUl.focus();
						return false;
					}
				});
		},
		afterSelect : function(values) {
			this.qs1.cache();
			this.qs2.cache();
			manufacturers += values + "|";
			$('#manufacturers').val(manufacturers);
		},
		afterDeselect : function(values) {
			this.qs1.cache();
			this.qs2.cache();
			manufacturers = manufacturers.replace(values + "|", "");
			$('#manufacturers').val(manufacturers);
		}
	});
</script>
