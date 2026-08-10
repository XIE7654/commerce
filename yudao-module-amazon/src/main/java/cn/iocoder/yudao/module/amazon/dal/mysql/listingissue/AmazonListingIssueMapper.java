package cn.iocoder.yudao.module.amazon.dal.mysql.listingissue;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
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
        return selectOne(AmazonListingIssueDO::getListingMarketplaceId, listingMarketplaceId,
                AmazonListingIssueDO::getIssueCode, issueCode, AmazonListingIssueDO::getSeverity, severity,
                AmazonListingIssueDO::getMessage, message);
    }
}
