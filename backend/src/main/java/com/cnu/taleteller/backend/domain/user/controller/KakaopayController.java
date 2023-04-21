package com.cnu.taleteller.backend.domain.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class KakaopayController {
    @Autowired
    @RequestMapping("/kakaopay")
    @ResponseBody
    public String kakaopay() {

        try {
            URL url = new URL("https://kapi.kakao.com/v1/payment/ready");
            HttpURLConnection sc = (HttpURLConnection) url.openConnection();
            sc.setRequestMethod("POST");
            sc.setRequestProperty("Authorization","KakaoAK be1292fe91dbda03131d7b87113001ce");
            sc.setRequestProperty("Content-type","application/x-www-form-urlencoded;charset=utf-8");
            sc.setDoOutput(true);
            String reqParameter = "cid=TC0ONETIME&partner_order_id=partner_order_id&partner_user_id=partner_user_id&item_name=초코파이&quantity=1&total_amount=2200&vat_amount=200&tax_free_amount=0&approval_url=http://localhost:8080/&fail_url=http://localhost:8080/&cancel_url=http://localhost:8080/";
            OutputStream give = sc.getOutputStream();
            DataOutputStream dataToss = new DataOutputStream(give);
            dataToss.writeBytes(reqParameter);
            dataToss.close();

            int result = sc.getResponseCode();

            InputStream receive;
            if(result==200){
                receive = sc.getInputStream();
            }else {
                receive = sc.getErrorStream();
            }
            InputStreamReader reader = new InputStreamReader(receive);
            BufferedReader typeC = new BufferedReader(reader);
            return typeC.readLine();
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "{\"result\":\"NO\"}";
    }
}
