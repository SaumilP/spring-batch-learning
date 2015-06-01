package org.saumilp.tutorials.spring.processor;

import org.saumilp.tutorials.spring.models.Person;
import org.springframework.batch.item.ItemProcessor;

/**
 * Item Processor
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override public Person process(Person person) throws Exception {
        return person;
    }
}
