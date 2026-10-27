package com.example.financetracker.scheduler;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.quartz.spi.TriggerFiredBundle;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail expenseReportJobDetail() {

        return JobBuilder.newJob(ExpenseJob.class)
                .withIdentity("expenseReportJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger expenseReportTrigger(JobDetail expenseReportJobDetail) {

        return TriggerBuilder.newTrigger()
                .forJob(expenseReportJobDetail)
                .withIdentity("expenseReportTrigger")
                .withSchedule(
                        CronScheduleBuilder.dailyAtHourAndMinute(23, 59)
                )
                .build();
    }

    @Bean
    public SchedulerFactoryBeanCustomizer jobFactoryCustomizer(
            ApplicationContext applicationContext) {

        return scheduler -> {
            AutowiringJobFactory jobFactory = new AutowiringJobFactory();
            jobFactory.setApplicationContext(applicationContext);
            scheduler.setJobFactory(jobFactory);
        };
    }

    public static class AutowiringJobFactory
            extends SpringBeanJobFactory
            implements ApplicationContextAware {

        private AutowireCapableBeanFactory beanFactory;

        @Override
        public void setApplicationContext(
                ApplicationContext applicationContext) {

            beanFactory = applicationContext.getAutowireCapableBeanFactory();
        }

        @Override
        protected Object createJobInstance(
                TriggerFiredBundle bundle) throws Exception {

            Object job = super.createJobInstance(bundle);

            beanFactory.autowireBean(job);

            return job;
        }
    }
}