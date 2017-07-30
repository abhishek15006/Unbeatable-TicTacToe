import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by Abhishek Vashisht 2015006
 */



public class TicTacToe extends JFrame implements ActionListener{
    private static JButton start,exitMain,exitGame;
    private static JPanel grid;
    private static JButton[] tiles;
    private static ButtonGroup bg;
    private static JPanel option_menu;
    private static JRadioButton user_user,user_ai,ai_com,user_com;
    private static JLabel status;
    private static JLabel welcome;
    private static Player player1,player2;
    private static char currentsym;
    private static int endseq[][] = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    private static char[] stat;
    private static Player currentPlayer;

    private TicTacToe(){

        setLayout(new FlowLayout());
        welcome = new JLabel("Welcome to TicTacToe");
        welcome.setFont(new Font("Aerial",Font.BOLD,45));
        start = new JButton("Start");
        exitMain = new JButton("Exit");

        start.addActionListener(this);
        exitMain.addActionListener(this);

        add(welcome);add(start);add(exitMain);
        setSize(550,200);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void startGame(){
        getContentPane().removeAll();
        repaint();
        //setLayout(new BorderLayout());
        grid = new JPanel();
        option_menu = new JPanel();

        tiles = new JButton[9];
        for(int i=0;i<9;++i){
            tiles[i] = new JButton(" ");
            tiles[i].setEnabled(false);
            tiles[i].setActionCommand(String.valueOf(i));
            tiles[i].setFont(new Font("Aerial",Font.BOLD,72));
            //tiles[i].setBackground(Color.ORANGE);
            tiles[i].addActionListener(this);
            grid.add(tiles[i]);
        }

        grid.setLayout(new GridLayout(3,3));

        option_menu.setLayout(new FlowLayout());

        user_user = new JRadioButton("User vs User");
        option_menu.add(user_user);
        user_user.addActionListener(this);

        user_ai = new JRadioButton("User vs AI");
        option_menu.add(user_ai);
        user_ai.addActionListener(this);

        ai_com = new JRadioButton("AI vs Computer");
        option_menu.add(ai_com);
        ai_com.addActionListener(this);

        user_com = new JRadioButton("User vs Computer");
        option_menu.add(user_com);
        user_com.addActionListener(this);
        exitGame = new JButton("Exit");
        option_menu.add(exitGame);
        exitGame.addActionListener(this);
        exitGame.setActionCommand("Exit Game");

        bg = new ButtonGroup();
        bg.add(user_user);
        bg.add(user_ai);
        bg.add(ai_com);
        bg.add(user_com);
        bg.add(exitGame);

        setLayout(new BorderLayout());
        add(option_menu,BorderLayout.PAGE_START);
        add(grid,BorderLayout.CENTER);

        status = new JLabel("Status : ");
        status.setBorder(new LineBorder(Color.blue,3));
        add(status,BorderLayout.PAGE_END);
        player1 = new Player();
        player1.name = " ";player1.sym = 'O';

        player2 = new Player();
        player2.name = " ";player2.sym = 'O';

        stat = new char[9];
        for(int i=0;i<9;++i){
            stat[i] = '-';
        }
        setSize(550,400);
        setVisible(true);

    }

    public void exitFromGame(){
        getContentPane().removeAll();
        repaint();
        setLayout(new FlowLayout());
        welcome = new JLabel("Welcome to TicTacToe");
        welcome.setFont(new Font("Aerial",Font.BOLD,45));
        start = new JButton("Start");
        exitGame = new JButton("Exit");

        start.addActionListener(this);
        exitGame.addActionListener(this);

        add(welcome);add(start);add(exitGame);
        setSize(550,200);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void gridReset(){
        for(int i=0;i<9;++i){
            tiles[i].setText(String.valueOf(""));
            tiles[i].setEnabled(true);
            tiles[i].setBackground(null);
            tiles[i].setBorder(BorderFactory.createEmptyBorder());
            stat[i] = '-';
        }
    }

    public void disableAll(){
        for(int i=0;i<9;++i){
            tiles[i].setEnabled(false);
        }
    }

    public void disableOptionmenu(){

        user_ai.setEnabled(false);
        user_user.setEnabled(false);
        user_com.setEnabled(false);
        ai_com.setEnabled(false);

    }

    public void enableOptionmenu(){

        user_ai.setEnabled(true);
        user_user.setEnabled(true);
        user_com.setEnabled(true);
        ai_com.setEnabled(true);

        bg.clearSelection();
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getActionCommand().equals("Start")){
            startGame();
        }
        else if(ae.getActionCommand().equals("Exit")){
            System.exit(0);
        }else if(ae.getActionCommand().equals("Exit Game")){
            exitFromGame();
        }else if(ae.getActionCommand().equals("User vs User")){
            new PlayerName(this,this,player1,player2,true,true).setModal(true);

            //GameOn();
            //gridReset();
            disableOptionmenu();
        }else if(ae.getActionCommand().equals("User vs AI")){
            player1.name = "AI";
            new PlayerName(this,this,player1,player2,false,true).setModal(true);
            //GameOn();
            //gridReset();


            disableOptionmenu();

        }else if(ae.getActionCommand().equals("AI vs Computer")){
            player1.name = "AI";
            player2.name = "Computer";
            GameOn();
            gridReset();

            if(currentPlayer.sym==player1.sym){
                AiAction();
            }else{
                ComputerAction();
            }
            disableOptionmenu();

        }else if(ae.getActionCommand().equals("User vs Computer")){
            new PlayerName(this,this,player1,player2,false,true);
            player1.name = "Computer";
            //gridReset();

            disableOptionmenu();

        }else{

            if(user_user.isSelected()){
                int index = Integer.valueOf(ae.getActionCommand());
                stat[index] = currentPlayer.sym;
                tiles[index].setText(String.valueOf(currentPlayer.sym));
                tiles[index].setEnabled(false);

                if(isGameOn()==1){
                    status.setText(player1.name + " WON");

                }else if(isGameOn()==2){
                    status.setText(player2.name + " WON");
                }else if(isGameOn()==3){
                    status.setText("Tie Game");
                }else{
                    if(currentPlayer.equals(player1))
                        currentPlayer = player2;
                    else
                        currentPlayer = player1;
                    updateStatus();
                }


            }else if(user_ai.isSelected()){

                int index = Integer.valueOf(ae.getActionCommand());
                stat[index] = player2.sym;
                tiles[index].setText(String.valueOf(player2.sym));
                tiles[index].setEnabled(false);

                currentPlayer = player1;

                System.out.println(player1.name + " & " + player2.name);

                if(isGameOn()==1){
                    status.setText(player1.name + " WON");

                }else if(isGameOn()==2){
                    status.setText(player2.name + " WON");
                }else if(isGameOn()==3){
                    status.setText("Tie Game");
                }else{
                    updateStatus();
                    AiAction();
                }

            }else if(ai_com.isSelected()){

            }else if(user_com.isSelected()){
                int index = Integer.valueOf(ae.getActionCommand());
                stat[index] = player2.sym;
                tiles[index].setText(String.valueOf(player2.sym));
                tiles[index].setEnabled(false);

                currentPlayer = player1;

                if(isGameOn()==1){
                    status.setText(player1.name + " WON");

                }else if(isGameOn()==2){
                    status.setText(player2.name + " WON");
                }else if(isGameOn()==3){
                    status.setText("Tie Game");
                }else{

                    updateStatus();
                    ComputerAction();
                }
            }
        }
    }

    public void GameOn(){
        gridReset();
        //char[] stat = new char[9];
        Random rand = new Random();
        int index = rand.nextInt(2);

        if(index==0){
            currentPlayer = player1;
            player1.sym = 'O';
            player2.sym = 'X';

        }else{
            currentPlayer = player2;
            player1.sym = 'X';
            player2.sym = 'O';

        }
        updateStatus();
        //System.out.println(currentPlayer.sym);
    }

    //returns 1 when player1 wins; returns 2 when player2 wins; returns 3 for tie; return 0 for gameon
    public int isGameOn(){
        int hyphen=0;

        for(int i=0;i<8;++i){
            if(stat[endseq[i][0]]=='O' && stat[endseq[i][1]]=='O' && stat[endseq[i][2]]=='O'){
                enableOptionmenu();
                disableAll();

                tiles[endseq[i][0]].setEnabled(true);
                tiles[endseq[i][1]].setEnabled(true);
                tiles[endseq[i][2]].setEnabled(true);

                tiles[endseq[i][0]].setBorder(new LineBorder(Color.ORANGE,5));
                tiles[endseq[i][1]].setBorder(new LineBorder(Color.ORANGE,5));
                tiles[endseq[i][2]].setBorder(new LineBorder(Color.ORANGE,5));

                disableAll();
                //enableOptionmenu();
                if(player1.sym=='O'){

                    return 1;
                }else if(player2.sym=='O'){

                    return 2;
                }
            }else if(stat[endseq[i][0]]=='X' && stat[endseq[i][1]]=='X' && stat[endseq[i][2]]=='X'){
                enableOptionmenu();
                disableAll();
                tiles[endseq[i][0]].setEnabled(true);
                tiles[endseq[i][1]].setEnabled(true);
                tiles[endseq[i][2]].setEnabled(true);

                tiles[endseq[i][0]].setBorder(new LineBorder(Color.ORANGE,5));
                tiles[endseq[i][1]].setBorder(new LineBorder(Color.ORANGE,5));
                tiles[endseq[i][2]].setBorder(new LineBorder(Color.ORANGE,5));
                disableAll();

                if(player1.sym=='X'){

                    return 1;
                }else if(player2.sym=='X'){

                    return 2;
                }
            }
        }

        for(int i=0;i<9;++i){
            if(stat[i]=='-')
                hyphen++;
        }

        if(hyphen==0){
            enableOptionmenu();
            return 3;
        }else
            return 0;
    }

    public int arr_stat(char curr_sym,char other_sym){
        int hyphen=0;

        for(int i=0;i<8;++i){
            if(stat[endseq[i][0]]==curr_sym && stat[endseq[i][1]]==curr_sym && stat[endseq[i][2]]==curr_sym){
                return 1;
            }else if(stat[endseq[i][0]]==other_sym && stat[endseq[i][1]]==other_sym && stat[endseq[i][2]]==other_sym){
                return 2;
            }
        }

        for(int i=0;i<9;++i){
            if(stat[i]=='-')
                hyphen++;
        }

        if(hyphen==0)
            return 3;
        else
            return 0;
    }

    public int minimax(boolean is_maximizing_player,int depth){

        char other_sym;
        if(currentPlayer.sym=='X')
            other_sym = 'O';
        else
            other_sym = 'X';

        if(arr_stat(currentPlayer.sym,other_sym)==3) {
            //System.out.println("Got 0");
            return 0;
        }
        if(arr_stat(currentPlayer.sym,other_sym)==1 && currentPlayer.name.equalsIgnoreCase(player1.name)) {
            //System.out.println("Got 10");
            return 10;
        }
        if(arr_stat(currentPlayer.sym,other_sym)==2 && currentPlayer.name.equalsIgnoreCase(player2.name)) {
            //System.out.println("Got 10");
            return 10;
        }
        if(arr_stat(currentPlayer.sym,other_sym)==1 && currentPlayer.name.equalsIgnoreCase(player2.name)) {
            //System.out.println("Got -10");
            return -10;
        }
        if(arr_stat(currentPlayer.sym,other_sym)==2 && currentPlayer.name.equalsIgnoreCase(player1.name)){
            //System.out.println("Got -10");
            return -10;
        }


        if(is_maximizing_player){
            int best = -1000;
            int val;
            for(int i=0;i<9;++i){
                if(stat[i]=='-'){
                    stat[i] = currentPlayer.sym;
                    val = minimax(false,depth + 1);
                    if(best<val)
                        best = val;
                    //System.out.println("best " + best + " max = " + is_maximizing_player + "depth = " + depth);
                    stat[i] = '-';
                }
            }
            return best;
        }else{
            int best = 1000;
            int val;
            for(int i=0;i<9;++i){
                if(stat[i]=='-'){
                    stat[i] = other_sym;
                    val = minimax(true,depth + 1);
                    if(best>val)
                        best = val;
                    //System.out.println("best " + best + " max = " + is_maximizing_player + "depth = " + depth);
                    stat[i] = '-';
                }
            }
            return best;
        }

    }

    public void AiAction(){
        int index = -1;
        final int ind;
        char arr[] = new char[9];
        int max[] = new int[9];
        System.out.println(currentPlayer.name);

        /*
        //finish if ai has a chance
        for(int i=0;i<8;++i){
            if(stat[endseq[i][0]]=='-' && stat[endseq[i][1]]==player1.sym && stat[endseq[i][2]]==player1.sym)
                index = endseq[i][0];
            else if(stat[endseq[i][0]]==player1.sym && stat[endseq[i][1]]=='-' && stat[endseq[i][2]]==player1.sym)
                index = endseq[i][1];
            else if(stat[endseq[i][0]]==player1.sym && stat[endseq[i][1]]==player1.sym && stat[endseq[i][2]]=='-')
                index = endseq[i][2];
        }



        //defend if player is about to win
        if(index<0)
        for(int i=0;i<8;++i){
            if(stat[endseq[i][0]]=='-' && stat[endseq[i][1]]==player2.sym && stat[endseq[i][2]]==player2.sym)
                index = endseq[i][0];
            else if(stat[endseq[i][0]]==player2.sym && stat[endseq[i][1]]=='-' && stat[endseq[i][2]]==player2.sym)
                index = endseq[i][1];
            else if(stat[endseq[i][0]]==player2.sym && stat[endseq[i][1]]==player2.sym && stat[endseq[i][2]]=='-')
                index = endseq[i][2];
        }

        if(index<0)
            for(int i=0;i<8;++i){
                if(stat[endseq[i][0]]=='-' && stat[endseq[i][1]]=='-' && stat[endseq[i][2]]==player1.sym)
                    index = endseq[i][0];
                else if(stat[endseq[i][0]]==player1.sym && stat[endseq[i][1]]=='-' && stat[endseq[i][2]]=='-')
                    index = endseq[i][1];
                else if(stat[endseq[i][0]]=='-' && stat[endseq[i][1]]==player1.sym && stat[endseq[i][2]]=='-')
                    index = endseq[i][2];
            }

        //Attack Mode, Neither victory nor lose condition
        if(index<0){
            int j=0;
            int choices[] = new int[9];
            for(int i=0;i<9;++i){
                if(stat[i]=='-')
                    choices[j++] = i;
            }
            Random rand = new Random();
            index = choices[rand.nextInt(j)];

        }
        */
        //ind = index;    //Selected Tile

        for(int j=0;j<9;++j){
            arr[j] = stat[j];
            max[j] = 0;
        }



        for(int i=0;i<9;++i){
            if(stat[i]=='-'){
                stat[i] = currentPlayer.sym;
                max[i] = minimax(false,0);
                /*
                for(int j=0;j<9;++j)
                    System.out.print(i + " " + max[j] + " ");
                System.out.println();
                */
                stat[i] = '-';
            }
        }

        for(int i=0;i<9;++i){
            if(stat[i]=='-'){
                if(index<0)
                    index = i;
                else{
                    if(max[index]<max[i])
                        index = i;
                }
            }
        }

        ind = index;

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                stat[ind] = currentPlayer.sym;
                tiles[ind].setText(String.valueOf(currentPlayer.sym));
                tiles[ind].setEnabled(false);

                if(isGameOn()==1){
                    status.setText(player1.name + " WON");
                    System.out.println(player1.name + " won");
                }else if(isGameOn()==2){
                    status.setText(player2.name + " WON");
                }else if(isGameOn()==3){
                    status.setText("Tie Game");
                }else{
                    //status.setText(player2.name + "'s turn");
                    if(currentPlayer.sym==player1.sym)
                        currentPlayer = player2;
                    else if(currentPlayer.sym==player2.sym)
                        currentPlayer = player1;

                    if(ai_com.isSelected()){

                        updateStatus();
                        ComputerAction();
                    }else if(user_ai.isSelected()){

                        updateStatus();
                    }
                }
            }
        });
        timer.setRepeats(false);
        timer.start();

        //Timer timer = new Timer(1000,null);timer.setRepeats(false);timer.start();
    }

    public void ComputerAction(){
        int index = -1;
        final int ind;

        if(index<0){
            ArrayList<Integer> choices = new ArrayList<>();
            for(int i=0;i<9;++i){
                if(stat[i]=='-')
                    choices.add(i);
            }
            Random rand = new Random();
            index = choices.get(rand.nextInt(choices.size()));
        }
        ind = index;
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stat[ind] = currentPlayer.sym;
                tiles[ind].setText(String.valueOf(currentPlayer.sym));
                tiles[ind].setEnabled(false);

                if(isGameOn()==1){
                    status.setText(player1.name + " WON");

                }else if(isGameOn()==2){
                    status.setText(player2.name + " WON");
                }else if(isGameOn()==3){
                    status.setText("Tie Game");
                }else{
                    if(currentPlayer.sym==player1.sym)
                        currentPlayer = player2;
                    else if(currentPlayer.sym==player2.sym)
                        currentPlayer = player1;

                    updateStatus();

                    if(ai_com.isSelected())
                        AiAction();

                }
            }
        });
        timer.setRepeats(false);
        timer.start();

        //Timer timer = new Timer(1000,null);timer.setRepeats(false);timer.start();
    }

    public static void main(String args[]){
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
            System.out.println("No Nimbus");
        }

        TicTacToe t = new TicTacToe();
        t.setTitle("TicTacToe Game");

    }

    public static void updateStatus(){
       status.setText("Status : " + currentPlayer.name + "'s turn");
    }

    class PlayerName extends JDialog implements ActionListener{
        private JTextField name1,name2;
        private JLabel label1,label2,warning;
        private JButton ok;
        private Player player1,player2;
        private TicTacToe ttt;

        private char currentsym;

        public PlayerName(JFrame frame,TicTacToe ttt,Player player1,Player player2,boolean enable_1,boolean enable_2){
            super(frame);
            this.ttt = ttt;
            setSize(300,300);
            setTitle("Players Details");
            setVisible(true);
            setLayout(new FlowLayout());
            setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

            this.player1 = player1;
            this.player2 = player2;

            label1 = new JLabel("Player 1 : ");
            add(label1);
            name1 = new JTextField(20);
            add(name1);
            label2 = new JLabel("Player 2 : ");
            add(label2);
            name2 = new JTextField(20);
            add(name2);
            ok = new JButton("OK");
            add(ok);
            warning = new JLabel("");
            add(warning);
            name1.setEnabled(enable_1);
            name2.setEnabled(enable_2);

            ok.addActionListener(this);
        }

        public void actionPerformed(ActionEvent ae){
            if(name1.isEnabled())
                player1.name = name1.getText().toString();
            if(name2.isEnabled())
                player2.name = name2.getText().toString();

            if((name1.isEnabled() && player1.name.equals(""))&& (name2.isEnabled() && player2.name.equals(""))){
                warning.setText("Both fields are empty");
            }else if(name2.isEnabled() && player2.name.equals("")){
                warning.setText("Player2 field is empty");
            }else if((name1.isEnabled() && player1.name.equals(""))){
                warning.setText("Player1 field is empty");
            }else{
                ttt.GameOn();
                System.out.println(currentPlayer.name);
                if(user_ai.isSelected()){
                    if(currentPlayer.sym==player1.sym)
                        AiAction();
                }else if(user_com.isSelected()){
                    if(currentPlayer.sym==player1.sym)
                        ComputerAction();
                }
                dispose();
            }

        }
    }

    class Player{
        String name;
        char sym;
    }

}
