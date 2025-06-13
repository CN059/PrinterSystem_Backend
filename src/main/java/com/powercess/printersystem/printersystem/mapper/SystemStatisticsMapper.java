package com.powercess.printersystem.printersystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemStatisticsMapper {
    void incrementDailyStats(@Param("date") java.time.LocalDate date,
                             @Param("pages") int pages,
                             @Param("income") int income);
}