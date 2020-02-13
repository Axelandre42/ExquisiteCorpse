package ovh.axelandre42.exquisitecorpse.lexicon.attribute;

import java.util.Comparator;

public interface AttributeType {
    /**
     * Returns a {@link Comparator} for attributes values based on priorities of those.
     * @return a {@link Comparator} for attributes values
     */
    Comparator<String> getPriorityComparator();

    /**
     * Tells the validity of an attribute value in its validity domain.
     *
     * @param value the value to test
     * @return the test result
     */
    boolean isValidValue(String value);
}
