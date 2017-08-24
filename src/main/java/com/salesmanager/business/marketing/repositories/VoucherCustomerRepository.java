package com.salesmanager.business.marketing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.salesmanager.marketing.model.VoucherCustomer;

@Repository("voucherCustomerRepository")
public interface VoucherCustomerRepository extends JpaRepository<VoucherCustomer, Long>{

}
