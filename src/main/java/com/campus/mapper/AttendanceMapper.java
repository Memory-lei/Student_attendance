package com.campus.mapper;

import com.campus.entity.AttendanceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface AttendanceMapper {

    // 基础CRUD操作
    int insert(AttendanceRecord record);
    int update(AttendanceRecord record);
    int delete(@Param("id") Integer id);

    // 查询方法
    AttendanceRecord selectById(@Param("id") Integer id);
    List<AttendanceRecord> selectByStudentId(@Param("studentId") Integer studentId);
    List<AttendanceRecord> selectByDate(@Param("date") Date date);
    AttendanceRecord selectByStudentAndDate(@Param("studentId") Integer studentId,
                                            @Param("date") Date date);

    // 批量操作（新增）
    int batchInsert(@Param("records") List<AttendanceRecord> records);

    // 统计查询
    List<Map<String, Object>> getAttendanceStatistics(@Param("startDate") Date startDate,
                                                      @Param("endDate") Date endDate);
    Map<String, Object> getStudentAttendanceSummary(@Param("studentId") Integer studentId);

    // 分页查询（新增）
    List<AttendanceRecord> selectByCondition(@Param("record") AttendanceRecord condition,
                                             @Param("offset") Integer offset,
                                             @Param("limit") Integer limit);
}