package BigTacToe;

import java.util.ArrayList;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BigTacToe extends Application {
	final int offset = 12, tsize = 100;
	String currentPlayer;
	Grid[][] board;
	int curr, curc;
	boolean neutral;
	Text playrn;
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
		playrn = new Text("Current Player: " + currentPlayer);
	}
	private class Tile extends Rectangle {
		int r, c;
		Grid g;
		Text t;
		boolean clicked;
		public Tile(int r, int c, Grid g) {
			this.r = r;
			this.c = c;
			this.g = g;
			this.setArcHeight(7);
			this.setArcWidth(7);
			this.setX(offset + (3*g.c + c) * tsize + g.c*offset);
			this.setY(offset + (3*g.r + r) * tsize + g.r*offset);
			this.setHeight(offset/2 + tsize - offset);
			this.setWidth(offset/2 + tsize - offset);
			this.setFill(Color.ALICEBLUE);
			this.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					handleClick();
				}
			});
			this.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>(){
				public void handle(MouseEvent event) {
					preview();
				}
			});
			this.t = new Text(" ");
			t.setFont(new Font("Courier",50));
			t.setX(offset + (3*g.c + c) * tsize + g.c*offset + 32);
			t.setY(offset + (3*g.r + r) * tsize + g.r*offset + 65);
			t.setMouseTransparent(true);
			clicked = false;
		}
		void preview() {
			if (this.clicked) {
				return;
			}
			for (int r1 = 0; r1< board.length; r1++) {
				for (int c1 = 0; c1< board[r1].length;c1++) {
					if (board[r1][c1].getFill().equals(Color.CORNFLOWERBLUE)) {
						for (int r2 = 0; r2< board[r][c].grid.length; r2++) {
							for (int c2 = 0; c2< board[r][c].grid[r].length;c2++) {
								if (!board[r1][c1].grid[r2][c2].clicked) {
									board[r1][c1].grid[r2][c2].t.setText(" ");
									board[r1][c1].grid[r2][c2].t.setFill(Color.WHITE);
									board[r1][c1].grid[r2][c2].t.toBack();
								}
							}
						}
					}
				}
			}
			if (this.g.getFill().equals(Color.CORNFLOWERBLUE)) {
				t.setText(currentPlayer);
				t.toFront();
				t.setFill(currentPlayer.equals("X")?Color.DARKORANGE:Color.CORNFLOWERBLUE);
			}
		}
		void handleClick() {
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
			t.toFront();
			t.setFill(currentPlayer.equals("X")?Color.DARKORANGE:Color.CORNFLOWERBLUE);
			selected(r, c);
			RotateTransition ro = new RotateTransition(Duration.millis(200), this);
			ro.setAxis(new Point3D(1,1,0));
			ro.setByAngle(180);
			ro.setAutoReverse(true);
			ro.setCycleCount(1);
			ro.play();
			currentPlayer = currentPlayer.equals("X")?"O":"X";
			if (board[r][c].finished) {
				neutral = true;
			} else {
				neutral = false;
			}
			playrn.setText("Current Player: " + currentPlayer);
			bigCheck();
		}
	}
	private class Grid extends Rectangle {
		boolean finished = false, first = true;
		int r, c;
		Text t;
		Tile[][] grid;
		ScaleTransition sc;
		public Grid(int r, int c) {
			this.r = r;
			this.c = c;
			grid = new Tile[3][3];
			for (int r1 = 0; r1< grid.length; r1++) {
				for (int c1 = 0; c1< grid[r1].length;c1++) {
					Tile t = new Tile(r1,c1, this);
					grid[r1][c1] = t;
				}
			}
			this.setArcHeight(7);
			this.setArcWidth(7);
			this.setX(3 * this.c * tsize + this.c * offset + offset/1.25);
			this.setY(3 * this.r * tsize + this.r * offset + offset/1.25);
			this.setHeight(3*tsize);
			this.setWidth(3*tsize);
			this.setFill(Color.CORNFLOWERBLUE);
			this.t = new Text(" ");
			t.setFont(new Font("Courier",150));
			t.setFill(Color.BLACK);
			t.setX(offset + (3*c) * tsize + c*offset + 100);
			t.setY(offset + (3*r) * tsize + r*offset + 100+90);
			sc = new ScaleTransition(Duration.millis(500), this);
			sc.setByX(0.05);
			sc.setByY(0.05);
			sc.setAutoReverse(true);
			sc.setCycleCount(2);
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
			boolean x = check("X");
			boolean o = check("O");
			if (x) {
				if(this.first) {
					RotateTransition ro = new RotateTransition(Duration.millis(400), this);
					ro.setAxis(new Point3D(1,0,0));
					ro.setByAngle(180);
					ro.setAutoReverse(true);
					ro.setCycleCount(1);
					ro.play();
					first = false;
				}
				this.toFront();
				this.setFill(Color.ANTIQUEWHITE);
				t.setFill(Color.DARKORANGE);
				this.t.setText("X");
				this.t.toFront();
				return true;
			}

			if (o) {
				if(this.first) {
					first = false;
					RotateTransition ro = new RotateTransition(Duration.millis(400), this);
					ro.setAxis(new Point3D(1,0,0));
					ro.setByAngle(180);
					ro.setAutoReverse(true);
					ro.setCycleCount(1);
					ro.play();
				}
				this.toFront();
				this.setFill(Color.ANTIQUEWHITE);
				t.setFill(Color.CORNFLOWERBLUE);
				this.t.setText("O");
				this.t.toFront();
				return true;
			}
			boolean empty = false;
			for (int r = 0; r< board.length; r++) {
				for (int c = 0; c< board.length;c++) {
					if (!grid[r][c].clicked) {
						empty = true;
					}
				}
			}
			if (!empty) {
				if(this.first) {
					RotateTransition ro = new RotateTransition(Duration.millis(400), this);
					ro.setAxis(new Point3D(1,0,0));
					ro.setByAngle(180);
					ro.setAutoReverse(true);
					ro.setCycleCount(1);
					ro.play();
					first = false;
				}
				this.toFront();
				this.setFill(Color.ANTIQUEWHITE);
				t.setFill(Color.DARKORANGE);
				this.t.setText("-");
				this.t.toFront();
				return true;
			}
			return false;
		}
		private boolean check(String s) {
			for (int r = 0; r< grid.length; r++) {
				for (int c = 0; c< grid[r].length;c++) {
					if (!grid[r][c].t.getText().equals(s)) {
						break;
					}
					if (c == 2) {
						return true;
					}
				}
			}
			for (int c = 0; c< grid.length; c++) {
				for (int r = 0; r< grid[c].length;r++) {
					if (!grid[r][c].t.getText().equals(s)) {
						break;
					}
					if (r == 2) {
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
					if (ce.equals(s)) {
						return true;
					}
				}
			}

			if (tr.equals(bl)) {
				if (bl.equals(ce)) {
					if (ce.equals(s)) {
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
				board[r][c].sc.stop();
				for (int r2 = 0; r2< board[r][c].grid.length; r2++) {
					for (int c2 = 0; c2< board[r][c].grid[r].length;c2++) {
						board[r][c].grid[r2][c2].setFill(Color.ANTIQUEWHITE);
					}
				}
			}
		}
		if (board[r1][c1].finished) {
			for (int r = 0; r< board.length; r++) {
				for (int c = 0; c< board.length;c++) {
					if (!board[r][c].finished) {
						board[r][c].setFill(Color.CORNFLOWERBLUE);
						for (int r2 = 0; r2< board[r][c].grid.length; r2++) {
							for (int c2 = 0; c2< board[r][c].grid[r].length;c2++) {
								board[r][c].grid[r2][c2].setFill(Color.AZURE);
							}
						}
						board[r][c].sc.play();
					}
				}
			}
		} else {
			for (int r = 0; r< board[r1][c1].grid.length; r++) {
				for (int c = 0; c< board[r1][c1].grid[r1].length;c++) {
					board[r1][c1].grid[r][c].setFill(Color.AZURE);
				}
			}
			board[r1][c1].setFill(Color.CORNFLOWERBLUE);
			board[r1][c1].sc.play();
		}
		for (int r = 0; r< board.length; r++) {
			for (int c = 0; c< board[r].length;c++) {
				if (board[r][c].finished) {
					board[r][c].setFill(Color.ANTIQUEWHITE);
				}
				ScaleTransition sct = new ScaleTransition(Duration.millis(1), board[r][c]);
				sct.setToX(1);
				sct.setToY(1);
				sct.setCycleCount(1);
				sct.play();
			}
		}
	}
	void bigCheck() {
		boolean test = false;
		if (bigCheck("X")) {
			playrn.setText("Player 1 Wins!");
			test = true;
		}
		if (bigCheck("O")) {
			playrn.setText("Player 2 Wins!");
			test = true;
		}
		boolean empty = false;
		for (int r = 0; r< board.length; r++) {
			for (int c = 0; c< board.length;c++) {
				if (!board[r][c].finished) {
					empty = true;
					break;
				}
			}
		}
		if (!empty) {
			playrn.setText("It's a Draw!");
			test = true;
		}
		if (test) {
			for (int r = 0; r< board.length; r++) {
				for (int c = 0; c< board.length;c++) {
					board[r][c].setMouseTransparent(true);
					for (int r2 = 0; r2< board[r][c].grid.length; r2++) {
						for (int c2 = 0; c2< board[r][c].grid[r].length;c2++) {
							board[r][c].grid[r2][c2].setMouseTransparent(true);
						}
					}
				}
			}
		}
	}
	private boolean bigCheck(String s) {
		for (int r = 0; r< board.length; r++) {
			for (int c = 0; c< board[r].length;c++) {
				if (!board[r][c].t.getText().equals(s)) {
					break;
				}
				if (c == 2) {
					return true;
				}
			}
		}
		for (int c = 0; c< board.length; c++) {
			for (int r = 0; r< board[c].length;r++) {
				if (!board[r][c].t.getText().equals(s)) {
					break;
				}
				if (r == 2) {
					return true;
				}
			}
		}
		String tl = board[0][0].t.getText();
		String bl = board[0][2].t.getText();
		String tr = board[2][0].t.getText();
		String br = board[2][2].t.getText();
		String ce = board[1][1].t.getText();

		if (tl.equals(br)) {
			if (tl.equals(ce)) {
				if (ce.equals(s)) {
					return true;
				}
			}
		}

		if (tr.equals(bl)) {
			if (bl.equals(ce)) {
				if (ce.equals(s)) {
					return true;
				}
			}
		}
		return false;
	}
	public void start(Stage primaryStage) {
		Group group = new Group();
		ObservableList<Node> list = group.getChildren();
		for (int r = 0; r< board.length; r++) {
			for (int c = 0; c< board[r].length;c++) {
				list.add(board[r][c]);
				list.addAll(board[r][c].getText());
				list.add(board[r][c].t);
				list.addAll(board[r][c].getTiles());
			}
		}
		Rectangle rect = new Rectangle(190,950,550,80);
		rect.setArcHeight(10);
		rect.setArcWidth(10);
		rect.setFill(Color.ANTIQUEWHITE);
		list.add(rect);
		playrn.setFill(Color.BLACK);
		playrn.setFont(new Font("Courier", 50));
		playrn.setX(210);
		playrn.setY(1005);
		list.add(playrn);
		Scene s = new Scene(group, 945, 1050);
		s.setFill(Color.CHOCOLATE);
		primaryStage.setTitle("BigTacToe");
		primaryStage.setScene(s);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
