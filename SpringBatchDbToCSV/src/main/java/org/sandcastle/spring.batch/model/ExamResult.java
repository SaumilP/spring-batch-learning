package org.sandcastle.spring.batch.model;

import org.joda.time.LocalDate;

/**
 * Model representing ExamResult.txt file for output
 */
public class ExamResult {
    private String studentName;
    private LocalDate dataOfBirth;
    private double percentage;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LocalDate getDataOfBirth() {
        return dataOfBirth;
    }

    public void setDataOfBirth(LocalDate dataOfBirth) {
        this.dataOfBirth = dataOfBirth;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override public String toString() {
        return "ExamResult{" +
               "studentName='" + studentName + '\'' +
               ", dataOfBirth=" + dataOfBirth +
               ", percentage=" + percentage +
               '}';
    }
}
