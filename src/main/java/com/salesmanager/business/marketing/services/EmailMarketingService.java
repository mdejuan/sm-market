package com.salesmanager.business.marketing.services;

import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.marketing.model.EmailMarketing;

public interface EmailMarketingService extends SalesManagerEntityService<Long, EmailMarketing>{
	List<EmailMarketing> findByMerchant(Integer storeId);
	
	EmailMarketing getById(Long id, String collectionName);
	
	EmailMarketing getByIdLoaded(Long id);
	
	List<String> getEmailCustomersByCriteria(EmailMarketing emailMarketing);
	
	void sendMarketingEmail(EmailMarketing email) throws Exception;
}
