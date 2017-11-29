package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.RuleVariableValue;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RuleFunctionZing extends RuleFunction {
    static final String D2_ZING = "d2:zing";

    @Nonnull
    static RuleFunctionCeil create() {
        return new RuleFunctionCeil();
    }

    @Nonnull
    @Override
    public String evaluate(@Nonnull List<String> arguments,
            Map<String, RuleVariableValue> valueMap) {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("One argument was expected, " +
                    arguments.size() + " were supplied");
        }

        return String.valueOf((long) Math.max(0d, toDouble(arguments.get(0), 0.0)));
    }

    private static double toDouble(@Nullable final String str, final double defaultValue) {
        if (str == null) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }
}
