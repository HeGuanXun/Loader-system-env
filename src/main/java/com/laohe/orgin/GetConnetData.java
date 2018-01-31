package com.laohe.orgin;

import com.laohe.env.SystemConfigUtils;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @Author: LaoHe
 * @Description:
 * @Date: 18:12 2018/1/31
 */
public class GetConnetData {

    /***p2p查询余额*/
    private BigDecimal LOAN_BALANCE_P2P;


    /**
     *@Author:create by LaoHe
     *@Description:去p2p查询余额
     *@param: * @param licenseNo,projectId,flag
     *@date:15:39 2018/1/26
     */
    private void checkPlatFormMoneyFromP2P(String licenseNo,Long projectId,int flag) {

        String orderNo = "checkPlatFormMoney" + System.currentTimeMillis() + projectId.toString();

        String url = SystemConfigUtils.getString("p2p_balance_url");
        Client restClient = ClientBuilder.newClient();

        restClient.property(ClientProperties.CONNECT_TIMEOUT, 1000 * 10);
        restClient.property(ClientProperties.READ_TIMEOUT, 1000 * 10);

        if (flag==1) {
            url = url + "?orderNo=" + orderNo + "&flag=" + flag + "&cardNumber=" + licenseNo;
        }else {
            url = url + "?orderNo=" + orderNo + "&flag=" + flag + "&licenseNo=" + licenseNo;
        }

        WebTarget webTarget = restClient.target(url);

        Response response = webTarget.request().get();
        Map<String, Object> dataMap = response.readEntity(Map.class);

        Object balance = dataMap.get("loanBalance");
        Object error = dataMap.get("error");

        LOAN_BALANCE_P2P = new BigDecimal(balance.toString());
    }
}
