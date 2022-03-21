package com.wy.stock.kline;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonParser;
import com.wy.bean.ConstantBean;
import com.wy.stock.finance.FinanceCommonService;
import com.wy.utils.ClassUtil;
import com.wy.utils.OkHttpUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yunwang
 * @Date 2022-03-20
 */
public class KLineSpider {
    //    private static String url="http://push2his.eastmoney.com/api/qt/stock/kline/get?fields1=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13&fields2=f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61&beg=0&end=20500101&ut=fa5fd1943c7b386f172d6893dbfba10b&rtntype=6&secid=1.%s&klt=102&fqt=%s";
    private static String url = "http://push2his.eastmoney.com/api/qt/stock/kline/get?fields1=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13&fields2=f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61&beg=0&end=20500101&ut=fa5fd1943c7b386f172d6893dbfba10b&rtntype=6&secid=%s.%s&klt=101&fqt=%s";

    public static void main(String[] args) {
        KLineDataEntity kLineDataEntity = getkLineDataEntity("1", "601318", "0");
        System.out.println(kLineDataEntity.toString());
    }

    public static KLineDataEntity getkLineDataEntity(String exchange, String code, String type) {
        String kLineUrl = String.format(url, exchange, code, type);
        String s = "";
        try {
            s = OkHttpUtil.doGet(kLineUrl);
        } catch (Exception e) {
        }
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        KLineBean kLineBean = JSON.parseObject(s, KLineBean.class);
        KLineDataEntity kLineDataEntity = KLineDataEntity.builder()
                .code(kLineBean.getData().getCode())
                .klines(getKLine(kLineBean.getData().getKlines()))
                .build();
        return kLineDataEntity;
    }

    private static List<KLineEntity> getKLine(List<String> klines) {
        Map<String, String> kLineMap = FinanceCommonService.convertDicMap(ConstantBean.KLine);
        List<KLineEntity> collect = klines.stream().map(
                k -> {
                    KLineEntity kLineEntity = new KLineEntity();
                    String[] split = k.split(",");
                    for (int i = 0; i < split.length; i++) {
                        String s = split[i];
                        String s1 = kLineMap.get(String.valueOf(i + 1));
                        ClassUtil.setFieldValueByFieldName(kLineEntity, s1, s);
                    }
                    return kLineEntity;
                }
        ).sorted(Comparator.comparing(KLineEntity::getDate).reversed()).collect(Collectors.toList());
        return collect;
    }
}
