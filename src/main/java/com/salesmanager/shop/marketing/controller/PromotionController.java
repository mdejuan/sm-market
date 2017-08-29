package com.salesmanager.shop.marketing.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
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

import com.salesmanager.business.marketing.services.PromotionService;
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
import com.salesmanager.marketing.model.Promotion;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.configuration.marketing.ConstantsMarketing;
import com.salesmanager.shop.configuration.marketing.CouponType;
import com.salesmanager.shop.configuration.marketing.PromotionType;
import com.salesmanager.shop.configuration.marketing.ThresholdType;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.utils.DateUtil;
/**
 * Custom promotion controller for admin
 * 
 * @author Marcos de Juan
 *
 */
@Controller
@RequestMapping("/admin/promotion")
public class PromotionController extends AbstractMarketingController{
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PromotionController.class);
	@Inject
	private MerchantStoreService merchantService;
	
	@Inject
	private PromotionService promotionService;
	@Inject
	private CategoryService categoryService;
	
	@Inject
	private ProductService productService;
	
	@Inject
	private ManufacturerService manufacturerService;
	
	
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/list.html", method=RequestMethod.GET)
	public String displayPromotions(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		return ConstantsMarketing.Tiles.Promotion.promotionList;		

	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/edit.html", method=RequestMethod.GET)
	public String displayPromotionEdit(@RequestParam("id") long id, HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {
		return displayPromotion(id,request,response,model,locale);
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/create.html", method=RequestMethod.GET)
	public String displayProotionCreate(HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {
		return displayPromotion(null,request,response,model,locale);
	}
	@Transactional
	private String displayPromotion(Long promotionId, HttpServletRequest request, HttpServletResponse response,Model model,Locale locale) throws Exception {
		
		this.setMenu(model, request);
		List<MerchantStore> stores = merchantService.list();
		
		Promotion promotion = new Promotion();
		
		if(promotionId!=null && promotionId!=0) {//edit mode
			
			
			promotion = promotionService.getByIdLoaded(promotionId);

			if(promotion==null) {
				return "redirect:/list.html";
			}
			

		} else {
			
		}
		model.addAttribute("thresholdTypes", ThresholdType.class);
		model.addAttribute("promotionTypes", PromotionType.class);
		model.addAttribute("couponType", CouponType.class);
		model.addAttribute("merchants", stores);
		model.addAttribute("promotion", promotion);
		
		return ConstantsMarketing.Tiles.Promotion.promotionDetails;
		
		
	}
		
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/save.html", method=RequestMethod.POST)
	public String savePromotion(@Valid @ModelAttribute("promotion") Promotion promotion, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		
		MerchantStore store = null;
		List<MerchantStore> stores = merchantService.list();
		//display menu
		setMenu(model,request);
		store = merchantService.getById(promotion.getMerchant().getId());
		Promotion dbEntity =	null;	

		if(promotion.getId() != null && promotion.getId() >0) { //edit entry
			
			//get from DB
			dbEntity = promotionService.getById(promotion.getId());
			
			if(dbEntity==null) {
				return "redirect:/admin/promotions/promotions.html";
			}
		}
		promotion.setMerchant(store);
		
		//set categories or products if option selected
		if(StringUtils.isNotEmpty(request.getParameter("cats")))
		{
			Set<Category> categories = new HashSet<Category>();
			String cats = (String) request.getParameter("cats");
			StringTokenizer st = new StringTokenizer(cats,"|");
			while (st.hasMoreElements()) {
				
				Category category = categoryService.getById(Long.valueOf(st.nextElement().toString()));
				categories.add(category);
			}
		
			
			promotion.setCategories(categories);
		}
		
		if(StringUtils.isNotEmpty(request.getParameter("products2")))
		{
			Set<Product> products = new HashSet<Product>();
			String prods = (String) request.getParameter("products2");
			StringTokenizer st = new StringTokenizer(prods,"|");
			while (st.hasMoreElements()) {
				
				Product product = productService.getById(Long.valueOf(st.nextElement().toString()));
				products.add(product);
			}	
			promotion.setProducts(products);
		}
		if(StringUtils.isNotEmpty(request.getParameter("productsFree")))
		{
			Set<Product> products = new HashSet<Product>();
			String prods = (String) request.getParameter("productsFree");
			StringTokenizer st = new StringTokenizer(prods,"|");
			while (st.hasMoreElements()) {
				
				Product product = productService.getById(Long.valueOf(st.nextElement().toString()));
				products.add(product);
			}	
			promotion.setFreeProducts(products);
		}
		if(StringUtils.isNotEmpty(request.getParameter("manufacturers")))
		{
			Set<Manufacturer> manufacturers = new HashSet<Manufacturer>();
			String manufact = (String) request.getParameter("manufacturers");
			StringTokenizer st = new StringTokenizer(manufact,"|");
			while (st.hasMoreElements()) {
				
				Manufacturer manufacturer = manufacturerService.getById(Long.valueOf(st.nextElement().toString()));
				manufacturers.add(manufacturer);
			}	
			promotion.setProductManufacturer(manufacturers);
		}
		
		if (result.hasErrors()) {
			return ConstantsMarketing.Tiles.Promotion.promotionDetails;
		}

		promotionService.save(promotion);
		model.addAttribute("promotionTypes", PromotionType.class);
		model.addAttribute("thresholdTypes", ThresholdType.class);
		model.addAttribute("couponType", CouponType.class);
		model.addAttribute("promotion", promotion);
		model.addAttribute("merchants", stores);
		model.addAttribute("success","success");
		return ConstantsMarketing.Tiles.Promotion.promotionDetails;
	}

	
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value="/paging.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> pagePromotions(HttpServletRequest request, HttpServletResponse response) {

		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			
			List<Promotion> promotions = null;
						
			promotions = promotionService.list();
				
			for(Promotion promotion : promotions) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("id", promotion.getId());
				entry.put("description", promotion.getDescription());
				entry.put("code", promotion.getCodePromo());
				entry.put("merchant", promotion.getMerchant().getStorename());
				entry.put("startDate", DateUtil.formatDate(promotion.getStartDate()));
				entry.put("discount", promotion.getDiscount());
				entry.put("promotionType", promotion.getPromotionType());
				
				resp.addDataEntry(entry);
				
				
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			

		
		} catch (Exception e) {
			LOGGER.error("Error while paging promotions", e);
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
	public @ResponseBody ResponseEntity<String> deletePromotion(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("id");

		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Long id = Long.parseLong(sid);
			
			Promotion entity = promotionService.getById(id);

			if(entity==null) {

				resp.setStatusMessage("Error finding promotion to delete");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				
			} else {
				
				promotionService.delete(entity);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
			}
		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting promotion", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}
	
	
	
	

}
