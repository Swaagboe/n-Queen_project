package nQueen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class TabuSearch {
	
	private ArrayList<int[]> solutions;
	private int size;
	private final int INF = Integer.MAX_VALUE;
	private int maxTabuSize = 1000;
	private Queue<int[]> tabuList;
	private int[][] conflictsBySwap;
	
	public TabuSearch(Board b){
		solutions = new ArrayList<int[]>();
		this.size = b.getSize();
		tabuList = new LinkedList<int[]>();
		int[] initialSolution = HelpMethods.initializeBoardWithoutRowConflicts(size);
		initialiseConflictsBySwap();
		buildConflictsBySwap(initialSolution);
		int[] oldSol = swap(initialSolution);
		Board board = new Board(oldSol);
		boolean foundSolution = board.checkIfLegal();
		int c = 0;
//		System.out.println("Old sol: " +Arrays.toString(oldSol));
		while (!foundSolution){
			buildConflictsBySwap(oldSol);
			int[] newSolution = swap(oldSol);							
			board = new Board(newSolution);
			foundSolution = board.checkIfLegal();
			if (!foundSolution){
				c++;
			}
			else{
				solutions.add(newSolution);
			}
			oldSol = newSolution;
		}
		System.out.println(board.toString());
		System.out.println(Arrays.toString(oldSol));
		System.out.println("Number of solutions for " + size + "x" + size + ": " + solutions.size());
		System.out.println("Number of iterations: " + c);
	
	}
	
	//initialiserer konfliktmatrisen for naboene
	public void initialiseConflictsBySwap(){
		conflictsBySwap = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = i+1; j < size; j++) {
				conflictsBySwap[i][j] = -1;
			}
		}
	}
	
	//finner antall konflikter ved aa foreta et dronningbytte med plass i og j
	public void buildConflictsBySwap(int[] queenPos){
		for (int i = 0; i < queenPos.length; i++) {
			for (int j = i+1; j < queenPos.length; j++) {
				int[] tempPos = queenPos;
				int valueToChange = tempPos[i];
				tempPos[i] = tempPos[j];
				tempPos[j] = valueToChange;
				Board b = new Board(tempPos);
				int conflicts = (int)b.getCurrentHeuristicValue();
				tempPos[j] = tempPos[i];
				tempPos[i] = valueToChange;
				conflictsBySwap[i][j] = conflicts;
			}
		}
	}
	
	//lager et nytt board ved aa finne beste nabo
	public int[] swap(int[] queenPos){
		int[] swap = findBestSwap(queenPos);
		int save = queenPos[swap[0]];
		int[] oldPos = queenPos.clone();
		queenPos[swap[0]] = queenPos[swap[1]];
		queenPos[swap[1]] = save;
		if (tabuList.size() >= maxTabuSize){
			tabuList.poll();
			tabuList.add(oldPos);
		}
		else{
			tabuList.add(oldPos);
		}
		return queenPos;
	}
	
	//finner de beste dronningene aa bytte. De som gir fearrrest konflikter. 
	public int[] findBestSwap(int[] queenPos){
		int[] ret = new int[2];
		ret[0] = -1;
		int best = INF;
		for (int i = 0; i < size; i++) {
			for (int j = i+1; j < size; j++) {
				int[] tempPos = queenPos;
				int valueToChange = tempPos[i];
				tempPos[i] = tempPos[j];
				tempPos[j] = valueToChange;
				boolean check = false;
				for (int[] k : tabuList) {
					if (compareArrays(k, tempPos)){
						check = true;
						break;
					}
				}
				if (conflictsBySwap[i][j] < best && !check){
					best = conflictsBySwap[i][j];
					ret[0] = i;
					ret[1] = j;
				}
				tempPos[j] = tempPos[i];
				tempPos[i] = valueToChange;
			}
		}
		return ret;
	}
	
	//sjekker om en array er lik en annen array
	public boolean compareArrays(int[] array1, int[] array2) {
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

}
