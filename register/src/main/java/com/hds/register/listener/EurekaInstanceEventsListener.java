package com.hds.register.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaRegistryAvailableEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.InstanceInfo;

import cn.junety.alarm.api.AlarmClient;

@Component
public class EurekaInstanceEventsListener {
	
	private final static Logger logger = LoggerFactory.getLogger(EurekaInstanceEventsListener.class);
	
	//@Autowired
    //private JavaMailSender jms;

	@EventListener(condition = "#event.replication==false")
	public void listen(EurekaInstanceCanceledEvent event) {
		String msg = "服务" + event.getAppName() + "\n" + event.getServerId() + "已下线";
		logger.info(msg);
		AlarmClient.info("http://mosh-data-1:6031/v1/alarm", 4, "alarm.register.down", msg);
		//this.send(msg);
	}
	
	@EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        String msg="服务"+instanceInfo.getAppName()+"\n"+  instanceInfo.getHostName()+":"+ instanceInfo.getPort()+ " \nip: " +instanceInfo.getIPAddr() +"进行注册";
        logger.info(msg);
    	AlarmClient.info("http://mosh-data-1:6031/v1/alarm", 4, "alarm.register.register", msg);
        //this.send(msg);
    }
 
    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        //logger.info("服务{}进行续约", event.getServerId() +"  "+ event.getAppName());
    }
 
    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        logger.info("注册中心启动,{}", System.currentTimeMillis());
    }
 
    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        logger.info("注册中心服务端启动,{}", System.currentTimeMillis());
    }
	
	/*private  void send(String msg){
        //用于封装邮件信息的实例
        SimpleMailMessage smm = new SimpleMailMessage();
        //由谁来发送邮件
        smm.setFrom("wldd1116@163.com");
        //邮件主题
        smm.setSubject("Eureka-Server");
        //邮件内容
        smm.setText(msg);
        //接受邮件
        //smm.setTo(new String[]{"***@qq.com","***@qq.com","***@qq.com"});
        smm.setTo("wldd1116@163.com");
        try {
        	jms.send(smm);
        } catch (Exception e) {
        }
    }*/
}
