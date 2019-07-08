package kay.app;

public class DriverSideProj {
	
	

	public static void main(String[] args) {
		
		
		String inputFile = "";
		
		NextWordsHolder nextWordsHolder = new NextWordsHolder();
		PhrasesTrie phrasesTrie = new PhrasesTrie();
		FileParser fileParser = new FileParser(nextWordsHolder, phrasesTrie);
		
		fileParser.readFile(inputFile);
		
		

	}

}
