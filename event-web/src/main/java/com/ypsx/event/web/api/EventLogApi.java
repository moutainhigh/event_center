package com.ypsx.event.web.api;

import com.ypsx.event.error.ExceptionUtil;
import com.ypsx.event.manager.EventLogManager;
import com.ypsx.event.model.EventLog;
import com.ypsx.event.model.EventLogQuery;
import com.ypsx.event.model.EventQuery;
import com.ypsx.event.web.request.EventLogQueryRequest;
import com.ypsx.event.web.vo.EventLogVO;
import com.ypsx.event.web.vo.EventTypeVO;
import com.ypsx.util.model.ExceptionInfo;
import com.ypsx.util.model.PageResult;
import com.ypsx.util.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dozer.DozerBeanMapperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chuchengyi
 */
@Controller
@RequestMapping(value = "eventLog")
@Api(description = "事件日志接口", tags = "事件日志管理")
public class EventLogApi {

    @Resource
    private EventLogManager eventLogManager;

    private final static Logger logger = LoggerFactory.getLogger("eventLog");

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result<Boolean> add(@RequestBody String bizId) throws Exception {
        Result<Boolean> result = new Result<>();
        try {
            EventLog eventLog = new EventLog();
            eventLog.setAppCode("test");
            eventLog.setEventType("2222");
            eventLog.setBizId(bizId);
            eventLog.setErrorMessage("1111");
            eventLog.setExecuteIp("127.0.0.1");
            eventLog.setSuccess(0);
            eventLog.setExecuteIndex(0);
            eventLog.setExecuteTime(new Date());
            result = eventLogManager.saveEventLog(eventLog);
            result.setSuccess(true);
        } catch (Throwable throwable) {
            ExceptionInfo exceptionInfo = ExceptionUtil.genSystemError(throwable);
            result.setException(exceptionInfo);
            logger.error("EventLogApi[add] is error :" + throwable.getMessage());
        }
        return result;

    }


    @ResponseBody
    @ApiOperation("查询事件执行日志")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public PageResult<List<EventLogVO>> list(@RequestBody EventLogQueryRequest queryRequest) throws Exception {
        PageResult<List<EventLogVO>> result = new PageResult<>();
        try {
            //构造线程对象
            EventLogQuery query = DozerBeanMapperBuilder.buildDefault().map(queryRequest, EventLogQuery.class);
            //执行查询
            Result<Integer> countResult = eventLogManager.countEventLog(query);
            //查询成功
            if (!countResult.isSuccess()) {
                result.setException(countResult.getExceptionInfo());
                return result;
            }
            result.setTotal(countResult.getModel());
            List<EventLogVO> list = new ArrayList<>();
            Result<List<EventLog>> queryResult = eventLogManager.listEventLog(query);
            if (queryResult.isSuccess()) {
                List<EventLog> dataList = queryResult.getModel();
                for (EventLog eventLog : dataList) {
                    EventLogVO eventTypeVO = DozerBeanMapperBuilder.buildDefault().map(eventLog, EventLogVO.class);
                    list.add(eventTypeVO);
                }
                result.setModel(list);
            } else {
                result.setException(queryResult.getExceptionInfo());
            }
            result.setSuccess(true);
        } catch (Throwable throwable) {
            ExceptionInfo exceptionInfo = ExceptionUtil.genSystemError(throwable);
            result.setException(exceptionInfo);
            logger.error("EventLogApi[list] is error :" + throwable.getMessage());
        }
        return result;
    }


}
