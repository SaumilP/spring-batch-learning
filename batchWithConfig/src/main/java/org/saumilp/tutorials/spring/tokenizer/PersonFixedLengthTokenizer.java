package org.saumilp.tutorials.spring.tokenizer;

import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.item.file.transform.RangeArrayPropertyEditor;

/**
 * File Tokenizer
 */
public class PersonFixedLengthTokenizer extends FixedLengthTokenizer {
    public PersonFixedLengthTokenizer(){
        RangeArrayPropertyEditor range = new RangeArrayPropertyEditor();
        range.setAsText("1-30,31-60,61-");
        setNames(new String[]{"firstName", "lastName", "year"});
        setColumns((Range[]) range.getValue());
    }
}
