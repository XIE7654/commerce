package cn.iocoder.yudao.module.amazon.sdk;

/** Amazon SP-API 请求结果的归档类型。 */
public enum AmazonApiCategory {

    A_PLUS_CONTENT_MANAGEMENT("a_plus_content_management"),
    AMAZON_WAREHOUSING_AND_DISTRIBUTION("amazon_warehousing_and_distribution"),
    APP_INTEGRATIONS("app_integrations"),
    APPLICATION_MANAGEMENT("application_management"),
    CATALOG_ITEMS("catalog_items"),
    CUSTOMER_FEEDBACK("customer_feedback"),
    DATA_KIOSK("data_kiosk"),
    DELIVERY_BY_AMAZON("delivery_by_amazon"),
    EASY_SHIP("easy_ship"),
    EXTERNAL_FULFILLMENT("external_fulfillment"),
    FEEDS("feeds"),
    FINANCES("finances"),
    FULFILLMENT_BY_AMAZON("fulfillment_by_amazon_fba"),
    FULFILLMENT_INBOUND("fulfillment_inbound"),
    FULFILLMENT_OUTBOUND("fulfillment_outbound"),
    INVOICING("invoicing"),
    LISTINGS("listings"),
    MERCHANT_FULFILLMENT("merchant_fulfillment"),
    MESSAGING("messaging"),
    NOTIFICATIONS("notifications"),
    ORDERS("orders"),
    PRODUCT_FEES("product_fees"),
    PRODUCT_PRICING("product_pricing"),
    REPLENISHMENT("replenishment"),
    REPORTS("reports"),
    SALES("sales"),
    SELLER_WALLET("seller_wallet"),
    SELLERS("sellers"),
    SERVICES("services"),
    SOLICITATIONS("solicitations"),
    SUPPLY_SOURCES("supply_sources"),
    TOKENS("tokens"),
    TRACKING("tracking"),
    UPLOADS("uploads"),
    VEHICLES("vehicles"),
    VENDOR_DIRECT_FULFILLMENT("vendor_direct_fulfillment"),
    VENDOR_RETAIL_PROCUREMENT("vendor_retail_procurement");

    /** 文件归档使用的安全目录名称，仅包含小写字母和下划线。 */
    private final String directoryName;

    AmazonApiCategory(String directoryName) {
        this.directoryName = directoryName;
    }

    /**
     * 获取文件归档目录名称。
     *
     * @return Amazon API 类型目录名称
     */
    public String getDirectoryName() {
        return directoryName;
    }

}
