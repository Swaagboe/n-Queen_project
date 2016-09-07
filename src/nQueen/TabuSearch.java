package nQueen;

import java.awt.List;
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
		int[] initialSolution = initialSolution();
		initialiseConflictsBySwap();
		buildConflictsBySwap(initialSolution);
		int[] oldSol = swap(initialSolution);
		Board board = new Board(oldSol);
		boolean foundSolution = board.checkIfLegal();
		int c = 0;
		while (!foundSolution){
			int[] newSolution = swap(oldSol);
			buildConflictsBySwap(newSolution);
			newSolution = swap(newSolution);							
			board = new Board(newSolution);
			foundSolution = board.checkIfLegal();
			if (!foundSolution){
				
				c = 0;
			}
			else{
				c++;
				System.out.println(c);
			}
			oldSol = newSolution;
		}
		System.out.println(board.toString());
		System.out.println(Arrays.toString(oldSol));
	
	}
	
	public int[] initialSolution(){
		int[] ret = new int[size];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = i+1;
		}
		return ret;
	}
	
	public void initialiseConflictsBySwap(){
		conflictsBySwap = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = i+1; j < size; j++) {
				conflictsBySwap[i][j] = -1;
			}
		}
	}
	
	
	public void buildConflictsBySwap(int[] queenPos){
		for (int i = 0; i < queenPos.length; i++) {
			for (int j = i+1; j < queenPos.length; j++) {
				int[] tempPos = queenPos;
				int valueToChange = tempPos[i];
				tempPos[i] = tempPos[j];
				tempPos[j] = valueToChange;
				Board b = new Board(tempPos);
				int[] rowConflicts = HelpMethods.countRowConflicts(b);
				int[] diagConflicts = HelpMethods.countDiagonalConflicts(b);
				tempPos[j] = tempPos[i];
				tempPos[i] = valueToChange;
				int countRow = 0;
				int countDiag = 0;				
				for (int k = 0; k < rowConflicts.length; k++) {
					countRow += rowConflicts[j];
				}
				for (int k = 0; k < diagConflicts.length; k++) {
					countDiag += diagConflicts[k];
				}
				conflictsBySwap[i][j] = countRow + countDiag;
			}
		}
	}
	
	public int[] swap(int[] queenPos){
		int[] swap = findBestSwap(queenPos);
		int save = queenPos[swap[0]];
		queenPos[swap[0]] = queenPos[swap[1]];
		queenPos[swap[1]] = save;
		System.out.println("NEW POSITION: " + Arrays.toString(queenPos));
		if (tabuList.size() >= maxTabuSize){
			tabuList.poll();
			tabuList.add(queenPos);
		}
		else{
			tabuList.add(queenPos);
		}
		System.out.println("Tabulist: ");
		for (int[] list : tabuList) {
			System.out.println(Arrays.toString(list));
		}
		return queenPos;
	}
	
	public int[] findBestSwap(int[] queenPos){
		int[] ret = new int[2];
		ret[0] = -1;
		int best = INF;
		int[] finalPos = null;
		System.out.println("Queenpos: " + Arrays.toString(queenPos));
		for (int i = 0; i < size; i++) {
			for (int j = i+1; j < size; j++) {
				int[] tempPos = queenPos;
				int valueToChange = tempPos[i];
				tempPos[i] = tempPos[j];
				tempPos[j] = valueToChange;
				System.out.println("Temp: "+Arrays.toString(tempPos));
				System.out.println("Conflicts: " + conflictsBySwap[i][j]);
				if (conflictsBySwap[i][j] < best && !tabuList.contains(tempPos)){
					best = conflictsBySwap[i][j];
					ret[0] = i;
					ret[1] = j;
					finalPos = tempPos;
					System.out.println("Swapped");
				}
			}
		}
//		if (tabuList.size() >= maxTabuSize){
//			tabuList.poll();
//			tabuList.add(finalPos);
//		}
//		else{
//			tabuList.add(finalPos);
//		}
//		System.out.println("Tabulist: ");
//		for (int[] list : tabuList) {
//			System.out.println(Arrays.toString(list));
//		}
		System.out.println("To be replaced: " + Arrays.toString(ret));
		return ret;
	}
	
	public boolean isInTabuList(){
		boolean ret = false;
		
		return ret;
	}

}
