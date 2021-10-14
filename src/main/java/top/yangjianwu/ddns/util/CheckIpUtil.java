package top.yangjianwu.ddns.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class CheckIpUtil {

    public static final Logger logger = LoggerFactory.getLogger(CheckIpUtil.class);

    public static String getIp() throws Exception{
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        try(BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()))) {
            return in.readLine();
        }
        catch (Exception e){
            logger.error("公网IP获取失败!");
            throw new Exception("公网IP获取失败!");
        }
    }
}
