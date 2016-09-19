package nQueen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	
	//starter med aa initialisere en loesning, denne har ingen radkonflikter
	public static int[] initializeBoardWithoutRowConflicts(int size){
		int[] ret = new int[size];
		ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=1; i<size+1; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        for (int i=0; i<size; i++) {
        	ret[i] = list.get(i);
        }
        return ret;
	}
	
	//sjekker om en array er lik en annen array
	public static boolean compareArrays(int[] array1, int[] array2) {
        boolean b = true;
        if (array1 != null && array2 != null){
          if (array1.length != array2.length)
              b = false;
          else
              for (int i = 0; i < array2.length; i++) {
                  if (array2[i] != array1[i]) {
                      b = false;    
                  }                 
            }
        }else{
          b = false;
        }
        return b;
    }
	
	public static int[] findNewSolutionByRotatingRight(int[] queenPos){
		int[] ret = new int[queenPos.length];
		for (int i = 0; i < queenPos.length; i++) {
			ret[queenPos[i]-1] = queenPos.length-i;
		}
		if (new Board(ret).checkIfLegal()){
			return ret;
		}
		else{
			return null;
		}
	}
	
	public static ArrayList<int[]> findDuplicateByRotation(ArrayList<int[]> ret){
		for (int l = 0; l < 2; l++) {
			int[] newSol = findNewSolutionByRotatingRight(ret.get(l).clone());
			boolean check = false;
			for (int i = 0; i < ret.size(); i++) {
				if (compareArrays(ret.get(i), newSol)){
					check =true;
					break;
				}
			}
			if (!check){
				ret.add(newSol);			
			}
			check = false;
			for (int i = 0; i < 2; i++) {
				newSol = findNewSolutionByRotatingRight(newSol.clone());
				for (int j = 0; j < ret.size(); j++) {
					if (compareArrays(ret.get(j), newSol.clone())){
						check =true;
						break;
					}
				}
				if (!check){
					ret.add(newSol.clone());			
				}
			}
		}
		return ret;
	}
	
	public static ArrayList<int[]> findDuplicateSolutions(int[] queenPos){
		ArrayList<int[]> ret = new ArrayList<int[]>();
		ret.add(queenPos);
		int[] rotationBoard = findNewSolutionByMirroring(queenPos);
		ret.add(rotationBoard);
		ret = findDuplicateByRotation(ret);
		return ret;
	}
	
	public static int[] findNewSolutionByMirroring(int[] queenPos){
		int[] ret = new int[queenPos.length];
		for (int i = 0; i < queenPos.length; i++) {
			ret[i] = queenPos[queenPos.length-1-i];
		}
		if (new Board(ret).checkIfLegal()){
			return ret;
		}
		else{
			return null;
		}
	}
	
	//sjekker om denne loesningen allerede finnes i solutions
	public static ArrayList<int[]> addIfNotAlreadyInSolutionSet(int[] insert, ArrayList<int[]> solutions){
		boolean check = false;
		for (int j = 0; j < solutions.size(); j++) {
			if (HelpMethods.compareArrays(solutions.get(j), insert.clone())){
				check =true;
				break;
			}
		}
		if (!check){
			solutions.add(insert.clone());			
		}
		return solutions;
	}
	
	public static Integer getNumberOfSolutions(int size){
		if (size == 4){return 2;}
		else if (size == 5){return 10;}
		else if (size == 6){return 4;}
		else if (size == 7){return 40;}
		else if (size == 8){return 92;}
		else if (size == 9){return 352;}
		else if (size == 10){return 724;}
		else if (size == 11){return 2680;}
		else if (size == 12){return 14200;}
		else if (size == 13){return 73712;}
		else if (size == 14){return 365596;}
		else if (size == 15){return 2279184;}
		else if (size == 16){return 14772512;}
		else if (size == 17){return 95815104;}
		else {return 666090624;}
	}


}
