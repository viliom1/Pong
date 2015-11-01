public class Main {

    public static void main(String[] args) {

        new GameEngine().start();

	    //char[][] board = board();
        //boardPrint(board);
        menuPrint();
        chooseGame();
        singlePlayer();
        AI();
        menuControl();
        menu();
        multiPlayer();
        gameSpeed();
        gameOver();
        pause();




    }

    private static char[][] board() {

        char[][] board = new char[23][88];
        for (int i = 0; i <board.length ; i++) {
            for (int j = 0; j <board[0].length ; j++) {
                if ((i > 7 && i < 13 ) && (j == 1 || j == board[0].length-2)){
                    board[i][j] = '#';
                }
                else if (j == 43 && i != 0){
                    board[i][j] = '|';
                }
                else if (i == 0 || i == board.length-1){
                    board[i][j] = '_';
                }
                else if (j == 0 || j == board[0].length-1){
                    board[i][j] = '|';
                }
                else{
                    board[i][j] = ' ';
                }
            }

        }
        return board;
    }

    private static void pause() {
    }

    private static void gameOver() {
    }

    private static void gameSpeed() {

    }

    private static void multiPlayer() {
    }

    private static void menu() {
    }

    private static void menuControl() {
    }

    private static void AI() {
    }

    private static void singlePlayer() {

    }

    private static void chooseGame() {
    }

    private static void menuPrint() {

    }

    private static void boardPrint(char[][] board) {
        for (int i = 0; i <board.length ; i++) {
            for (int j = 0; j <board[0].length ; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }
}
