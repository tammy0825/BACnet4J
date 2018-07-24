/**
 * Copyright (C) 2018 Infinite Automation Software. All rights reserved.
 */
package com.serotonin.bacnet4j.npdu.mstp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.serotonin.bacnet4j.npdu.mstp.realtime.RealtimeDriver;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.util.sero.StreamUtils;


/**
 * MS/TP Master node using a real-time serial driver 
 *   installed in the linux Kernel.
 * 
 * @author Terry Packer
 */
public class RealtimeMasterNode extends MasterNode {

    private final byte thisStation;
    private final RealtimeDriver driver;
    private final int baud;
    private int responseTimeoutMs = 1000;
    private long lastFrameSendTime; //Track response timeouts
    
    public RealtimeMasterNode(final String portId, final File driver, final File configProgram, final byte thisStation, 
            final int retryCount, final int baud, int responseTimeoutMs) throws IllegalArgumentException {
        super(portId, null, null, (byte)0xFF, retryCount);
        this.thisStation = thisStation;
        this.baud = baud;
        this.driver = new RealtimeDriver(driver, configProgram);
        this.responseTimeoutMs = responseTimeoutMs;
    }
    
    @Override
    protected void validate(final int retryCount) {
        this.retryCount = retryCount;
        nextStation = thisStation;
        pollStation = thisStation;
        tokenCount = Constants.POLL;
        soleMaster = false;
        state = MasterNodeState.idle;
    }
    
    @Override
    public void setMaxMaster(final int maxMaster) {
        super.setMaxMaster(maxMaster);
    }

    @Override
    public void setMaxInfoFrames(final int maxInfoFrames) {
        super.setMaxInfoFrames(maxInfoFrames);
    }

    @Override
    public void setUsageTimeout(final int usageTimeout) {
        super.setUsageTimeout(usageTimeout);
    }
    
    public void setResponseTimeoutMs(int responseTimeoutMs) {
        this.responseTimeoutMs = responseTimeoutMs;
    }
    
    @Override
    public void initialize(final Transport transport) throws Exception {
        //Setup I/O
        File file = new File(portId);
        in = new FileInputStream(file);
        out = new FileOutputStream(file);

        //Configure Driver
        this.driver.configure(portId, baud, thisStation, maxMaster, maxInfoFrames, usageTimeout);
        super.initialize(transport);
    }
    
    @Override
    protected void doCycle() {
        readFrame();
        
        if (state == MasterNodeState.idle)
            idle();
        
        if (state == MasterNodeState.useToken)
            useToken();
        
        if(state == MasterNodeState.doneWithToken)
            state = MasterNodeState.idle;
        
        if (state == MasterNodeState.waitForReply)
            waitForReply();
        
        //TODO Can't currently get to this state since we don't have 
        // the frame type from the driver so we have to do this every time
        //if (state == MasterNodeState.answerDataRequest)
        answerDataRequest();

        //TODO Remove when done debugging
        //try {Thread.sleep(500);}catch(InterruptedException e) {}
    }

    /* (non-Javadoc)
     * @see com.serotonin.bacnet4j.npdu.mstp.MstpNode#readFrame()
     */
    @Override
    protected void readFrame() {
        readInputStream();
        if (receiveError) {
            // EatAnError
            receiveError = false;
            eventCount++;
            activity = true;
        }
    }
    
    @Override
    protected void readInputStream() {
        try {
            //Read 1 message from the driver
            LOG.info("About to read.");
            while((readCount = in.read(readArray)) > 0) {
                
            //readCount = in.read(readArray);
            LOG.info("Read: " + StreamUtils.dumpArrayHex(readArray, 0, readCount));
            //if(readCount > 0) {
                bytesIn += readCount;
                if (LOG.isTraceEnabled())
                    LOG.trace(tracePrefix() + "in: " + StreamUtils.dumpArrayHex(readArray, 0, readCount));
                
                int pos = 0;
                //frame.setFrameType(FrameType.bacnetDataNotExpectingReply);
                frame.setSourceAddress(readArray[pos++]);
                byte[] data = new byte[readCount - 1];
                for(int i=0; i<readCount - 1; i++) {
                    data[i] = readArray[pos++];
                }
                frame.setData(data);
                if (LOG.isTraceEnabled())
                    LOG.trace("in: " + frame);
                eventCount += readCount;
                receivedValidFrame = true;
            }
        } catch (final IOException e) {
            if (StringUtils.equals(e.getMessage(), "Stream closed."))
                throw new RuntimeException(e);
            if (LOG.isDebugEnabled())
                LOG.debug(thisStation + " Input stream listener exception", e);
            receiveError = true;
        }
    }
    
