// com/campus/mapper/AttendanceMapper.java
package com.campus.mapper;

import com.campus.entity.AttendanceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface AttendanceMapper {

    // 添加考勤记录
    int insert(AttendanceRecord record);

    // 根据学生ID查询考勤记录
    List<AttendanceRecord> selectByStudentId(@Param("studentId") String studentId);

    // 根据日期查询考勤记录
    List<AttendanceRecord> selectByDate(@Param("date") Date date);

    // 根据学生ID和日期查询
    AttendanceRecord selectByStudentAndDate(@Param("studentId") String studentId,
                                            @Param("date") Date date);

    // 更新考勤记录（补签）
    int update(AttendanceRecord record);

    // 删除考勤记录
    int delete(Integer id);

    // 统计考勤情况
    List<Map<String, Object>> getAttendanceStatistics(@Param("startDate") Date startDate,
                                                      @Param("endDate") Date endDate);

    // 查询某个学生的考勤统计
    Map<String, Object> getStudentAttendanceSummary(@Param("studentId") String studentId);
}