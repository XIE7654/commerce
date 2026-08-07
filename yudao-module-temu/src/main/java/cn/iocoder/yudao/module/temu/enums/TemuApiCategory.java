package cn.iocoder.yudao.module.temu.enums;

/**
 * Temu OpenAPI 文档菜单对应的响应归档目录。
 *
 * <p>目录名称与 Temu 开放平台的一级菜单保持一致，未知接口归档到 {@link #OTHER}，
 * 以保证新增接口在未补充映射前仍可保留响应数据。</p>
 */
public enum TemuApiCategory {

    AUTHORIZATION("authorization"),
    PRODUCT("product"),
    PRICE("price"),
    ORDER("order"),
    ORDER_CANCELLATION("order_cancellation"),
    FULFILLMENT("fulfillment"),
    RETURN_AND_REFUND("return_and_refund"),
    PROMOTION("promotion"),
    WEBHOOK("webhook"),
    ADS("ads"),
    COMPLIANCE("compliance"),
    OTHER("other");

    /** 文件归档使用的安全目录名称。 */
    private final String directoryName;

    TemuApiCategory(String directoryName) {
        this.directoryName = directoryName;
    }

    /**
     * 根据 Temu API type 确定所属文档菜单。
     *
     * @param apiType Temu OpenAPI 接口 type
     * @return 对应的归档目录分类
     */
    public static TemuApiCategory fromApiType(String apiType) {
        if (apiType == null || apiType.isBlank()) {
            return OTHER;
        }
        if (apiType.startsWith("bg.open.accesstoken") || apiType.startsWith("temu.local.mall.")) {
            return AUTHORIZATION;
        }
        if (apiType.startsWith("temu.searchrec.")) {
            return ADS;
        }
        if (apiType.startsWith("bg.promotion.")) {
            return PROMOTION;
        }
        if (apiType.startsWith("bg.tmc.")) {
            return WEBHOOK;
        }
        if (apiType.startsWith("bg.logistics.") || apiType.startsWith("temu.logistics.")
                || apiType.startsWith("bg.cooperativewarehouse.") || apiType.startsWith("bg.order.fulfillment.")
                || apiType.startsWith("bg.order.unshipped.")) {
            return FULFILLMENT;
        }
        if (apiType.startsWith("temu.order.cancel.") || apiType.startsWith("bg.aftersales.cancel.")) {
            return ORDER_CANCELLATION;
        }
        if (apiType.startsWith("bg.aftersales.") || apiType.startsWith("temu.aftersales.")) {
            return RETURN_AND_REFUND;
        }
        if (apiType.startsWith("bg.compliance.") || apiType.startsWith("bg.flash.") || apiType.startsWith("bg.arbok.")) {
            return COMPLIANCE;
        }
        if (apiType.startsWith("temu.local.goods.recommendedprice.") || apiType.startsWith("bg.local.goods.priceorder.")
                || apiType.startsWith("bg.local.goods.sku.list.price.") || "bg.order.amount.query".equals(apiType)) {
            return PRICE;
        }
        if (apiType.startsWith("bg.order.") || apiType.startsWith("temu.local.order.")) {
            return ORDER;
        }
        if (apiType.startsWith("bg.local.") || apiType.startsWith("temu.local.")) {
            return PRODUCT;
        }
        return OTHER;
    }

    /**
     * 获取文件归档目录名称。
     *
     * @return 小写目录名称
     */
    public String getDirectoryName() {
        return directoryName;
    }

}
