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

	private List<Entry<String, Set<String>>> sentence = new ArrayList<Entry<String, Set<String>>>();
	private int currentWorkingIndex = 0;
	private long lastResult = 0;
	private int currentOffset = 0;

	public SentenceBuilder(byte[] data, Lexicon lexicon, LanguageGrammar grammar) {
		this.data = data;
		this.lexicon = lexicon;
		this.grammar = grammar;
	}

	public SentenceBuilder next(String type) {
		List<Entry<String, Set<String>>> matching;

		if (grammar.shouldHaveConstraints(type)) {
			Set<String> constraints = grammar.getConstraints(type, sentence.get(currentWorkingIndex).getValue());
			System.out.println(constraints);
			matching = lexicon.matching(type, constraints);
		} else {
			matching = lexicon.matching(type, Collections.emptySet());
			currentWorkingIndex = sentence.size();
		}

		long selectedLong = selectBytes(matching.size(), lastResult);
		int selectedValue = (int) (selectedLong % matching.size());
		lastResult = selectedLong - matching.size();

		System.out.println(String.format("Selected %d/%d. %d bytes are still remaining.", selectedValue,
				matching.size(), data.length - currentOffset));

		sentence.add(matching.get(selectedValue));

		return this;
	}

	private long selectBytes(int lowerBound, long old) {
		long selected = 0;
		ByteBuffer oldBuf = ByteBuffer.allocate(Long.BYTES * 2);
		oldBuf.putLong(old);
		oldBuf.put(data, currentOffset,
				(data.length - currentOffset) >= Long.BYTES ? Long.BYTES : (data.length - currentOffset));
		byte[] arr = oldBuf.array();
		int i = 0;
		for (; selected < lowerBound && i < Long.BYTES; i++) {
			ByteBuffer buf = ByteBuffer.wrap(arr, i, Long.BYTES);
			selected = buf.getLong();
		}
		currentOffset += i;
		return selected;
	}

	public SentenceBuilder before(String type) {
		List<Entry<String, Set<String>>> matching;

		if (grammar.shouldHaveConstraints(type)) {
			matching = lexicon.matching(type,
					grammar.getConstraints(type, sentence.get(currentWorkingIndex).getValue()));
		} else {
			matching = lexicon.matching(type, Collections.emptySet());
			currentWorkingIndex = sentence.size();
		}

		long selectedLong = selectBytes(matching.size(), lastResult);
		int selectedValue = (int) (selectedLong % matching.size());
		lastResult = selectedLong - matching.size();

		System.out.println(String.format("Selected %d/%d. %d bytes are still remaining.", selectedValue,
				matching.size(), data.length - currentOffset));

		sentence.add(sentence.size() - 1, matching.get(selectedValue));
		currentWorkingIndex += 1;
		return this;
	}

	public String build() {
		return sentence.parallelStream().map(e -> e.getKey()).collect(Collectors.joining(" "));
	}
}
