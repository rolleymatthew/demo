package com.wy.service.impl;

import com.wy.bean.ResultVO;
import com.wy.service.StockService;
import com.wy.stock.etf.ETFFundDataService;
import com.wy.stock.hszh.GetSHSZHKStockDateService;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yunwang on 2021/11/2 10:11
 */
@Service
public class StockServiceImpl implements StockService {

    private static AtomicInteger flag =new AtomicInteger(0);

    @Override
    public ResultVO hsshDataByDay(int dayCount) {
        if (flag.incrementAndGet()==1){
            GetSHSZHKStockDateService.getMutilSheet(dayCount);
            flag.decrementAndGet();
        }else {
            return ResultVO.build(-1,"已经在运行抓取");
        }
        return ResultVO.ok();
    }

    @Override
    public ResultVO ETFDataByDay(int dayCount) {
        if (flag.incrementAndGet()==1){
            ETFFundDataService.getETFFundData(dayCount);
            flag.decrementAndGet();
        }else {
            return ResultVO.build(-1,"已经在运行抓取");
        }
        return ResultVO.ok();
    }

    @Override
    public ResultVO hsshAndETFDataByDay(int dayCount) {
        if (flag.incrementAndGet()==1){
            ETFFundDataService.getETFFundData(dayCount);
            GetSHSZHKStockDateService.getMutilSheet(dayCount);
            flag.decrementAndGet();
        }else {
            return ResultVO.build(-1,"已经在运行抓取");
        }
        return ResultVO.ok();
    }
}
