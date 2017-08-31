package com.salesmanager.business.marketing.services;

import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.marketing.model.Promotion;

public interface PromotionService extends SalesManagerEntityService<Long, Promotion>{
	
	List<Promotion> findByMerchant(Integer storeId);
	
	Promotion getById(Long id, String collectionName);
	
	Promotion getByIdLoaded(Long id);
	
	void activatePromotions(List<Promotion> promotions) throws ServiceException;
	
	void disablePromotions(List<Promotion> promotions) throws ServiceException;
	
	List<Product> getProductList(Promotion promotion) throws ServiceException;

}
