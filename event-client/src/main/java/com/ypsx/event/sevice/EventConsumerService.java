package com.ypsx.event.sevice;

import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventResult;

/**
 * 功能：消费服务信息
 *
 * @author chuchengyi
 */
public interface EventConsumerService  extends ConsumerService{


    /**
     * 功能：小费事件消息
     *
     * @param event
     * @return
     */
    public EventResult consumerEvent(Event event);
}
