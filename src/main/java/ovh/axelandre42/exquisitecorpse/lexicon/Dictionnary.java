package ovh.axelandre42.exquisitecorpse.lexicon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionnary {
	private Map<Class<? extends Word>, List<Word>> words = new HashMap<>();
	
	public void add(Word word) {
		List<Word> sublist;
		if ((sublist = words.get(word.getClass())) == null) {
			sublist = new ArrayList<>();
			words.put(word.getClass(), sublist);
		}
		
		sublist.add(word);
	}
	
	public Word get(String word, Class<? extends Word> type) {
		return words.get(type).parallelStream().filter(w -> w.match(word)).findFirst().orElse(null);
	}
	
	public Word get(int index, Class<? extends Word> type) {
		return words.get(type).get(index);
	}
}
