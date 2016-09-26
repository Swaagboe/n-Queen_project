package nQueen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class SimulatedAnnealing {
	private double temperature;
	public Board currentSolution;
	private double dT;
	private Board bestSolution;
	private Board[] solutions;
	public double bestSolutionHeuristic;
	private Random random;
	private int numberOfNeighbours;
	private ArrayList<int[]> finalSolutions;
	private int[][] conflictsBySwap;
	private int size;
	private final int INF = Integer.MAX_VALUE;
	private int counter;



	public SimulatedAnnealing(double initialTemp, double dT,Board initialBoard, int numberOfNeighbours) {
		this.random = new Random();
		finalSolutions = new ArrayList<int[]>();
		size = initialBoard.getSize();
		this.currentSolution = new Board(initialBoard.getQueenPositions());
		this.temperature = initialTemp;
		this.dT=dT;
		bestSolution = new Board(initialBoard.getQueenPositions());
		bestSolutionHeuristic = currentSolution.getCurrentHeuristicValue();
		this.numberOfNeighbours = numberOfNeighbours;
		initialiseConflictsBySwap();
		buildConflictsBySwap(currentSolution.getQueenPositions());

	}

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

	public void initialiseConflictsBySwap(){
		conflictsBySwap = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = i+1; j < size; j++) {
				conflictsBySwap[i][j] = -1;
			}
		}
	}

	public ArrayList<int[]> run() {
		while(temperature > 0 && 1000 > finalSolutions.size()){

			temperature= temperature - this.dT;
			buildConflictsBySwap(currentSolution.getQueenPositions());
			swap(currentSolution.getQueenPositions());


			if(currentSolution.getCurrentHeuristicValue() == 0){
				finalSolutions = HelpMethods.addIfNotAlreadyInSolutionSet(currentSolution.getQueenPositions(), finalSolutions);
				ArrayList<int[]> rotationSol = HelpMethods.findDuplicateSolutions(currentSolution.getQueenPositions());
				for (int[] is : rotationSol) {
					finalSolutions = HelpMethods.addIfNotAlreadyInSolutionSet(is, finalSolutions);
				}
				currentSolution = new Board(HelpMethods.initializeBoardWithoutRowConflicts(bestSolution.getSize()));
			}

		}


		return finalSolutions;
	}

	//lager et nytt board ved aa finne beste nabo
	public void swap(int[] queenPos){
		int[] newPos = queenPos.clone();
		int[] swap = findBestSwap(queenPos.clone());
		int save = queenPos[swap[0]];
		newPos[swap[0]] = queenPos[swap[1]];
		newPos[swap[1]] = save;
		Board newSolution = new Board(newPos);
		double currentSolutionHeuristic = currentSolution.getCurrentHeuristicValue();
		if (newSolution.getCurrentHeuristicValue() < currentSolutionHeuristic){
			this.currentSolution = newSolution;
		}
		else {
			if(propability(newSolution) > (random.nextInt(1000))/1000) {
				this.currentSolution = newSolution;
			}

			else{
				this.currentSolution = new Board(HelpMethods.switchTwoQueensAtRandom(currentSolution.getQueenPositions()));
			}

		}



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

				if (conflictsBySwap[i][j] <= best){
					if(random.nextInt(100) > 50){
						best = conflictsBySwap[i][j];
						ret[0] = i;
						ret[1] = j;
					}
				}
			}
		}
		return ret;
	}

	public double propability(Board board) {
		if((HelpMethods.simpleHeuristic(board) == currentSolution.getCurrentHeuristicValue())){
			return random.nextDouble();
		}

		double q = ((HelpMethods.simpleHeuristic(board) - currentSolution.getCurrentHeuristicValue())/currentSolution.getCurrentHeuristicValue());

		return Math.min(1,Math.exp(-q/temperature));
	}

	public void init(String input){
		String[] queenPosString = input.split(" ");
		int[] queensPosition = new int[queenPosString.length];
		for (int i = 0; i < queenPosString.length; i++) {
			queensPosition[i] = Integer.parseInt(queenPosString[i]);
		}
		this.currentSolution = new Board(queensPosition);
	}

	public double getTemperature() {
		return this.temperature;
	}

	public static void main(String[] args) {
		for(int i = 0 ; i < 800; i+=50){

			int[] brett = new int[30];
			for(int j = 0 ; j < 30; j++) {
				brett[j] = j+1;
			}
			SimulatedAnnealing test = new SimulatedAnnealing(i+750, 0.1, new Board(brett), 10);
			long startTime = System.nanoTime();
			ArrayList<int[]> losning = test.run();

			long endTime = System.nanoTime();
			long duration = (long) ((endTime - startTime)/(Math.pow(10, 6)));

			System.out.println("Time to find " + losning.size() + "solutions " + duration + " millisec\n Intitial temp: " + (i+750) + "\n\n");
		}
	}
}