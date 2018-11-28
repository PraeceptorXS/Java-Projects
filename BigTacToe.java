package BigTacToe;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BigTacToe extends Application {
	final int offset = 12, tsize = 100;
	String currentPlayer;
	Grid[][] board;
	int curr, curc;
	boolean neutral;
	public BigTacToe() {
		currentPlayer = "X";
		board = new Grid[3][3];
		for (int r = 0; r< board.length; r++) {
			for (int c = 0; c< board[r].length;c++) {
				Grid g = new Grid(r,c);
				board[r][c] = g;
			}
		}
		neutral = true;
	}
	private class Tile extends Rectangle {
		String value;
		int r, c;
		Grid g;
		Text t;
		boolean clicked;
		public Tile(int r, int c, Grid g) {
			this.value = " ";
			this.r = r;
			this.c = c;
			this.g = g;
			this.setArcHeight(7);
			this.setArcWidth(7);
			this.setX(offset + (3*g.c + c) * tsize + g.c*offset);
			this.setY(offset + (3*g.r + r) * tsize + g.r*offset);
			this.setHeight(offset/2 + tsize - offset);
			this.setWidth(offset/2 + tsize - offset);
			this.setFill(Color.ANTIQUEWHITE);
			this.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					handleClick();
				}
			});
			this.t = new Text(value);
			t.setX(offset + (3*g.c + c) * tsize + g.c*offset + tsize /2);
			t.setY(offset + (3*g.r + r) * tsize + g.r*offset + tsize /2);
			clicked = false;
		}
		void handleClick() {
			System.out.println(this.g.r + " " + this.g.c + " - " + curr + " " + curr);
			if (this.clicked) {
				return;
			}
			if (this.g.finished) {
				return;
			}
			if ((!(this.g.r == curr) || !(this.g.c == curc)) && !neutral) {
				return;
			}
			curr = r;
			curc = c;
			clicked = true;
			t.setText(currentPlayer);
			currentPlayer = value.equals("X")?"O":"X";
			selected(r, c);
			if (this.g.finished) {
				g.setFill(Color.BLACK);
				neutral = true;
			}
			neutral = false;
		}
	}
	private class Grid extends Rectangle {
		String value;
		boolean finished = false;
		int r, c;
		Tile[][] grid;
		public Grid(int r, int c) {
			this.value = " ";
			this.r = r;
			this.c = c;
			grid = new Tile[3][3];
			for (int r1 = 0; r1< grid.length; r1++) {
				for (int c1 = 0; c1< grid[r1].length;c1++) {
					Tile t = new Tile(r1,c1, this);
					grid[r1][c1] = t;
				}
			}
			this.setX(offset + 3 * this.c * tsize + this.c * offset);
			this.setY(offset + 3 * this.r * tsize + + this.r * offset);
			this.setHeight(3*tsize - offset);
			this.setWidth(3*tsize - offset);
			this.setFill(Color.DARKORANGE);
		}
		ArrayList<Rectangle> getTiles() {
			ArrayList<Rectangle> tiles = new ArrayList<>();
			for (int r1 = 0; r1< grid.length; r1++) {
				for (int c1 = 0; c1< grid[r1].length;c1++) {
					tiles.add(grid[r1][c1]);
				}
			}
			return tiles;
		}
		ArrayList<Text> getText() {
			ArrayList<Text> text = new ArrayList<>();
			for (int r1 = 0; r1< grid.length; r1++) {
				for (int c1 = 0; c1< grid[r1].length;c1++) {
					text.add(grid[r1][c1].t);
				}
			}
			return text;
		}
		boolean check() {
			for (int r = 0; r< grid.length; r++) {
				for (int c = 0; c< grid[r].length;c++) {
					if (!grid[r][c].t.getText().equals(currentPlayer)) {
						break;
					}
					if (c == 2) {
						System.out.println("Won on square" + r + " " + c);
						return true;
					}
				}
			}
			for (int c = 0; c< grid.length; c++) {
				for (int r = 0; r< grid[c].length;r++) {
					if (!grid[r][c].t.getText().equals(currentPlayer)) {
						break;
					}
					if (r == 2) {
						System.out.println("Won on square" + r + " " + c);
						return true;
					}
				}
			}
			String tl = grid[0][0].t.getText();
			String bl = grid[0][2].t.getText();
			String tr = grid[2][0].t.getText();
			String br = grid[2][2].t.getText();
			String ce = grid[1][1].t.getText();
			
			if (tl.equals(br)) {
				if (tl.equals(ce)) {
					if (ce.equals(currentPlayer)) {
						System.out.println("Won on square" + r + " " + c);
						return true;
					}
				}
			}
			
			if (tr.equals(bl)) {
				if (bl.equals(ce)) {
					if (ce.equals(currentPlayer)) {
						System.out.println("Won on square" + r + " " + c);
						return true;
					}
				}
			}
			return false;
		}
	}
	void selected(int r1, int c1) {
		for (int r = 0; r< board.length; r++) {
			for (int c = 0; c< board[r].length;c++) {
				board[r][c].setFill(Color.DARKORANGE);
				board[r][c].finished = board[r][c].check();
				for (int r2 = 0; r2< board[r][c].grid.length; r2++) {
					for (int c2 = 0; c2< board[r][c].grid[r].length;c2++) {
						board[r][c].grid[r2][c2].setFill(Color.ANTIQUEWHITE);
					}
				}
			}
		}
		for (int r = 0; r< board[r1][c1].grid.length; r++) {
			for (int c = 0; c< board[r1][c1].grid[r1].length;c++) {
				board[r1][c1].grid[r][c].setFill(Color.AZURE);
			}
		}
		board[r1][c1].setFill(Color.CORNFLOWERBLUE);	
		board[r1][c1].finished = board[r1][c1].check();
		if (board[r1][c1].finished) {
			
		}
		
		for (int r = 0; r< board.length; r++) {
			for (int c = 0; c< board[r].length;c++) {
				if (board[r][c].finished) {
					board[r][c].setFill(Color.BLACK);
				}
			}
		}
	}
	public void start(Stage primaryStage) {
		Group group = new Group();
		ObservableList<Node> list = group.getChildren();
		for (int r = 0; r< board.length; r++) {
			for (int c = 0; c< board[r].length;c++) {
				list.add(board[r][c]);
				list.addAll(board[r][c].getTiles());
				list.addAll(board[r][c].getText());
			}
		}
		System.out.println();
		int a = 0;
		Scene s = new Scene(group, 950, 950);
		s.setFill(Color.DIMGRAY);
		primaryStage.setScene(s);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
