package nQueen;

import java.util.ArrayList;
import java.util.Arrays;

public class Backtracking {
	
	private ArrayList<int[]> solutions;
	private int size;
	private boolean stepByStep;
	
	public Backtracking(Board board, boolean stepByStep){
		solutions = new ArrayList<int[]>();
		size = board.getSize();
		this.stepByStep = stepByStep;
		if (stepByStep){
			ArrayList<Integer> possNumber = new ArrayList<Integer>();
			for (Integer integer : board.getPossibleNumbers()) {
				possNumber.add(size+1-integer);
			}
			System.out.println("Current board: "+Arrays.toString(board.getQueenPosition())
					+"\t Numbers available: " + possNumber);				
		}
		backtrack(board);
		if (stepByStep){
			System.out.println();
		}
		printSolutions();
		System.out.println("Number of solutions for " + solutions.get(0).length+"x"+solutions.get(0).length+": "+  + solutions.size());
	}
	
	public Board backtrack(Board board){
		int[] queenPos = board.getQueenPositions();
		boolean breakIt = false;
		for (int i = 0; i < size; i++) {
			if (queenPos[i] != 0){
				continue;
			}
			boolean[][] ruter = board.getBoard();
			for (int j = 0; j < size; j++) {
				if (!board.getPossibleNumbers().contains(j+1)){
					continue;
				}
				ruter[j][i] = (true);
				if (stepByStep){
					ArrayList<Integer> possNumber = new ArrayList<Integer>();
					board.buildPossibleNumbers();
					for (Integer integer : board.getPossibleNumbers()) {
						possNumber.add(size+1-integer);
					}
					System.out.println("Current board: "+Arrays.toString(board.getQueenPosition())
							+"\t Numbers available: " + possNumber);				
				}
				if (board.checkIfLegal()){
					for (int k = 0; k < board.getPossibleNumbers().size(); k++) {
						if (board.getPossibleNumbers().get(k) == j+1){
							board.removePossibleNumbers(k);							
						}
					}
					Board newBoard = new Board(board.getQueenPosition());
					backtrack(newBoard);
					breakIt = true;
					if (board.isFilledUp()){
						solutions.add(board.getQueenPositions());
					}
					ruter[j][i]=(false);
					queenPos = board.getQueenPosition();
				}
				else{
					ruter[j][i]=(false);
				}
			}
			if (breakIt){
				break;
			}
		}
		return board;
	}
	
	public void printSolutions(){
		System.out.println("SOLUTIONS:");
		for (int i = 0; i < solutions.size(); i++) {
			System.out.println(Arrays.toString(solutions.get(i)));
		}
	}
	
	public void printSolutionsBoard(){
		for (int[] sol : solutions) {
			Board b = new Board(sol);
			System.out.println(b.toString());
		}
	}
	
	public static Board init(String input){
		String[] queenPosString = input.split(" ");
		int[] queensPosition = new int[queenPosString.length];
		for (int i = 0; i < queenPosString.length; i++) {
			queensPosition[i] = Integer.parseInt(queenPosString[i]);
		}
		Board board = new Board(queensPosition);
		if (!board.checkIfLegal()){
			System.out.println("This input is not valid!");
			System.exit(2);
		}
		else{
		}
		return board;
	}
	
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		if (args.length == 0){
			String input = "1 3 5 7 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0";
			Board b = Backtracking.init(input);
			new Backtracking(b, false);					
		}
		else if (args[1].equals("true")){
			System.out.println("Step by step:");
			String input = args[0];
			Board b = Backtracking.init(input);
			new Backtracking(b, true);					
		}
		else{
			String input = args[0];
			Board b = Backtracking.init(input);
			new Backtracking(b, false);								
		}
		long endTime = System.nanoTime();
		long duration = (long) ((endTime - startTime)/(Math.pow(10, 6)));
		System.out.println("Time: " + (double)duration/1000 + " sec");
	}
	

}
