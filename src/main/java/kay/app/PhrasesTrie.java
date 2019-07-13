package kay.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PhrasesTrie {
	
	public PhraseTrieNode root;

	public PhrasesTrie() {
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
	private List<PhraseFreq> list;
	
	// Print suggestions for given query prefix. 
	public int printAutoSuggestions(String[] query) {
		list = new ArrayList<>();
		if (query == null || query.length == 0) {
//			System.out.println("Enter a non-empty string.");
			return 0;
		}

		// Get the last node of the query. 
		PhraseTrieNode lastNode = this.getLastNode(query);
		if (lastNode == null) {
			// There is no string that starts with this query. 
//			System.out.println("No phrase found.");
			return 0;
		}

		if (lastNode.isEnd() && lastNode.getNumChild() == 0) {
			// The query is present as a phrase, and there is no subtree
			// below the last node. 
			list.add(new PhraseFreq(query.toString(), 1));
//			System.out.println(Arrays.toString(query));
			return -1;
		}

		if (lastNode.getNumChild() != 0) {
			List<String> prefix = new ArrayList<>(Arrays.asList(query));
			suggestionsRecur(lastNode, prefix);
		}

//		displayResult();

		return 1;
	}


	// Print auto-suggestions for given prefix. 
	// @param node  the last node in the prefix. 
	private void suggestionsRecur(PhraseTrieNode node, List<String> prefix) {
		if (node.isEnd()) {
			//			System.out.println("prefix: " + prefix.toString() + " count: " + node.getCount());
			list.add(new PhraseFreq(prefix.toString(), node.getCount()));
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
		list.sort(new Comparator<PhraseFreq>() {
			@Override
			public int compare(PhraseFreq o1, PhraseFreq o2) {
				// Descending order. 
				return o2.count - o1.count;
			}
		});

		for (PhraseFreq phraseFreq : list) {
			System.out.println(phraseFreq.phrase + " " + phraseFreq.count);
		}
	} 
	
	
	/**
	 * Builds the result list using StringBuffer to display it on the JLabel object. 
	 * @param word  the key to be used
	 * @param top  the number of rows of the result to be displayed
	 * @return string
	 */
	public synchronized String buildResult(String[] words, int top) {
		StringBuffer sb = new StringBuffer();
		sb.append("prefix: \tphrase: \t\tcount: \n");
		sb.append(Arrays.toString(words) + "\n");
		
		printAutoSuggestions(words);
		
		if (list.isEmpty()) {
			return "No phrase found.";
		}
		
		list.sort(new Comparator<PhraseFreq>() {
			@Override
			public int compare(PhraseFreq o1, PhraseFreq o2) {
				// Descending order. 
				return o2.count - o1.count;
			}
		});

		int i = 0;
		for (PhraseFreq phraseFreq : list) {
			if (i == top) { 
				break; 
			}
			sb.append("\t" + phraseFreq.phrase + "         " + "\t" + phraseFreq.count);
			sb.append("\n");
			i++;

//			System.out.println(phraseFreq.phrase + " " + phraseFreq.count);
		}
		
		return sb.toString();
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





































