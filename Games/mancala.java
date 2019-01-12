package Mancala;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Mancala extends Application {
	private ArrayList<Pit> board = new ArrayList<>();
	private ArrayList<Text> nums = new ArrayList<>();
	@Override
	public void start(Stage primaryStage) {
		Group g = new Group();
		ObservableList<Node> list= g.getChildren();
		for (int x = 0; x<14; x++) {
			board.add(new Pit(x<7?0:1, x==0?true:x==7?true:false, x));
		}
		Button b = new Button("Play Greedy Player " + (currentTurn));
		b.setLayoutX(340);
		b.setLayoutY(370);
		list.addAll(board);
		b.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				playGreedy();
				b.setText("Play Greedy Player " + (currentTurn+1));
				for (int x = 0; x < 14; x++) {
					list.set(x, board.get(x));
				}
			}
		});
		list.add(b);
		for (int x = 0; x<14; x++) {
			Pit p = board.get(x);
			Text t = new Text(p.x == 0?145:p.x == 7?645:190+(p.x<7?p.x*60:(14-p.x)*60), p.x == 0?240:p.x == 7?240:205+p.player*60, Integer.toString(p.number));
			t.setFont(new Font("Avenir",p.x == 0?40:p.x == 7?40:20));
			nums.add(t);
		}
		nums.add(new Text(100,100,""));
		list.addAll(nums);
		Scene s = new Scene(g, 830, 420);
		primaryStage.setScene(s);
		primaryStage.show();
	}

	void playGreedy() {
		ArrayList<Pit> tempBoard = new ArrayList<>();
		for (int x = 0; x<14; x++) {
			Pit p = board.get(x);
			Pit np = new Pit(p.player,p.selected,p.x);
			np.number = p.number;
			tempBoard.add(np);
		}
		int max = -1;
		int maxIndex = -1;
		int player = currentTurn;
		for (int x = 1; x < 7; x++) {
			board.get(x).selected = true;
			playTurn(1);
			if (board.get(0).number > max) {
				max = board.get(0).number;
				maxIndex = x;
			}
			currentTurn = player;
			for (int i = 0; i<14; i++) {
				Pit p = tempBoard.get(i);
				Pit np = new Pit(p.player,p.selected,p.x);
				np.number = p.number;
				np.selected = false;
				board.set(i, np);
			}
		}
		board.get(maxIndex).selected = true;
		playTurn();
	}
	int currentTurn = 1;
	public void playTurn() {
		boolean canPlay = false;
		if (currentTurn == 1) {
			for (int x = 1; x<7; x++) {
				if (board.get(x).number != 0) {
					canPlay = true;
				}
			}
		} else {
			for (int x = 8; x<14; x++) {
				if (board.get(x).number != 0) {
					canPlay = true;
				}
			}
		}
		if (!canPlay) {
			currentTurn = (currentTurn+1)%2;
		} else {
			for (int x = 0; x<14; x++) {
				if (board.get(x).selected&&!board.get(x).score) {
					playTurn(currentTurn);
				}
			}
		}
		if (currentTurn == 0) {
			for (int x = 1; x<7; x++) {
				board.get(x).setFill(new Color(0.7/(2), 0.8/(2), 0.8/(2), 1));
			}
			for (int x = 8; x<14; x++) {
				board.get(x).setFill(new Color(0.7, 0.8, 0.8, 1));
			}
		} else {
			for (int x = 1; x<7; x++) {
				board.get(x).setFill(new Color(0.7/(1), 0.8/(1), 0.8/(1), 1));
			}
			for (int x = 8; x<14; x++) {
				board.get(x).setFill(new Color(0.7/(2), 0.8/(2), 0.8/(2), 1));	
			}
		}
		board.get(0).setFill(new Color(0.3,0.5,0.5,1));
		board.get(7).setFill(new Color(0.3,0.5,0.5/2,1));
		if (board.get(0).number + board.get(7).number == 48) {
			if (board.get(0).number == board.get(7).number) {
				nums.get(14).setText("It's a tie!");
			} else {
				nums.get(14).setText("Player " + ((currentTurn+1)%2+1) + " Wins");
			}
		}
	}
	private void playTurn(int player) {
		int loc = 0; 
		for (int x = 0; x<14; x++) {
			if (board.get(x).selected&&!board.get(x).score) {
				board.get(x).selected = false;
				Pit p = board.get(x);
				int num = p.number;
				p.number = 0;
				for(int i = 0; i < num; i++) {
					loc = (i+x+1)%14;
					if ((loc == 0 || loc == 7) && !(board.get(loc).player == player)) {
						loc+=1;
						num+=1;
						i+=1;
					}
					board.get(loc).number+=1;
				}
				if (loc != 0 && loc != 7 && board.get(loc).number != 1) {
					board.get(loc).selected = true;
					playTurn(player);
				} else if (loc == 0 || loc == 7) {

				} else {
					currentTurn = (currentTurn+1)%2;
				}
			}
		}
		for (int x = 0; x<14; x++) {
			Text t = nums.get(x);
			t.setText(Integer.toString(board.get(x).number));
		}
	}

	private class Pit extends Circle {
		int number, player, x;
		boolean score, selected = false;
		public Pit(int player, boolean score, int x) {
			super(x == 0?160:x == 7?660:200+(x<7?x*60:(14-x)*60), x == 0?230:x == 7?230:200+player*60, x == 0?60:x == 7?60:30);
			number = 4;
			this.x = x;
			this.player = player;
			this.score = score;
			if (score)  {
				this.setFill(new Color(0.3,0.5,0.5/(player+1),1));
				number = 0;
			} else {
				this.setFill(new Color(0.7/(player+1), 0.8/(player+1), 0.8/(player+1), 1));
			}
			this.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
						selected = true;
						playTurn();
					}
				}
			});
		}
		public String toString() {
			return "Player: "+player+" Gems: "+number+" Index: "+x+'\n';
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
