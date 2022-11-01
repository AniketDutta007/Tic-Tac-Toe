import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class TicTacToe {
    JFrame frame;
    JPanel panel;
    JLabel player1Label, player2Label;
    JTextField player1Field, player2Field;
    JButton play;
    Font font;
    Cursor handCursor;
    String player1, player2;
    TicTacToe(){
        frame = new JFrame();
        frame.setSize(450, 200);
        frame.setLocation(100, 300);
        frame.setTitle("Player Details");

        font = new Font("Tahoma", Font.PLAIN, 18);
        handCursor = new Cursor(Cursor.HAND_CURSOR);

        panel = new JPanel();
        panel.setLayout(null);

        player1Label = new JLabel("Player 1");
        player1Label.setFont(font);
        player1Label.setSize(95, 25);
        player1Label.setLocation(30, 30);
        panel.add(player1Label);

        player1Field = new JTextField();
        player1Field.setFont(font);
        player1Field.setSize(250, 25);
        player1Field.setLocation(150, 30);
        player1Field.setColumns(15);
        panel.add(player1Field);

        player2Label = new JLabel("Player 2");
        player2Label.setFont(font);
        player2Label.setSize(100, 25);
        player2Label.setLocation(30, 65);
        panel.add(player2Label);

        player2Field = new JTextField();
        player2Field.setFont(font);
        player2Field.setSize(250, 25);
        player2Field.setLocation(150, 65);
        player2Field.setColumns(15);
        panel.add(player2Field);

        play = new JButton("Let's play");
        play.setFont(font);
        play.setSize(150, 35);
        play.setLocation(150, 105);
        panel.add(play);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1 = player1Field.getText();
                player2 = player2Field.getText();
                new Game(player1, player2);
                dispose();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args){
        new TicTacToe();
    }
    public void dispose(){
        frame.dispose();
//        this.setVisible(false);
    }
}

