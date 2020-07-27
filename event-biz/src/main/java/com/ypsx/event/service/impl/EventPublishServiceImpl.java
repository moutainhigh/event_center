package com.ypsx.event.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ypsx.event.manager.EventLogManager;
import com.ypsx.event.manager.EventManager;
import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventResult;
import com.ypsx.event.service.EventScanService;
import com.ypsx.event.sevice.EventPublishService;
import com.ypsx.util.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * 功能：服务实现方式的事件发布
 *
 * @author chuchengyi
 */
@Service(version = "1.0")
public class EventPublishServiceImpl implements EventPublishService {

    @Resource
    private EventManager eventManager;
    @Resource
    private EventLogManager eventLogManager;
    @Resource
    private EventScanService eventScanService;

    /**
     * 功能：时间间隔
     */
    private static final int TIME = 30 * 1000;

    /**
     * 功能：定义日志信息
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");

    @Override
    public Result<String> publishEvent(Event event) {
        Result<String> result = new Result<String>();
        try {
            //保存事件信息
            result = eventManager.saveEvent(event);
            //获取到首次执行的时间
            Long firstExecuteTime = event.getExecuteTime();
            //当首次执行事件为不为空的时候 判断是否需要放入时间轮中去执行
            if (firstExecuteTime != null) {
                //获取当前时间
                Long now = System.currentTimeMillis();
                Long timeTick = firstExecuteTime - now;
                //提交事件到事件轮
                if (timeTick < TIME) {
                    eventScanService.submitEvent(event);
                }
            }
        } catch (Throwable throwable) {
            result.setSuccess(false);
            result.setErrorMessage(throwable.getMessage());
            logger.error("EventPublishServiceImpl[publishEvent] is error:" + throwable.getMessage());
        }
        return result;
    }

    @Override
    public Result<Boolean> cancelEvent(Event event) {
        return null;
    }

    @Override
    public Result<Boolean> successEvent(Event event) {
        return null;
    }

    @Override
    public Result<Boolean> reportEventExecute(Event event, EventResult eventResult) {
        Result<Boolean> result = new Result<Boolean>();
        try {
            eventLogManager.saveEventLog(eventResult, event);
        } catch (Throwable throwable) {
            result.setModel(false);
            result.setSuccess(false);
            result.setErrorMessage(throwable.getMessage());
            logger.error("EventPublishServiceImpl[reportEventExecute] is error:" + throwable.getMessage());
        }
        return result;
    }


}
