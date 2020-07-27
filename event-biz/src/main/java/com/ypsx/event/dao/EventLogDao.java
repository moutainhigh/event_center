package com.ypsx.event.dao;

import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventLog;
import com.ypsx.event.model.EventLogQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 功能：事件执行日志的接口
 *
 * @author chuchengyi
 */
@Repository
public interface EventLogDao {

    /**
     * 功能：插入事件执行日志信息
     *
     * @param eventLog
     * @return
     */
    public int insertEventLog(EventLog eventLog);


    /**
     * 功能：根据条件查询事件信息
     *
     * @param query
     * @return
     */
    public List<EventLog> listEventLog(EventLogQuery query);

    /**
     * 功能：统计数据的条数
     *
     * @param query
     * @return
     */
    public int countEventLog(EventLogQuery query);
}
