import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.util.*;
import java.lang.Math;

public class TicTacToe extends JFrame{
	//main
	public static void main(String[] args)
    {
     chooseTurn();
    }

	//this function asks the user if he/she want to go first then calls TIcTacToe
public static void chooseTurn()
{
	JPanel myPanel = new JPanel();
	String aiLabel = "";
    String playerLabel = "";
    int firstTurn = 0;
    String[] options = new String[] {"X", "O"};

    myPanel.add(new JLabel("Play as:"));

    int result = JOptionPane.showOptionDialog(null, myPanel,
             "First to move?", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
             null, options, options[0]);

    if(result == 1){
  	 firstTurn = 1;
       aiLabel = "X";
       playerLabel = "O";
       TicTacToe game = new TicTacToe(aiLabel, playerLabel, firstTurn);
       game.board[1][1].setText("X");
       game.board[1][1].setFont(new Font("Arial", Font.PLAIN, 80));
       game.board[1][1].setBackground(Color.BLACK);
       game.board[1][1].setForeground(Color.WHITE);
       game.board[1][1].setEnabled(false);
    }else{
  	 firstTurn = 2;
       aiLabel = "O";
       playerLabel = "X";
       TicTacToe game = new TicTacToe(playerLabel, aiLabel, firstTurn);
    }

}

    Container mainPanel = this.getContentPane();
    JPanel boardPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    JButton[][] board = new JButton[3][3];
    JButton resetButton = new JButton("Reset");

    ArrayList<CoordData> totalScore = new ArrayList<>();
    ArrayList<ArrayList<Integer>> available;


public TicTacToe(String comp, String player, int turn){
    final String pl = player;
    final String ai = comp;
    final int whoseTurn = turn;
    Color xcolor = Color.BLACK;
    Color ocolor = Color.WHITE;
    Color neutral = Color.LIGHT_GRAY;
    buttonPanel.setLayout(new FlowLayout());
    boardPanel.setLayout(new GridLayout(3, 3, 5, 5));


    for(int i = 0; i < 3; i ++){
        for(int j = 0; j < 3; j++){
                board[i][j] = new JButton("");
                board[i][j].setBackground(neutral);
                board[i][j].addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                        int x = 0;
                        int y = 0;
                        for(int i = 0; i < board.length; i++) {
                                for(int j = 0; j < board[i].length; j++) {
                                	//player move
                                        //identify which button is clicked
                                        if(ae.getSource() == board[i][j]){
                                        	//when clicked
                                        	if(pl == "X"){
                                            	board[i][j].setText("X");
                                            	board[i][j].setFont(new Font("Arial", Font.PLAIN, 80));
                                            	board[i][j].setBackground(xcolor);
                                            	board[i][j].setForeground(ocolor);
                                                board[i][j].setEnabled(false);
                                            } else {
                                            	board[i][j].setText("O");
                                            	board[i][j].setFont(new Font("Arial", Font.PLAIN, 80));
                                            	board[i][j].setBackground(ocolor);
                                            	board[i][j].setForeground(xcolor);
                                                board[i][j].setEnabled(false);
                                            }

                                                alphaBeta(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1, ai, pl);
                                                ArrayList<Integer> coord = move();
                                                //ai move
                                                if(ai == "X"){
                                                	board[coord.get(0)][coord.get(1)].setText("X");
                                                	board[coord.get(0)][coord.get(1)].setFont(new Font("Arial", Font.PLAIN, 80));
                                                	board[coord.get(0)][coord.get(1)].setBackground(xcolor);
                                                	board[coord.get(0)][coord.get(1)].setForeground(ocolor);
                                                    board[coord.get(0)][coord.get(1)].setEnabled(false);
                                                } else {
                                                	board[coord.get(0)][coord.get(1)].setText("O");
                                                	board[coord.get(0)][coord.get(1)].setFont(new Font("Arial", Font.PLAIN, 80));
                                                	board[coord.get(0)][coord.get(1)].setBackground(ocolor);
                                                	board[coord.get(0)][coord.get(1)].setForeground(xcolor);
                                                    board[coord.get(0)][coord.get(1)].setEnabled(false);
                                                }

                                                if(isWin()){
                                                JPanel myPanel = new JPanel();

                                                        if (isWinX()) {
                                                             myPanel.add(new JLabel("X Won!"));
                                                            JOptionPane.showMessageDialog(null, myPanel,
                                                            "X Won!", JOptionPane.OK_OPTION);
                                                        } else if (isWinO()) {
                                                             myPanel.add(new JLabel("O Won!"));
                                                            JOptionPane.showMessageDialog(null, myPanel,
                                                            "O Won!", JOptionPane.OK_OPTION);
                                                        } else {
                                                            myPanel.add(new JLabel("It's a Draw"));
                                                            JOptionPane.showMessageDialog(null, myPanel,
                                                            "It's a Draw!", JOptionPane.OK_OPTION);
                                                        }
                                                        restart(board, whoseTurn);
                                                }


                                        }
                                }
                        }

                }
                });
                boardPanel.add(board[i][j]);
        }
    }


    resetButton.addActionListener(new ActionListener()
    {
        public void actionPerformed(ActionEvent ae)
        {
           restart(board, whoseTurn);
        }

    });

    buttonPanel.add(resetButton);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    mainPanel.add(boardPanel);
    setSize(500,550);
    setVisible(true);
    setTitle("X-O-X");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}

