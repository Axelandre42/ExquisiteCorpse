/* 
 * Copyright 2019 Alexandre Waeles
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to
 * do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ovh.axelandre42.exquisitecorpse.lexicon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collector;

/**
 * @author Alexandre Waeles
 *
 */
public class Lexicon {
	private Map<String, List<Word>> lexicon = new HashMap<>();

	private void typeExists(String type) {
		if (!lexicon.containsKey(type)) {
			lexicon.put(type, new ArrayList<>());
		}
	}

	public void add(String type, Word word) {
		typeExists(type);
		lexicon.get(type).add(word);
	}

	public Word getFromRoot(String type, String word) {
		typeExists(type);
		return lexicon.get(type).parallelStream().filter(w -> w.getRoot().equals(word)).findAny().orElse(null);
	}

	public Set<Entry<String, Set<String>>> all(String type) {
		typeExists(type);
		return lexicon.get(type).parallelStream().map(w -> w.entries())
				.collect(Collector.of(HashSet::new, HashSet::addAll, (left, right) -> {
					left.addAll(right);
					return left;
				}));
	}

	public List<Entry<String, Set<String>>> matching(String type, Set<String> constraints) {
		typeExists(type);
		List<Entry<String, Set<String>>> results = lexicon.get(type).parallelStream().map(w -> w.get(constraints))
				.collect(Collector.of(ArrayList::new, ArrayList::addAll, (left, right) -> {
					left.addAll(right);
					return left;
				}));
		return results;
	}

	public int sizeMatching(String type, Set<String> constraints) {
		return matching(type, constraints).size();
	}

	public Set<String> types() {
		return lexicon.keySet();
	}
}
