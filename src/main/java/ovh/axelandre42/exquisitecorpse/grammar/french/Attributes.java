package ovh.axelandre42.exquisitecorpse.grammar.french;

import ovh.axelandre42.exquisitecorpse.lexicon.attribute.AttributeType;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum Attributes implements AttributeType {
    ;

    private Map<String, Integer> validityMapping;

    Attributes(String[] validityDomain, int[] priorityDomain) {
        if (validityDomain.length != priorityDomain.length)
            throw new IllegalArgumentException("validityDomain and priorityDomain must have same length");

        validityMapping = IntStream.range(0, validityDomain.length).boxed().collect(Collectors.toMap(i -> validityDomain[i], i -> priorityDomain[i]));
    }

    @Override
    public Comparator<String> getPriorityComparator() {
        return Comparator.comparingInt(value -> validityMapping.get(value));
    }

    @Override
    public boolean isValidValue(String value) {
        return validityMapping.containsKey(value);
    }
}
