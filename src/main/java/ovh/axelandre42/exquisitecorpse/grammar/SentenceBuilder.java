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
package ovh.axelandre42.exquisitecorpse.grammar;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import ovh.axelandre42.exquisitecorpse.lexicon.Lexicon;

/**
 * @author Alexandre Waeles
 *
 */
public class SentenceBuilder {
	private byte[] data;
	private Lexicon lexicon;
	private LanguageGrammar grammar;
	private Random random = new Random();

	private List<Entry<String, Set<String>>> sentence = new ArrayList<Entry<String, Set<String>>>();
	private int currentWorkingIndex = 0;
	private int lastResult = 0;
	private int currentOffset = 0;
	private int currentPaddingOffset = 0;

	public SentenceBuilder(byte[] data, Lexicon lexicon, LanguageGrammar grammar) {
		this.data = data;
		this.lexicon = lexicon;
		this.grammar = grammar;
	}

	public SentenceBuilder next(String type, boolean last) {
		List<Entry<String, Set<String>>> matching;

		if (grammar.shouldHaveConstraints(type)) {
			Set<String> constraints = grammar.getConstraints(type, sentence.get(currentWorkingIndex).getValue());
			matching = lexicon.matching(type, constraints);
		} else {
			matching = lexicon.matching(type, Collections.emptySet());
			currentWorkingIndex = sentence.size();
		}

		int selectedLong;
		if (last) {
			selectedLong = selectTailBytes(lastResult);
		} else if (remaining() <= 0) {
			selectedLong = selectRandomBytes(matching.size(), lastResult);
		} else {
			selectedLong = selectBytes(matching.size(), lastResult);
		}

		int selectedValue = selectValue(selectedLong, matching);

		sentence.add(matching.get(selectedValue));

		return this;
	}

	private int selectTailBytes(int old) {
		ByteBuffer oldBuf = ByteBuffer.allocate(Integer.BYTES + 1);
		oldBuf.putInt(old);
		oldBuf.put((byte) (currentPaddingOffset + 1));
		byte[] arr = oldBuf.array();
		ByteBuffer buf = ByteBuffer.wrap(arr, 1, Integer.BYTES);
		return buf.getInt();
	}

	private int selectRandomBytes(int lowerBound, int old) {
		int selected = 0;
		ByteBuffer oldBuf = ByteBuffer.allocate(Integer.BYTES * 2);
		oldBuf.putInt(old);
		oldBuf.putInt(random.nextInt());
		byte[] arr = oldBuf.array();
		int i = 0;
		for (; selected < lowerBound && i < Integer.BYTES; i++) {
			ByteBuffer buf = ByteBuffer.wrap(arr, i, Integer.BYTES);
			selected = buf.getInt();
		}
		currentPaddingOffset += i;
		return selected;
	}

	private int selectBytes(int lowerBound, int old) {
		int selected = 0;
		ByteBuffer oldBuf = ByteBuffer.allocate(Integer.BYTES * 2);
		oldBuf.putInt(old);
		int len = remaining() >= Integer.BYTES ? Integer.BYTES : remaining();
		oldBuf.put(data, currentOffset, len);
		byte[] arr = oldBuf.array();
		int i = 0;
		for (; currentOffset + i < data.length && selected < lowerBound && i < Integer.BYTES; i++) {
			ByteBuffer buf = ByteBuffer.wrap(arr, i, Integer.BYTES);
			selected = buf.getInt();
		}
		currentOffset += i;
		return selected;
	}

	private int selectValue(int selectedLong, List<Entry<String, Set<String>>> db) {
		int selectedValue = selectedLong % db.size();
		lastResult = selectedLong / db.size();
		return selectedValue;
	}

	public SentenceBuilder before(String type, boolean last) {
		List<Entry<String, Set<String>>> matching;

		if (grammar.shouldHaveConstraints(type)) {
			matching = lexicon.matching(type,
					grammar.getConstraints(type, sentence.get(currentWorkingIndex).getValue()));
		} else {
			matching = lexicon.matching(type, Collections.emptySet());
			currentWorkingIndex = sentence.size();
		}

		int selectedLong;
		if (last) {
			selectedLong = selectTailBytes(lastResult);
		} else if (remaining() <= 0) {
			selectedLong = selectRandomBytes(matching.size(), lastResult);
		} else {
			selectedLong = selectBytes(matching.size(), lastResult);
		}

		int selectedValue = selectValue(selectedLong, matching);

		sentence.add(sentence.size() - 1, matching.get(selectedValue));
		currentWorkingIndex += 1;
		return this;
	}

	public int remaining() {
		return data.length - currentOffset;
	}

	public String build() {
		String result = sentence.parallelStream().map(e -> e.getKey()).collect(Collectors.joining(" "));
		sentence = new ArrayList<>();
		return result;
	}
}
