package com.salesmanager.business.marketing.services;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.salesmanager.business.marketing.repositories.VoucherRepository;
import com.salesmanager.business.marketing.repositories.util.JpaUtils;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.marketing.model.Voucher;

@Service("voucherService")
public class VoucherServiceImpl extends SalesManagerEntityServiceImpl<Long, Voucher> implements VoucherService {
	VoucherRepository voucherRepository;
	@PersistenceContext
    private EntityManager em;
	@Inject
	public VoucherServiceImpl(VoucherRepository voucherRepository) {
		super(voucherRepository);
		this.voucherRepository = voucherRepository;
	}
	@Override
	public List<Voucher> findByMerchant(Integer storeId) {
		
		return voucherRepository.findByMerchant(storeId);
	}
	@Override
	@Transactional
	public Voucher getById(Long id, String collectionName) {
		Voucher promotion = this.getById(id);
		JpaUtils.initializeRelation(em, promotion, 2,"products");
		return promotion;
	}
	
	@Override
	@Transactional
	public Voucher getByIdLoaded(Long id) {
		Voucher promotion = this.getById(id);
		JpaUtils.initialize(em, promotion, 2);
		return promotion;
	}

}
