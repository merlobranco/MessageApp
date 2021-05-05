/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlobranco.view;

import com.merlobranco.ejb.MessageRemote;
import com.merlobranco.ejb.TestRemote;
import com.merlobranco.entity.Message;
import com.myPower24.ejbLib.entity.LoggerMyPower;
import com.myPower24.ejbLib.logerRemote.LoggerMyPowerRemote;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author brais
 */
@Named(value="testView")
@ViewScoped
public class TestView implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = Logger.getLogger(TestView.class.getName());
    
//    @EJB
    @EJB(name="TestRemote")
    TestRemote testRemote;
    
//    @EJB
    @EJB(name="MessageRemote")
    MessageRemote messageRemote;
    
    @EJB(name="LoggerMyPowerRemote")
    LoggerMyPowerRemote loggerMyPowerRemote;
    
    @Named("testSMS") 
    @Produces
    private String testSMS;
    
    @Named("sms") 
    @Produces
    private String sms;
    
    @Named("messages")
    @Produces
    private List<Message> messages;
    
    @Named("loggers")
    @Produces
    private List<LoggerMyPower> loggers;
    
    @Named("counter") 
    @Produces
    private Integer counter;
    
    @Named("time") 
    @Produces
    private Long time;

    public List<LoggerMyPower> getLoggers() {
        return loggers;
    }

    @PostConstruct
    public void init() {
        counter = 0;
        time = 0l;
        testSMS = "";
        sms = "";
        messages = messageRemote.findOrdered();
    }
    
    public void testEJB() {
        testSMS = testRemote.method();
    }
    
    public void trySometing(){
        loggers = loggerMyPowerRemote.findAll();
    }
    
    public void createMessage() {
        if (sms == null || sms.trim().isEmpty())
            return;
        try {
            Message message = new Message(sms);
            messageRemote.create(message);
            LOG.log(Level.INFO, "Sending: {0}", message);
            messages = messageRemote.findOrdered();
            sms = "";
        }
        catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    public void runSpeedTest() {
        Long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            counter = testRemote.next();
        }
        time = System.nanoTime() - start;
        LOG.log(Level.INFO, "Time: {0}", time);
    }
    
    public String getTestSMS() {
        return testSMS;
    }

    public void setTestSMS(String testSMS) {
        this.testSMS = testSMS;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }
    
    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public List<Message> getMessages() {
        return messages;
    }  
}
