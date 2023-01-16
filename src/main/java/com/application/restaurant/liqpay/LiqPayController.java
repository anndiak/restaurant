package com.application.restaurant.liqpay;

import com.application.restaurant.dao.RequestRepository;
import com.application.restaurant.model.Request;
import com.liqpay.LiqPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RestController
public class LiqPayController {
    private final String PUBLIC_KEY = "sandbox_i52897702628";
    private final String PRIVATE_KEY = "sandbox_NPySTQCAZy1b1X0hYJbCqxDDDT3DZLh8drrgzdN3";

    @Autowired
    private RequestRepository requestRepository;

    @GetMapping("/pay")
    public String redirectToPayButton(@RequestParam("request_id") String request_id){
        Request request = requestRepository.getRequestById(request_id);
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(2).plusMinutes(20);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        HashMap<String, String> params = new HashMap<>();
        params.put("action", "pay");
        params.put("amount", String.valueOf(request.getOrder().getTotalPrice()));
        params.put("currency", "UAH");
        params.put("description", "Payment for request with id:" + request.getId());
        params.put("order_id", request.getId());
        params.put("version", "3");
        params.put("expired_date", formatter.format(localDateTime));
        params.put("language", "en");
        LiqPay liqpay = new LiqPay(PUBLIC_KEY, PRIVATE_KEY);
        return liqpay.cnb_form(params);
    }

    public String getPaymentStatus(String id) throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("action", "status");
        params.put("version", "3");
        params.put("order_id", id);

        LiqPay liqpay = new LiqPay(PUBLIC_KEY, PRIVATE_KEY);
        HashMap<String, Object> res = (HashMap<String, Object>) liqpay.api("request", params);
        return res.get("status").toString();
    }
}
