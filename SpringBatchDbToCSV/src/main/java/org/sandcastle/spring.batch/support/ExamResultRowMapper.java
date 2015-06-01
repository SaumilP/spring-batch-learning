package org.sandcastle.spring.batch.support;

import org.joda.time.LocalDate;
import org.sandcastle.spring.batch.model.ExamResult;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database Table Row to Model mapper class
 */
public class ExamResultRowMapper implements RowMapper<ExamResult> {

    @Override public ExamResult mapRow(ResultSet resultSet, int i) throws SQLException {
        ExamResult result = new ExamResult();
        result.setStudentName(resultSet.getString("student_name"));
        result.setDataOfBirth(new LocalDate(resultSet.getDate("date_of_birth")));
        result.setPercentage(resultSet.getDouble("percentage"));
        return result;
    }
}
