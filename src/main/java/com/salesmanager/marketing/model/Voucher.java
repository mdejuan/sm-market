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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.common.audit.AuditListener;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.generic.SalesManagerEntity;
import com.salesmanager.core.model.merchant.MerchantStore;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "VOUCHER", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class Voucher extends SalesManagerEntity<Long, Voucher> implements Auditable{

	private static final long serialVersionUID = 2145708545601246238L;
	@Id
	@Column(name = "VOUCHER_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "VOUCHER_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	@NotEmpty
	@Column(name = "DESCRIPTION", nullable=false)
	private String description;
	
	@NotEmpty
	@Column(name = "CODE", nullable=false)
	private String code;
	@Column(name = "MAX_REDEEMED", nullable=true)
	private Integer maxRedeemed;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private Date startDate; 
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	private Date endDate; 
	@Column(name = "AMOUNT", nullable=true)
	private BigDecimal amount;
	
	@Column(name = "QTYMIN", nullable=true)
	private Integer qtymin;
	@Column(name = "QTYMAX", nullable=true)
	private Integer qtymax;
	
	@Column(name = "CUSTOMER_NEW", nullable=true)
	private boolean isNewCustomer;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CUSTOMER_LAST_ORDER")
	private Date custLastOrder;
	@Column(name = "ORDER_DISC_PERCENTAGE", nullable=true)
	private Integer orderDiscountPercentage;
	
	@Column(name = "ORDER_TOTAL_DISCOUNT", nullable=true)
	private Integer orderTotalDiscount;
	
	@Column(name = "ACTIVE", nullable=true)
	private boolean active;
	
	@ManyToOne(targetEntity = MerchantStore.class)
	@JoinColumn(name="MERCHANT_ID")
	private MerchantStore merchant;
	
	@OneToMany(targetEntity = VoucherCustomer.class, cascade = CascadeType.ALL, mappedBy = "voucher")
	private Set<VoucherCustomer> customers = new HashSet<VoucherCustomer>();
	
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
		return isNewCustomer;
	}
	public void setNewCustomer(boolean isNewCustomer) {
		this.isNewCustomer = isNewCustomer;
	}
	public Date getCustLastOrder() {
		return custLastOrder;
	}
	public void setCustLastOrder(Date custLastOrder) {
		this.custLastOrder = custLastOrder;
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
	public MerchantStore getMerchant() {
		return merchant;
	}
	public void setMerchant(MerchantStore merchant) {
		this.merchant = merchant;
	}
	public AuditSection getAuditSection() {
		return auditSection;
	}
	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getMaxRedeemed() {
		return maxRedeemed;
	}
	public void setMaxRedeemed(Integer maxRedeemed) {
		this.maxRedeemed = maxRedeemed;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Set<VoucherCustomer> getCustomers() {
		return customers;
	}
	public void setCustomers(Set<VoucherCustomer> customers) {
		this.customers = customers;
	}

	
	
}
