
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
		$('#manufacturers-select').multiSelect();	
		$('#sortOrder').numeric();
		$('#01div').hide();

		if ('${email.id}' == '') {			
			$('#merchant').find('option:first').attr('selected', 'selected');
			$('#emailForm :input').not(':button,:hidden').each(function() {
				$(this).val('');
			});
			$('#btnSubmit').attr('disabled', true);$('#btnSubmit').attr('disabled', true);			
			
			
		} else {
			
			
			getCategories();
			getProducts("p1");
			getManufacturers();
			
    	}



		$("#emailForm :input").on("change keyup paste", function() {
			isValid();
		});

		
		$('#merchant').change(function() {
			var option = $('#merchant').find('option:selected').val();
			if(option!='')
			{
				getCategories();
				getProducts("p1");
				getManufacturers();
			}
			
		});
		
		
		$('#active').change(function() {
			if ($(this).is(":checked")) {
				$('#active').val('true');	
				
			} else {
				$('#active').val('false');	
			}

		});
		
		
		function getProducts(combos) {
			var combo = combos;
			$.ajax({
				type : 'GET',
				url : '<c:url value="/admin/emailmarketing/products.html"/>',
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
							
							if ('${email.id}' != '') {
								$("#products-select > option").each(function(index1, element1) {

									$("#01product > option").each(function(index2, element2) {

										if (element1.value == element2.value) {
											$('#products-select').multiSelect('select', element2.value);
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

		function getCategories() {
			$.ajax({
				type : 'GET',
				url : '<c:url value="/admin/emailmarketing/categories.html"/>',
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
							if ('${email.id}' != '') {
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
				url : '<c:url value="/admin/emailmarketing/manufacturers.html"/>',
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
							if ('${email.id}' != '') {
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
				return true;
			} else {
				$('#btnSubmit').attr('disabled', true);
				return false;
			}
		}

		function isFValid() {
			var fieldValid;
			$("form#emailForm :input").each(function() {
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
					<s:message code="menu.email" text="Email Marketing management" />
				</h3>


				<h3>
					<c:choose>
						<c:when test="${email.id!=null && email.id>0}">
							<s:message code="label.email.edit" text="Edit Email Marketing" />
						</c:when>
						<c:otherwise>
							<s:message code="label.email.create" text="Create Email Marketing" />
						</c:otherwise>
					</c:choose>

				</h3>
				<br />

				<c:url var="emailSave" value="/admin/emailmarketing/save.html" />


				<form:form method="POST" commandName="email"
					action="${emailSave}" id="emailForm">


					<form:errors path="*" cssClass="alert alert-error" element="div" />
					<div id="store.success" class="alert alert-success"
						style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
						<s:message code="message.success" text="Request successfull" />
					</div>


					<form:hidden path="id" id="idEmail" />
					<input type="hidden" id="cats" name="cats" />
					<input type="hidden" id="products2" name="products2" />
					<input type="hidden" id="manufacturers" name="manufacturers" />
								
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
						
					<div class="control-group">
						<label class="required"><s:message
								code="label.email.date" text="Date" /></label>
						<div class="controls">

							<form:input cssClass="input-large" readonly="true"
								path="date" class="small" type="date"
								data-date-format="<%=com.salesmanager.core.business.constants.Constants.DEFAULT_DATE_FORMAT%>" />
							<script type="text/javascript">
								$('#date').datepicker();
							</script>
							<span class="help-inline"><form:errors path="date"
									cssClass="error" /></span>

						</div>
					</div>

					
					<div class="control-group" id="lastOrderDiv">
						<label class="required"><s:message
								code="label.email.customerLastOrder"
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

					
					<div id="categoriesDiv">
						<div class="control-group" id="categories">
							<label class="required"><s:message
									code="label.email.categories"
									text="Customer who bought these Categories" /></label>
							<div class="controls">
								<select multiple="multiple" id="categories-select">
								</select>
							</div>
						</div>

					</div>
					<div id="productsDiv">
						<div class="control-group" id="products">
							<label class="required"><s:message
									code="label.email.products" text="Customer who bought these Products" /></label>
							<div class="controls">
								<select multiple="multiple" id="products-select">
								</select>
							</div>
						</div>

					</div>
					<div id="manufacturersDiv">
						<div class="control-group" id="manufacturers">
							<label class="required"><s:message
									code="label.email.manufacturers" text="Customer who bought from these Manufacturers" /></label>
							<div class="controls">
								<select multiple="multiple" id="manufacturers-select">
								</select>
							</div>
						</div>

					</div>
					
					
					<div class="control-group" >
						<label class="required"><s:message
								code="label.email.title" text="Title" /></label>
						
						<div class="controls">
							<form:input cssClass="input-large highlight" id="title"
								path="title" />
							<span class="help-inline"><form:errors path="title"
									cssClass="error" /></span>

						</div>
					</div>
					<div class="control-group" >
						<label class="required"><s:message
								code="label.email.content" text="Content" /></label>
						<div class="controls">
							<form:textarea class="span6 highlight" rows="20" path="content" />	
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

					<form:select path="email.products" id="01product">

						<form:options items="${email.products}" itemValue="id"
							itemLabel="id" />
					</form:select>


					<form:select path="email.categories" id="01category">

						<form:options items="${email.categories}" itemValue="id"
							itemLabel="id" />
					</form:select>
					
					<form:select path="email.productManufacturer" id="01manufacturer">

						<form:options items="${email.productManufacturer}" itemValue="id"
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
