package com.salesmanager.business.marketing.services;

import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.marketing.model.Voucher;

public interface VoucherService extends SalesManagerEntityService<Long, Voucher>{
List<Voucher> findByMerchant(Integer storeId);
	
	Voucher getById(Long id, String collectionName);
	
	Voucher getByIdLoaded(Long id);
}
