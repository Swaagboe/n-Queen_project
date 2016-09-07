package nQueen;

import java.util.ArrayList;
import java.util.Arrays;


public class Board {
	
	//[row][column]
	private boolean[][] board;
	private int size;
	private int[] queenPositions;
	private double currentHeuristicValue;
	private int[] conflicts;

	
	public double getCurrentHeuristicValue() {
		return currentHeuristicValue;
	}

	public void setCurrentHeuristicValue(double currentHeuristicValue) {
		this.currentHeuristicValue = currentHeuristicValue;
	}

	public Board(int[] queenPositions){
		this.queenPositions = queenPositions.clone();
		
		this.size = queenPositions.length;
		board = new boolean[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				boolean r = false;
				if (size - queenPositions[j] == i){
					r = true;
					board[i][j] = r;
				}
				board[i][j] = r;
			}
		}
		
		
		this.conflicts = HelpMethods.countConflicts(this);
		currentHeuristicValue = HelpMethods.simpleHeuristic(this);
	}
	
	public int[] getConflicts() {
		return this.conflicts;
	}
	
	public int[] getQueenPositions(){
		return queenPositions;
	}
	
	public int[] getQueenPosition() {
		int[] ret = new int[size];
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret.length; j++) {
				if (board[i][j]){
					ret[j] = size-i;
				}
			}
		}
		return ret;
	}

	public boolean[][] getBoard() {
		return board;
	}

	public int getSize() {
		return size;
	}
	
	public boolean isFilledUp(){
		int[] queenPos = this.getQueenPosition();
		int c = 0;
		for (int i = 0; i < queenPos.length; i++) {
			if (queenPos[i] != 0){
				c++;
			}
		}
		if (c == size){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void moveQueen(int row, int column) {

		

		int rowIndex = this.getSize() - this.queenPositions[column];
		this.board[rowIndex][column] = false;
		this.board[row][column] = true;
		this.queenPositions[column] = this.getSize() - row;
		conflicts[rowIndex] -=1;
		conflicts[row] += 1;
		
		int index = rowIndex + column;
		
		if(index!=0 && index !=this.getSize() && index != this.getSize()*2 ){
			int offset = (this.getSize()*2-3) +  this.getSize();
			int columnIndex = this.getSize() -column - 1;
			
			int diagonalOneIndex = rowIndex + column- 1 + this.getSize();
			int diagonalTwoIndex = offset + rowIndex + columnIndex - 1;


			conflicts[diagonalOneIndex] -=1;
			conflicts[diagonalTwoIndex] -=1;
		}
		
		index = row + column;
		
		if(index!=0 && index !=this.getSize() && index != this.getSize()*2 ){
			int offset = (this.getSize()*2-3) +  this.getSize();
			int columnIndex = this.getSize() -column - 1;
			
			int diagonalOneIndex = row + column -1 + this.getSize();
			int diagonalTwoIndex = offset + columnIndex + row - 1;
			conflicts[diagonalOneIndex] +=1;
			conflicts[diagonalTwoIndex] +=1;
		}
		
		this.currentHeuristicValue = HelpMethods.simpleHeuristic(this);
		

	}
	
	public void removeQueen(int row, int column) {
		this.board[row][column] = false;
		this.queenPositions[column] = 0;
	}

	
	public String toString(){
		String s = "  ";
		String letter = "a";
		for (int i = 0; i < size; i++) {
			s += "  " + letter + "  ";
			int charValue = letter.charAt(0);
			String next = String.valueOf( (char) (charValue + 1));
			letter = next;
		}
		s += "\n  ";
		for (int i = 0; i < size; i++) {
			s+= "+----";
		}
		s += "+\n";
		for (int i = 0; i < size; i++) {
			int put = size-i;
			if (i>= 9){
				s += "" + put + "";			
			}
			else{
				s += "" + put + " ";
			}
			for (int j = 0; j < size; j++) {
				if (board[i][j]){
					s += "| " + "Q" + "  ";					
				}
				else{
					s += "| " + " " + "  ";							
				}
			}
			s += "|";
			s += "\n  ";				

			for (int k = 0; k < size; k++) {
				s+= "+----";
			}
			s += "+\n";

			
		}
		return s;
	}
	
	public boolean checkIfLegal(){
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				boolean r = board[i][j];
				if (r){
					if (!checkDiagonally(r, i, j) || !checkHorizontally(r, i, j) || !checkVertically(r, i, j)){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private boolean checkVertically(boolean r, int row, int col){
		for (int i = 0; i < board.length; i++) {
			if (board[i][col] && i != row){
				return false;
			}
		}
		return true;
	}
	
	private boolean checkHorizontally(boolean r, int row, int col){
		for (int i = 0; i < board.length; i++) {
			if (board[row][i] && i != col){
				return false;
			}
		}
		return true;
	}
	
	private boolean checkDiagonally(boolean r, int row, int col){

		for (int i = 1; i+row < board.length && i+col < board.length; i++) {
			if (board[row+i][col+i]){
				return false;
			}
		}
		for (int i = 1; i+row < board.length && col-i >= 0; i++) {
			if (board[row+i][col-i]){
				return false;
			}
		}
		for (int i = 1; row-i >= 0 && col-i >= 0; i++) {
			if (board[row-i][col-i]){
				return false;
			}
		}
		for (int i = 1; row-i >= 0 && col+i < board.length; i++) {
			if (board[row-i][col+i]){
				return false;
			}
		}
		return true;
	}
	
}
