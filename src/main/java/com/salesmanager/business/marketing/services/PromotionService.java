package com.salesmanager.business.marketing.services;

import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.marketing.model.Promotion;

public interface PromotionService extends SalesManagerEntityService<Long, Promotion>{
	
	List<Promotion> findByMerchant(Integer storeId);
	
	Promotion getById(Long id, String collectionName);
	
	Promotion getByIdLoaded(Long id);

}