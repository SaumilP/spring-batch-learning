package org.sandcastle.spring.batch.listners;

import org.joda.time.DateTime;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.util.List;

/**
 * Optional Job Listener providing opportunity to execute some batch related logic => before & after execution of Job
 */
public class ExamResultJobListener implements JobExecutionListener {

    private DateTime startTime, stopTime;

    @Override public void beforeJob(JobExecution jobExecution) {
        startTime = new DateTime();
        System.out.println("ExamResult Job starts at " + startTime);
    }

    @Override public void afterJob(JobExecution jobExecution) {
        stopTime = new DateTime();
        System.out.println("ExamResult Job stops at " + stopTime);
        System.out.println("Total Time : " + getTimeInMillis(startTime, stopTime));

        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            System.out.println("ExamResult Job completed successfully");
        } else {
            System.out.println("ExamResult job failed with below exceptions");
            List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
            for (Throwable th : exceptionList) {
                System.err.println("Exception: " + th.getLocalizedMessage());
            }
        }
    }

    private long getTimeInMillis(DateTime startTime, DateTime stopTime) {
        return stopTime.getMillis() - startTime.getMillis();
    }
}
