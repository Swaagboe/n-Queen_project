package nQueen;

public class HelpMethods {
	
	public static countConflicts (Board board){
		
	}
	
	public static int[] countRowConflicts(Board board){
		int numberOfRows = board.getSize(); //Lager en variabel for antall rader
		int[] queenPositions = board.getQueenPosition(); //Gjenbruker kode for dronningposisjoner som allerede er indeksert etter rad
		int[] indexedConflicts = new int[numberOfRows]; //Lager listen som returneres. Indeksert etter rad, og gir antall konflikter i den raden
		
		for(int i = 0 ; i < numberOfRows ; i++) {
			indexedConflicts[queenPositions[i]-1] +=1;
		}
		
		for(int column = 0 ; column < numberOfRows ; column++) {
			if (indexedConflicts[column]!=0)
				indexedConflicts[column] -=1;
		}
		
		return indexedConflicts;
		
	}
	
	public static int[] countColumnConflicts(Board board){
		int numberOfColumns = board.getSize(); //Lager en variabel for antall kolonner
		boolean[][] iterateableBoard = board.getBoard(); //MŒ kunne iterere over brettet, sŒ lager en peker som peker pŒ brettet
		int[] indexedConflicts = new int[numberOfColumns];
		
		for(int row = 0 ; row < numberOfColumns ; row++) {
			for(int column = 0 ; column < numberOfColumns ; column++){
				if(iterateableBoard[row][column]){
						indexedConflicts[column]+=1;
				}
			}
		}
		
		for(int column = 0 ; column < numberOfColumns ; column++) {
			if (indexedConflicts[column]!=0)
				indexedConflicts[column] -=1;
		}
		
		return indexedConflicts;
	}

}
