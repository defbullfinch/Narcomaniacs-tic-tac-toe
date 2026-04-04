import javax.swing.*;
import java.awt.Font;
import java.util.Random;

public class BotLogic {
    private static final Random rand = new Random();

    //метод рандомно перемешивает значения массива
    private static void shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }

    //"Для дибилов"
    public static void makeDibilMove(JButton[] buttons, String botSymbol) {
        int emptyCount = 0;
        for (JButton b : buttons) if (b.getText().equals("")) emptyCount++;
        if (emptyCount == 0) return;

        while (true) {
            int ran = rand.nextInt(9);
            if (buttons[ran].getText().equals("")) {
                setButton(buttons[ran], botSymbol);
                break;
            }
        }
    }

    //"Нормальный"
    public static void makeNormalMove(JButton[] b, String botSymbol) {
        int emptyCount = 0;
        for (JButton btn : b) if (btn.getText().equals("")) emptyCount++;
        if (emptyCount == 0) return;

        //со второго уровня игрок играет рандомно или за х, или за о
        String p = (botSymbol.equals("o")) ? "x" : "o"; //символ игрока

        /*логика бота в нормальном уровне основывается на том, чтобы НЕ ПРОИГРАТЬ, А НЕ ВЫИГРАТЬ*
        ниже представлены все возможные комбинации расположения двух символов игрока*/

        if (b[0].getText().equals(p) && b[1].getText().equals(p) && b[2].getText().equals(""))
            setButton(b[2], botSymbol);
        else if (b[0].getText().equals(p) && b[2].getText().equals(p) && b[1].getText().equals(""))
            setButton(b[1], botSymbol);
        else if (b[1].getText().equals(p) && b[2].getText().equals(p) && b[0].getText().equals(""))
            setButton(b[0], botSymbol);

        else if (b[3].getText().equals(p) && b[4].getText().equals(p) && b[5].getText().equals(""))
            setButton(b[5], botSymbol);
        else if (b[3].getText().equals(p) && b[5].getText().equals(p) && b[4].getText().equals(""))
            setButton(b[4], botSymbol);
        else if (b[4].getText().equals(p) && b[5].getText().equals(p) && b[3].getText().equals(""))
            setButton(b[3], botSymbol);

        else if (b[6].getText().equals(p) && b[7].getText().equals(p) && b[8].getText().equals(""))
            setButton(b[8], botSymbol);
        else if (b[6].getText().equals(p) && b[8].getText().equals(p) && b[7].getText().equals(""))
            setButton(b[7], botSymbol);
        else if (b[7].getText().equals(p) && b[8].getText().equals(p) && b[6].getText().equals(""))
            setButton(b[6], botSymbol);

        else if (b[0].getText().equals(p) && b[4].getText().equals(p) && b[8].getText().equals(""))
            setButton(b[8], botSymbol);
        else if (b[0].getText().equals(p) && b[8].getText().equals(p) && b[4].getText().equals(""))
            setButton(b[4], botSymbol);
        else if (b[4].getText().equals(p) && b[8].getText().equals(p) && b[0].getText().equals(""))
            setButton(b[0], botSymbol);

        else if (b[2].getText().equals(p) && b[4].getText().equals(p) && b[6].getText().equals(""))
            setButton(b[6], botSymbol);
        else if (b[2].getText().equals(p) && b[6].getText().equals(p) && b[4].getText().equals(""))
            setButton(b[4], botSymbol);
        else if (b[4].getText().equals(p) && b[6].getText().equals(p) && b[2].getText().equals(""))
            setButton(b[2], botSymbol);

        else if (b[0].getText().equals(p) && b[3].getText().equals(p) && b[6].getText().equals(""))
            setButton(b[6], botSymbol);
        else if (b[0].getText().equals(p) && b[6].getText().equals(p) && b[3].getText().equals(""))
            setButton(b[3], botSymbol);
        else if (b[3].getText().equals(p) && b[6].getText().equals(p) && b[0].getText().equals(""))
            setButton(b[0], botSymbol);

        else if (b[1].getText().equals(p) && b[4].getText().equals(p) && b[7].getText().equals(""))
            setButton(b[7], botSymbol);
        else if (b[1].getText().equals(p) && b[7].getText().equals(p) && b[4].getText().equals(""))
            setButton(b[4], botSymbol);
        else if (b[4].getText().equals(p) && b[7].getText().equals(p) && b[1].getText().equals(""))
            setButton(b[1], botSymbol);

        else if (b[2].getText().equals(p) && b[5].getText().equals(p) && b[8].getText().equals(""))
            setButton(b[8], botSymbol);
        else if (b[2].getText().equals(p) && b[8].getText().equals(p) && b[5].getText().equals(""))
            setButton(b[5], botSymbol);
        else if (b[5].getText().equals(p) && b[8].getText().equals(p) && b[2].getText().equals(""))
            setButton(b[2], botSymbol);

        else {
            //если бот не проигрывает, то приоритет занять углы или центр
            int[] corners = {0, 2, 4, 6, 8}; //массив углов и центра поля

            shuffleArray(corners); //перемешали массив

            //проверка ли углы заняты заняты
            boolean moved = false;
            for (int corner : corners) {
                if (b[corner].getText().equals("")) {
                    setButton(b[corner], botSymbol);
                    moved = true;
                    break;
                }
            }
            //если углы заняты, то занимает рандомную свободную клетку
            if (!moved) {
                int[] others = {1, 3, 5, 7}; //остальные всякие клеточки, которые остались
                shuffleArray(others);
                for (int cell : others) {
                    if (b[cell].getText().equals("")) {
                        setButton(b[cell], botSymbol);
                        moved = true;
                        break;
                    }
                }
            }
        }
    }

    // "Эксперт"
    public static void makeExpertMove(JButton[] b, String botSymbol) {
        String playerSymbol = botSymbol.equals("x") ? "o" : "x";
        int[][] winLines = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };
        //пытается выиграть самомтоятельно
        for (int[] line : winLines) {
            int emptyIndex = getWinningMove(b, line, botSymbol);
            if (emptyIndex != -1) {
                setButton(b[emptyIndex], botSymbol);
                return;
            }
        }
        //не дает выиграть игроку (хз ли работать будет)
        for (int[] line : winLines) {
            int emptyIndex = getWinningMove(b, line, playerSymbol);
            if (emptyIndex != -1) {
                setButton(b[emptyIndex], botSymbol);
                return;
            }
        }
        makeNormalMove(b, botSymbol); //если никто не выигрывает ход по логике Нормального
    }

    // Вспомогательный метод: ищет, есть ли в линии 2 одинаковых символа и 1 пустая клетка
    private static int getWinningMove(JButton[] b, int[] line, String symbol) {
        int count = 0;
        int emptyIndex = -1;
        for (int i : line) {
            if (b[i].getText().equals(symbol)) count++;
            else if (b[i].getText().equals("")) emptyIndex = i;
        }
        return (count == 2 && emptyIndex != -1) ? emptyIndex : -1;
    }

    public static void setButton(JButton btn, String symbol) {
        btn.setFont(new Font("Arial", Font.BOLD, 100));
        btn.setEnabled(false);
        // Если выбрана тема, устанавливаем картинку, иначе текст. Здесь пока текст для простоты.
        if (PlayerProfile.activeTheme.equals("DEFAULT")) {
            btn.setText(symbol);
        } else {
            String imagePath = "src/theme_" + PlayerProfile.activeTheme + "_" + symbol + ".png";
            btn.setIcon(new ImageIcon(imagePath));
            btn.setText(symbol); // Оставляем текст скрытым для логики проверок победы (важно!)
            btn.setFont(new Font("Arial", Font.PLAIN, 0));
        }
    }
}
