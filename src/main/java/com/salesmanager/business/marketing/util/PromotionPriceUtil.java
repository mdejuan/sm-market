package com.salesmanager.business.marketing.util;

import java.math.BigDecimal;

import com.salesmanager.core.model.catalog.product.price.ProductPrice;
import com.salesmanager.core.model.catalog.product.price.ProductPriceType;
import com.salesmanager.marketing.model.Promotion;

public class PromotionPriceUtil {
	
	public static ProductPrice resolveProductPrice(ProductPrice defaultPrice, Promotion promotion)
	{
		defaultPrice.setDefaultPrice(false);
		ProductPrice productPrice = new ProductPrice();
		
		if(promotion.isDiscountPercentage())
		{		
			Integer discount = promotion.getOrderDiscountPercentage();
			double amountDiscouted = (defaultPrice.getProductPriceAmount().doubleValue() * discount.doubleValue()) / 100;
			productPrice.setProductPriceSpecialAmount(new BigDecimal(defaultPrice.getProductPriceAmount().doubleValue() - amountDiscouted));
		}
		else if(!promotion.isDiscountPercentage())
		{
			productPrice.setProductPriceSpecialAmount(new BigDecimal(defaultPrice.getProductPriceAmount().doubleValue() - promotion.getAmount().doubleValue()));
		}
		productPrice.setCode("promotion");
		productPrice.setDefaultPrice(true);
		productPrice.setProductAvailability(defaultPrice.getProductAvailability());
		productPrice.setProductPriceAmount(defaultPrice.getProductPriceAmount());
		productPrice.setProductPriceType(ProductPriceType.ONE_TIME);
		return productPrice;
	}
}
