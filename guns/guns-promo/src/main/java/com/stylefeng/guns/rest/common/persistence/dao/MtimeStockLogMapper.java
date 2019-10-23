package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhou
 * @since 2019-10-21
 */
public interface MtimeStockLogMapper extends BaseMapper<MtimeStockLog> {

    int insertLog(MtimeStockLog mtimeStockLog);

    int updateLogStatus(@Param("uuid") String promoLogId,
                         @Param("status") int status);
}
