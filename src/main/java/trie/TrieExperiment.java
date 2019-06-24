package trie;

public class TrieExperiment {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		String[] strs = new String[]{ "leet", "leed", "le", "let" };
        System.out.println(longestCommonPrefix5(strs));
        System.out.println();
		
        // Test search
        Trie trie = new Trie();
        trie.insert("aa");
        trie.insert("abc");
        trie.insert("abcd");
        trie.insert("abbbaba");
        System.out.println(trie.search("aa")); // true
        System.out.println(trie.search("abc")); // true
        System.out.println(trie.search("abcd")); // true
        System.out.println(trie.search("abbbaba")); // true   
        System.out.println(trie.search("ac")); // false
        System.out.println(trie.search("b")); // false
        System.out.println(trie.search("")); // false
        System.out.println();
        
        // Test printAutoSuggestions(). 
		trie.printAutoSuggestions(trie.root, "ab");
//		abbbaba
//		abc
//		abcd
		System.out.println();
		trie.printAutoSuggestions(trie.root, "abc");
//		abc
//		abcd
		System.out.println();
		trie.printAutoSuggestions(trie.root, ""); // Enter a non-empty string.
		System.out.println();
		trie.printAutoSuggestions(trie.root, "ad"); // No string found.
		System.out.println();
		trie.printAutoSuggestions(trie.root, "aa"); // aa
		System.out.println();
		
		trie = new Trie();
		trie.insert("hello"); 
		trie.insert("dog"); 
		trie.insert("hell"); 
		trie.insert("cat"); 
		trie.insert("a"); 
		trie.insert("hel"); 
		trie.insert("help"); 
		trie.insert("helps"); 
		trie.insert("helping"); 
	    trie.printAutoSuggestions(trie.root, "hel"); 
	    System.out.println();
//	    hel
//	    hell
//	    hello
//	    help
//	    helping
//	    helps
		
	    
	    
		

	}
	
	// 5. Trie. 
    // O(S) time and space, where S is the number of all characters in the array. 
    public static String longestCommonPrefix5(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }
        
        // Create a Trie. 
        // O(S) time, O(S) space. 
        Trie trie = new Trie();
        for (int i = 1; i < strs.length; i++) {
            trie.insert(strs[i]);
        }
        
        // LCP Query: O(m) time, where m is strs[0] length. 
        String lcp = trie.searchLongestPrefix(strs[0]);
        
        return lcp;
    }
	
	
	
	
	

}

class TrieNode {
    // R links to node children. 
    private TrieNode[] links;
    private final int R = 26;
    private boolean isEnd;
    // Number of child nodes (non-null links). 
    private int size;
    
    public TrieNode() {
        links = new TrieNode[R];
        isEnd = false;
        size = 0;
    }
    
    public boolean containsKey(char c) {
        return links[c - 'a'] != null;
    }
    
    public TrieNode get(char c) {
        return links[c - 'a'];
    }
    
    public void put(char c, TrieNode node) {
        links[c - 'a'] = node;
        size++;
    }
    
    public void setEnd() {
        isEnd = true;
    }
    
    public boolean isEnd() {
        return isEnd;
    }
    
    public int getNumChild() {
        return size;
    }
}


class Trie {
    public TrieNode root;
    
    public Trie() {
        root = new TrieNode();      
    }
    
    // Inserts a word into the trie. O(m) time, O(m) space, where m is the word length. 
    public void insert(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char currChar = word.charAt(i);
            if (!node.containsKey(currChar)) {
                node.put(currChar, new TrieNode());
            }
            node = node.get(currChar);
        }
        node.setEnd();
    }
    
    // Search the word in the Trie. O(m) time, O(1) space, where m is the word length. 
    public boolean search(String word) {
        TrieNode node = getLastNode(word);
        return node != null && node.isEnd();
    }   
    // Return the last node. 
    public TrieNode getLastNode(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char currChar = word.charAt(i);
            if (!node.containsKey(currChar)) {  
                return null;
            }
            node = node.get(currChar);
        }
        
        return node;
    }
    
    // Returns true if there is any word in the trie
    // that starts with the given prefix. O(m) time, O(1) space, where m is the word length.
    public boolean startsWith(String prefix) {
        TrieNode node = getLastNode(prefix);
        return node != null;
    }
    
    public String searchLongestPrefix(String word) {
        TrieNode node = root;
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char currChar = word.charAt(i);
            if (node.containsKey(currChar) && node.getNumChild() == 1 && !node.isEnd()) {
                prefix.append(currChar);
                node = node.get(currChar);
            } else {
                return prefix.toString();
            }
        }   
        
        return prefix.toString();
    }
    
    
    // Print suggestions for given query prefix. 
    private String query = "";
    public int printAutoSuggestions(TrieNode root, String query) {
    	if (query.isEmpty()) {
    		System.out.println("Enter a non-empty string.");
			return 0;
		}
    	this.query = query;
    	
    	// Get the last node of the query. 
    	TrieNode lastNode = this.getLastNode(query);
    	if (lastNode == null) {
    		// There is no string that starts with this query. 
    		System.out.println("No string found.");
			return 0;
		}
    	
    	if (lastNode.isEnd() && lastNode.getNumChild() == 0) {
    		// The query is present as a word, and there is no subtree
    		// below the last node. 
    		System.out.println(query);
			return -1;
		}
    	
    	if (lastNode.getNumChild() != 0) {
//    		StringBuilder prefix = new StringBuilder(query);
    		suggestionsRecur(lastNode, "");
		}
    	
		return 1;
    }

    // Print auto-suggestions for given prefix. 
    // @param node  the last node in the prefix. 
	private void suggestionsRecur(TrieNode node, String curPrefix) {
		if (node.isEnd()) {
			System.out.println(query + curPrefix);
			// Don't return here. because there might be a child node ahead. 
//			return; // NG!
		}
		if (node.getNumChild() == 0) {
			// No child node. 
			return;
		}
		
		for (int i = 0; i < 26; i++) {
			char c = (char) (i + 'a');
			if (node.containsKey(c)) {	
//				curPrefix += c; // NG!
//				node = node.get(c); // NG!
				suggestionsRecur(node.get(c), curPrefix + c);
			}	
		}
	}
    
    
    
    
}






































