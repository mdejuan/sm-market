package com.salesmanager.business.marketing.repositories;

import java.util.List;

import com.salesmanager.marketing.model.EmailMarketing;

public interface EmailMarketingRepositoryCustom {
	List<String> getCustomerEmailsByCriteria(final EmailMarketing emailMarketing);
}
