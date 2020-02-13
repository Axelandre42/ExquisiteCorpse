package ovh.axelandre42.exquisitecorpse.lexicon;

import ovh.axelandre42.exquisitecorpse.lexicon.attribute.AttributeType;

import java.util.Map;

public interface Lemma {

    /**
     * Returns the {@link String} value of the lemma.
     *
     * @return the {@link String} value of the lemma
     */
    String get();

    /**
     * Returns the matching attribute to the given attribute type.
     *
     * @param attribute the type of the attribute
     * @return the attribute value
     */
    String getAttribute(AttributeType attribute);
}
