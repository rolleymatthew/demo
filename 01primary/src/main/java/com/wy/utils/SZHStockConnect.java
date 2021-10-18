package com.wy.utils;

import com.alibaba.fastjson.JSON;
import com.wy.bean.EastMoneyBeab;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yunwang on 2021/10/18 11:31
 */
@NoArgsConstructor
@Data
public class SZHStockConnect {
    private static String url1 = "http://datacenter-web.eastmoney.com/api/data/v1/get?" +
            "sortColumns=ADD_MARKET_CAP&sortTypes=-1&pageSize=%s" +
            "&pageNumber=%s&reportName=RPT_MUTUAL_STOCK_NORTHSTA&columns=ALL&source=WEB";
    private static String url2 = "&client=WEB&filter=(TRADE_DATE%3D%27";
    private static String url3 = "%27)(INTERVAL_TYPE%3D%221%22)(MUTUAL_TYPE%3D%22003%22)";
    private static int pageSize = 500;
    public static String FORMAT_SHORT = "yyyy-MM-dd";

    public static void main(String[] args) {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_SHORT);
        Date date = new Date();
        date = DateUtil.getPreviousWorkingDay(date, -1);
        String ss = df.format(date);
        List<EastMoneyBeab.ResultDTO.DataDTO> data = getDataDTOS(ss);
        System.out.println(data.size());
    }

    public static List<EastMoneyBeab.ResultDTO.DataDTO> getDataDTOS(String ss) {
        String urlString = String.format(url1, pageSize, 1) + url2 + ss + url3;
//        System.out.println(urlString);
        List<EastMoneyBeab.ResultDTO.DataDTO> data = getResultDTO(urlString, ss);
//        for (EastMoneyBeab.ResultDTO.DataDTO datum : data) {
//            System.out.println(datum.getSecurityName());
//        }
        return data;
    }

    private static List<EastMoneyBeab.ResultDTO.DataDTO> getResultDTO(String urlString, String string) {
        EastMoneyBeab.ResultDTO result = getDto(urlString);
        if (result == null) return null;
        List<EastMoneyBeab.ResultDTO.DataDTO> ret = result.getData();
        int page = 1;
        while ((result.getCount() - pageSize * page) > 0) {
            page++;
            urlString = String.format(url1, pageSize, page) + url2 + string + url3;
//            System.out.println(urlString);
            result = getDto(urlString);
            if (result != null) {
                ret.addAll(result.getData());
            }
        }
        return ret;
    }

    private static EastMoneyBeab.ResultDTO getDto(String urlString) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "*/*");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("Proxy-Connection", "keep-alive");
        headerMap.put("Cookie", "qgqp_b_id=fd1d741819ade5de2667536e7e066065; st_inirUrl=; st_pvi=96277215775651; st_sp=2021-08-28 08:51:06; JSESSIONID=839A6354C32B3841684666E48FC5C736; _ga=GA1.2.1194978459.1634479741; _gid=GA1.2.1086517748.1634479741");
        headerMap.put("Host", "datacenter-web.eastmoney.com");
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36");
        String ss = OkHttpUtil.doGet(urlString, headerMap, null);
        EastMoneyBeab eastMoneyBeab = JSON.parseObject(ss, EastMoneyBeab.class);
        if (eastMoneyBeab == null || eastMoneyBeab.getCode() != 0) {
            return null;
        }
        EastMoneyBeab.ResultDTO result = eastMoneyBeab.getResult();
        if (result == null || result.getCount() == 0 || result.getData().isEmpty()) {
            return null;
        }
        return result;
    }

}
