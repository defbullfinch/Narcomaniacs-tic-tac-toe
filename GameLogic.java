import javax.swing.*;

public class GameLogic {
    public static boolean checkWin(JButton[] b, String symbol) {
       //комбинации побед
        int[][] winCombinations = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, //по горизонтали
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, //по вертикали
                {0, 4, 8}, {2, 4, 6}             //по диагонали
        };
        //проверка на ПОТУЖНУ ПЕРЕМОГУ
        for (int[] combo : winCombinations) {
            if (b[combo[0]].getText().equals(symbol) &&
                    b[combo[1]].getText().equals(symbol) &&
                    b[combo[2]].getText().equals(symbol)) {
                return true;
            }
        }
        return false;

    }
    public static boolean isDraw(JButton[] buttons) {
        for (JButton b : buttons) {
            if (b.getText().equals("")) {
                return false;
            }
        }
        return true; //тру если занято все и срабатівает ничья
    }
}

