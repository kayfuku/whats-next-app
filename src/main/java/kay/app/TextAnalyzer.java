package kay.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Preprocesses text data before storing data in NextWordsHolder. 
 * @author Kei Fukutani
 */
public class TextAnalyzer {

	// This data structure is also held in NextWordsHolder. 
	private HashMap<String, HashMap<String, Integer>> wordToCountMap;
	private String lastWord = null;


	public TextAnalyzer(HashMap<String, HashMap<String, Integer>> wordToCountMap, PhrasesTrie phrasesTrie) {
		this.wordToCountMap = wordToCountMap;
		this.phrasesTrie = phrasesTrie;
	}


	/**
	 * Splits the line, counts the number of the next words, and make the wordToCountMap. 
	 * Also, it handles with the last word of the line. 
	 * @param line  the input text
	 */
	public void makeNextWordsCountMap(String line) {

		String[] words = preprocessText(line);

		if (lastWord != null) {
			countNextWords(lastWord, words[0]);
		}

		String word = "";
		String nextWord = "";
		for (int i = 0; i + 1 < words.length; i++) {
			word = words[i];
			nextWord = words[i + 1];

			countNextWords(word, nextWord);
		}

		lastWord = words[words.length - 1];		
	}


	/**
	 * For each word, count the number of its next word, and stores it in 
	 * the nextWordToCount map. The map is put in another map as value, where 
	 * each word as key. 
	 * @param word  one word
	 * @param nextWord  the next word following the word
	 */
	private void countNextWords(String word, String nextWord) {

		if (!wordToCountMap.containsKey(word)) {
			HashMap<String, Integer> nextWordToCount = new HashMap<>();
			nextWordToCount.put(nextWord, 1);
			wordToCountMap.put(word, nextWordToCount);
		} else {
			HashMap<String, Integer> nextWordToCount = wordToCountMap.get(word);
			nextWordToCount.put(nextWord, nextWordToCount.getOrDefault(nextWord, 0) + 1);
			wordToCountMap.put(word, nextWordToCount);
		}
	}


	
	// For phrases. 
	private PhrasesTrie phrasesTrie;
	private int NUM_WORDS = 3;
	private List<List<String>> phrasesFragmentsLast;
	private List<List<String>> phrasesFragmentsFirst;	
	
	/**
	 * For phrases, build a trie. 
	 * @param line  the input text
	 */
	public void buildTrie(String line) {
		String[] words = preprocessText(line);
		
		if (words.length < NUM_WORDS) {
			return;
		}

		// Handle fragments of a phrase. 
		// Concatenate the fragments between the end of the previous line and 
		// the beginning of this line. 
		if (phrasesFragmentsLast != null) {
			phrasesFragmentsFirst = new ArrayList<>();
			for (int i = 0; i < NUM_WORDS - 1; i++) {
				List<String> fragments = new ArrayList<>();
				phrasesFragmentsFirst.add(fragments);
			}
			for (int i = 0; i < phrasesFragmentsFirst.size(); i++) {
				for (int j = 0; j <= i && j < words.length; j++) {
					phrasesFragmentsFirst.get(i).add(words[j]);
				}				
			}
			for (int i = 0; i < phrasesFragmentsFirst.size(); i++) {
				List<String> fragmentsFirst = phrasesFragmentsFirst.get(i);
				List<String> fragmentsLast = phrasesFragmentsLast.get(i);
				// Concatenate fragments. ex)
				// line 1: w1 w2 w3 w4 w5
				// line 2: w6 w7 w8 w9 w10
				// => [w4 w5 w6]
				fragmentsLast.addAll(fragmentsFirst);

				// Convert String list to String array. 
				String[] phrase = new String[fragmentsLast.size()];
				phrase = fragmentsLast.toArray(phrase);

				phrasesTrie.insertPhrase(phrase);
			}
		}		

		for (int i = 0; i + NUM_WORDS <= words.length; i++) {
			String[] phrase = Arrays.copyOfRange(words, i, i + NUM_WORDS);
			phrasesTrie.insertPhrase(phrase);
		}

		// Handle fragments of a phrase. 
		phrasesFragmentsLast = new ArrayList<>();
		for (int i = words.length - NUM_WORDS + 1; i < words.length; i++) {
			makeTempFragment(i, words);
		}


	}

	private void makeTempFragment(int i, String[] words) {
		List<String> fragments = new ArrayList<>(words.length - i);
		phrasesFragmentsLast.add(fragments);		
		for (List<String> frags : phrasesFragmentsLast) {
			frags.add(words[i]);
		}
	}




	/**
	 * Splits the given text by whitespace, removes all non-alphanumeric characters and 
	 * converts the string to lower case. 
	 * @param text  the text to be preprocessed
	 * @return preprocessed text
	 */
	private String[] preprocessText(String text) {
		String[] words = text.split("\\s+");
		String word;
		for (int i = 0; i < words.length; i++) {
			// Removes non-alphanumeric characters. 
			word = words[i].replaceAll("[^A-Za-z0-9']", "");
			word = word.toLowerCase();
			words[i] = word;
		}
		return words;
	}




































}
