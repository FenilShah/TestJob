package com.oomoqu.rest.job;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class PromotionStatusJob extends QuartzJobBean{
	private int timeout;
	
	PromotionStatusServices promotionStatusServices;

    /**
     * Setter called after the ExampleJob is instantiated
     * with the value from the JobDetailFactoryBean (5)
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public void setPromotionStatusServices(PromotionStatusServices promotionStatusServices){
    	this.promotionStatusServices = promotionStatusServices;
    }

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		try{
			System.out.println("Promotion status job running at " + new Date());
			promotionStatusServices.checkStatus();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
