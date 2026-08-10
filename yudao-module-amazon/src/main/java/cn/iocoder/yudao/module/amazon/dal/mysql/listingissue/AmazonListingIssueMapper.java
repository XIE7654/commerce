package cn.iocoder.yudao.module.amazon.dal.mysql.listingissue;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingissue.AmazonListingIssueDO;
import org.apache.ibatis.annotations.Mapper;

/** Amazon Listing 问题 Mapper。 */
@Mapper
public interface AmazonListingIssueMapper extends BaseMapperX<AmazonListingIssueDO> {

    /**
     * 按站点记录、问题代码、严重程度和说明查询，用于同步时幂等写入。
     *
     * @param listingMarketplaceId Listing 站点记录编号
     * @param issueCode Amazon 问题代码
     * @param severity 问题严重程度
     * @param message 问题说明
     * @return 问题记录；不存在时返回 {@code null}
     */
    default AmazonListingIssueDO selectByUniqueFields(Long listingMarketplaceId, String issueCode, String severity,
                                                       String message) {
        // BaseMapperX 的 selectOne 重载最多支持三组条件，四个唯一字段需使用查询构造器组合。
        return selectOne(new LambdaQueryWrapperX<AmazonListingIssueDO>()
                .eq(AmazonListingIssueDO::getListingMarketplaceId, listingMarketplaceId)
                .eq(AmazonListingIssueDO::getIssueCode, issueCode)
                .eq(AmazonListingIssueDO::getSeverity, severity)
                .eq(AmazonListingIssueDO::getMessage, message));
    }
}
