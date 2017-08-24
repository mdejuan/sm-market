package com.salesmanager.business.marketing.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.salesmanager.marketing.model.Voucher;

@Repository("voucherRepository")
public interface VoucherRepository extends JpaRepository<Voucher, Long>{
	@Query("select distinct o from Voucher as o  left join fetch o.merchant om where om.id = ?1")
	List<Voucher> findByMerchant(Integer storeId);
}
