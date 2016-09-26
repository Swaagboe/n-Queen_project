package nQueen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GeneticAlgorithm {

	private ArrayList<int[]> population;
	private ArrayList<int[]> solutions;
	private HashMap<Integer, int[]> ranking;
	private final int INF = Integer.MAX_VALUE;
	private int populationSize;
	private int size;
	private int mutationRate;
	private boolean stepByStep;


	public GeneticAlgorithm(Board board, int mutationRate, int numberOfSol, boolean stepByStep){
		this.stepByStep = stepByStep;
		population = new ArrayList<int[]>();
		this.mutationRate = mutationRate;
		solutions = new ArrayList<int[]>();
		ranking = new HashMap<Integer, int[]>();
		this.populationSize = numberOfSol;
		size = board.getSize();
		population = generateRandomSolutions(numberOfSol, board.getQueenPositions());
		if (stepByStep){
			System.out.println("Initial board: \n"+ Arrays.toString(board.getQueenPositions()) + "\n");
			System.out.println("Population");
			for (int[] chromosome : population) {
				System.out.println(Arrays.toString(chromosome));
			}
			System.out.println();
		}

		int iterations = 0;
		long startTime = System.nanoTime();
		long duration = 1;
		while (solutions.size() < 1000 && duration < 200) {
			for (int[] sol : population) {
				Board b = new Board(sol);
				if (b.checkIfLegal()){
					ArrayList<int[]> rotationSol = HelpMethods.findDuplicateSolutions(sol);
					for (int[] is : rotationSol) {
						solutions = HelpMethods.addIfNotAlreadyInSolutionSet(is, solutions);
					}
				}
			}
			long endTime = System.nanoTime();
			duration = (long) ((endTime - startTime)/(Math.pow(10, 9)));
			iterations++;
			doFitnessEvaluation();
//			if (iterations%50 == 0){
//				System.out.println("Number of solutions for " + size + "x" + size + ": " + solutions.size());
//				System.out.println("Number of iterations: " + iterations);				
//			}
		}
		//		for (int[] sol : solutions) {
		//			System.out.println(Arrays.toString(sol));
		//		}
		System.out.println("Number of solutions for " + size + "x" + size + ": " + solutions.size());
		System.out.println("Number of iterations: " + iterations);
	}

	public ArrayList<int[]> generateRandomSolutions(int numberOfSol, int[] initial){
		ArrayList<int[]> ret = new ArrayList<int[]>();
		ret.add(initial);
		for (int i = 0; i < numberOfSol-1; i++) {
			int[] randomSol = HelpMethods.initializeBoardWithoutRowConflicts(size);
			ret.add(randomSol);
		}
		return ret;
	}

	public void doFitnessEvaluation(){
		int[] conflicts = new int[population.size()];
		int i = 0;
		double sum = 0;
		double[] fitnessEvaluation = new double[population.size()];
		for (int[] sol : population) {
			Board b = new Board(sol);
			conflicts[i] = (int)b.getCurrentHeuristicValue();
			sum+= conflicts[i];
			i++;
		}
		for (int j = 0; j < fitnessEvaluation.length; j++) {
			fitnessEvaluation[j] = (double)conflicts[j]/sum;
		}
		selection(fitnessEvaluation);
	}


	public void selection(double[] fitnessEvaluation){
		int length = 0;
		ArrayList<int[]> newPopulation = new ArrayList<int[]>();
		length = population.size()/50;
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
			ranking.put(c, population.get(bestPosition));
			c++;
		}
		newPopulation = addChromosomesBasedOnRanking(newPopulation);
		if (stepByStep){
			System.out.println("Best parents:");
			 Iterator it = ranking.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pair = (Map.Entry)it.next();
			        System.out.println(Arrays.toString((int[]) pair.getValue()));
			    }
			System.out.println();
			System.out.println("Parents: ");
			for (int[] is : newPopulation) {
				System.out.println(Arrays.toString(is));
			}
			System.out.println();
		}
		doCrossover(newPopulation);
	}

	public ArrayList<int[]> addChromosomesBasedOnRanking(ArrayList<int[]> newPopulation){
		while (newPopulation.size() <= populationSize){
			for (int i = 0; i < 14; i++) {
				for (int j = i+1; j < 14; j++) {
					newPopulation.add(ranking.get(i));
					newPopulation.add(ranking.get(j));	
				}
			}
		}
		return newPopulation;
	}

	public void doCrossover(ArrayList<int[]> newPopulation){
		int a;
		if (size%2 == 1){
			a = (size/2+1);
		}
		else{
			a = size/2;
		}
		for (int i = 0; i < newPopulation.size(); i+=2) {
			int[] newQueen1 = newPopulation.get(i).clone();
			int[] newQueen2 = newPopulation.get(i+1).clone();
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
			newPopulation.set(i, newQueen1);
			newPopulation.set(i+1, newQueen2);
		}
		if (stepByStep){
			System.out.println("Children (after crossover): ");
			for (int[] is : newPopulation) {
				System.out.println(Arrays.toString(is));
			}
			System.out.println();
		}
		doMutation(newPopulation);
	}

	public void doMutation(ArrayList<int[]> newPopulation){
		for (int[] is : newPopulation) {
			Board b = new Board(is);
			if (b.checkIfLegal()){
				continue;
			}
			int a = HelpMethods.generateRandomNumer(1, 100);
			if (a > mutationRate && b.getCurrentHeuristicValue() != 0){//doing mutation with a small independent probability
				int pos1 = HelpMethods.generateRandomNumer(0, size-1);
				int pos2 = HelpMethods.generateRandomNumer(0, size-1);
				int save = is[pos1];
				is[pos1] = is[pos2];
				is[pos2] = save;
			}
		}
		population = newPopulation;
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
			String input = "1 2 3 4 6 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24";
			Board b = GeneticAlgorithm.init(input);
			new GeneticAlgorithm(b, 92, 700, false);					
		}
		else if (args[1].equals("true")){
			System.out.println("Step by step:");
			String input = args[0];
			Board b = GeneticAlgorithm.init(input);
			new GeneticAlgorithm(b, 92, 700, true);						
		}
		else{
			String input = args[0];
			Board b = GeneticAlgorithm.init(input);
			new GeneticAlgorithm(b, 92, 700, false);								
		}
		long endTime = System.nanoTime();
		long duration = (long) ((endTime - startTime)/(Math.pow(10, 6)));
		System.out.println("Time: " + (double)duration/1000 + " sec");
	}
}
