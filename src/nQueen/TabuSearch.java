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
	private boolean stepByStep;

	public TabuSearch(Board b, boolean stepByStep){
		this.stepByStep = stepByStep;
		solutions = new ArrayList<int[]>();
		this.size = b.getSize();
		tabuList = new LinkedList<int[]>();
		int numberOfSolutions = HelpMethods.getNumberOfSolutions(size);
		int[] initialSolution = b.getQueenPositions();
		if (stepByStep){
			System.out.println("Initial solution:");
			System.out.println(Arrays.toString(initialSolution));
		}
		initialSolution = HelpMethods.modifyInitialSolutionIfRowConflicts(initialSolution.clone());
		if (stepByStep){
			System.out.println("Modifying input to no row conflicts:");
			System.out.println(Arrays.toString(initialSolution));
			System.out.println();
		}
		tabuList.add(initialSolution);
		if (stepByStep){
			System.out.println("Tabu list:");
			for (int[] sol : tabuList) {
				System.out.println(Arrays.toString(sol));
			}
			System.out.println();
		}
		conflictsBySwap = new int[size][size];
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
		while (solutions.size()<1000 && duration < 10){
			if (stepByStep){
				System.out.println("Tabu list:");
				for (int[] sol : tabuList) {
					System.out.println(Arrays.toString(sol));
				}
				System.out.println();
			}
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
		System.out.println("SOLUTIONS:");
		for (int[] is : solutions) {
			System.out.println(Arrays.toString(is));
		}
		System.out.println("Number of solutions for " + size + "x" + size + ": " + solutions.size());
		System.out.println("Number of iterations: " + c);

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
		if (stepByStep){
			System.out.println("Neighbours: ");
		}
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
				if (stepByStep){
					System.out.println(Arrays.toString(tempPos));
				}
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
		if (stepByStep){
			System.out.println();			
		}
		return ret;
	}
	
	public static Board init(String input){
		String[] queenPosString = input.split(" ");
		int[] queensPosition = new int[queenPosString.length];
		for (int i = 0; i < queenPosString.length; i++) {
			queensPosition[i] = Integer.parseInt(queenPosString[i]);
		}
		return new Board(queensPosition);
	}
	
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		if (args.length == 0){
			String input = "1 2 3 4 6 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30";
			Board b = TabuSearch.init(input);
			new TabuSearch(b, false);					
		}
		else if (args[1].equals("true")){
			System.out.println("Step by step:");
			String input = args[0];
			Board b = TabuSearch.init(input);
			new TabuSearch(b, true);						
		}
		else{
			String input = args[0];
			Board b = TabuSearch.init(input);
			new TabuSearch(b, false);								
		}
		long endTime = System.nanoTime();
		long duration = (long) ((endTime - startTime)/(Math.pow(10, 6)));
		System.out.println("Time: " + (double)duration/1000 + " sec");
	}

}