class Game extends JFrame implements ActionListener {
    String[] players;
    JLabel message, indicator;
    JButton[][] buttons;
    JButton resetButton;
    boolean[][] isCellVisited;
    //isCellVisited[i][j] false - not visited
    //isCellVisited[i][j] true - visited
    int[][] board;
    //board[i][j] 0 - O
    //board[i][j] 1 - X
    int turn = 0;
    //turn 0 - O
    //turn 1 - X
    int cellsVisited;
    boolean isMessageVisible;
    int winner;
    //winner -1 - Game not over
    //winner 0 - Player1 wins
    //winner 1 - Player2 wins
    //winner 2 - Draw
    Game(String player1, String player2){
        isCellVisited = new boolean[3][3];
        board = new int[3][3];

        players = new String[2];
        int playerOId = (int)Math.floor(Math.random()*2+1);
        players[0] = playerOId==1?player1:player2;//player plays with O
        players[1] = playerOId==1?player2:player1;//player plays with X

        turn = (int)Math.floor(Math.random()*2);
        cellsVisited = 0;
        isMessageVisible = false;

        winner = -1;

        for (int i=0; i<board.length; i++)
            Arrays.fill(board[i],-1);

//        setSize(482,500);
        setSize(482,500);
        setLocation(100,100);
        setLayout(null);

        indicator = new JLabel(players[turn]+" - "+(turn==0?'O':'X'),SwingConstants.CENTER);
        indicator.setFont(new Font("Tahoma",Font.BOLD,20));
        indicator.setForeground(Color.GREEN);
        indicator.setSize(getWidth(),25);
        indicator.setLocation(0,50);
        add(indicator);

        message = new JLabel("Invalid Move!",SwingConstants.CENTER);
        message.setFont(new Font("Tahoma",Font.BOLD,20));
        message.setForeground(Color.RED);
        message.setVisible(false);
        message.setSize(getWidth(),25);
        message.setLocation(0,400);
        add(message);

//        ImageIcon replayIcon = new ImageIcon("replay.png");
//        ImageIcon replayIcon = new ImageIcon(new ImageIcon("replay.png").getImage().getScaledInstance(30,25, Image.SCALE_SMOOTH) );

//        resetButton = new JButton(replayIcon);
        resetButton = new JButton("Replay");
        resetButton.setFont(new Font("Tahoma",Font.BOLD,18));
        resetButton.setVisible(false);
        resetButton.setSize(100,35);
        resetButton.setLocation(191,445);
        add(resetButton);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                new Game(players[0], players[1]);
//                dispose();
                reset();
            }
        });

        buttons = new JButton[3][3];

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Tahoma",Font.BOLD,50));
                buttons[i][j].setSize(75,75);
                buttons[i][j].setLocation(100+94*j,100+94*i);
                buttons[i][j].setActionCommand(""+i+""+j);
                buttons[i][j].addActionListener(this);
                add(buttons[i][j]);
            }
        }

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //vertical lines
        g.drawLine(191,120,191,410);
        g.drawLine(286,120,286,410);
        //horizontal lines
        g.drawLine(96,214,381,214);
        g.drawLine(96,310,381,310);
    }
    public void draw(){
        boolean gameOver = isGameOver();
        //gameOver true - Game over
        //gameOver false - Game not over
        if(gameOver){
            setSize(482,550);
            indicator.setVisible(false);
            resetButton.setVisible(true);
            resetButton.setSize(100,35);
            resetButton.setLocation(191,445);
            if(winner==2){
                message.setText("Sorry! It's a draw.");
                System.out.println("Sorry! It's a draw.");
            } else{
                message.setText("Congratulations! "+players[winner]+" you won.");
                System.out.println("Congratulations! "+players[winner]+" you won.");
            }
            isMessageVisible = true;
        } else{
            indicator.setText(players[turn]+" - "+(turn==0?'O':'X'));
            indicator.setSize(getWidth(),25);
            indicator.setLocation(0,50);
        }
        message.setVisible(isMessageVisible);
        message.setSize(getWidth(),25);
        message.setLocation(0,400);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setSize(75, 75);
                buttons[i][j].setLocation(100 + 94 * j, 100 + 94 * i);
                buttons[i][j].setText(isCellVisited[i][j] ? board[i][j] == 0 ? "O" : "X" : "");
                buttons[i][j].setEnabled(!gameOver);
            }
        }
        repaint();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int i, j;
        i = e.getActionCommand().charAt(0) - '0';
        j = e.getActionCommand().charAt(1) - '0';
        if(isCellVisited[i][j]){
            isMessageVisible = true;
            System.out.println("Wrong Move!!!");
        } else{
            isMessageVisible = false;
            isCellVisited[i][j] = true;
            board[i][j] = turn;
            turn = (turn+1)%2;
            cellsVisited++;
        }
        draw();
    }
    public boolean isGameOver(){

        if(board[0][0]==board[0][1]&&board[0][1]==board[0][2]&&(board[0][0]==0||board[0][0]==1)) {
            winner = board[0][0];
            return true;
        } if(board[1][0]==board[1][1]&&board[1][1]==board[1][2]&&(board[1][0]==0||board[1][0]==1)) {
            winner = board[1][0];
            return true;
        } if(board[2][0]==board[2][1]&&board[2][1]==board[2][2]&&(board[2][0]==0||board[2][0]==1)) {
            winner = board[2][0];
            return true;
        }

        if(board[0][0]==board[1][0]&&board[1][0]==board[2][0]&&(board[0][0]==0||board[0][0]==1)) {
            winner = board[0][0];
            return true;
        } if(board[0][1]==board[1][1]&&board[1][1]==board[2][1]&&(board[0][1]==0||board[0][1]==1)) {
            winner = board[0][1];
            return true;
        } if(board[0][2]==board[1][2]&&board[1][2]==board[2][2]&&(board[0][2]==0||board[0][2]==1)) {
            winner = board[0][2];
            return true;
        }

        if(board[0][0]==board[1][1]&&board[1][1]==board[2][2]&&(board[0][0]==0||board[0][0]==1)) {
            winner = board[0][0];
            return true;
        } if(board[2][0]==board[1][1]&&board[1][1]==board[0][2]&&(board[2][0]==0||board[2][0]==1)) {
            winner = board[0][0];
            return true;
        }

        if(cellsVisited>=9) {
            winner = 3;
            return true;
        }
        else {
            return false;
        }
    }
    public void dispose(){
        this.dispose();
    }
    public void reset(){
        setSize(482,500);

        String player1, player2;
        player1 = players[0];
        player2 = players[1];

        isCellVisited = new boolean[3][3];
        board = new int[3][3];

        int playerOId = (int)Math.floor(Math.random()*2+1);
        players[0] = playerOId==1?player1:player2;//player plays with O
        players[1] = playerOId==1?player2:player1;//player plays with X

        turn = (int)Math.floor(Math.random()*2);
        cellsVisited = 0;
        isMessageVisible = false;

        winner = -1;

        for (int i=0; i<board.length; i++)
            Arrays.fill(board[i],-1);

        indicator.setText(players[turn]+" - "+(turn==0?'O':'X'));
        indicator.setVisible(true);
        indicator.setSize(getWidth(),25);
        indicator.setLocation(0,50);
        message.setVisible(false);
        resetButton.setVisible(false);

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                buttons[i][j].setText("");
                buttons[i][j].setSize(75,75);
                buttons[i][j].setLocation(100+94*j,100+94*i);
                buttons[i][j].setEnabled(true);
            }
        }

        repaint();
    }
}

