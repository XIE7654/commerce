package cn.iocoder.yudao.module.temu.sdk.product;

import lombok.Data;

/** bg.local.goods.cats.get 请求参数。 */
@Data
public class CatsGetReqVO {

    /** 分类名称语言；不传时由 Temu 使用默认语言。 */
    private String language;

    /** 父分类 ID；不传时查询一级分类。 */
    private Long parentCatId;
}
