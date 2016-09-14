package nQueen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HelpMethods {


	public static int[] countConflicts (Board board){
		int[] diagonalConflicts = HelpMethods.countDiagonalConflictsFast(board.getQueenPositions());
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
			int rowIndex = board.getSize() - board.getQueenPositions()[i];
			if(queenPositions[i] != 0){
				indexedConflicts[rowIndex] +=1;
			}
		}
		for(int column = 0 ; column < numberOfRows ; column++) {
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
		int numberOfDiagonals = (board.getSize()*2-2)*2;
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
				//System.out.println((columnIndex));

				if(columnIndex+row>0 && columnIndex+row<board.getSize()*2-2 && iterateableBoard[row][column])
					indexedConflicts[offset - 1 + columnIndex+row]++;
			}
		}

		for(int i = 0 ; i < indexedConflicts.length ; i++) {
			indexedConflicts[i] -= 1 ;
		}

		return indexedConflicts;
	}

	public static int[] countDiagonalConflictsFast(int[] queenPositions) {
		int numberOfDiagonals = (queenPositions.length*2-2)*2;
		int[] indexedConflicts = new int[numberOfDiagonals];

		for(int i = 0 ; i < queenPositions.length ; i++){ //legger inn konflikter i diagonal opp til hoyre
			int rowIndex = queenPositions.length - queenPositions[i] - 1;
			int columnIndex = i;

			if(rowIndex+columnIndex > 0 && rowIndex + columnIndex < queenPositions.length*2){
				indexedConflicts[rowIndex+columnIndex]+=1;
			}
		}

		int offset = numberOfDiagonals/2;

		for(int i = 0 ; i < queenPositions.length ; i++) {
			int rowIndex = queenPositions.length - queenPositions[i];
			//System.out.println(rowIndex +"    min metode");
			int columnIndex = queenPositions.length - i - 1;

			if(columnIndex + rowIndex > 0 && columnIndex + rowIndex < queenPositions.length*2-2) {
				indexedConflicts[offset  + rowIndex + columnIndex-1]+=1;
			}

		}

		for(int i = 0 ; i < indexedConflicts.length ; i++) {
			indexedConflicts[i] -= 1 ;
		}

		return indexedConflicts;

	}

	public static Board[] createNeighbours(Board board, int numberOfNeighbours) {
		Random random = new Random();
		Board[] neighbours = new Board[numberOfNeighbours];
		for(int i = 0 ; i < numberOfNeighbours ; i++) {
			neighbours[i] = new Board(board.getQueenPositions());
			HelpMethods.switchTwoQueensAtRandom(neighbours[i].getQueenPositions());
			neighbours[i].updateConflicts();
			neighbours[i].updateHeuristicValue();

		}
		return neighbours;


	}

	public static int simpleHeuristic(Board board) {
		int heuristicValue = 0;
		int[] conflicts = HelpMethods.countConflicts(board);
		for(int i = 0 ; i < conflicts.length ; i++){
			if(conflicts[i] > 0)
				heuristicValue +=conflicts[i];

		}

		return heuristicValue;
	}

	public static int generateRandomNumer(int min, int max){
		Random rand = new Random();
		return rand.nextInt((max - min) +1)+min;

	}

	public static int[] switchTwoQueensAtRandom(int[] list){
		int pos1 = HelpMethods.generateRandomNumer(0, list.length-1);
		int pos2 = HelpMethods.generateRandomNumer(0, list.length-1);
		int save = list[pos1];
		list[pos1] = list[pos2];
		list[pos2] = save;
		return list;


	}


}
