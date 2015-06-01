package org.sandcastle.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main Report Generator Class
 */
public class ReportGenerator {
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");

        JobLauncher launcher = (JobLauncher)context.getBean("jobLauncher");
        Job job = (Job)context.getBean("examResultJob");

        try {
            JobExecution execution = launcher.run(job, new JobParameters());
            System.out.println("Job Exit Result : " + execution.getStatus());
        } catch( JobExecutionException jee){
            System.err.println("Job Execution failed");
            jee.printStackTrace();
        }
    }
}
