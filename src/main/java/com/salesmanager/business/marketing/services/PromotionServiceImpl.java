package com.salesmanager.business.marketing.services;


import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.salesmanager.business.marketing.repositories.PromotionRepository;
import com.salesmanager.business.marketing.repositories.util.JpaUtils;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.marketing.model.Promotion;
@Service("promotionService")
public class PromotionServiceImpl extends SalesManagerEntityServiceImpl<Long, Promotion> implements PromotionService{

	PromotionRepository promotionRepository;
	@PersistenceContext
    private EntityManager em;
	@Inject
	public PromotionServiceImpl(PromotionRepository promotionRepository) {
		super(promotionRepository);
		this.promotionRepository = promotionRepository;
	}
	@Override
	public List<Promotion> findByMerchant(Integer storeId) {
		
		return promotionRepository.findByMerchant(storeId);
	}
	@Override
	@Transactional
	public Promotion getById(Long id, String collectionName) {
		Promotion promotion = this.getById(id);
		JpaUtils.initializeRelation(em, promotion, 2,"products");
		return promotion;
	}
	
	@Override
	@Transactional
	public Promotion getByIdLoaded(Long id) {
		Promotion promotion = this.getById(id);
		JpaUtils.initialize(em, promotion, 2);
		return promotion;
	}
	
	
	

}
