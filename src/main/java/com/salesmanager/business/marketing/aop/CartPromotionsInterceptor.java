package com.salesmanager.business.marketing.aop;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.salesmanager.business.marketing.services.PromotionService;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.catalog.product.PricingService;
import com.salesmanager.core.business.utils.CacheUtils;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.marketing.model.Promotion;
import com.salesmanager.shop.configuration.marketing.PromotionType;
import com.salesmanager.shop.model.order.OrderTotal;
import com.salesmanager.shop.model.shoppingcart.ShoppingCartData;
import com.salesmanager.shop.model.shoppingcart.ShoppingCartItem;

@Aspect
@Component
public class CartPromotionsInterceptor {
	@Inject
	private PromotionService promotionService;
	@Inject
	Environment env;
	@Inject
	private PricingService pricingService;
	@Inject
	private CacheUtils cache;

	@AfterReturning(pointcut = "execution(* com.salesmanager.shop.store.controller.shoppingCart.facade.ShoppingCartFacadeImpl.getShoppingCartData(..))", returning = "result")
	public void addPromotions(JoinPoint joinPoint, Object result) throws Exception {
		Object[] args = joinPoint.getArgs();
		MerchantStore store = null;
		for (Object arg : args) {
			if (arg != null && arg.getClass().getName().equals(MerchantStore.class.getName())) {
				store = (MerchantStore) arg;
			}
		}
		ShoppingCartData cart = new ShoppingCartData();
		cart = (ShoppingCartData) result;
		List<Promotion> promotions = getOrderPromotions();
		List<ShoppingCartItem> items = cart.getShoppingCartItems();
		Map promoMessages = new HashMap<String, String>();
		if (!items.isEmpty() && !promotions.isEmpty()) {
			List<OrderTotal> totals = cart.getTotals();
			BigDecimal orderTotal = null;
			BigDecimal orderSubTotal = null;
			for (OrderTotal order : totals) {
				if (order.getCode().equals("order.total.total")) {
					orderTotal = order.getValue();
				} else if (order.getCode().equals("order.total.subtotal")) {
					orderSubTotal = order.getValue();
				}
			}
			for (Promotion promotion : promotions) {

				if (promotion.getPromotionType().equals(PromotionType.Order_Discount.toString())
						&& promotion.getMerchant().getId() == store.getId() && checkConditions(promotion, orderTotal)) {

					if (promotion.isDiscountPercentage()) {
						
							Integer discount = promotion.getOrderDiscountPercentage();

							double amountDiscouted = (orderSubTotal.doubleValue() * discount.doubleValue()) / 100;
							BigDecimal total = new BigDecimal(orderTotal.doubleValue() - amountDiscouted);
							cart.setTotal(pricingService.getDisplayAmount(total, promotion.getMerchant()));
							promoMessages.put(MessageFormat
									.format(env.getProperty("message.promotion.orderPercentageDiscount"), discount),
									amountDiscouted);
						
					} else if (!promotion.isDiscountPercentage()) {
						BigDecimal amountDiscounted = promotion.getAmount();
						BigDecimal total = new BigDecimal(orderTotal.doubleValue() - amountDiscounted.doubleValue());
						cart.setTotal(pricingService.getDisplayAmount(total, promotion.getMerchant()));
						promoMessages.put(MessageFormat.format(env.getProperty("message.promotion.orderDiscount"),
								amountDiscounted, store.getCurrency().getName()), amountDiscounted);
					}
				} else if (promotion.getPromotionType().equals(PromotionType.Free_Shipping.toString())) {
					List<Product> productlist = promotionService.getProductList(promotion);
					List<Long> productsIds = new ArrayList<Long>();
					boolean contains = true;
					if (productlist.isEmpty()) {
						contains = false;
					} else {

						for (Product product : productlist) {
							productsIds.add(product.getId());
						}

						for (ShoppingCartItem item : items) {
							if (!productsIds.contains(item.getProductId())) {
								contains = false;
								break;
							}
						}
					}

					if (contains) {
						promoMessages.put(env.getProperty("message.promotion.freeShipping"), "");
					}

				}

			}

		}

		cart.setPromotionMessages(promoMessages);

	}

	public boolean checkConditions(Promotion promotion, BigDecimal orderTotal) {
		if (promotion.getQtymin() != null) {
			if (promotion.getQtymin() > orderTotal.doubleValue()) {
				return false;
			}
		}
		if (promotion.getQtymax() != null) {
			if (promotion.getQtymax() < orderTotal.doubleValue()) {
				return false;
			}
		}

		return true;
	}

	public List<Promotion> getOrderPromotions() throws Exception {

		Date today = new Date();
		
		List<Promotion> promotionsAvailable = (List<Promotion>) cache.getFromCache("0promotionsAvailable_");
		if (promotionsAvailable == null) {
			promotionsAvailable = new ArrayList<Promotion>();
			List<Promotion> promotions = new ArrayList<Promotion>();
			promotions = promotionService.list();

			for (Promotion promotion : promotions) {
				if ((promotion.getPromotionType().equals(PromotionType.Order_Discount.toString())
						|| promotion.getPromotionType().equals(PromotionType.Free_Shipping.toString())
						|| promotion.getPromotionType().equals(PromotionType.Threshold.toString())
						|| promotion.getPromotionType().equals(PromotionType.Buy_X_get_Y_Free.toString()))
						&& promotion.isActive() && promotion.getStartDate().compareTo(today) < 0
						&& promotion.getEndDate().compareTo(today) > 0) {

					promotionsAvailable.add(promotion);
				}

			}
			cache.putInCache(promotionsAvailable, "0promotionsAvailable_");
		}
		return promotionsAvailable;
	}
}
