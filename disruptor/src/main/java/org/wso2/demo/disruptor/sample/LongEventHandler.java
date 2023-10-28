package org.wso2.demo.disruptor.sample;

import com.lmax.disruptor.EventHandler;

public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println(Thread.currentThread().getName() +  " Event: " + event + " endOfBatch " + endOfBatch);
    }
}