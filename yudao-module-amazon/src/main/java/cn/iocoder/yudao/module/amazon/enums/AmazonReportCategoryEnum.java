package cn.iocoder.yudao.module.amazon.enums;

import java.util.EnumSet;
import java.util.Set;

/**
 * Amazon Reports 文档中的报表分类及其可用报表值。
 */
public enum AmazonReportCategoryEnum {

    AMAZON_BUSINESS("Amazon Business", EnumSet.of(AmazonReportTypeEnum.FEE_DISCOUNTS_REPORT)),
    ANALYTICS("Analytics", EnumSet.range(AmazonReportTypeEnum.GET_BRAND_ANALYTICS_SEARCH_CATALOG_PERFORMANCE_REPORT,
            AmazonReportTypeEnum.GET_SALES_AND_TRAFFIC_REPORT)),
    B2B_PRODUCT_OPPORTUNITIES("B2B Product Opportunities", EnumSet.range(
            AmazonReportTypeEnum.GET_B2B_PRODUCT_OPPORTUNITIES_RECOMMENDED_FOR_YOU,
            AmazonReportTypeEnum.GET_B2B_PRODUCT_OPPORTUNITIES_NOT_YET_ON_AMAZON)),
    BROWSE_TREE("Browse Tree", EnumSet.of(AmazonReportTypeEnum.GET_XML_BROWSE_TREE_DATA)),
    EASY_SHIP("Easy Ship", EnumSet.range(AmazonReportTypeEnum.GET_EASYSHIP_DOCUMENTS,
            AmazonReportTypeEnum.GET_EASYSHIP_WAITING_FOR_PICKUP)),
    FBA("FBA", EnumSet.range(AmazonReportTypeEnum.GET_AMAZON_FULFILLED_SHIPMENTS_DATA_GENERAL,
            AmazonReportTypeEnum.GET_FBA_FULFILLMENT_REMOVAL_SHIPMENT_DETAIL_DATA)),
    INVENTORY("Inventory", EnumSet.range(AmazonReportTypeEnum.GET_FLAT_FILE_OPEN_LISTINGS_DATA,
            AmazonReportTypeEnum.GET_REFERRAL_FEE_PREVIEW_REPORT)),
    INVOICE("Invoice", EnumSet.range(AmazonReportTypeEnum.GET_FLAT_FILE_VAT_INVOICE_DATA_REPORT,
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
    PERFORMANCE("Performance", EnumSet.range(AmazonReportTypeEnum.GET_SELLER_FEEDBACK_DATA,
            AmazonReportTypeEnum.GET_COUPON_PERFORMANCE_REPORT)),
    REGULATORY_COMPLIANCE("Regulatory Compliance", EnumSet.range(AmazonReportTypeEnum.END_USER_DATA_REPORT,
            AmazonReportTypeEnum.GET_EPR_ANNUAL_REPORTS)),
    RETURNS("Returns", EnumSet.range(AmazonReportTypeEnum.GET_XML_RETURNS_DATA_BY_RETURN_DATE,
            AmazonReportTypeEnum.GET_FLAT_FILE_MFN_SKU_RETURN_ATTRIBUTES_REPORT)),
    SETTLEMENT_PAYMENTS("Settlement/Payments", EnumSet.range(AmazonReportTypeEnum.GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE,
            AmazonReportTypeEnum.GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE_V2)),
    TAX("Tax", EnumSet.range(AmazonReportTypeEnum.GST_MTR_STOCK_TRANSFER_REPORT, AmazonReportTypeEnum.GET_GST_STR_ADHOC));

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