//this function counts the no of X's and O's and gets the total score of a state
public int getScore(String player) {
        int score = 0;
        int X = 0;
        int O = 0;

        //rows
        for (int i = 0; i < 3; i++) {
        	X = O = 0;
            for (int j = 0; j < 3; j++) {
            	if (board[i][j].getText().equals("X")) {
                    X++;
                } else {
                    O++;
                }
            }
            score = score + zeroSum(X, O, player);
        }

        //colums
        for (int j = 0; j < 3; j++) {
        	X = O = 0;
            for (int i = 0; i < 3; i++) {
                if (board[i][j].getText().equals("X")) {
                    X++;
                } else {
                    O++;
                }
            }
            score = score + zeroSum(X, O, player);
        }



        //diagonal1
        X = O = 0;
        for (int i = 0, j = 0; i < 3; i++, j++) {
            if (board[i][j].getText().equals("X")) {
                X++;
            } else if (board[i][j].getText().equals("O")) {
                O++;
            }
        }

        score = score + zeroSum(X, O, player);

        //diagonal2
        X = O = 0;
        for (int i = 2, j = 0; i > -1; --i, j++) {
            if (board[i][j].getText().equals("X")) {
                X++;
            } else if (board[i][j].getText().equals("O")) {
                O++;
            }
        }

        score = score + zeroSum(X, O, player);

        return score;
    }

	//this function determine the score depending on the no of instances of X and O
    public int zeroSum(int X, int O, String player){
        int score;

        if(player == "O") {
	        if (X == 3) {
	            score = 3;
	        } else if (X == 2 && O == 0) {
	            score = 2;
	        } else if (X == 1 && O == 0) {
	            score = 1;
	        } else if (O == 3) {
	            score = -3;
	        } else if (O == 2 && X == 0) {
	            score = -2;
	        } else if (O == 1 && X == 0) {
	            score = -1;
	        } else {
	            score = 0;
	        }
        } else {
        	if (X == 3) {
                score = -3;
            } else if (X == 2 && O == 0) {
                score = -2;
            } else if (X == 1 && O == 0) {
                score = -1;
            } else if (O == 3) {
                score = 3;
            } else if (O == 2 && X == 0) {
                score = 2;
            } else if (O == 1 && X == 0) {
                score = 1;
            } else {
                score = 0;
            }
        }
        return score;
    }


