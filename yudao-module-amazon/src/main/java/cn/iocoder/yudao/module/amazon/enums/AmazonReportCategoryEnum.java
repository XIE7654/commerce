package cn.iocoder.yudao.module.amazon.enums;

import java.util.EnumSet;
import java.util.Set;

/**
 * Amazon Reports 文档中的报表分类及其可用报表值。
 */
public enum AmazonReportCategoryEnum {

    AMAZON_BUSINESS("Amazon Business", EnumSet.of(AmazonReportTypeEnum.FEE_DISCOUNTS_REPORT)),
    ANALYTICS("Analytics", EnumSet.of(AmazonReportTypeEnum.GET_BRAND_ANALYTICS_SEARCH_CATALOG_PERFORMANCE_REPORT,
            AmazonReportTypeEnum.GET_BRAND_ANALYTICS_SEARCH_QUERY_PERFORMANCE_REPORT,
            AmazonReportTypeEnum.GET_BRAND_ANALYTICS_MARKET_BASKET_REPORT,
            AmazonReportTypeEnum.GET_BRAND_ANALYTICS_SEARCH_TERMS_REPORT,
            AmazonReportTypeEnum.GET_BRAND_ANALYTICS_REPEAT_PURCHASE_REPORT,
            AmazonReportTypeEnum.GET_VENDOR_REAL_TIME_INVENTORY_REPORT,
            AmazonReportTypeEnum.GET_VENDOR_REAL_TIME_TRAFFIC_REPORT,
            AmazonReportTypeEnum.GET_VENDOR_REAL_TIME_SALES_REPORT,
            AmazonReportTypeEnum.GET_VENDOR_SALES_REPORT,
            AmazonReportTypeEnum.GET_VENDOR_NET_PURE_PRODUCT_MARGIN_REPORT,
            AmazonReportTypeEnum.GET_VENDOR_TRAFFIC_REPORT,
            AmazonReportTypeEnum.GET_VENDOR_FORECASTING_REPORT,
            AmazonReportTypeEnum.GET_VENDOR_INVENTORY_REPORT,
            AmazonReportTypeEnum.GET_SALES_AND_TRAFFIC_REPORT)),
    B2B_PRODUCT_OPPORTUNITIES("B2B Product Opportunities", EnumSet.of(
            AmazonReportTypeEnum.GET_B2B_PRODUCT_OPPORTUNITIES_RECOMMENDED_FOR_YOU,
            AmazonReportTypeEnum.GET_B2B_PRODUCT_OPPORTUNITIES_NOT_YET_ON_AMAZON)),
    BROWSE_TREE("Browse Tree", EnumSet.of(AmazonReportTypeEnum.GET_XML_BROWSE_TREE_DATA)),
    EASY_SHIP("Easy Ship", EnumSet.of(AmazonReportTypeEnum.GET_EASYSHIP_DOCUMENTS,
            AmazonReportTypeEnum.GET_EASYSHIP_PICKEDUP, AmazonReportTypeEnum.GET_EASYSHIP_WAITING_FOR_PICKUP)),
    FBA("FBA", EnumSet.of(AmazonReportTypeEnum.GET_AMAZON_FULFILLED_SHIPMENTS_DATA_GENERAL,
            AmazonReportTypeEnum.GET_AMAZON_FULFILLED_SHIPMENTS_DATA_INVOICING,
            AmazonReportTypeEnum.GET_AMAZON_FULFILLED_SHIPMENTS_DATA_TAX,
            AmazonReportTypeEnum.GET_FBA_FULFILLMENT_CUSTOMER_SHIPMENT_SALES_DATA,
            AmazonReportTypeEnum.GET_FBA_FULFILLMENT_CUSTOMER_SHIPMENT_PROMOTION_DATA,
            AmazonReportTypeEnum.GET_FBA_FULFILLMENT_CUSTOMER_TAXES_DATA,
            AmazonReportTypeEnum.GET_REMOTE_FULFILLMENT_ELIGIBILITY, AmazonReportTypeEnum.GET_AFN_INVENTORY_DATA,
            AmazonReportTypeEnum.GET_AFN_INVENTORY_DATA_BY_COUNTRY, AmazonReportTypeEnum.GET_LEDGER_SUMMARY_VIEW_DATA,
            AmazonReportTypeEnum.GET_LEDGER_DETAIL_VIEW_DATA, AmazonReportTypeEnum.GET_RESERVED_INVENTORY_DATA,
            AmazonReportTypeEnum.GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA,
            AmazonReportTypeEnum.GET_FBA_MYI_ALL_INVENTORY_DATA,
            AmazonReportTypeEnum.GET_RESTOCK_INVENTORY_RECOMMENDATIONS_REPORT,
            AmazonReportTypeEnum.GET_FBA_FULFILLMENT_INBOUND_NONCOMPLIANCE_DATA,
            AmazonReportTypeEnum.GET_STRANDED_INVENTORY_UI_DATA,
            AmazonReportTypeEnum.GET_STRANDED_INVENTORY_LOADER_DATA,
            AmazonReportTypeEnum.GET_FBA_STORAGE_FEE_CHARGES_DATA,
            AmazonReportTypeEnum.GET_FBA_INVENTORY_PLANNING_DATA,
            AmazonReportTypeEnum.GET_FBA_OVERAGE_FEE_CHARGES_DATA,
            AmazonReportTypeEnum.GET_FBA_ESTIMATED_FBA_FEES_TXT_DATA,
            AmazonReportTypeEnum.GET_FBA_REIMBURSEMENTS_DATA,
            AmazonReportTypeEnum.GET_FBA_FULFILLMENT_LONGTERM_STORAGE_FEE_CHARGES_DATA,
            AmazonReportTypeEnum.GET_FBA_FULFILLMENT_CUSTOMER_RETURNS_DATA,
            AmazonReportTypeEnum.GET_FBA_FULFILLMENT_CUSTOMER_SHIPMENT_REPLACEMENT_DATA,
            AmazonReportTypeEnum.GET_FBA_RECOMMENDED_REMOVAL_DATA,
            AmazonReportTypeEnum.GET_FBA_FULFILLMENT_REMOVAL_ORDER_DETAIL_DATA,
            AmazonReportTypeEnum.GET_FBA_FULFILLMENT_REMOVAL_SHIPMENT_DETAIL_DATA)),
    INVENTORY("Inventory", EnumSet.of(AmazonReportTypeEnum.GET_FLAT_FILE_OPEN_LISTINGS_DATA,
            AmazonReportTypeEnum.GET_MERCHANT_LISTINGS_ALL_DATA, AmazonReportTypeEnum.GET_MERCHANT_LISTINGS_DATA,
            AmazonReportTypeEnum.GET_MERCHANT_LISTINGS_INACTIVE_DATA,
            AmazonReportTypeEnum.GET_MERCHANT_LISTINGS_DATA_BACK_COMPAT,
            AmazonReportTypeEnum.GET_MERCHANT_LISTINGS_DATA_LITE,
            AmazonReportTypeEnum.GET_MERCHANT_LISTINGS_DATA_LITER,
            AmazonReportTypeEnum.GET_MERCHANT_CANCELLED_LISTINGS_DATA,
            AmazonReportTypeEnum.GET_MERCHANTS_LISTINGS_FYP_REPORT, AmazonReportTypeEnum.GET_PAN_EU_OFFER_STATUS,
            AmazonReportTypeEnum.GET_MFN_PANEU_OFFER_STATUS, AmazonReportTypeEnum.GET_REFERRAL_FEE_PREVIEW_REPORT)),
    INVOICE("Invoice", EnumSet.of(AmazonReportTypeEnum.GET_FLAT_FILE_VAT_INVOICE_DATA_REPORT,
            AmazonReportTypeEnum.GET_XML_VAT_INVOICE_DATA_REPORT)),
    ORDER("Order", EnumSet.of(AmazonReportTypeEnum.GET_FLAT_FILE_ACTIONABLE_ORDER_DATA_SHIPPING,
            AmazonReportTypeEnum.GET_ORDER_REPORT_DATA_INVOICING, AmazonReportTypeEnum.GET_ORDER_REPORT_DATA_TAX,
            AmazonReportTypeEnum.GET_ORDER_REPORT_DATA_SHIPPING, AmazonReportTypeEnum.GET_FLAT_FILE_ORDER_REPORT_DATA_INVOICING,
            AmazonReportTypeEnum.GET_FLAT_FILE_ORDER_REPORT_DATA_SHIPPING, AmazonReportTypeEnum.GET_FLAT_FILE_ORDER_REPORT_DATA_TAX,
            AmazonReportTypeEnum.GET_FLAT_FILE_ALL_ORDERS_DATA_BY_LAST_UPDATE_GENERAL,
            AmazonReportTypeEnum.GET_FLAT_FILE_ALL_ORDERS_DATA_BY_ORDER_DATE_GENERAL,
            AmazonReportTypeEnum.GET_FLAT_FILE_ARCHIVED_ORDERS_DATA_BY_ORDER_DATE,
            AmazonReportTypeEnum.GET_XML_ALL_ORDERS_DATA_BY_LAST_UPDATE_GENERAL,
            AmazonReportTypeEnum.GET_XML_ALL_ORDERS_DATA_BY_ORDER_DATE_GENERAL,
            AmazonReportTypeEnum.GET_FLAT_FILE_PENDING_ORDERS_DATA, AmazonReportTypeEnum.GET_PENDING_ORDERS_DATA,
            AmazonReportTypeEnum.GET_CONVERGED_FLAT_FILE_PENDING_ORDERS_DATA)),
    PAYMENT("Payment", EnumSet.of(AmazonReportTypeEnum.GET_DATE_RANGE_FINANCIAL_HOLDS_DATA)),
    PERFORMANCE("Performance", EnumSet.of(AmazonReportTypeEnum.GET_SELLER_FEEDBACK_DATA,
            AmazonReportTypeEnum.GET_V1_SELLER_PERFORMANCE_REPORT,
            AmazonReportTypeEnum.GET_V2_SELLER_PERFORMANCE_REPORT,
            AmazonReportTypeEnum.GET_PROMOTION_PERFORMANCE_REPORT,
            AmazonReportTypeEnum.GET_COUPON_PERFORMANCE_REPORT)),
    REGULATORY_COMPLIANCE("Regulatory Compliance", EnumSet.of(AmazonReportTypeEnum.END_USER_DATA_REPORT,
            AmazonReportTypeEnum.FBA_BULK_INVOICE, AmazonReportTypeEnum.MARKETPLACE_ASIN_PAGE_VIEW_METRICS,
            AmazonReportTypeEnum.GET_EPR_MONTHLY_REPORTS, AmazonReportTypeEnum.GET_EPR_QUARTERLY_REPORTS,
            AmazonReportTypeEnum.GET_EPR_ANNUAL_REPORTS)),
    RETURNS("Returns", EnumSet.of(AmazonReportTypeEnum.GET_XML_RETURNS_DATA_BY_RETURN_DATE,
            AmazonReportTypeEnum.GET_FLAT_FILE_RETURNS_DATA_BY_RETURN_DATE,
            AmazonReportTypeEnum.GET_XML_MFN_PRIME_RETURNS_REPORT,
            AmazonReportTypeEnum.GET_CSV_MFN_PRIME_RETURNS_REPORT,
            AmazonReportTypeEnum.GET_XML_MFN_SKU_RETURN_ATTRIBUTES_REPORT,
            AmazonReportTypeEnum.GET_FLAT_FILE_MFN_SKU_RETURN_ATTRIBUTES_REPORT)),
    SETTLEMENT_PAYMENTS("Settlement/Payments", EnumSet.of(AmazonReportTypeEnum.GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE,
            AmazonReportTypeEnum.GET_V2_SETTLEMENT_REPORT_DATA_XML,
            AmazonReportTypeEnum.GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE_V2)),
    TAX("Tax", EnumSet.of(AmazonReportTypeEnum.GST_MTR_STOCK_TRANSFER_REPORT, AmazonReportTypeEnum.GST_MTR_B2B,
            AmazonReportTypeEnum.GST_MTR_B2C, AmazonReportTypeEnum.GET_FLAT_FILE_SALES_TAX_DATA,
            AmazonReportTypeEnum.SC_VAT_TAX_REPORT, AmazonReportTypeEnum.GET_VAT_TRANSACTION_DATA,
            AmazonReportTypeEnum.GET_GST_MTR_B2B_CUSTOM, AmazonReportTypeEnum.GET_GST_MTR_B2C_CUSTOM,
            AmazonReportTypeEnum.GET_GST_STR_ADHOC));

    private final String reportType;
    private final Set<AmazonReportTypeEnum> availableReports;

    AmazonReportCategoryEnum(String reportType, Set<AmazonReportTypeEnum> availableReports) {
        this.reportType = reportType;
        this.availableReports = Set.copyOf(availableReports);
    }

    /** 返回 Reports 文档中的 Report type 分类名称。 */
    public String getReportType() { return reportType; }

    /** 返回该分类可请求的 Available reports。 */
    public Set<AmazonReportTypeEnum> getAvailableReports() { return availableReports; }
}
