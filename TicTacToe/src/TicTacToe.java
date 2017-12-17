import javax.swing.GroupLayout.Alignment;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class TicTacToe extends Application {

	// Indicate which player has turn and which player begins the game
	private char whoseTurn='X';
	
	// Create and initialize grid of cells
	private Cell[][] cell=new Cell[3][3];
	
	// Label with a game status
	private Label lblStatus=new Label("X's turn to play");
	
	// It holds how a player won the game - only four options possible
	private enum winnerType {ROW,COLUMN,DIAGONAL,SUBDIAGONAL};
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch();
	}
	

	@Override
	public void start(Stage primaryStage) {
		// TODO Auto-generated method stub
		
		// Define the style of status label
		lblStatus.setStyle("-fx-font-weight: bold ; -fx-font-size: 12 ");
		lblStatus.setTextFill(Color.web("#ccff00"));
		
		/* Pane which hold all cells inside
		 * Each cell is created and placed in this pane 
		 */
		GridPane pane=new GridPane();
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++) {
				pane.add(cell[i][j]=new Cell(),j,i); // Invokes constructor of Cell in every loop
			}
		
		// The most outer pane in which all panes, status panel are placed
		BorderPane borderPane=new BorderPane();
		borderPane.setStyle("-fx-background-color: black");
		borderPane.setCenter(pane);
		borderPane.setBottom(lblStatus);
		borderPane.setAlignment(lblStatus, Pos.CENTER);// Define position of satus label within this pane
		
		
		//Create a scene and place it in the stage
		Scene scene=new Scene(borderPane,450,170);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	// Determine if all cells are occupied
	public boolean isFull() {
		
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				if(cell[i][j].getToken()==' ')
					return false;
		
		return true;
	}
	
	// Determine if a player (X or O) won this game
	public boolean isWon(char token) {
		
		// Tokens in one row
		for(int i=0;i<3;i++)
			if(cell[i][0].getToken()==token &&
			   cell[i][1].getToken()==token &&
			   cell[i][2].getToken()==token) {
				
				colorChange(winnerType.ROW,i);
				return true;
				
			}
		
		// Tokens in one column
		for(int j=0;j<3;j++)
			if(cell[0][j].getToken()==token &&
			   cell[1][j].getToken()==token &&
			   cell[2][j].getToken()==token) {
				
				colorChange(winnerType.COLUMN,j);
				return true;
			}
		
		// Tokens placed diagonal
		if(cell[0][0].getToken()==token &&
		   cell[1][1].getToken()==token &&
		   cell[2][2].getToken()==token) {
			
			colorChange(winnerType.DIAGONAL,0);
			return true;
		}
		
		// Tokens placed diagonal-it's a second diagonal
		if(cell[0][2].getToken()==token &&
		   cell[1][1].getToken()==token &&
		   cell[2][0].getToken()==token) {
			
			colorChange(winnerType.SUBDIAGONAL,0);
			return true;
		}
		
		return false;
	}
	
	// Define inner class for a cell
	public class Cell extends Pane {
		
		// Token used in a cell (' '-means that cell is empty)
		private char token=' ';
		
		// Default constructor for this class
		public Cell() {
			this.setStyle("-fx-border-color: #ccff00");
			this.setPrefSize(2000, 2000);
			this.setOnMouseClicked(e -> handleEvent());
		}
		
		// Return token
		public char getToken() {
			return token;
		}
		
		/* Change the background of a cell.
		 * This is used when a player wins.
		 * Fields for winner's tokens will be highlighted
		 */
		public void changeBackground() {
			this.setStyle("-fx-background-color: red");
		}
		
		// Set a new token
		public Cell setToken(char c) {
			
			token=c;
			
			/* Token 'X' is created using two lines.
			 * Ellipse will be token 'O'.
			 */
			if(token=='X') {
				
				// Create lines and bind them with the cell
				Line line1 = new Line(10, 10,this.getWidth() - 10, this.getHeight() - 10);
				line1.endXProperty().bind(this.widthProperty().subtract(10));
				line1.endYProperty().bind(this.heightProperty().subtract(10));
				Line line2 = new Line(10, this.getHeight() - 10,this.getWidth() - 10, 10);
				line2.startYProperty().bind(this.heightProperty().subtract(10));
				line2.endXProperty().bind(this.widthProperty().subtract(10));
				
				// Define properties of lines
				line1.setStroke(Color.WHITE);
				line1.setStrokeWidth(8);
				line2.setStroke(Color.WHITE);
				line2.setStrokeWidth(8);
			
				// Add the lines to the cell
				this.getChildren().addAll(line1, line2);			
				
			} else if (token == '0') { 
				
				// Create ellipse and bind it with the cell
				Ellipse ellipse = new Ellipse(this.getWidth() / 2,this.getHeight() / 2, this.getWidth() / 2 - 10,this.getHeight() / 2 - 10);
				ellipse.centerXProperty().bind(this.widthProperty().divide(2));
				ellipse.centerYProperty().bind(this.heightProperty().divide(2));
				ellipse.radiusXProperty().bind(this.widthProperty().divide(2).subtract(10));
				ellipse.radiusYProperty().bind(this.heightProperty().divide(2).subtract(10));
				
				// Define properties of ellipse
				ellipse.setStroke(Color.WHITE);
				ellipse.setStrokeWidth(8);
				ellipse.setFill(Color.TRANSPARENT);
					
				// Add the ellipse to the cell
				this.getChildren().add(ellipse); 
				
			}	
			
			return this; //return updated cell
		}
		
		// Define how to handle mouse click event
		private void handleEvent() {
			
			// If the cell is empty and the game is not over
			if(token==' ' && whoseTurn!=' ') {
				
				// Set token in the cell and return this cell
				Cell c=setToken(whoseTurn);
				
				// Animation which is used to show the token in this cell
				FadeTransition appear=new FadeTransition(Duration.millis(1000),c);
				appear.setFromValue(0);
				appear.setToValue(1);
				appear.setCycleCount(1);
				appear.play();
		
				// Check the game status
				if(isWon(whoseTurn)) {
					
					lblStatus.setText(whoseTurn+ " won!!!");
					whoseTurn=' '; // Game over
					
				} else if(isFull()) {
					
					lblStatus.setText("Draw!!!");
					whoseTurn=' '; // Game over
					
				} else {
				
					// Change the turn
					whoseTurn=(whoseTurn=='X')?'0':'X';
					
					//Display whose turn
					lblStatus.setText(whoseTurn+" turn");
				}
			}
		}
		
	}
	
	/* Change the background of winner's tokens.
	 * Three options:
	 * tokens in the same row,
	 * tokens in the same column,
	 * diagonal win.
	 */
	private void colorChange(winnerType typeOfWin, int index) {
		
		switch(typeOfWin) {
		
		case COLUMN: {
			cell[0][index].changeBackground();
			cell[1][index].changeBackground();
			cell[2][index].changeBackground();
			break;
			}
		case ROW: {
			cell[index][0].changeBackground();
			cell[index][1].changeBackground();
			cell[index][2].changeBackground();
			break;
			}
		case DIAGONAL:{
			cell[0][0].changeBackground();
			cell[1][1].changeBackground();
			cell[2][2].changeBackground();
			break;
			}
		case SUBDIAGONAL: {
			cell[0][2].changeBackground();
			cell[1][1].changeBackground();
			cell[2][0].changeBackground();
			break;
		}
		}

	}
}
	
