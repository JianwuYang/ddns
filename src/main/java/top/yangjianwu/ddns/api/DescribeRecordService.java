package top.yangjianwu.ddns.api;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;
import com.tencentcloudapi.dnspod.v20210323.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yangjianwu.ddns.common.DDNSProperties;

import static top.yangjianwu.ddns.common.Constant.*;

@Service
public class DescribeRecordService implements InitializingBean {

    public static final Logger logger = LoggerFactory.getLogger(DescribeRecordService.class);

    public DnspodClient client;

    @Autowired
    private DDNSProperties properties;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(properties.getSecretId()
                , properties.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(URL);
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        this.client =  new DnspodClient(cred, "", clientProfile);
    }

    public DescribeRecordResponse describeRecord() {
        try{
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DescribeRecordRequest req = new DescribeRecordRequest();
            req.setDomain(properties.getDomain());
            req.setRecordId(properties.getRecordId());
            // 返回的resp是一个DescribeRecordResponse的实例，与请求对象对应
            return client.DescribeRecord(req);
        } catch (TencentCloudSDKException e) {
            logger.error("解析记录查询失败！", e);
            return null;
        }
    }

    public ModifyRecordResponse modifyRecord(String ip) {
        try {
            // 实例化一个请求对象,每个接口都会对应一个request对象
            ModifyRecordRequest req = new ModifyRecordRequest();
            req.setDomain(properties.getDomain());
            req.setRecordType("A");
            req.setRecordLine("默认");
            req.setValue(ip);
            req.setRecordId(properties.getRecordId());
            // 返回的resp是一个ModifyRecordResponse的实例，与请求对象对应
            return client.ModifyRecord(req);
        } catch (TencentCloudSDKException e) {
            logger.error("解析记录修改失败！", e);
            return null;
        }
    }

}