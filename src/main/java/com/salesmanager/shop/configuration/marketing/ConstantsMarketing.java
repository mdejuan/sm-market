package com.salesmanager.shop.configuration.marketing;

import com.salesmanager.shop.admin.controller.ControllerConstants;

public interface ConstantsMarketing extends ControllerConstants {
	interface Tiles {
		interface Promotion {
			final String promotionList = "admin-promotion-list";
			final String promotionDetails = "admin-promotion-details";
		}
		interface Voucher {
			final String voucherList = "admin-voucher-list";
			final String voucherDetails = "admin-voucher-details";
			final String voucherCustomerList = "admin-voucher-customer-list";
			
		}
		interface EmailMarketing {
			final String emailList = "admin-email-list";
			final String emailDetails = "admin-email-details";
		}
	}
}
