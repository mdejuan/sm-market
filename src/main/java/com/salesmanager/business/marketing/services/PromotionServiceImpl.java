package com.salesmanager.business.marketing.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.salesmanager.business.marketing.repositories.PromotionRepository;
import com.salesmanager.business.marketing.repositories.util.JpaUtils;
import com.salesmanager.business.marketing.util.PromotionPriceUtil;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.catalog.product.availability.ProductAvailabilityService;
import com.salesmanager.core.business.services.catalog.product.price.ProductPriceService;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.ProductCriteria;
import com.salesmanager.core.model.catalog.product.ProductList;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;
import com.salesmanager.core.model.catalog.product.manufacturer.Manufacturer;
import com.salesmanager.core.model.catalog.product.price.ProductPrice;
import com.salesmanager.core.model.catalog.product.price.ProductPriceType;
import com.salesmanager.marketing.model.Promotion;

@Service("promotionService")
public class PromotionServiceImpl extends SalesManagerEntityServiceImpl<Long, Promotion> implements PromotionService {

	PromotionRepository promotionRepository;
	@Inject
	ProductService productService;
	@Inject
	ProductPriceService productPriceService;
	@Inject
	ProductAvailabilityService availabilityService;
	
	@PersistenceContext
	private EntityManager em;

	@Inject
	public PromotionServiceImpl(PromotionRepository promotionRepository) {
		super(promotionRepository);
		this.promotionRepository = promotionRepository;
	}

	public List<Promotion> findByMerchant(Integer storeId) {

		return promotionRepository.findByMerchant(storeId);
	}

	@Transactional
	public Promotion getById(Long id, String collectionName) {
		Promotion promotion = this.getById(id);
		JpaUtils.initializeRelation(em, promotion, 2, "products");
		return promotion;
	}

	@Transactional
	public Promotion getByIdLoaded(Long id) {
		Promotion promotion = this.getById(id);
		JpaUtils.initialize(em, promotion, 2);
		return promotion;
	}

	public void activatePromotions(List<Promotion> promotions) throws ServiceException {
		for (Promotion promotion : promotions) {
			List<Product> productlist = getProductList(promotion);
			for (Product productPromoted : productlist) {
				Set<ProductPrice> prices = null;
				ProductPrice defaultPrice = null;
				ProductPrice basePrice = null;
				for (ProductAvailability availability : productPromoted.getAvailabilities()) {
					if (availability.getRegion()
							.equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS)) {
						prices = availability.getPrices();
					}
				}
				for (ProductPrice price : prices) {
					if (price.isDefaultPrice()) {
						defaultPrice = price;
						defaultPrice.setDefaultPrice(false);
						break;
					}

				}
				for (ProductPrice price : prices) {
					if (price.getCode().equals("base")) {
						basePrice = price;
						break;
					}

				}
				ProductPrice productPrice = PromotionPriceUtil.resolveProductPrice(basePrice, promotion);
				
				productPriceService.saveOrUpdate(productPrice);		
				productPriceService.saveOrUpdate(defaultPrice);
			}
		}

	}

	public void disablePromotions(List<Promotion> promotions) throws ServiceException {
		for (Promotion promotion : promotions) {
			List<Product> productlist = getProductList(promotion);
			for (Product productPromoted : productlist) {
				Set<ProductPrice> prices = null;
				ProductPrice defaultPrice = null;
				ProductPrice promoPrice = null;
				for (ProductAvailability availability : productPromoted.getAvailabilities()) {
					if (availability.getRegion()
							.equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS)) {
						prices = availability.getPrices();
					}
				}
				for (ProductPrice price : prices) {
					if (price.isDefaultPrice() && price.getCode().startsWith("promo-")) {
						promoPrice = price;
						promoPrice.setDefaultPrice(false);
					
					}
					else if (price.getCode().equals("base")) {	
						defaultPrice = price;
						price.setDefaultPrice(true);
						
					}

				}
				
				productPriceService.saveOrUpdate(defaultPrice);
				productPriceService.saveOrUpdate(promoPrice);
			}
		}

	}

	public List<Product> getProductList(Promotion promotion) throws ServiceException {
		List<Product> productList = new ArrayList<Product>();
		List<Long> categoryIds = new ArrayList<Long>();

		Promotion promotionLoaded = this.getByIdLoaded(promotion.getId());
		productList.addAll(promotionLoaded.getProducts());
		for (Category category : promotionLoaded.getCategories()) {
			categoryIds.add(category.getId());
		}
		if (!categoryIds.isEmpty()) {
			productList.addAll(productService.getProducts(categoryIds));
		}
		for (Manufacturer manufacturer : promotionLoaded.getProductManufacturer()) {
			ProductCriteria productCriteria = new ProductCriteria();
			productCriteria.setManufacturerId(manufacturer.getId());
			ProductList products = productService.listByStore(promotionLoaded.getMerchant(),
					promotionLoaded.getMerchant().getDefaultLanguage(), productCriteria);
			productList.addAll(products.getProducts());
		}

		return productList;
	}

}