public boolean isWin() {
	if(isWinX() || isWinO() || availabilityList().isEmpty())
		return true;
	else
		return false;

    }

    public boolean isWinX() {
    	for (int i = 0; i < 3; i++) {
            if (((board[i][0].getText().equals(board[i][1].getText()) && board[i][0].getText().equals(board[i][2].getText()) && board[i][0].getText().equals("X"))
              || (board[0][i].getText().equals(board[1][i].getText()) && board[0][i].getText().equals(board[2][i].getText()) && board[0][i].getText().equals("X")))) {
                return true;
            }
        }

        if ((board[0][0].getText().equals(board[1][1].getText()) && board[0][0].getText().equals(board[2][2].getText())  && board[0][0].getText().equals("X"))
         || (board[0][2].getText().equals(board[1][1].getText()) && board[0][2].getText().equals(board[2][0].getText()) && board[0][2].getText().equals("X"))) {
            return true;
        }

        return false;
    }

    public boolean isWinO() {
    	for (int i = 0; i < 3; i++) {
            if (((board[i][0].getText().equals(board[i][1].getText()) && board[i][0].getText().equals(board[i][2].getText()) && board[i][0].getText().equals("O"))
              || (board[0][i].getText().equals(board[1][i].getText()) && board[0][i].getText().equals(board[2][i].getText()) && board[0][i].getText().equals("O")))) {
                return true;
            }
        }

        if ((board[0][0].getText().equals(board[1][1].getText()) && board[0][0].getText().equals(board[2][2].getText())  && board[0][0].getText().equals("O"))
         || (board[0][2].getText().equals(board[1][1].getText()) && board[0][2].getText().equals(board[2][0].getText()) && board[0][2].getText().equals("O"))) {
            return true;
        }

        return false;
    }


    //this function determine the cells that are not used
public ArrayList<ArrayList<Integer>> availabilityList() {
        available = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].getText().equals("")) {
                    ArrayList<Integer> coord = new ArrayList<>();
                    coord.add(i);
                    coord.add(j);
                    available.add(coord);
                }
            }
        }
        return available;
    }


public int alphaBeta(int alpha, int beta, int depth, int turn, String ai, String player){
		int negInfinity = Integer.MIN_VALUE, posInfinity = Integer.MAX_VALUE;
		int minValue = Integer.MAX_VALUE,  maxValue = Integer.MIN_VALUE;
        if(beta<=alpha){
        	if(turn == 1)
        		return posInfinity;
        	else
        		return negInfinity;
        	}

        if(isWin()) return getScore(player);

        ArrayList<ArrayList<Integer>> pointsAvailable = availabilityList();

        if(pointsAvailable.isEmpty()) return 0;

        if(depth==0) totalScore.clear();



        for(int i=0;i<pointsAvailable.size(); i++){
            ArrayList<Integer> coord = pointsAvailable.get(i);

            int currentScore = 0;

            if(turn == 1){
                board[coord.get(0)][coord.get(1)].setText(ai);
                currentScore = alphaBeta(alpha, beta, depth+1, 2, ai, player);
                maxValue = Math.max(maxValue, currentScore);

                alpha = Math.max(currentScore, alpha);

                if(depth == 0)
                    totalScore.add(new CoordData(currentScore, coord));
            }else if(turn == 2){
                board[coord.get(0)][coord.get(1)].setText(player);
                currentScore = alphaBeta(alpha, beta, depth+1, 1, ai, player);
                minValue = Math.min(minValue, currentScore);

                beta = Math.min(currentScore, beta);
            }

            board[coord.get(0)][coord.get(1)].setText("");
            if(currentScore == posInfinity || currentScore == negInfinity) break;

        }
        if(turn == 1)
        	return maxValue;
        else
        	return minValue;
    }

public ArrayList<Integer> move() {
        int MAX = -100000;
        int best = -1;

        for (int i = 0; i < totalScore.size(); i++) {
            if (MAX < totalScore.get(i).score) {
                MAX = totalScore.get(i).score;
                best = i;
            }
        }
        //ArrayList<Integer>
        return totalScore.get(best).coordinates;
    }

public void restart(JButton[][] board, int whoseTurn) {
	for (int i = 0; i < 3; i++){
        for (int j = 0; j < 3; j++){
            board[i][j].setText("");
            board[i][j].setBackground(Color.LIGHT_GRAY);
            board[i][j].setEnabled(true);
        }
	}
	if(whoseTurn == 1){
        board[1][1].setText("X");
     	board[1][1].setFont(new Font("Arial", Font.PLAIN, 80));
     	board[1][1].setBackground(Color.BLACK);
     	board[1][1].setForeground(Color.WHITE);
        board[1][1].setEnabled(false);
}
}

}
