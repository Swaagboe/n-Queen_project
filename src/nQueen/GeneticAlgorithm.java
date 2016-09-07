package nQueen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class GeneticAlgorithm {

	private ArrayList<int[]> randomSolutions;
	private ArrayList<int[]> solutions;
	private HashMap<Integer, Integer> numberOfSolutionsTable;
	private final double INF = Double.MAX_VALUE;
	private int numberOfSol;
	private int size;

	public GeneticAlgorithm(Board board){
		randomSolutions = new ArrayList<int[]>();
		solutions = new ArrayList<int[]>();
		numberOfSolutionsTable = new HashMap<Integer, Integer>();
		generateNumberOfSolutions();
		numberOfSol = numberOfSolutionsTable.get(board.getQueenPosition().length);
		size = board.getQueenPosition().length;
		while (1 != solutions.size()) {
			randomSolutions = generateRandomSolutions(numberOfSol);
			for (int[] sol : randomSolutions) {
				Board b = new Board(sol);			
				if (b.checkIfLegal()){
					if (!solutions.contains(sol)){
						solutions.add(sol.clone());
					}
				}
			}
//			System.out.println(solutions.size());
			doFitnessEvaluation();			
		}
		for (int[] sol : solutions) {
			System.out.println(Arrays.toString(sol));
		}
		System.out.println("Number of solutions for " + size + "x" + size + ": " + solutions.size());
	}

	public void generateNumberOfSolutions(){
		numberOfSolutionsTable.put(1, 0);
		numberOfSolutionsTable.put(2, 0);
		numberOfSolutionsTable.put(3, 0);
		numberOfSolutionsTable.put(4, 2);
		numberOfSolutionsTable.put(5, 10);
		numberOfSolutionsTable.put(6, 4);
		numberOfSolutionsTable.put(7, 40);
		numberOfSolutionsTable.put(8, 92);
		numberOfSolutionsTable.put(9, 352);
		numberOfSolutionsTable.put(10, 724);
		numberOfSolutionsTable.put(11, 724);
		numberOfSolutionsTable.put(12, 724);
		numberOfSolutionsTable.put(13, 724);
		numberOfSolutionsTable.put(14, 724);
		numberOfSolutionsTable.put(15, 724);
		numberOfSolutionsTable.put(16, 724);
		numberOfSolutionsTable.put(17, 724);
		numberOfSolutionsTable.put(18, 724);
		numberOfSolutionsTable.put(19, 724);
		numberOfSolutionsTable.put(20, 724);
		numberOfSolutionsTable.put(21, 724);
		numberOfSolutionsTable.put(22, 724);
		numberOfSolutionsTable.put(23, 724);
		numberOfSolutionsTable.put(24, 724);
		numberOfSolutionsTable.put(25, 724);
		numberOfSolutionsTable.put(26, 724);
		numberOfSolutionsTable.put(27, 724);
		numberOfSolutionsTable.put(28, 724);
		numberOfSolutionsTable.put(29, 724);
		numberOfSolutionsTable.put(30, 724);
	}

	public ArrayList<int[]> generateRandomSolutions(int numberOfSol){
		ArrayList<int[]> ret = new ArrayList<int[]>();
		for (int i = 0; i < numberOfSol; i++) {
			int[] randomSol = generateRandomList(1, size);
			ret.add(randomSol);
		}
//		for (int[] sol : ret) {
//			System.out.println(Arrays.toString(sol));
//		}
//		System.out.println(ret.size());
		return ret;
	}

	
	public int[] generateRandomList(int min, int max){
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

	public void doFitnessEvaluation(){
		int[] conflicts = new int[randomSolutions.size()];
		int i = 0;
		double sum = 0;
		for (int[] sol : randomSolutions) {
			Board b = new Board(sol);
			conflicts[i] = (int)b.getCurrentHeuristicValue();
			sum+= conflicts[i];
			i++;
		}
		double[] fitnessEvaluation = new double[randomSolutions.size()];
		for (int j = 0; j < fitnessEvaluation.length; j++) {
			fitnessEvaluation[j] = (double)conflicts[j]/sum;
		}

		selection(fitnessEvaluation);
	}



	public void selection(double[] fitnessEvaluation){
		int length = 0;
		if (fitnessEvaluation.length%2 == 1){
			length = (fitnessEvaluation.length/2)+1;			
		}
		else{
			length = fitnessEvaluation.length/2;						
		}
		ArrayList<int[]> newRandomSolution = new ArrayList<int[]>();
		int veryBest = 0;
		for (int j = 0; j < length; j++) {
			double best = 1;
			int bestPosition = 0;
			for (int j2 = 0; j2 < fitnessEvaluation.length; j2++) {
				if (fitnessEvaluation[j2] <= best){
					best = fitnessEvaluation[j2];
					bestPosition = j2;
				}		
			}
			if (j == 0){
				veryBest = bestPosition;
			}
//			System.out.println("best: " + best);
//			System.out.println("bestPos: " + bestPosition);
			fitnessEvaluation[bestPosition] = INF;
			newRandomSolution.add(randomSolutions.get(bestPosition));
			newRandomSolution.add(randomSolutions.get(veryBest));
		}
//		System.out.println();
//		for (int[] is : newRandomSolution) {
//
//			System.out.println(Arrays.toString(is));
//		}
//		System.out.println();
		doCrossover(newRandomSolution);
	}

	public void doCrossover(ArrayList<int[]> newRandomSolution){
		for (int i = 0; i < newRandomSolution.size(); i+=2) {
			int[] newQueenPos1 = new int[size];
			int[] newQueenPos2 = new int[size];
			int r = HelpMethods.generateRandomNumer(1, size);
			boolean check = true;
			while (check){
				r = HelpMethods.generateRandomNumer(1, size);
				//System.out.println("r: " + r);
				int[] list1 = new int[r];
				int[] list2 = new int[r];
				for (int j = 0; j < r; j++) {
					list1[j] = newRandomSolution.get(i)[j];
					list2[j] = newRandomSolution.get(i+1)[j];
				}
				Arrays.sort(list1);
				Arrays.sort(list2);
				boolean listCheck = true;
				for (int j = 0; j < r; j++) {
					if (list1[j] != list2[j]){
						listCheck = false;
						break;
					}
				}
//				System.out.println("list1: "+Arrays.toString(list1));
//				System.out.println("list2: "+Arrays.toString(list2));
				if (listCheck){
					check = false;
				}
			}
//			System.out.println("Random: " + r);
			for (int j = 0; j < r; j++) {
				newQueenPos1[j] = newRandomSolution.get(i)[j];
				newQueenPos2[j] = newRandomSolution.get(i+1)[j];
			}
			for (int j = r; j < size; j++) {
				newQueenPos1[j] = newRandomSolution.get(i+1)[j];
				newQueenPos2[j] = newRandomSolution.get(i)[j];
			}
			newRandomSolution.set(i, newQueenPos1);
			newRandomSolution.set(i+1, newQueenPos2);
		}
//		for (int[] is : newRandomSolution) {
//			System.out.println(Arrays.toString(is));
//		}
		doMutation(newRandomSolution);

	}

	public void doMutation(ArrayList<int[]> newRandomSolution){
		for (int[] is : newRandomSolution) {
			int c = HelpMethods.generateRandomNumer(1, 6);
			if (c == 2){//doing mutation with a small independent probability
				int col = HelpMethods.generateRandomNumer(0, size-1);
				int newRow = HelpMethods.generateRandomNumer(1, size);
				is[col] = newRow;
			}

		}
		randomSolutions = newRandomSolution;
	}

}
