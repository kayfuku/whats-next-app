What's Next App
============
Kei Fukutani

## Goal
This app allows user to know the tendency of following words.  
When user types in word(s), this app displays a list of words or phrase that follow the word(s) and the probability of the words or phrase following based on a text file(s).   

## Example 
This app shows the phrases and its frequency that show up in the text files of your choice. User types in the length of the consecutive words and prefix word(s). The example below shows that there are 80 "take care of" in the 30 books text files, followed by 14 "take charge of", 12 "take off your", and so on.  
  

<img src="images/example2.jpg" width="800">


## Design 
* **File Parser** reads lines from a text file using Text Analyzer. 
* **Text Analyzer** splits lines into words and keeps track of the count of the next word. It also computes the probability of the next word showing up. The probability is computed as the following.  
    *Probability* = (The count of the next word) / (The sum of the counts of all the next words)

* **Data Structure** stores fixed length consecutive words in Trie data structure. Each Trie node has its own word and a hash map, where the key is the word and the value is the link to the next Trie node.  
I use Trie because Trie performs better than hash map when it comes to finding all the words which have a common prefix.   

<img src="images/trie_image.jpg" width="600">

* **User Interface** provides a user with text field that the user type in a word and button that displays the result.  
This runs in a separate thread from main thread. 

<img src="images/design1.jpeg" width="800">


## User Interface
First, user enters a length of window that scans the text files, and next, user presses the button "Select Books", and selects text files. Then, press the button "Read" on the panel to the right of main panel. The app starts reading the text files the user has selected. After the app has finished reading those books, the user enters word(s) in the text field and presses the submit button. This app displays a list of words or phrase that follow the word(s) and the probability.  

<img src="images/ui1.jpg" width="600">
<img src="images/ui2.jpg" width="600">
<img src="images/example2.jpg" width="800">




