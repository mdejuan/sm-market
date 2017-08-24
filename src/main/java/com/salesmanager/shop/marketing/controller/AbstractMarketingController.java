package com.salesmanager.shop.marketing.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.services.catalog.category.CategoryService;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.manufacturer.Manufacturer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.constants.Constants;

public abstract class AbstractMarketingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMarketingController.class);
	@Inject
	private CategoryService categoryService;
	@Inject
	private LanguageService languageService;
	@Inject
	private ProductService productService;
	@Inject
	private MerchantStoreService merchantService;
	@Inject
	private ManufacturerService manufacturerService;
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/categories.html", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> getCategories(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("merchant");

		
		String lang = request.getParameter("lang");
		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Integer id = Integer.parseInt(sid);
			MerchantStore merchant = merchantService.getById(id);
			
			Language language = null;
			
			if(!StringUtils.isBlank(lang)) {
				language = languageService.getByCode(lang);
			}
			
			if(language==null) {
				language = (Language)request.getAttribute("LANGUAGE");
			}
			
			if(language==null) {
				language = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
			}
			
			List<Category> categories = categoryService.listByStore(merchant,language);
			
			for(Category category : categories) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("id", category.getId());
				entry.put("code", category.getCode());
				resp.addDataEntry(entry);
				
			}
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while getting categories for promotion", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/products.html", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> getProducts(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("merchant");

		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Integer id = Integer.parseInt(sid);
			MerchantStore merchant = merchantService.getById(id);
			
			List<Product> products = productService.listByStore(merchant);
			
			for(Product product : products) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("id", product.getId());
				entry.put("description", product.getProductDescription().getDescription());
				resp.addDataEntry(entry);
				
			}
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while getting products for promotion", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/manufacturers.html", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> getManufacturers(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("merchant");

		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Integer id = Integer.parseInt(sid);
			MerchantStore merchant = merchantService.getById(id);
			List<Manufacturer> manufacturers = manufacturerService.listByStore(merchant);
			
			for(Manufacturer manufacturer : manufacturers) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("id", manufacturer.getId());
				entry.put("code", manufacturer.getCode());
				resp.addDataEntry(entry);
				
			}
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while getting manufacturers for emailMarketing", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}
	
}
