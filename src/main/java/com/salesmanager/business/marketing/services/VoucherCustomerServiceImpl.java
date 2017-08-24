package com.salesmanager.business.marketing.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import com.salesmanager.business.marketing.repositories.VoucherCustomerRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.marketing.model.VoucherCustomer;

@Service("voucherCustomerService")
public class VoucherCustomerServiceImpl extends SalesManagerEntityServiceImpl<Long, VoucherCustomer> implements VoucherCustomerService{
	VoucherCustomerRepository voucherCustomerRepository;
	@PersistenceContext
    private EntityManager em;
	@Inject
	public VoucherCustomerServiceImpl(VoucherCustomerRepository voucherCustomerRepository) {
		super(voucherCustomerRepository);
		this.voucherCustomerRepository = voucherCustomerRepository;
	}
}
