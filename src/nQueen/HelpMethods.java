package nQueen;

import java.util.Arrays;

public class HelpMethods {


	public static int[] countConflicts (Board board){
		int[] diagonalConflicts = HelpMethods.countDiagonalConflicts(board);
		int[] rowConflicts = HelpMethods.countRowConflicts(board);
		int[] totalConflicts = new int[rowConflicts.length +diagonalConflicts.length];

		System.arraycopy(rowConflicts, 0, totalConflicts, 0, rowConflicts.length);
		System.arraycopy(diagonalConflicts, 0, totalConflicts, rowConflicts.length, diagonalConflicts.length);

		return totalConflicts;
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
		boolean[][] iterateableBoard = board.getBoard(); //M kunne iterere over brettet, s lager en peker som peker p brettet
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

	public static int[] countDiagonalConflicts(Board board){
		int numberOfDiagonals = (board.getSize()*2-3)*2;
		int[] indexedConflicts = new int[numberOfDiagonals];
		boolean[][] iterateableBoard = board.getBoard();

		for(int row = 0 ; row < board.getSize(); row++){ //legger til konflikter som ligger i diagonal opp til hoeyre
			for (int column = 0 ; column < board.getSize() ; column++){
				if(column+row>0 && column+row<board.getSize()*2 && iterateableBoard[row][column])
					indexedConflicts[row+column-1]+=1;
			}
		}
		int offset = numberOfDiagonals/2;
		for(int row = 0 ; row < board.getSize() ; row++){
			for(int column =0 ; column < board.getSize() ; column++) {
				int columnIndex = board.getSize() -column - 1;

				if(columnIndex+row>0 && columnIndex+row<board.getSize()*2-2 && iterateableBoard[row][column])
					indexedConflicts[offset - 1 + columnIndex+row]++;
			}
		}

		for(int i = 0 ; i < indexedConflicts.length ; i++) {
			if( indexedConflicts[i] == 1)
				indexedConflicts[i] = 0;
		}

		return indexedConflicts;


	}

}
