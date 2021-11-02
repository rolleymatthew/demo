package com.wy.stock.etf;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.wy.bean.Contant;
import com.wy.bean.ETFBean;
import com.wy.bean.FinanceDataBean;
import com.wy.utils.DateUtil;
import com.wy.utils.OkHttpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yunwang on 2021/10/29 10:14
 */
public class ETFFundDataService {
    //ETF抓取地址
    private static String url = "http://query.sse.com.cn/commonQuery.do?" +
            "isPagination=true&pageHelp.pageSize=%s" +
            "&pageHelp.pageNo=1&pageHelp.cacheSize=1" +
            "&sqlId=COMMON_SSE_ZQPZ_ETFZL_XXPL_ETFGM_SEARCH_L&STAT_DATE=%s" +
            "&_=1606103195911";

    //一次抓取数据量
    private static final int pageSize = 500;

    //文件前缀
    public static final String FILE_PRE = "ETF";

    public static void main(String[] args) {
        int dayCount = 90;
        getETFFundData(dayCount);
    }

    public static boolean getETFFundData(int dayCount) {
        Date date = new Date();
        //输出文件命名
        String fileNameDate = "";
        //输出的数据
        List<ETFBean.PageHelpDTO.DataDTO> etfFundBeanDataDTOList = new ArrayList<>();
        for (int i = 0; i <= dayCount; i++) {
            date = DateUtil.getPreviousWorkingDay(date, -1);
            String shortDate = DateUtil.fmtShortDate(date);
            System.out.println("getETFFundData:" + shortDate);
            List<ETFBean.PageHelpDTO.DataDTO> etfFundBeanDataDTOListNext = getETFFundBean(shortDate, pageSize);

            if (CollectionUtils.isEmpty(etfFundBeanDataDTOListNext)) {
                //文件为空跳过到前一天
                i--;
                continue;
            }

            //提取后一天的数据
            if (CollectionUtils.isEmpty(etfFundBeanDataDTOList)) {
                etfFundBeanDataDTOList.addAll(etfFundBeanDataDTOListNext);
                //第一次的日期
                fileNameDate = shortDate;
                continue;
            }

            //两天的数据进行计算得出规模差值写入当天数据
            if (CollectionUtils.isNotEmpty(etfFundBeanDataDTOList) && CollectionUtils.isNotEmpty(etfFundBeanDataDTOListNext)) {
                Map<Integer, Double> totVolMap = etfFundBeanDataDTOListNext.stream().collect(Collectors.toMap(ETFBean.PageHelpDTO.DataDTO::getSecCode
                        , ETFBean.PageHelpDTO.DataDTO::getTotVol));
                etfFundBeanDataDTOList.stream().forEach(x -> {
                    //从map中找到前一天的数据相减
                    double v = 0;
                    if (totVolMap.containsKey(x.getSecCode())) {
                        v = x.getTotVol() - totVolMap.get(x.getSecCode());
                    }
                    x.setAddVol(v);
                });
            }

            //写入excle文件
            String fileName = Contant.DIR + File.separator + FILE_PRE + File.separator + FILE_PRE + fileNameDate + Contant.FILE_EXT;
            EasyExcel.write(fileName, ETFBean.PageHelpDTO.DataDTO.class)
                    .sheet(FILE_PRE + shortDate)
                    .doWrite(etfFundBeanDataDTOList);

            //更新数据
            etfFundBeanDataDTOList.clear();
            etfFundBeanDataDTOList.addAll(etfFundBeanDataDTOListNext);
            fileNameDate = shortDate;
        }
        return true;
    }

    private static List<ETFBean.PageHelpDTO.DataDTO> getETFFundBean(String shortDate, int size) {
        String urlString = String.format(url, size, shortDate);
        String etfStringByURL = getETFStringByURL(urlString);
        ETFBean etfBean = JSON.parseObject(etfStringByURL, ETFBean.class);
        if (etfBean == null) {
            System.out.println("date format json error:" + urlString);
            return null;
        }
        if (CollectionUtils.isEmpty(etfBean.getResult())) {
            System.out.println("date empty error:" + urlString);
            return null;
        }
        return etfBean.getResult().stream().sorted(Comparator.comparing(ETFBean.PageHelpDTO.DataDTO::getTotVol).reversed()).collect(Collectors.toList());
    }

    private static String getETFStringByURL(String url) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "*/*");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("Proxy-Connection", "keep-alive");
        headerMap.put("Cookie", "yfx_c_g_u_id_10000042=_ck21101414504517997211586335557; VISITED_MENU=%5B%228491%22%5D; JSESSIONID=34BC087D3D7D634FB2AEB302A405762C; yfx_f_l_v_t_10000042=f_t_1634194245788__r_t_1634194245788__v_t_1634194438289__r_c_0");
        headerMap.put("Host", "query.sse.com.cn");
        headerMap.put("Referer", "http://www.sse.com.cn/");
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36");
        String ss = "";
        try {
            ss = OkHttpUtil.doGet(url, headerMap, null);
        } catch (Exception e) {
            System.out.println("get url error:" + url);
        }
        if (StringUtils.isEmpty(ss)) {
            return null;
        }
        return ss;
    }

}
