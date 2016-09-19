package nQueen;


public class Utility {
	
	private Board board;
	
	public Utility(String input){
		init(input);
//		System.out.println(board.toString());
//		System.out.println(Arrays.toString(board.getQueenPosition()));
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
		new Backtracking(board);
		long endTime = System.nanoTime();
		long duration = (long) ((endTime - startTime)/(Math.pow(10, 9)));
		System.out.println("Time: " + duration + " sec");
		
		
	}
	
	public static void main(String[] args) {
		//new Utility("17 29 22 6 12 28 21 3 24 10 9 5 2 15 8 14 26 23 7 19 27 25 1 16 11 30 18 4 13 20");
		new Utility("2 4 0 0 0 0");
	}

}
