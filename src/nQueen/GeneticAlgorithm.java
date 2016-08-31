package nQueen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class GeneticAlgorithm {
	
	private ArrayList<int[]> solutions;
	private HashMap<Integer, Integer> numberOfSolutions;
	private int[] conflicts; //number of conflicts on board from solutions.get(i)
	
	public GeneticAlgorithm(Board board){
		solutions = new ArrayList<int[]>();
		numberOfSolutions = new HashMap<Integer, Integer>();
		generateNumberOfSolutions();
		int numberOfSol = numberOfSolutions.get(board.getQueenPosition().length);
		int size = board.getQueenPosition().length;
		conflicts = new int[numberOfSol];
		generateRandomSolutions(numberOfSol, size);
		doFitnessEvaluation();
		selection();
	}
	
	public void generateNumberOfSolutions(){
		numberOfSolutions.put(1, 0);
		numberOfSolutions.put(2, 0);
		numberOfSolutions.put(3, 0);
		numberOfSolutions.put(4, 2);
		numberOfSolutions.put(5, 10);
		numberOfSolutions.put(6, 4);
		numberOfSolutions.put(7, 40);
		numberOfSolutions.put(8, 92);
		numberOfSolutions.put(9, 352);
		numberOfSolutions.put(10, 724);
		numberOfSolutions.put(11, 2680);
		numberOfSolutions.put(12, 14200);
		numberOfSolutions.put(13, 73712);
		numberOfSolutions.put(14, 365596);
		numberOfSolutions.put(15, 2279184);
	}
	
	public void generateRandomSolutions(int numberOfSol, int size){
		Random rand = new Random();
		for (int i = 0; i < numberOfSol; i++) {
			int[] randomSol = new int[size];
			for (int j = 0; j < size; j++) {
				int r = rand.nextInt((size - 1) +1)+1;
				randomSol[j] = r;
			}
			solutions.add(randomSol);
		}
		for (int[] sol : solutions) {
			System.out.println(Arrays.toString(sol));
		}
		System.out.println(solutions.size());
	}
	
	public void doFitnessEvaluation(){
		int i = 0;
		for (int[] sol : solutions) {
			Board b = new Board(sol);
			int numberOfConflicts;
			//conflicts[i] = numberOfConflicts;
			i++;
		}
	}
	
	public void selection(){
		
	}

}
