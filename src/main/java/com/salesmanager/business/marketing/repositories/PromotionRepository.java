package com.salesmanager.business.marketing.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.salesmanager.marketing.model.Promotion;
@Repository("promotionRepository")
public interface PromotionRepository extends JpaRepository<Promotion, Long>{
	@Query("select distinct o from Promotion as o  left join fetch o.merchant om where om.id = ?1")
	List<Promotion> findByMerchant(Integer storeId);
}
