package com.salesmanager.business.marketing.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.salesmanager.marketing.model.EmailMarketing;

@Repository("emailMarketingRepository")
public interface EmailMarketingRepository extends JpaRepository<EmailMarketing, Long>, EmailMarketingRepositoryCustom{
	@Query("select distinct o from EmailMarketing as o left join fetch o.merchant om where om.id = ?1")
	List<EmailMarketing> findByMerchant(Integer storeId);
}
