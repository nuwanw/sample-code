package org.wso2.demo.disruptor.sample;

import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.util.DaemonThreadFactory;
import java.nio.ByteBuffer;

public class LongEventMain {
    public static void main(String[] args) throws Exception {
        int bufferSize = 1024;

        Disruptor<LongEvent> longEventDisruptor =
                new Disruptor<>(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);

//        longEventDisruptor.handleEventsWith((event, sequence, endOfBatch) ->
//                System.out.println(Thread.currentThread().getName() +  "Event: " + event + " endOfBatch " + endOfBatch));
        longEventDisruptor.handleEventsWith(new LongEventHandler());
        longEventDisruptor.start();


        RingBuffer<LongEvent> ringBuffer = longEventDisruptor.getRingBuffer();
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            System.out.println(Thread.currentThread().getName() + " Event # "  + l);
            bb.putLong(0, l);
            ringBuffer.publishEvent((event, sequence, buffer) -> event.set(buffer.getLong(0)), bb);
            Thread.sleep(1000);
        }
    }
}
