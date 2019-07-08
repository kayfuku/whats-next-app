package trie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhraseTrieExperiment {
	
	private static List<List<String>> phrasesFragmentsLast;
	private static List<List<String>> phrasesFragmentsFirst;
	private static PhraseTrie trie;
	private static int NUM_WORDS = 3;


	public static void main(String[] args) {		

		// Test search
		String line = "aa bb cc dd ee bb cc ff bb cc ff ee gg";
		String[] words = line.split("\\s+");
		System.out.println(Arrays.toString(words)); 
		// [aa, bb, cc, dd, ee, bb, cc, ff, bb, cc, ff, ee, gg]
		int NUM_WORDS = 3;

		PhraseTrie phraseTrie = new PhraseTrie();
		for (int i = 0; i + NUM_WORDS <= words.length; i++) {
			String[] phrase = Arrays.copyOfRange(words, i, i + NUM_WORDS);
			phraseTrie.insertPhrase(phrase);
			System.out.println(Arrays.toString(phrase));
		}
		//		[aa, bb, cc]
		//		[bb, cc, dd]
		//		[cc, dd, ee]
		//		[dd, ee, bb]
		//		[ee, bb, cc]
		//		[bb, cc, ff]
		//		[cc, ff, bb]
		//		[ff, bb, cc]
		//		[bb, cc, ff]
		//		[cc, ff, ee]
		//		[ff, ee, gg]		
		System.out.println("Phrases inserted!");
		System.out.println();

		// Test search(). 
		String[] phrase = new String[]{ "aa", "bb", "cc" };
		System.out.println(phraseTrie.search(phrase)); // true
		phrase = new String[]{ "bb", "cc", "dd" };
		System.out.println(phraseTrie.search(phrase)); // true
		phrase = new String[]{ "bb", "cc" };
		System.out.println(phraseTrie.search(phrase)); // false
		System.out.println();

		// Test printAutoSuggestions(). 
		String[] prefix = new String[]{ "bb" };
		phraseTrie.printAutoSuggestions(prefix);
		//		[bb, cc, ff] 2
		//		[bb, cc, dd] 1
		prefix = new String[]{ "ac" };
		phraseTrie.printAutoSuggestions(prefix); // No phrase found.
		System.out.println();


		// Test handling fragments. 
		trie = new PhraseTrie();
		line = "aa bb cc dd ee ff gg hh ii";
		buildTrie(line);
		for (List<String> frags : phrasesFragmentsLast) {
			System.out.println(frags);
		}
		line = "jj kk ll mm nn oo pp qq rr";
		buildTrie(line);
		for (List<String> frags : phrasesFragmentsFirst) {
			System.out.println(frags);
		}
		prefix = new String[]{ "gg" };
		trie.printAutoSuggestions(prefix);
		prefix = new String[]{ "hh" };
		trie.printAutoSuggestions(prefix);
		prefix = new String[]{ "ii" };
		trie.printAutoSuggestions(prefix);

		




	}	
	
	
	public static void buildTrie(String line) {
		String[] words = line.split("\\s+");
		
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
			    for (int j = 0; j <= i; j++) {
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
//				System.out.println(fragmentsLast);
				
				// Convert String list to String array. 
				String[] phrase = new String[fragmentsLast.size()];
				phrase = fragmentsLast.toArray(phrase);
				
				trie.insertPhrase(phrase);
				System.out.println(Arrays.toString(phrase) + " inserted!");
			}
		}
		

		for (int i = 0; i + NUM_WORDS <= words.length; i++) {
			String[] phrase = Arrays.copyOfRange(words, i, i + NUM_WORDS);
			trie.insertPhrase(phrase);
			System.out.println(Arrays.toString(phrase) + " inserted!");
		}
		
		
		// Handle fragments of a phrase. 
		phrasesFragmentsLast = new ArrayList<>();
		for (int i = words.length - NUM_WORDS + 1; i < words.length; i++) {
			List<String> fragments = new ArrayList<>();
			phrasesFragmentsLast.add(fragments);		
			for (List<String> frags : phrasesFragmentsLast) {
				frags.add(words[i]);
			}
		}
		
	}
	
	



}

class PhraseTrieNode {
	// R links to node children. 
	private Map<String, PhraseTrieNode> links;
	private boolean isEnd;
	private int count;
	// Number of child nodes (non-null links). 
	private int size;

	public PhraseTrieNode() {
		links = new HashMap<>();
		isEnd = false;
		count = 0;
		size = 0;
	}

	public boolean containsKey(String word) {
		return links.containsKey(word);
	}

