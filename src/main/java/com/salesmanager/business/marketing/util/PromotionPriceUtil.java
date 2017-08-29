package com.salesmanager.business.marketing.util;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.salesmanager.core.model.catalog.product.price.ProductPrice;
import com.salesmanager.core.model.catalog.product.price.ProductPriceDescription;
import com.salesmanager.core.model.catalog.product.price.ProductPriceType;
import com.salesmanager.marketing.model.Promotion;

public class PromotionPriceUtil {
	
	public static ProductPrice resolveProductPrice(ProductPrice basePrice, Promotion promotion)
	{
		
		ProductPrice productPrice = new ProductPrice();
		
		if(promotion.isDiscountPercentage())
		{		
			Integer discount = promotion.getOrderDiscountPercentage();
			double amountDiscouted = (basePrice.getProductPriceAmount().doubleValue() * discount.doubleValue()) / 100;
			productPrice.setProductPriceSpecialAmount(new BigDecimal(basePrice.getProductPriceAmount().doubleValue() - amountDiscouted));
		}
		else if(!promotion.isDiscountPercentage())
		{
			productPrice.setProductPriceSpecialAmount(new BigDecimal(basePrice.getProductPriceAmount().doubleValue() - promotion.getAmount().doubleValue()));
		}
		Set<ProductPriceDescription> descriptions = new HashSet<ProductPriceDescription>();
		ProductPriceDescription description = new ProductPriceDescription();
		description.setDescription(promotion.getDescription());
		description.setName(promotion.getPromotionType());
		description.setTitle(promotion.getCodePromo());
		description.setProductPrice(productPrice);
		description.setLanguage(promotion.getMerchant().getDefaultLanguage());
		descriptions.add(description);
		productPrice.setDescriptions(descriptions);
		productPrice.setCode("promo-"+promotion.getCodePromo());
		productPrice.setDefaultPrice(true);
		productPrice.setProductAvailability(basePrice.getProductAvailability());
		productPrice.setProductPriceAmount(basePrice.getProductPriceAmount());
		productPrice.setProductPriceType(ProductPriceType.ONE_TIME);
		return productPrice;
	}
}
