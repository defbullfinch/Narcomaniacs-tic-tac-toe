import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class GameWindow extends JFrame {
    private JButton[] buttons = new JButton[9];
    private int level;

    //уровень "Для дибилов"
    private String playerSymbol = "x";
    private String botSymbol = "o";

    public GameWindow(String title, int level) {
        this.level = level;
        setTitle(title + LanguageManager.getText("streak") + PlayerProfile.winStreak);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 3));

        //уровни "Нормальный" и "Эксперт"
        if (level == 2 || level == 3) {
            Random random = new Random();
            if (random.nextBoolean()) {
                playerSymbol = "x";
                botSymbol = "o";
            } else {
                playerSymbol = "o";
                botSymbol = "x";
            }
        }
        initializeButtons();
        setVisible(true);

        if (botSymbol.equals("x")) {
            //задержка шоб лучше выглядело (и желательно шоб никто не залетел)
            SwingUtilities.invokeLater(() -> {
                if (level == 2 || level == 3) {
                    BotLogic.makeNormalMove(buttons, botSymbol);
                }
            });
        }
    }

    private void initializeButtons() {
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 100));
            buttons[i].setFocusPainted(false);

            buttons[i].addActionListener(e -> handlePlayerMove((JButton) e.getSource()));
            add(buttons[i]);
        }
    }

    private void handlePlayerMove(JButton btn) {
        BotLogic.setButton(btn, playerSymbol);

        //проверка победы игрока
        if (GameLogic.checkWin(buttons, playerSymbol)) {
            System.out.println("ПОТУЖНА ПЄРЄМОГА!");
            PlayerProfile.addWin(level);
            JOptionPane.showMessageDialog(this, LanguageManager.getText("win_msg"));
            endGame();
            return;
        }

        //проверка ничьи
        if (GameLogic.isDraw(buttons)) {
            JOptionPane.showMessageDialog(this, LanguageManager.getText("draw_msg"));
            endGame();
            return; // Стрик НЕ сбрасываем
        }

        //
        if (level == 1) BotLogic.makeDibilMove(buttons, botSymbol);
        else if (level == 2) BotLogic.makeNormalMove(buttons, botSymbol);
        else if (level == 3) BotLogic.makeExpertMove(buttons, botSymbol);

        //проверка победы бота
        if (GameLogic.checkWin(buttons, botSymbol)) {
            System.out.println("лошара:) 🫵");
            PlayerProfile.resetStreak();
            JOptionPane.showMessageDialog(this,LanguageManager.getText("loose_msg") );
            endGame();
            return;
        }

        //проверка ничьи х2
        if (GameLogic.isDraw(buttons)) {
            JOptionPane.showMessageDialog(this, LanguageManager.getText("draw_msg"));
            endGame();
        }
    }

    private void endGame() {
        for (JButton b : buttons) b.setEnabled(false);
        // Возвращаемся в меню через 1.5 секунды
        Timer timer = new Timer(1500, (ActionEvent e) -> {
            this.dispose();
            StartWindow.createStartWindow();
        });
        timer.setRepeats(false);
        timer.start();
    }
}