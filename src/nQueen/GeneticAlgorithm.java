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
	private HashMap<Integer, int[]> ranking;
	private final double INF = Double.MAX_VALUE;
	private int numberOfSol;
	private int size;
	private int mutationRate;

	public GeneticAlgorithm(Board board, int mutationRate){
		randomSolutions = new ArrayList<int[]>();
		this.mutationRate = mutationRate;
		solutions = new ArrayList<int[]>();
		numberOfSolutionsTable = new HashMap<Integer, Integer>();
		ranking = new HashMap<Integer, int[]>();
//		numberOfSol = numberOfSolutionsTable.get(board.getQueenPosition().length);
		numberOfSol = 100;
		size = board.getQueenPosition().length;
		randomSolutions = generateRandomSolutions(numberOfSol);
		int iterations = 0;
		while (solutions.size() < 1) {
			for (int[] sol : randomSolutions) {
				Board b = new Board(sol);
				if (b.checkIfLegal()){
					if (!solutions.contains(sol)){
						solutions.add(sol.clone());
					}
				}
			}
			iterations++;
			doFitnessEvaluation();	
			
		}
		for (int[] sol : solutions) {
			System.out.println(Arrays.toString(sol));
		}
		System.out.println("Number of solutions for " + size + "x" + size + ": " + solutions.size());
		System.out.println("Number of iterations: " + iterations);
	}

	public ArrayList<int[]> generateRandomSolutions(int numberOfSol){
		ArrayList<int[]> ret = new ArrayList<int[]>();
		for (int i = 0; i < numberOfSol; i++) {
			int[] randomSol = generateRandomList(1, size);
			ret.add(randomSol);
		}
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
		ArrayList<int[]> newRandomSolution = new ArrayList<int[]>();
		//Setter length til 20 og velger da ut bare de 20 beste brettene.
		length = randomSolutions.size()/2;
		int c = 0;
		for (int j = 0; j < length; j++) {
			double best = 1;
			int bestPosition = 0;
			for (int j2 = 0; j2 < fitnessEvaluation.length; j2++) {
				if (fitnessEvaluation[j2] <= best){
					best = fitnessEvaluation[j2];
					bestPosition = j2;
				}		
			}
			fitnessEvaluation[bestPosition] = INF;
			ranking.put(c, randomSolutions.get(bestPosition));
			c++;
		}
		//Legger til de 50% beste fra randomSolutions
		for (int i = 0; i < ranking.size(); i++) {
			newRandomSolution.add(ranking.get(i));
		}
		//Legger til de 50% beste fra randomSolutions men denne gang slik: 1st, 20th, 2nd, 19th, 3rd, 18th, ...
		for (int i = 0; i < ranking.size()/2; i++) {
			newRandomSolution.add(ranking.get(i));
			newRandomSolution.add(ranking.get(ranking.size()-1));
		}
		//System.out.println(newRandomSolution);
		doCrossover2(newRandomSolution);
	}
	
	public void doCrossover2(ArrayList<int[]> newRandomSolution){
		int a;
		if (size%2 == 1){
			a = (size/2+1);
		}
		else{
			a = size/2;
		}
		for (int i = 0; i < newRandomSolution.size(); i+=2) {
			int[] newQueen1 = newRandomSolution.get(i).clone();
			int[] newQueen2 = newRandomSolution.get(i+1).clone();
			int r = HelpMethods.generateRandomNumer(1, a/2)*2;
			ArrayList<Integer> flipList = new ArrayList<Integer>();
			ArrayList<Integer> posToFlip = new ArrayList<Integer>();
			for (int j = 0; j < r; j++) {
				int positionToFlip = HelpMethods.generateRandomNumer(0, size-1);
				flipList.add(newQueen1[positionToFlip]);
				posToFlip.add(positionToFlip);
			}
			for (int j = 0; j < r; j+=2) {
				int queen1ToFlip1 = flipList.get(j);
				int queen1ToFlip2 = flipList.get(j+1);
				for (int k = 0; k < newQueen2.length; k++) {
					if (newQueen1[k] == queen1ToFlip1){
						newQueen1[k] = queen1ToFlip2;
						continue;
					}
					if (newQueen1[k] == queen1ToFlip2){
						newQueen1[k] = queen1ToFlip1;
						continue;
					}
				}
				for (int k = 0; k < newQueen2.length; k++) {
					if (newQueen2[k] == queen1ToFlip1){
						newQueen2[k] = queen1ToFlip2;
						continue;
					}
					if (newQueen2[k] == queen1ToFlip2){
						newQueen2[k] = queen1ToFlip1;
						continue;
					}				
				}
			}
			newRandomSolution.set(i, newQueen1);
			newRandomSolution.set(i+1, newQueen2);
		}
		doMutation(newRandomSolution);
	}

	public void doMutation(ArrayList<int[]> newRandomSolution){
		for (int[] is : newRandomSolution) {
			Board b = new Board(is);
			if (b.checkIfLegal()){
				break;
			}
			int a = HelpMethods.generateRandomNumer(1, 100);
			if (a > mutationRate){//doing mutation with a small independent probability
				int pos1 = HelpMethods.generateRandomNumer(0, size-1);
				int pos2 = HelpMethods.generateRandomNumer(0, size-1);
				int save = is[pos1];
				is[pos1] = is[pos2];
				is[pos2] = save;
			}
			

		}
		randomSolutions = newRandomSolution;
	}

}
