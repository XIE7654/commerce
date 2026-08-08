package cn.iocoder.yudao.module.amazon.service.sellerwallet;

import cn.iocoder.yudao.module.amazon.controller.admin.sellerwallet.vo.AmazonSellerWalletReqVO;
import java.util.Map;

/** Amazon Seller Wallet 服务。 */
public interface AmazonSellerWalletService {
    /** @param request 店铺及站点参数 @return Seller Wallet 账户列表 */ Map<String, Object> listAccounts(AmazonSellerWalletReqVO request);
    /** @param request 店铺、站点及账户编号 @return Seller Wallet 账户详情 */ Map<String, Object> getAccount(AmazonSellerWalletReqVO request);
    /** @param request 店铺、站点及账户编号 @return Seller Wallet 账户余额 */ Map<String, Object> listAccountBalances(AmazonSellerWalletReqVO request);
    /** @param request 店铺、站点及换汇金额参数 @return 转账预览 */ Map<String, Object> getTransferPreview(AmazonSellerWalletReqVO request);
    /** @param request 店铺、站点及账户编号 @return 账户交易列表 */ Map<String, Object> listAccountTransactions(AmazonSellerWalletReqVO request);
    /** @param request 含签名和交易请求体的创建参数 @return 创建后的交易 */ Map<String, Object> createTransaction(AmazonSellerWalletReqVO request);
    /** @param request 店铺、站点及交易编号 @return 交易详情 */ Map<String, Object> getTransaction(AmazonSellerWalletReqVO request);
    /** @param request 店铺、站点及账户编号 @return 定时转账列表 */ Map<String, Object> listTransferSchedules(AmazonSellerWalletReqVO request);
    /** @param request 含签名和定时转账请求体的创建参数 @return 创建后的定时转账 */ Map<String, Object> createTransferSchedule(AmazonSellerWalletReqVO request);
    /** @param request 含签名和定时转账请求体的更新参数 @return 更新后的定时转账 */ Map<String, Object> updateTransferSchedule(AmazonSellerWalletReqVO request);
    /** @param request 店铺、站点及定时转账编号 @return 定时转账详情 */ Map<String, Object> getTransferSchedule(AmazonSellerWalletReqVO request);
    /** @param request 店铺、站点及定时转账编号 @return Amazon 删除响应 */ Map<String, Object> deleteTransferSchedule(AmazonSellerWalletReqVO request);
}
