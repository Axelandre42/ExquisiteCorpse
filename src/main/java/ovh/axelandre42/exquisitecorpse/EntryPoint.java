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
package ovh.axelandre42.exquisitecorpse;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import ovh.axelandre42.exquisitecorpse.grammar.LanguageGrammar;
import ovh.axelandre42.exquisitecorpse.grammar.SentenceBuilder;
import ovh.axelandre42.exquisitecorpse.lexicon.Lexicon;
import ovh.axelandre42.exquisitecorpse.lexicon.Word;

/**
 * @author Alexandre Waeles
 *
 */
public class EntryPoint {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Lexicon lexicon = new Lexicon();
		loadResource("/assets/dict/fr/a.tsv", lexicon);
		loadResource("/assets/dict/fr/b.tsv", lexicon);
		loadResource("/assets/dict/fr/c.tsv", lexicon);
		loadResource("/assets/dict/fr/d.tsv", lexicon);
		loadResource("/assets/dict/fr/e.tsv", lexicon);
		loadResource("/assets/dict/fr/f.tsv", lexicon);
		loadResource("/assets/dict/fr/g.tsv", lexicon);
		loadResource("/assets/dict/fr/h.tsv", lexicon);
		loadResource("/assets/dict/fr/i.tsv", lexicon);
		loadResource("/assets/dict/fr/k.tsv", lexicon);
		loadResource("/assets/dict/fr/l.tsv", lexicon);
		loadResource("/assets/dict/fr/m.tsv", lexicon);
		loadResource("/assets/dict/fr/n.tsv", lexicon);
		loadResource("/assets/dict/fr/o.tsv", lexicon);
		loadResource("/assets/dict/fr/p.tsv", lexicon);
		loadResource("/assets/dict/fr/q.tsv", lexicon);
		loadResource("/assets/dict/fr/r.tsv", lexicon);
		loadResource("/assets/dict/fr/s.tsv", lexicon);
		loadResource("/assets/dict/fr/t.tsv", lexicon);
		loadResource("/assets/dict/fr/u.tsv", lexicon);
		loadResource("/assets/dict/fr/v.tsv", lexicon);
		loadResource("/assets/dict/fr/w.tsv", lexicon);
		loadResource("/assets/dict/fr/x.tsv", lexicon);
		loadResource("/assets/dict/fr/y.tsv", lexicon);
		loadResource("/assets/dict/fr/z.tsv", lexicon);

		for (String type : lexicon.types()) {
			System.out.println(String.format("Lexicon of type \'%s\' is filled with %d entries", type,
					lexicon.sizeMatching(type, Collections.emptySet())));
		}

		byte[] data = new byte[4];
		Random random = new Random();
		random.nextBytes(data);

		SentenceBuilder builder = new SentenceBuilder(data, lexicon, new LanguageGrammar() {

			@Override
			public boolean shouldHaveConstraints(String type) {
				if (type.equals("Nom"))
					return false;

				return true;
			}

			@Override
			public Set<String> getConstraints(String type, Set<String> flags) {
				if (type.equals("Det")) {
					if (flags.contains("PL")) {
						return new HashSet<>(Arrays.asList("PL", "InvGen"));
					} else if (flags.contains("InvPL")) {
						flags.remove("InvPL");
						return flags;
					} else {
						return flags;
					}
				} else if (type.equals("Ver")) {
					return flags.parallelStream().filter(c -> c.equals("PL") || c.equals("SG"))
							.collect(Collectors.toSet());
				}

				return null;
			}
		});

	}

	private static void loadResource(String resourcePath, Lexicon lexicon) {
		Scanner sc = new Scanner(EntryPoint.class.getResourceAsStream(resourcePath));

		while (sc.hasNextLine()) {
			String raw = sc.nextLine();
			if (raw.isEmpty())
				continue;
			String[] line = raw.split("\\s");
			String[] attrs = line[2].split(":");

			addWordToLexicon(line[0], line[1], attrs, attrs[0], lexicon);
		}

		sc.close();
	}

	private static void addWordToLexicon(String word, String root, String[] attrs, String type, Lexicon lexicon) {
		Word w = lexicon.getFromRoot(type, root);
		if (w == null) {
			w = new Word();
			w.setRoot(root);
			lexicon.add(type, w);
		}
		for (int i = 1; i < attrs.length; i++) {
			w.add(word, new HashSet<>(Arrays.asList(attrs[i].split("\\+"))));
		}
	}

	private static Entry<String, Set<String>> randomSelect(String type, Set<String> constraints, Lexicon lexicon,
			Random random) {
		int bound = lexicon.sizeMatching(type, constraints);
		int index = random.nextInt(bound);
		return lexicon.matching(type, constraints).get(index);
	}

}
