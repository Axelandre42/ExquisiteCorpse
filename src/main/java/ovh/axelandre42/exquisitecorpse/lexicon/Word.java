package ovh.axelandre42.exquisitecorpse.lexicon;

import ovh.axelandre42.exquisitecorpse.lexicon.attribute.AttributeType;

import java.util.HashMap;
import java.util.Map;

public class Word implements Lemma {

    private String word;
    private Map<AttributeType, String> attributes;

    public Word(String word) {
        this.word = word;
        this.attributes = new HashMap<>();
    }

    @Override
    public String get() {
        return this.word;
    }

    @Override
    public String getAttribute(AttributeType attribute) {
        return attributes.get(attribute);
    }
}
