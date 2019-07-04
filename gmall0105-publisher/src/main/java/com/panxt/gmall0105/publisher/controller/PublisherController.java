package com.panxt.gmall0105.publisher.controller;

import com.alibaba.fastjson.JSON;
import com.panxt.gmall0105.publisher.service.PublisherService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author panxt
 * @create 2019-06-28 8:50
 */
@RestController
public class PublisherController {

    @Autowired
    PublisherService publisherService;

    @GetMapping("realtime-total")
    public String getRealtimeTotal(@RequestParam("date") String date){
        final Long dauTotal = publisherService.getDauTotal(date);

        List  totalList = new ArrayList();

        Map dauMap = new HashMap();
        dauMap.put("id","dau");
        dauMap.put("name","新增日活");
        dauMap.put("value", dauTotal );
        totalList.add(dauMap);

        Map newMidMap=new HashMap();
        newMidMap.put("id","newMid");
        newMidMap.put("name","新增设备");
        newMidMap.put("value", 233 );
        totalList.add(newMidMap);


        HashMap orderAmountMap = new HashMap();
        orderAmountMap.put("id","order_amount");
        orderAmountMap.put("name","新增交易额");
        Double orderAmount = publisherService.getOrderAmount(date);
        orderAmountMap.put("value",orderAmount);
        totalList.add(orderAmountMap);


        return   JSON.toJSONString(totalList) ;

    }

    @GetMapping("realtime-hour")
    public String getRealtimeHour(@RequestParam("id")String id ,@RequestParam("date") String tdate){
        if("dau".equals(id)){
            Map hourMap=new HashMap();
            Map dauHourTMap = publisherService.getDauHour(tdate);
            String ydate = getYdate(tdate);
            Map dauHourYMap = publisherService.getDauHour(ydate);
            hourMap.put("yesterday",dauHourYMap);
            hourMap.put("today",dauHourTMap);
            return JSON.toJSONString(hourMap);
        }else if("order_amount".equals(id)){
            Map hourMap=new HashMap();
            Map orderHourTMap = publisherService.getOrderAmountHour(tdate);
            String ydate = getYdate(tdate);
            Map orderHourYMap = publisherService.getOrderAmountHour(ydate);
            hourMap.put("yesterday",orderHourYMap);
            hourMap.put("today",orderHourTMap);
            return JSON.toJSONString(hourMap);

        }
        return  null;
    }

    public String getYdate(String tDateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ydateStr=null;
        try {
            Date tdate = simpleDateFormat.parse(tDateStr);
            Date ydate = DateUtils.addDays(tdate, -1);
            ydateStr=simpleDateFormat.format(ydate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ydateStr;
    }
}
