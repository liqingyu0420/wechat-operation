package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.FansActionStat;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author wang xiao
 * @date Created in 11:33 2020/9/16
 */
public interface FansActionStatService extends IService<FansActionStat> {

    /**
     * 统计区间内 粉丝活跃数量 (秒级时间戳)
     *
     * @param accountId accountId
     * @param startDate startDate
     * @param endDate endDate
     * @return int
     * @author wangxiao
     * @date 11:39 2020/9/16
     */
    int countInactiveFansNum(String accountId, long startDate, long endDate);


    /**
     * 查询区间内 粉丝活跃数量 (秒级时间戳)
     *
     * @param accountId accountId
     * @param startDate startDate
     * @param endDate endDate
     * @return int
     * @author wangxiao
     * @date 11:39 2020/9/16
     */
    List<FansActionStat> queryFansActionStat(String accountId, String startDate, String endDate);


    /**
     * 查询区间内 粉丝活跃数量 (秒级时间戳)
     * @param accountId accountId
     * @param startDate startDate
     * @param endDate endDate
     * @return int
     * @author wangxiao
     * @date 11:39 2020/9/16
     */
    List<FansActionStat> queryFansActionStat(String accountId, LocalDateTime startDate, LocalDateTime endDate);



    /**
     * 查询区间内 粉丝活跃数量 (秒级时间戳)
     * @param accountId accountId
     * @param start start
     * @param end end
     * @return int
     * @author wangxiao
     * @date 11:39 2020/9/16
     */
    List<FansActionStat> queryFansActionStat(String accountId, long start, long end);

    /**
     *  保存多条
     * @author wangxiao
     * @date 18:47 2020/9/24
     * @param params params
     * @return boolean
     */
    boolean saveBatchFansActionStat (FansActionStat ...params);
}
