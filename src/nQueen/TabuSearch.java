package nQueen;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
		int[] initialSolution = initialSolution(1, size);
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
	
	public int[] initialSolution(int min, int max){
		int[] ret = new int[size];
		ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=min; i<max+1; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        for (int i=0; i<max; i++) {
        	ret[i] = list.get(i);
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
				int conflicts = (int)b.getCurrentHeuristicValue();
				tempPos[j] = tempPos[i];
				tempPos[i] = valueToChange;
				conflictsBySwap[i][j] = conflicts;
			}
		}
	}
	
	public int[] swap(int[] queenPos){
		int[] swap = findBestSwap(queenPos);
		int save = queenPos[swap[0]];
		int[] oldPos = queenPos.clone();
		queenPos[swap[0]] = queenPos[swap[1]];
		queenPos[swap[1]] = save;
//		System.out.println("NEW POSITION: " + Arrays.toString(queenPos));
		if (tabuList.size() >= maxTabuSize){
			tabuList.poll();
			tabuList.add(oldPos);
		}
		else{
			tabuList.add(oldPos);
		}
//		System.out.println("Tabulist: ");
//		for (int[] list : tabuList) {
//			System.out.println(Arrays.toString(list));
//		}
		return queenPos;
	}
	
	public int[] findBestSwap(int[] queenPos){
		int[] ret = new int[2];
		ret[0] = -1;
		int best = INF;
		int[] finalPos = null;
//		System.out.println("Queenpos: " + Arrays.toString(queenPos));
		for (int i = 0; i < size; i++) {
			for (int j = i+1; j < size; j++) {
				int[] tempPos = queenPos;
				int valueToChange = tempPos[i];
				tempPos[i] = tempPos[j];
				tempPos[j] = valueToChange;
//				System.out.println("Temp: "+Arrays.toString(tempPos));
//				System.out.println("Conflicts: " + conflictsBySwap[i][j]);
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
					finalPos = tempPos;
//					System.out.println("Swapped");
				}
				tempPos[j] = tempPos[i];
				tempPos[i] = valueToChange;
			}
		}
//		System.out.println("To be replaced: " + Arrays.toString(ret));
		return ret;
	}
	
	public boolean isInTabuList(){
		boolean ret = false;
		
		return ret;
	}
	
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
