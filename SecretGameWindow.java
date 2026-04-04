import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecretGameWindow extends JFrame {
    private JButton[] buttons = new JButton[25]; // Сетка 5x5
    private String playerSymbol = "x";

    // Индексы центральных кнопок (3х3), где можно собрать линию
    private int[] coreIndices = {6, 7, 8, 11, 12, 13, 16, 17, 18};

    public SecretGameWindow() {
        setTitle("n#!А1р*к0Кт");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Во весь экран
        setLayout(new BorderLayout());

        // Фон картинкой (замени путь на свой страшный фон)
        JLabel background = new JLabel(new ImageIcon("src/creepy_bg.jpg"));
        background.setLayout(new GridLayout(5, 5));

        setupGrid(background);

        add(background);
        setVisible(true);
    }

    /* эээээ, крч так примерно:
-+++-
+***+
+***+
+***+
-+++-
*/

    private void setupGrid(JLabel bg) {
        int[] plusIndices = {1, 2, 3, 5, 9, 10, 14, 15, 19, 21, 22, 23};
        List<Integer> activePluses = new ArrayList<>();
        for (int i : plusIndices) activePluses.add(i);
        Collections.shuffle(activePluses);

        // Оставляем только 4 рабочих
        List<Integer> finalActivePluses = activePluses.subList(0, 4);

        for (int i = 0; i < 25; i++) {
            if (i == 0 || i == 4 || i == 20 || i == 24) {
                // Углы (-) — просто пустота
                bg.add(new JLabel(""));
            } else {
                buttons[i] = new JButton("");
                buttons[i].setFont(new Font("Arial", Font.PLAIN, 0));

                // Проверяем, это обычная звезда (*) или плюсик (+)
                boolean isCore = false;
                for (int core : coreIndices) if (core == i) isCore = true;

                if (isCore) {
                    // Обычная центральная кнопка
                    buttons[i].addActionListener(e -> secretMove((JButton) e.getSource()));
                } else {
                    // Плюсики (дополнительные кнопки)
                    if (finalActivePluses.contains(i)) {
                        // Рабочая, но НЕВИДИМАЯ кнопка
                        makeInvisible(buttons[i]);
                        buttons[i].addActionListener(e -> secretMove((JButton) e.getSource()));
                    } else {
                        // Обманка (не нажимается вообще)
                        makeInvisible(buttons[i]);
                        buttons[i].setEnabled(false);
                    }
                }
                bg.add(buttons[i]);
            }
        }
    }

    private void makeInvisible(JButton btn) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
    }
    int[][] secretWinLines = {
            {6, 7, 8}, {11, 12, 13}, {16, 17, 18}, // Горизонтали
            {6, 11, 16}, {7, 12, 17}, {8, 13, 18}, // Вертикали
            {6, 12, 18}, {8, 12, 16}               // Диагонали
    };

    private void secretMove(JButton btn) {
        // Как только нажали на невидимую кнопку, она становится видимой
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);

        BotLogic.setButton(btn, playerSymbol);

        // Тут нужно будет дописать проверку победы по массиву coreIndices
        // и рандомный ход бота по оставшимся доступным кнопкам.
    }
}