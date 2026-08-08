package cn.iocoder.yudao.module.amazon.dal.mysql.apilog;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.apilog.AmazonApiRequestLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Amazon SP-API 请求调用日志 Mapper。
 */
@Mapper
public interface AmazonApiRequestLogMapper extends BaseMapperX<AmazonApiRequestLogDO> {
}
