package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.RuleVariableValue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

@RunWith(JUnit4.class)
public class RuleFunctionDaysBetweenShould {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void evaluateMustReturnCorrectNumberOfDays() {
        RuleFunction daysBetween = RuleFunctionDaysBetween.create();

        String days = daysBetween.evaluate(Arrays.asList(
                "2016-01-01", "2016-01-31"), new HashMap<String, RuleVariableValue>());
        assertThat(days).isEqualTo("30");
    }

    @Test
    public void thrown_illegal_argument_exception_when_evaluate_only_one_day() {
        thrown.expect(IllegalArgumentException.class);
        RuleFunctionDaysBetween.create().evaluate(Arrays.asList("2016-01-01"),
                new HashMap<String, RuleVariableValue>());
    }

    @Test
    public void thrown_illegal_argument_exception_when_evaluate_more_than_two_days() {
        thrown.expect(IllegalArgumentException.class);
        RuleFunctionDaysBetween.create().evaluate(Arrays.asList("2016-01-01","2016-01-01","2016-01-01"),
                new HashMap<String, RuleVariableValue>());
    }

    @Test
    public void thrown_illegal_argument_exception_when_evaluate_with_no_date_strings() {
        thrown.expect(ParseException.class);
        RuleFunctionDaysBetween.create().evaluate(Arrays.asList("one","two"),
                new HashMap<String, RuleVariableValue>());
    }
    @Test
    public void evaluateMustFailOnWrongArgumentCount() {
        try {
            RuleFunctionDaysBetween.create().evaluate(Arrays.asList("one"),
                    new HashMap<String, RuleVariableValue>());
            fail("IllegalArgumentException was expected, but nothing was thrown.");
        } catch (IllegalArgumentException illegalArgumentException) {
            // noop
        }

        try {
            RuleFunctionDaysBetween.create().evaluate(Arrays.asList("one", "two", "three"),
                    new HashMap<String, RuleVariableValue>());
            fail("IllegalArgumentException was expected, but nothing was thrown.");
        } catch (IllegalArgumentException illegalArgumentException) {
            // noop
        }
    }
}
