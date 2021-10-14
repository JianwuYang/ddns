package top.yangjianwu.ddns.task;

import com.tencentcloudapi.dnspod.v20210323.models.DescribeRecordResponse;
import com.tencentcloudapi.dnspod.v20210323.models.ModifyRecordResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.yangjianwu.ddns.api.DescribeRecordService;
import top.yangjianwu.ddns.util.CheckIpUtil;

@Component
public class DDNSTask {

    private static final Logger log = LoggerFactory.getLogger(DDNSTask.class);

    private String cacheValue = "";

    @Autowired
    private DescribeRecordService describeRecordService;

    @Scheduled(fixedRate = 600000)
    public void ddns() {
        try{
            String ip = CheckIpUtil.getIp();
            // 如果缓存值与当前值相同，直接返回无需远程查询
            if(ip.equals(cacheValue)){
                log.info("============ip同缓存无变化============");
                return;
            }
            DescribeRecordResponse describeRecordResponse = describeRecordService.describeRecord();
            if (describeRecordResponse == null || describeRecordResponse.getRecordInfo() == null){
                log.error("============获取解析记录失败============");
                throw new Exception("获取解析记录失败");
            }

            // 旧的解析值
            String oldValue = describeRecordResponse.getRecordInfo().getValue();

            if(oldValue.equals(ip)) {
                cacheValue = ip;
                log.info("============ip同远程无变化============");
            }
            else {
                log.info("============ip由{}变化为{}============", oldValue, ip);
                ModifyRecordResponse modifyRecordResponse = describeRecordService.modifyRecord(ip);
                if(modifyRecordResponse == null || modifyRecordResponse.getRecordId() == null){
                    log.error("============更新解析记录失败============");
                    throw new Exception("更新解析记录失败");
                }
                cacheValue = ip;
            }
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
