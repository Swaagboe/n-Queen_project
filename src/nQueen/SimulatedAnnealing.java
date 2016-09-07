package nQueen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class SimulatedAnnealing {
	private double temperature;
	public Board currentSolution;
	private int maximumConflicts;
	private double dT;
	private Board bestSolution;
	private Board[] solutions;
	public double bestSolutionHeuristic;
	private Random random;
	private int numberOfNeighbours;
	private double currentSolutionHeuristic;
	

	public SimulatedAnnealing(double initialTemp, double dT,Board initialBoard, int numberOfNeighbours) {
		this.random = new Random();
		this.currentSolution = new Board(initialBoard.getQueenPositions());
		this.temperature = initialTemp;
		this.dT=dT;
		bestSolution = new Board(initialBoard.getQueenPositions());
		bestSolutionHeuristic = currentSolution.getCurrentHeuristicValue();
		System.out.println(bestSolutionHeuristic);
		currentSolutionHeuristic = currentSolution.getCurrentHeuristicValue();
		this.numberOfNeighbours = numberOfNeighbours;
		}
	
	public Board run() {
		while(temperature > 0 && bestSolutionHeuristic !=0){
			
			temperature= temperature - this.dT;
			Board[] neighbours = HelpMethods.createNeighbours(currentSolution, numberOfNeighbours);
			Board bestNeighbour = neighbours[0];
			double bestNeighbourHeuristic = HelpMethods.simpleHeuristic(bestNeighbour);
			double currentSolutionStability = HelpMethods.simpleHeuristic(currentSolution);

			for(Board board : neighbours) {
				if(HelpMethods.simpleHeuristic(board)< bestNeighbourHeuristic){
					bestNeighbour = board;
					bestNeighbourHeuristic = HelpMethods.simpleHeuristic(bestNeighbour);
				}
			}
			
			
			
			if(bestNeighbourHeuristic < currentSolutionStability) {
				this.currentSolution = bestNeighbour;
				currentSolutionStability = bestNeighbourHeuristic;
				if(currentSolutionStability < bestSolutionHeuristic){
					bestSolutionHeuristic = currentSolutionStability;
					this.bestSolution = currentSolution;
				}
			}else {
				if(this.propability(bestNeighbour) > random.nextInt(1000)/1000) {
					currentSolution = bestNeighbour;
				}
				else {
					currentSolution = neighbours[random.nextInt(neighbours.length)];
				}
			}
			
			}
		return bestSolution;
	}
	
	public double propability(Board board) {
			
		double q = ((HelpMethods.simpleHeuristic(board) - HelpMethods.simpleHeuristic(board)/HelpMethods.simpleHeuristic(currentSolution)));
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
		int[] brett = new int[20];
		for(int i = 0 ; i < 20 ; i++) {
			brett[i] = i+1;
		}
		
		
		SimulatedAnnealing test = new SimulatedAnnealing(1000, 0.001, new Board(brett), 10);
		long startTime = System.nanoTime();
		Board l¿sning = test.run();
		long endTime = System.nanoTime();
		
		System.out.println(Arrays.toString(l¿sning.getConflicts()));
		System.out.println(l¿sning.getCurrentHeuristicValue());
		System.out.println(l¿sning);
		System.out.println(test.getTemperature());
		long duration = (long) ((endTime - startTime)/(Math.pow(10, 9)));
		System.out.println("Time: " + duration);
		System.out.println(l¿sning.getCurrentHeuristicValue());
	}
}
	