	public PhraseTrieNode get(String word) {
		return links.get(word);
	}

	public void put(String word, PhraseTrieNode node) {
		links.put(word, node);
		size = links.size();
	}

	public void setEnd() {
		isEnd = true;
		count++;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public int getNumChild() {
		return size;
	}

	public Map<String, PhraseTrieNode> getMap() {
		return links;    	
	}

	public int getCount() {
		return count;
	}

} 


class PhraseTrie {
	public PhraseTrieNode root;

	public PhraseTrie() {
		root = new PhraseTrieNode();      
	}

	// Inserts a phrase into the trie. O(N) time, O(N) space, where N is the total number 
	// of words in the phrase. 
	public void insertPhrase(String[] phrase) {
		PhraseTrieNode node = root;
		for (int i = 0; i < phrase.length; i++) {
			String currWord = phrase[i];
			if (!node.containsKey(currWord)) {
				node.put(currWord, new PhraseTrieNode());
			}
			node = node.get(currWord);
		}
		// This is not meaningful for now. 
		node.setEnd();
	}

	// Search the phrase in the Trie. O(N) time, O(1) space, where N is the phrase length. 
	public boolean search(String[] phrase) {
		PhraseTrieNode node = getLastNode(phrase);
		return node != null && node.isEnd();
	}   
	// Return the last node. 
	public PhraseTrieNode getLastNode(String[] phrase) {
		PhraseTrieNode node = root;
		for (int i = 0; i < phrase.length; i++) {
			String currWord = phrase[i];
			if (!node.containsKey(currWord)) {  
				return null;
			}
			node = node.get(currWord);
		}

		return node;
	}

	// Returns true if there is any word in the trie
	// that starts with the given prefix. O(N) time, O(1) space, where N is the phrase length.
	public boolean startsWith(String[] prefix) {
		PhraseTrieNode node = getLastNode(prefix);
		return node != null;
	}

	public List<String> searchLongestPrefix(String[] phrase) {
		PhraseTrieNode node = root;
		List<String> prefix = new ArrayList<>();
		for (int i = 0; i < phrase.length; i++) {
			String currWord = phrase[i];
			if (node.containsKey(currWord) && node.getNumChild() == 1 && !node.isEnd()) {
				prefix.add(currWord);
				node = node.get(currWord);
			} else {
				return prefix;
			}
		}   

		return prefix;
	}


	// Hold results for given query prefix. 
	private List<PhraseFreq> results;

	// Print suggestions for given query prefix. 
	public int printAutoSuggestions(String[] query) {
		if (query == null || query.length == 0) {
			System.out.println("Enter a non-empty string.");
			return 0;
		}
		
		results = new ArrayList<>();

		// Get the last node of the query. 
		PhraseTrieNode lastNode = this.getLastNode(query);
		if (lastNode == null) {
			// There is no string that starts with this query. 
			System.out.println("No phrase found.");
			return 0;
		}

		if (lastNode.isEnd() && lastNode.getNumChild() == 0) {
			// The query is present as a phrase, and there is no subtree
			// below the last node. 
			System.out.println(Arrays.toString(query));
			return -1;
		}

		if (lastNode.getNumChild() != 0) {
			List<String> prefix = new ArrayList<>(Arrays.asList(query));
			suggestionsRecur(lastNode, prefix);
		}

		displayResult();

		return 1;
	}


	// Print auto-suggestions for given prefix. 
	// @param node  the last node in the prefix. 
	private void suggestionsRecur(PhraseTrieNode node, List<String> prefix) {
		if (node.isEnd()) {
			//			System.out.println("prefix: " + prefix.toString() + " count: " + node.getCount());
			results.add(new PhraseFreq(prefix.toString(), node.getCount()));
		}
		if (node.getNumChild() == 0) {
			// No child node. 
			return;
		}

		for (String word : node.getMap().keySet()) {
			if (node.containsKey(word)) {	
				List<String> curPrefix = new ArrayList<>(prefix);
				curPrefix.add(word);
				suggestionsRecur(node.get(word), curPrefix);
			}	
		}
	}


	private void displayResult() {
		results.sort(new Comparator<PhraseFreq>() {
			@Override
			public int compare(PhraseFreq o1, PhraseFreq o2) {
				// Descending order. 
				return o2.count - o1.count;
			}
		});

		for (PhraseFreq phraseFreq : results) {
			System.out.println(phraseFreq.phrase + " " + phraseFreq.count);
		}
	}


	private class PhraseFreq {
		String phrase;
		int count;

		public PhraseFreq(String str, int count) {
			this.phrase = str;
			this.count = count;
		}
	}



}






































