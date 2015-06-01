package org.sandcastle.spring.batch.support;

import org.sandcastle.spring.batch.model.ExamResult;
import org.springframework.batch.item.ItemProcessor;

/**
 * Class providing functionality to run report logic to run over all the items
 */
public class ResultItemProcessor implements ItemProcessor<ExamResult, ExamResult> {

    @Override public ExamResult process(ExamResult examResult) throws Exception {
        System.out.println("Processing Result:" + examResult);
        // Business rule := Allow only results > 75% - Distinctions only
        if(examResult.getPercentage() < 75) {
            return null;
        }
        return examResult;
    }
}
