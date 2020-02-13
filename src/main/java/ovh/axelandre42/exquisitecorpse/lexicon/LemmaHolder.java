package ovh.axelandre42.exquisitecorpse.lexicon;

import ovh.axelandre42.exquisitecorpse.lexicon.attribute.AttributeType;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LemmaHolder extends ArrayList<Lemma> implements Lemma {

    private boolean detach;

    public LemmaHolder(boolean detach) {
        this.detach = detach;
    }

    @Override
    public String get() {
        return this.stream().map(lemma -> lemma.get()).collect(Collectors.joining(" ")) + (detach ? "," : "");
    }

    @Override
    public String getAttribute(AttributeType attribute) {
        return this.stream().map(lemma -> lemma.getAttribute(attribute)).collect(Collectors.maxBy(attribute.getPriorityComparator())).get();
    }
}
