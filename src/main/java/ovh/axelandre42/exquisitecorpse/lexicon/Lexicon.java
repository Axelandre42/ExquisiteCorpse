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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexandre Waeles
 *
 */
public class Lexicon {
	private Map<String, List<Word>> lexicon = new HashMap<>();

	public void add(String type, Word word) {
		lexicon.get(type).add(word);
	}

	public Word get(String type, String word) {
		return lexicon.get(type).parallelStream().filter(w -> w.match(word)).findFirst().orElse(null);
	}

	public Word get(String type, int index) {
		return lexicon.get(type).get(index);
	}

	public int size(String type) {
		return lexicon.get(type).size();
	}

	public int indexOf(String type, Word word) {
		return lexicon.get(type).indexOf(word);
	}
}
