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
import java.util.List;
import java.util.Map;

/**
 * This class represents a list of every word of a dictionary.
 * 
 * @author Alexandre Waeles
 */
public class Dictionary {
	private Map<Class<? extends Word>, List<Word>> words = new HashMap<>();

	/**
	 * Adds a new word into the dictionary.
	 * 
	 * @param word the word to add.
	 * @see Word
	 */
	public void add(Word word) {
		List<Word> sublist;
		if ((sublist = words.get(word.getClass())) == null) {
			sublist = new ArrayList<>();
			words.put(word.getClass(), sublist);
		}

		sublist.add(word);
	}

	/**
	 * Finds a word instance from a given string.
	 * 
	 * @param word the word to find.
	 * @param type the wanted type of the word.
	 * @return a {@link Word} instance.
	 */
	public Word get(String word, Class<? extends Word> type) {
		return words.get(type).parallelStream().filter(w -> w.match(word)).findFirst().orElse(null);
	}

	/**
	 * Finds a word instance from its index.
	 * 
	 * @param index the index of the word.
	 * @param type  the wanted type of the word.
	 * @return a {@link Word} instance.
	 */
	public Word get(int index, Class<? extends Word> type) {
		return words.get(type).get(index);
	}
}
