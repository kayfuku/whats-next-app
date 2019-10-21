What's Next App
============
Kei Fukutani

## Goal
This app allows user to know the tendency of following words.  
When user types in a word, this app displays a list of words that follow the word and the probability of the word following based on a text file(s).   

## Example 
This app shows the phrases and its frequency that show up in the text files of your choice. User types in the length of the consecutive words and prefix word(s). The example below shows that there are 80 "take care of" in the 30 books text files, followed by 14 "take charge of", 12 "take off your", and so on.  
  

<img src="images/example2.jpg" width="800">


## Design 
* **File Parser** reads lines from a text file using Text Analyzer. 
* **Text Analyzer** splits lines into words and keeps track of the count of the next word. It also computes the probability of the next word showing up. The probability is computed as the following.  
    *Probability* = (The count of the next word) / (The sum of the counts of all the next words)

* **Data Structure** stores every consecutive words in all the text files whose length is specified in Trie data structure. 
<img src="images/trie_image.jpg" width="800">

* **User Interface** provides a user with text field that the user type in a word and button that displays the result.  
This runs in a separate thread from main thread. 

<img src="images/design1.jpeg" width="800">


## User Interface
First, user presses the button "Select Books" , and selects text files. Then, press the button "Read" on the panel to the right of main panel. The app starts reading the text files the user has selected. After the app has finished reading those books, the user enters a word in the text field and presses the submit button, and this app displays a list of the next words and the probability. 

<img src="images/ui1.jpeg" width="600">
<img src="images/ui2.jpeg" width="600">
<img src="images/ui3.jpeg" width="800">


## Implementation 
* In the **Data Structure**, Map1 maps one word to Map2 that maps the next word to its count. 
* The **User Interface** is provided by the javax.swing package which contains JFrame, JPanel, JTextfield, etc. 





