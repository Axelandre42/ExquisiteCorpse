package ovh.axelandre42.exquisitecorpse.lexicon;

import java.util.HashMap;
import java.util.Map;

public abstract class Word {
	
	protected Map<String, Map<String, String>> variants = new HashMap<>();
	
	public boolean match(String word) {
		return variants.containsKey(word);
	}
}
