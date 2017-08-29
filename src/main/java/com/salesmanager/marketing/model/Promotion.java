package com.salesmanager.marketing.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.attribute.ProductOption;
import com.salesmanager.core.model.catalog.product.manufacturer.Manufacturer;
import com.salesmanager.core.model.common.audit.AuditListener;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.generic.SalesManagerEntity;
import com.salesmanager.core.model.merchant.MerchantStore;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "PROMOTION", schema=SchemaConstant.SALESMANAGER_SCHEMA, uniqueConstraints=
@UniqueConstraint(columnNames = {"CODE_PROMO"}))
public class Promotion extends SalesManagerEntity<Long, Promotion> implements Auditable{

	private static final long serialVersionUID = 2145708542301246238L;
	@Id
	@Column(name = "PROMOTION_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PROMOTION_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	@NotEmpty
	@Column(name = "DESCRIPTION", nullable=false)
	private String description;
	@NotEmpty
	@Column(name = "CODE_PROMO", nullable=false)
	private String codePromo;
	@NotEmpty
	@Column(name = "PROMOTION_TYPE", nullable=false)
	private String promotionType;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private Date startDate; 
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	private Date endDate; 
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "PROMOTION_PRODUCTS", schema=SchemaConstant.SALESMANAGER_SCHEMA, joinColumns = { 
			@JoinColumn(name = "PROMOTION_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "PRODUCT_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private Set<Product> products = new HashSet<Product>();
	
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "PROMOTION_PRODUCTS_EXCLUDED", schema=SchemaConstant.SALESMANAGER_SCHEMA, joinColumns = { 
			@JoinColumn(name = "PROMOTION_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "PRODUCT_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private Set<Product> excludedProducts = new HashSet<Product>();
	
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "PROMOTION_PRODUCTS_FREE", schema=SchemaConstant.SALESMANAGER_SCHEMA, joinColumns = { 
			@JoinColumn(name = "PROMOTION_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "PRODUCT_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private Set<Product> freeProducts = new HashSet<Product>();
	
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "PROMOTION_CATEGORY ", schema=SchemaConstant.SALESMANAGER_SCHEMA, joinColumns = { 
			@JoinColumn(name = "PROMOTION_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "CATEGORY_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private Set<Category> categories = new HashSet<Category>();
	
	
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "PROMOTION_PRODUCT_ATTRIBUTE", schema=SchemaConstant.SALESMANAGER_SCHEMA, joinColumns = { 
			@JoinColumn(name = "PROMOTION_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "PRODUCT_OPTION_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private Set<ProductOption> productOptions = new HashSet<ProductOption>();
	
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "PROMOTION_MANUFACTURER", schema=SchemaConstant.SALESMANAGER_SCHEMA, joinColumns = { 
			@JoinColumn(name = "PROMOTION_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "MANUFACTURER_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private Set<Manufacturer> productManufacturer = new HashSet<Manufacturer>();
	
	
	@Column(name = "AMOUNT", nullable=true)
	private BigDecimal amount;
	
	@Column(name = "QTYMIN", nullable=true)
	private Integer qtymin;
	
	@Column(name = "QTYMAX", nullable=true)
	private Integer qtymax;
	
	@Column(name = "CUSTOMER_NEW", nullable=true)
	private boolean newCustomer;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CUSTOMER_LAST_ORDER")
	private Date custLastOrder;
	
	@Column(name = "ORDER_PROMO", nullable=true)
	private Integer order;
	
	@Column(name = "ORDER_DISC_PERCENTAGE", nullable=true)
	private Integer orderDiscountPercentage;
	
	@Column(name = "ORDER_TOTAL_DISCOUNT", nullable=true)
	private Integer orderTotalDiscount;
	
	@Column(name = "DISCOUNT", nullable=true)
	private Integer discount;
	
	@Column(name = "DISCOUNT_PERCENTAGE", nullable=true)
	private boolean discountPercentage;
	
	@Column(name = "ACUMULATIVE", nullable=true)
	private boolean acumulative;
	
	@Column(name = "ACTIVE", nullable=true)
	private boolean active;
	
	@ManyToOne(targetEntity = MerchantStore.class)
	@JoinColumn(name="MERCHANT_ID")
	private MerchantStore merchant;
	
	@Column(name = "THRESHOLD", nullable=true)
	private Integer threshold;
	
	@Column(name = "THRESHOLD_TYPE", nullable=true)
	private String thresholdType;
	
	@Column(name = "COUPON_TYPE", nullable=true)
	private String couponType;
	
	@Column(name = "COUPON_SLOT", nullable=true)
	private String couponSlot;
	
	@Column(name = "MONEY_BACK", nullable=true)
	private Integer moneyBack;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public AuditSection getAuditSection() {
		return auditSection;
	}
	public void setAuditSection(AuditSection audit) {
		this.auditSection = audit;
		
	}
	
	public String getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Set<Product> getProducts() {
		return products;
	}
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	public Set<Product> getExcludedProducts() {
		return excludedProducts;
	}
	public void setExcludedProducts(Set<Product> excludedProducts) {
		this.excludedProducts = excludedProducts;
	}
	public Set<Category> getCategories() {
		return categories;
	}
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	public Set<ProductOption> getProductOptions() {
		return productOptions;
	}
	public void setProductOptions(Set<ProductOption> productOptions) {
		this.productOptions = productOptions;
	}
	public Set<Manufacturer> getProductManufacturer() {
		return productManufacturer;
	}
	public void setProductManufacturer(Set<Manufacturer> productManufacturer) {
		this.productManufacturer = productManufacturer;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getQtymin() {
		return qtymin;
	}
	public void setQtymin(Integer qtymin) {
		this.qtymin = qtymin;
	}
	public Integer getQtymax() {
		return qtymax;
	}
	public void setQtymax(Integer qtymax) {
		this.qtymax = qtymax;
	}
	
	
	
	public boolean isNewCustomer() {
		return newCustomer;
	}
	public void setNewCustomer(boolean newCustomer) {
		this.newCustomer = newCustomer;
	}
	public Date getCustLastOrder() {
		return custLastOrder;
	}
	public void setCustLastOrder(Date custLastOrder) {
		this.custLastOrder = custLastOrder;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public MerchantStore getMerchant() {
		return merchant;
	}
	public void setMerchant(MerchantStore merchant) {
		this.merchant = merchant;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getOrderDiscountPercentage() {
		return orderDiscountPercentage;
	}
	public void setOrderDiscountPercentage(Integer orderDiscountPercentage) {
		this.orderDiscountPercentage = orderDiscountPercentage;
	}
	public Integer getOrderTotalDiscount() {
		return orderTotalDiscount;
	}
	public void setOrderTotalDiscount(Integer orderTotalDiscount) {
		this.orderTotalDiscount = orderTotalDiscount;
	}
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	
	public boolean isDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(boolean discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	public Set<Product> getFreeProducts() {
		return freeProducts;
	}
	public void setFreeProducts(Set<Product> freeProducts) {
		this.freeProducts = freeProducts;
	}
	public Integer getThreshold() {
		return threshold;
	}
	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}
	public String getThresholdType() {
		return thresholdType;
	}
	public void setThresholdType(String thresholdType) {
		this.thresholdType = thresholdType;
	}
	public String getCouponType() {
		return couponType;
	}
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	public String getCouponSlot() {
		return couponSlot;
	}
	public void setCouponSlot(String couponSlot) {
		this.couponSlot = couponSlot;
	}
	public Integer getMoneyBack() {
		return moneyBack;
	}
	public void setMoneyBack(Integer moneyBack) {
		this.moneyBack = moneyBack;
	}
	public boolean isAcumulative() {
		return acumulative;
	}
	public void setAcumulative(boolean acumulative) {
		this.acumulative = acumulative;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getCodePromo() {
		return codePromo;
	}
	public void setCodePromo(String codePromo) {
		this.codePromo = codePromo;
	}
	
	
	
}