    @Override
    protected void idle() {
        //Don't worry about invalid frames, assume we can use token if we didn't get a frame
        if (receivedValidFrame) {
            if (LOG.isDebugEnabled())
                LOG.debug(thisStation + " idle:receivedValidFrame");
            frame();
            receivedValidFrame = false;
            activity = true;
        }else {
            //We can use the token
            state = MasterNodeState.useToken;
        }
    }
    
    /* (non-Javadoc)
     * @see com.serotonin.bacnet4j.npdu.mstp.MasterNode#frame()
     */
    @Override
    protected void frame() {
        receivedDataNoReply(frame);
        
        //TODO How to decide?  via NPDU or do we modify the driver
        //state = MasterNodeState.answerDataRequest;
        //replyDeadline = lastNonSilence + Constants.REPLY_DELAY;
    }
    
    @Override
    protected void waitForReply() {
        if(lastFrameSendTime + responseTimeoutMs > clock.millis()) {
            if (LOG.isDebugEnabled())
                LOG.debug(thisStation + " waitForReply:ReplyTimeout");
            state = MasterNodeState.idle;
        }else if (receivedValidFrame) {
            if (LOG.isDebugEnabled())
                LOG.debug(thisStation + " waitForReply:ReceivedReply");
            receivedDataNoReply(frame);
            state = MasterNodeState.idle;
            receivedValidFrame = false;
        }
    }
    
    /* (non-Javadoc)
     * @see com.serotonin.bacnet4j.npdu.mstp.MasterNode#answerDataRequest()
     */
    @Override
    protected void answerDataRequest() {
        synchronized (this) {
            if (replyFrame != null) {
                // Reply
                if (LOG.isDebugEnabled())
                    LOG.debug(thisStation + " answerDataRequest:Reply");
                sendFrame(replyFrame);
                replyFrame = null;
                state = MasterNodeState.idle;
                activity = true;
            }
        }
    }
    
    
    @Override
    protected void sendFrame(final Frame frame) {
        LOG.info("Sending frame: " + frame);
        try {
            if (LOG.isTraceEnabled())
                LOG.trace(tracePrefix() + "out: " + frame);
            
            // Header
            byte[] writeArray = new byte[5 + frame.getLength()];
            int pos = 0;
            writeArray[pos++] = frame.getFrameType().id;
            writeArray[pos++] = frame.getDestinationAddress();
            writeArray[pos++] = frame.getSourceAddress();
            writeArray[pos++] = (byte)(frame.getLength() >> 8 & 0xff);
            writeArray[pos++] = (byte)(frame.getLength() & 0xff);
            
           // out.write(frame.getFrameType().id & 0xff);
           // out.write(frame.getDestinationAddress() & 0xff);
           // out.write(frame.getSourceAddress() & 0xff);
           // out.write(frame.getLength() >> 8 & 0xff);
           // out.write(frame.getLength() & 0xff);
            //Driver doesn't want CRC out.write(sendHeaderCRC.getCrc(frame));
            bytesOut += 8;

            if (frame.getLength() > 0) {
                // Data
                //out.write(frame.getData());
                for(int i=0; i<frame.getLength(); i++)
                    writeArray[pos++] = frame.getData()[i];
                //No CRC with Driver
                //final int crc = sendDataCRC.getCrc(frame);
                //out.write(crc & 0xff);
                //out.write(crc >> 8 & 0xff);
                bytesOut += frame.getLength(); // + 2;
            }
            out.write(writeArray);
            out.flush();
            lastFrameSendTime = clock.millis();
            LOG.info("Sent frame " + frame);
        } catch (final IOException e) {
            // Only write the same error message once. Prevents logs from getting filled up unnecessarily with repeated
            // error messages.
            if (!StringUtils.equals(e.getMessage(), lastWriteError)) {
                // NOTE: should anything else be informed of this?
                LOG.error("Error while sending frame", e);
                lastWriteError = e.getMessage();
            }
        }
    }
    
    @Override
    public void terminate() {
        super.terminate();
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            LOG.error("Error closing streams.", e);
        }
    }
}
