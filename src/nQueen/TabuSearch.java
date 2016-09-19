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
		int numberOfSolutions = HelpMethods.getNumberOfSolutions(size);
		int[] initialSolution = HelpMethods.initializeBoardWithoutRowConflicts(size);
		initialiseConflictsBySwap();
		buildConflictsBySwap(initialSolution);
		int[] oldSol = swap(initialSolution);
		Board board = new Board(oldSol);
		boolean foundSolution = board.checkIfLegal();
		if (foundSolution){
			ArrayList<int[]> rotationSol = HelpMethods.findDuplicateSolutions(oldSol);
			for (int[] is : rotationSol) {
				solutions = HelpMethods.addIfNotAlreadyInSolutionSet(is, solutions);
			}		
		}
		int c = 0;
		long startTime = System.nanoTime();
		long duration = 1;
		while (solutions.size()<numberOfSolutions && duration < 10){
			buildConflictsBySwap(oldSol);
			int[] newSolution = swap(oldSol);							
			board = new Board(newSolution);
			foundSolution = board.checkIfLegal();
			if (!foundSolution){
				c++;
			}
			else{
				ArrayList<int[]> rotationSol = HelpMethods.findDuplicateSolutions(oldSol);
				for (int[] is : rotationSol) {
					solutions = HelpMethods.addIfNotAlreadyInSolutionSet(is, solutions);
				}
			}
			oldSol = newSolution;
			long endTime = System.nanoTime();
			duration = (long) ((endTime - startTime)/(Math.pow(10, 9)));
		}
		System.out.println(board.toString());
		System.out.println("SOLUTIONS:");
		for (int[] is : solutions) {
			System.out.println(Arrays.toString(is));
		}
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
				int[] tempPos = queenPos.clone();
				int valueToChange = tempPos[i];
				tempPos[i] = tempPos[j];
				tempPos[j] = valueToChange;
				Board b = new Board(tempPos);
				int conflicts = (int)b.getCurrentHeuristicValue();
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
				int[] tempPos = queenPos.clone();
				int valueToChange = tempPos[i];
				tempPos[i] = tempPos[j];
				tempPos[j] = valueToChange;
				boolean check = false;
				for (int[] k : tabuList) {
					if (HelpMethods.compareArrays(k, tempPos)){
						check = true;
						break;
					}
				}
				if (conflictsBySwap[i][j] < best && !check){
					best = conflictsBySwap[i][j];
					ret[0] = i;
					ret[1] = j;
				}
			}
		}
		return ret;
	}

}
