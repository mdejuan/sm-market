package com.salesmanager.shop.marketing.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.salesmanager.business.marketing.services.VoucherCustomerService;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.marketing.model.VoucherCustomer;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.configuration.marketing.ConstantsMarketing;
import com.salesmanager.shop.utils.DateUtil;

@Controller
@RequestMapping("/admin/voucher/customer")
public class VoucherCustomerController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VoucherCustomerController.class);
	
	@Inject
	private VoucherCustomerService voucherCustomerService;
		
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/list.html", method=RequestMethod.GET)
	public String displayVouchers(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		return ConstantsMarketing.Tiles.Voucher.voucherCustomerList;		

	}
	
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/paging.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> pageVouchers(HttpServletRequest request, HttpServletResponse response) {

		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			
			List<VoucherCustomer> vouchers = null;
						
			vouchers = voucherCustomerService.list();
				
			for(VoucherCustomer voucher : vouchers) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("id", voucher.getId());
				entry.put("idCustomer", voucher.getIdCustomer());
				entry.put("merchant", voucher.getVoucher().getMerchant());
				entry.put("date", DateUtil.formatDate(voucher.getDate()));
				
				resp.addDataEntry(entry);
				
				
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			

		
		} catch (Exception e) {
			LOGGER.error("Error while paging vouchers", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
		
		
	}
private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		
		activeMenus.put("marketing", "marketing");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("marketing");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/remove.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteVoucher(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("id");

		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Long id = Long.parseLong(sid);
			
			VoucherCustomer entity = voucherCustomerService.getById(id);

			if(entity==null) {

				resp.setStatusMessage("Error finding voucher to delete");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				
			} else {
				
				voucherCustomerService.delete(entity);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
			}
		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting voucher for customer", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}
}
