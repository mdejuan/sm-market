package com.salesmanager.shop.marketing.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.salesmanager.business.marketing.services.VoucherService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.marketing.model.Voucher;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.configuration.marketing.ConstantsMarketing;
import com.salesmanager.shop.utils.DateUtil;

@Controller
@RequestMapping("/admin/voucher")
public class VoucherController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VoucherController.class);
	@Inject
	private MerchantStoreService merchantService;
	
	@Inject
	private VoucherService voucherService;
		
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/list.html", method=RequestMethod.GET)
	public String displayVouchers(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		return ConstantsMarketing.Tiles.Voucher.voucherList;		

	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/edit.html", method=RequestMethod.GET)
	public String displayVoucherEdit(@RequestParam("id") long id, HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {
		return displayVoucher(id,request,response,model,locale);
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/create.html", method=RequestMethod.GET)
	public String displayVoucherCreate(HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {
		return displayVoucher(null,request,response,model,locale);
	}
	@Transactional
	private String displayVoucher(Long voucherId, HttpServletRequest request, HttpServletResponse response,Model model,Locale locale) throws Exception {
		
		this.setMenu(model, request);
		List<MerchantStore> stores = merchantService.list();
		
		Voucher voucher = new Voucher();
		
		if(voucherId!=null && voucherId!=0) {//edit mode
			
			
			voucher = voucherService.getByIdLoaded(voucherId);
			//Hibernate.initialize(voucher);
			
			//voucher.setProducts(voucher.getProducts());
			if(voucher==null) {
				return "redirect:/list.html";
			}
			

		} else {
			
		}
		
		model.addAttribute("merchants", stores);
		model.addAttribute("voucher", voucher);
		
		return ConstantsMarketing.Tiles.Voucher.voucherDetails;
		
		
	}
		
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/save.html", method=RequestMethod.POST)
	public String saveVoucher(@Valid @ModelAttribute("voucher") Voucher voucher, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		
		MerchantStore store = null;
		List<MerchantStore> stores = merchantService.list();
		//display menu
		setMenu(model,request);
		store = merchantService.getById(voucher.getMerchant().getId());
		Voucher dbEntity =	null;	

		if(voucher.getId() != null && voucher.getId() >0) { //edit entry
			
			//get from DB
			dbEntity = voucherService.getById(voucher.getId());
			
			if(dbEntity==null) {
				return "redirect:/admin/vouchers/vouchers.html";
			}
		}
		voucher.setMerchant(store);
		
		
		
		if (result.hasErrors()) {
			return ConstantsMarketing.Tiles.Voucher.voucherDetails;
		}

		voucherService.save(voucher);
		
		model.addAttribute("voucher", voucher);
		model.addAttribute("merchants", stores);
		model.addAttribute("success","success");
		return ConstantsMarketing.Tiles.Voucher.voucherDetails;
	}

	
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/paging.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> pageVouchers(HttpServletRequest request, HttpServletResponse response) {

		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			
			List<Voucher> vouchers = null;
						
			vouchers = voucherService.list();
				
			for(Voucher voucher : vouchers) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("id", voucher.getId());
				entry.put("description", voucher.getDescription());
				entry.put("merchant", voucher.getMerchant().getStorename());
				entry.put("startDate", DateUtil.formatDate(voucher.getStartDate()));
				entry.put("code", voucher.getCode());
				
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
			
			Voucher entity = voucherService.getById(id);

			if(entity==null) {

				resp.setStatusMessage("Error finding voucher to delete");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				
			} else {
				
				voucherService.delete(entity);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
			}
		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting voucher", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}
	
	
	
}
