package nQueen;

import java.util.Arrays;

public class Utility {
	
	private Board board;
	
	public Utility(String input){
		init(input);
		System.out.println(board.toString());
		System.out.println(Arrays.toString(board.getQueenPosition()));
//		System.out.println(Arrays.toString(HelpMethods.countRowConflicts(board)));
//		System.out.println(Arrays.toString(HelpMethods.countDiagonalConflicts(board)));
//		System.out.println(Arrays.toString(HelpMethods.countConflicts(board)));
		run();
		
		
	}
	
	public void init(String input){
		String[] queenPosString = input.split(" ");
		int[] queensPosition = new int[queenPosString.length];
		for (int i = 0; i < queenPosString.length; i++) {
			queensPosition[i] = Integer.parseInt(queenPosString[i]);
		}
		this.board = new Board(queensPosition);
//		if (!board.checkIfLegal()){
//			System.out.println(board.toString());
//			System.out.println("This input is not valid!");
//			System.exit(2);
//		}
//		else{
//			System.out.println(board.toString());
//		}
	}
	
	
	public void run(){
		long startTime = System.nanoTime();
		new TabuSearch(board);
		long endTime = System.nanoTime();
		long duration = (long) ((endTime - startTime)/(Math.pow(10, 9)));
		System.out.println("Time to find solution on " + board.getSize() +"x" +board.getSize()+ ": " + duration + " sec");
		
		
	}
	
	public static void main(String[] args) {
		new Utility("0 0 0 0");
	}

}
