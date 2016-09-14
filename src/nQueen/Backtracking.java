package nQueen;

import java.util.ArrayList;

public class Backtracking {
	
	private ArrayList<int[]> solutions;
	
	public Backtracking(Board board){
		solutions = new ArrayList<int[]>();
		backtrack(board);
		printSolutions();
		//printSolutionsBoard();
	}
	
	public Board backtrack(Board board){
		int[] queenPos = board.getQueenPosition();
		boolean breakIt = false;
		for (int i = 0; i < queenPos.length; i++) {
			if (queenPos[i] != 0){
				continue;
			}
			boolean[][] ruter = board.getBoard();
			for (int j = 0; j < queenPos.length; j++) {
				ruter[j][i] = (true);
				if (board.checkIfLegal()){
					//System.out.println("SUCCESS");
					//System.out.println(board);
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
					//System.out.println("FAIL");
					//System.out.println(board);
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
			//System.out.println(Arrays.toString(solutions.get(i)));
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
