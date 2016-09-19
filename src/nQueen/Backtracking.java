package nQueen;

import java.util.ArrayList;
import java.util.Arrays;

public class Backtracking {
	
	private ArrayList<int[]> solutions;
	private ArrayList<Integer> possibleNumber;
	private int size;
	
	public Backtracking(Board board){
		solutions = new ArrayList<int[]>();
		size = board.getSize();
//		buildPossibleNumbers(board);
//		System.out.println(possibleNumber);
		backtrack(board);
		printSolutions();
		//printSolutionsBoard();
	}
//	
//	public void buildPossibleNumbers(Board board){
//		possibleNumber = new ArrayList<Integer>();
//		boolean[][] ruter = board.getBoard();
//		for (int i = 0; i < size; i++) {
//			boolean add = false;
//			for (int j = 0; j < size; j++) {
//				if (ruter[i][j]){
//					add = true;
//					break;
//				}
//			}
//			if (!add){
//				possibleNumber.add(i+1);
//			}
//		}
//	}
	
	public Board backtrack(Board board){
		int[] queenPos = board.getQueenPosition();
		boolean breakIt = false;
		for (int i = 0; i < queenPos.length; i++) {
			if (queenPos[i] != 0){
				continue;
			}
			boolean[][] ruter = board.getBoard();
			for (int j = 0; j < queenPos.length; j++) {
				int c = j+1;
//				System.out.println("j: " + c);
//				System.out.println("Possiblenumbers: " + board.getPossibleNumbers());
				if (!board.getPossibleNumbers().contains(j+1)){
//					System.out.println("here");
					continue;
				}
				ruter[j][i] = (true);
				if (board.checkIfLegal()){
					for (int k = 0; k < board.getPossibleNumbers().size(); k++) {
						if (board.getPossibleNumbers().get(k) == j+1){
//							System.out.println("REMOVE");
							board.removePossibleNumbers(k);							
						}
					}
					System.out.println("SUCCESS");
					System.out.println(board);
					Board newBoard = new Board(board.getQueenPosition());
					backtrack(newBoard);
					breakIt = true;
					if (board.isFilledUp()){
						solutions.add(board.getQueenPosition());
					}
					ruter[j][i]=(false);
					queenPos = board.getQueenPosition();
				}
				else{
					System.out.println("FAIL");
					System.out.println(board);
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
		System.out.println("Number of solutions for " + solutions.get(0).length+"x"+solutions.get(0).length+": "+  + solutions.size());
	}
	
	public void printSolutionsBoard(){
		for (int[] sol : solutions) {
			Board b = new Board(sol);
			System.out.println(b.toString());
		}
	}
	
	

}
