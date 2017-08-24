package com.salesmanager.business.marketing.repositories;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections4.CollectionUtils;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.manufacturer.Manufacturer;
import com.salesmanager.marketing.model.EmailMarketing;


public class EmailMarketingRepositoryImpl implements EmailMarketingRepositoryCustom{
	@PersistenceContext
    private EntityManager em;
	
	List<String> emailCustomers = new ArrayList<String>();
	@Override
	public List<String> getCustomerEmailsByCriteria(EmailMarketing emailMarketing) {
		
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select distinct p.sku from Product as p");
		
		StringBuilder countBuilderWhere = new StringBuilder();
		countBuilderWhere.append(" where p.merchantStore.id=:mId");
		
		if(!CollectionUtils.isEmpty(emailMarketing.getCategories())) {
			countBuilderSelect.append(" INNER JOIN p.categories categs");
			countBuilderWhere.append(" and categs.id in (:cid)");
		}
		
		if(!CollectionUtils.isEmpty(emailMarketing.getProductManufacturer())) {
			countBuilderSelect.append(" INNER JOIN p.manufacturer manuf");
			countBuilderWhere.append(" and manuf.id in (:manufid)");
		}
		
		if(!CollectionUtils.isEmpty(emailMarketing.getProducts())) {
			countBuilderWhere.append(" and p.id in (:prodid)");
		}
		
		
		
		Query q = this.em.createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());
		q.setParameter("mId", emailMarketing.getMerchant().getId());
		if(!CollectionUtils.isEmpty(emailMarketing.getCategories())) {
			List<Long> categoryIds = new ArrayList<Long>();
			for(Category c : emailMarketing.getCategories()) {
				
				categoryIds.add(c.getId());	
			}
    		q.setParameter("cid", categoryIds);
    	}
    	
		if(!CollectionUtils.isEmpty(emailMarketing.getProducts())) {
			List<Long> prodIds = new ArrayList<Long>();
			for(Product p : emailMarketing.getProducts()) {
				
				prodIds.add(p.getId());	
			}
			q.setParameter("prodid", prodIds);
		}
		
		
		if(!CollectionUtils.isEmpty(emailMarketing.getProductManufacturer())) {
			List<Long> manIds = new ArrayList<Long>();
			for(Manufacturer m : emailMarketing.getProductManufacturer()) {
				manIds.add(m.getId());	
			}
			q.setParameter("manufid", manIds);
		}
		
		List<String> productSkus =  q.getResultList();
		
		if(!CollectionUtils.isEmpty(productSkus))
		{
			StringBuilder countBuilderSelectOrder = new StringBuilder();
			countBuilderSelectOrder.append("select distinct o.customerEmailAddress from Order as o");
			
			StringBuilder countBuilderWhereOrder = new StringBuilder();
			countBuilderWhere.append(" where o.merchant.id=:mId");
			
			countBuilderSelectOrder.append(" INNER JOIN o.orderProducts products");
			countBuilderWhereOrder.append(" and products.sku in (:prodSku)");
			Query q2 = this.em.createQuery(
					countBuilderSelectOrder.toString() + countBuilderWhereOrder.toString());
			q2.setParameter("prodSku", productSkus);
			emailCustomers =  q.getResultList();
		}
		return emailCustomers;
	}
	

}
