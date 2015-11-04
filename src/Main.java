public class Main {

    public static void main(String[] args) {

        StartMenu start = new StartMenu().start();
        //GameEngine ge = new GameEngine().start();

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
