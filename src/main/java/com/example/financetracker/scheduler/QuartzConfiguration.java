package com.example.financetracker.scheduler;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail autoImportJobDetails() {
        // SUCCESS: Points directly to your cleanly named ExpenseJob class!
        return JobBuilder.newJob(ExpenseJob.class)
                .withIdentity("autoImportJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger autoImportJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(10) // Fires every 10 seconds
                .repeatForever();          // Runs continuously

        return TriggerBuilder.newTrigger()
                .forJob(autoImportJobDetails())
                .withIdentity("autoImportTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
