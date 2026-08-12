package cn.iocoder.yudao.module.temu.framework.client.product;

import lombok.Data;

/** bg.local.goods.cats.get 请求参数。 */
@Data
public class CatsGetRequest {

    /** 分类名称的语言；未设置时由 Temu 使用默认语言。 */
    private String language;

    /** 父分类 ID；未设置时查询一级分类。 */
    private Long parentCatId;
}
