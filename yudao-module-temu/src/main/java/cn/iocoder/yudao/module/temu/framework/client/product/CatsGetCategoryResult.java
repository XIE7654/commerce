package cn.iocoder.yudao.module.temu.framework.client.product;

import lombok.Data;

/** Temu 商品分类信息。 */
@Data
public class CatsGetCategoryResult {

    /** 分类 ID。 */
    private Long catId;
    /** 分类可用状态。 */
    private Integer availableStatus;
    /** 分类层级。 */
    private Integer level;
    /** 分类名称。 */
    private String catName;
    /** 是否为二手商品分类。 */
    private Boolean secondHandCategory;
    /** 分类类型。 */
    private Integer catType;
    /** 是否为叶子分类。 */
    private Boolean leaf;
    /** 父分类 ID。 */
    private Long parentId;
    /** 扩展分类类型。 */
    private Integer expandCatType;
}
