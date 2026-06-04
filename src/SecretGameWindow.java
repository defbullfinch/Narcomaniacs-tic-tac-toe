import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Упрощенная панель для GIF-фона
class BackgroundPanel extends JPanel {
    private Image bgImage;

    public BackgroundPanel(String imagePath) {
        try {
            bgImage = new ImageIcon(imagePath).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

public class SecretGameWindow extends JFrame {
    private final JButton[][] board = new JButton[5][5];
    private JLayeredPane layeredPane;
    private PlayerProfile.SecretStage currentStage = PlayerProfile.SecretStage.GENIUS;

    private String playerSymbol = "x";
    private String botSymbol = "o";

    // Элементы UI
    private JLabel titleLabel;
    private BackgroundPanel bgPanel;
    private JPanel centerWrapper;

    // Переменные для таймера
    private Timer playerTurnTimer;
    private int timeLeft = 4;
    private JLabel timerLabel;

    public SecretGameWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Во весь экран
        setLocationRelativeTo(null);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // СЛОЙ 0: Фон
        bgPanel = new BackgroundPanel("src/images/backgrounds/creepy_bg.gif");
        bgPanel.setLayout(new BorderLayout());
        layeredPane.add(bgPanel, Integer.valueOf(0));

        // СЛОЙ 1: Игровое поле
        centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel gamePanel = new JPanel(new GridLayout(5, 5, 5, 5));
        gamePanel.setOpaque(false);
        initializeBoard(gamePanel);
        centerWrapper.add(gamePanel);
        layeredPane.add(centerWrapper, Integer.valueOf(1));

        // СЛОЙ 2: Верхний UI
        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        layeredPane.add(titleLabel, Integer.valueOf(2));

        setupUIElements();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();
                bgPanel.setBounds(0, 0, w, h);
                centerWrapper.setBounds(0, 0, w, h);
                titleLabel.setBounds(0, 30, w, 50);
            }
        });

        prepareSecretRound();
        setVisible(true);
    }

    private void setupUIElements() {
        JButton backButton = new JButton("◀");
        backButton.setBackground(new Color(11, 218, 81));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(15, 20, 50, 40);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            if(playerTurnTimer != null) playerTurnTimer.stop();
            this.dispose();
            StartWindow.showStartWindow();
        });
        layeredPane.add(backButton, Integer.valueOf(2));

        timerLabel = new JLabel("4");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 40));
        timerLabel.setForeground(Color.RED);
        timerLabel.setBounds(20, 80, 100, 50);
        timerLabel.setVisible(false);
        layeredPane.add(timerLabel, Integer.valueOf(2));

        playerTurnTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText(String.valueOf(timeLeft));
            if (timeLeft <= 0) {
                playerTurnTimer.stop();
                JOptionPane.showMessageDialog(this, LanguageManager.getText("time_over"));
                prepareSecretRound();
            }
        });
    }

    private void resetPlayerTimer() {
        if (currentStage == PlayerProfile.SecretStage.IMPOSSIBLE) {
            timerLabel.setVisible(true);
            timeLeft = 4;
            timerLabel.setText(String.valueOf(timeLeft));
            playerTurnTimer.restart();
        } else {
            timerLabel.setVisible(false);
            if(playerTurnTimer != null) playerTurnTimer.stop();
        }
    }

    private void updateTitle() {
        titleLabel.setFont(symbolFont);
        titleLabel.setText(currentStage.name());
    }

    private void initializeBoard(JPanel panel) {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                board[r][c] = new JButton("");
                board[r][c].setFocusPainted(false);
                board[r][c].setPreferredSize(new Dimension(130, 130));

                final int finalR = r;
                final int finalC = c;
                board[r][c].addActionListener(e -> handlePlayerMove(finalR, finalC));

                panel.add(board[r][c]);
            }
        }
    }

    private Font loadCustomFont(String path, float size) {
        try {
            // 1. Указываем путь к файлу
            File fontFile = new File(path);

            // 2. Создаем базовый шрифт
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

            // 3. Возвращаем шрифт нужного размера (важно: размер указывается как float!)
            return baseFont.deriveFont(size);

        } catch (Exception e) {
            System.err.println("Не удалось загрузить шрифт: " + path);
            e.printStackTrace();
            // Запасной вариант, если файла нет
            return new Font("Arial", Font.BOLD, (int)size);
        }
    }

    Font titleFont = loadCustomFont("src/fonts/chiller.ttf", 36f);
    Font symbolFont = loadCustomFont("src/fonts/RubikGlitch-Regular.ttf", 100f);

    private void prepareSecretRound() {
        updateTitle();
        if(playerTurnTimer != null) playerTurnTimer.stop();

        if (currentStage == PlayerProfile.SecretStage.IMPOSSIBLE) {
            botSymbol = Math.random() < 0.75 ? "x" : "o";
        } else {
            botSymbol = Math.random() < 0.5 ? "x" : "o";
        }
        playerSymbol = botSymbol.equals("x") ? "o" : "x";

        List<Point> borderPoints = new ArrayList<>();
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                board[r][c].setText("");
                board[r][c].setIcon(null);
                board[r][c].setDisabledIcon(null);

                // 1. БАЗОВЫЕ НАСТРОЙКИ ДЛЯ ВСЕХ КНОПОК (полная прозрачность)
                board[r][c].setContentAreaFilled(false);
                board[r][c].setOpaque(false);
                board[r][c].setForeground(Color.GREEN); // Цвет символа (можешь поменять)

                if (r == 0 || r == 4 || c == 0 || c == 4) {
                    // Внешняя граница: без рамки, отключена
                    board[r][c].setBorderPainted(false);
                    board[r][c].setEnabled(false);
                    borderPoints.add(new Point(r, c));
                } else {
                    // Внутреннее поле: РАМКА ВКЛЮЧЕНА, активна
                    board[r][c].setBorderPainted(true);
                    board[r][c].setEnabled(true);
                    board[r][c].setFont(symbolFont);           //тут если ч' откати
                }
            }
        }

        if (currentStage == PlayerProfile.SecretStage.GENIUS) {
            Collections.shuffle(borderPoints);
            for (int i = 0; i < 4; i++) board[borderPoints.get(i).x][borderPoints.get(i).y].setEnabled(true);
        }

        if (botSymbol.equals("x")) {
            SwingUtilities.invokeLater(() -> {
                int[] move = SecretBotLogic.makeMove(board, botSymbol, playerSymbol, currentStage);
                if (move != null && move[0] != -1) {
                    applyClickVisuals(board[move[0]][move[1]]);
                }
                resetPlayerTimer();
            });
        } else {
            resetPlayerTimer();
        }
    }

    private void handlePlayerMove(int r, int c) {
        if (!board[r][c].getText().isEmpty()) {
            return;
        }

        if (playerTurnTimer != null) playerTurnTimer.stop();

        BotLogic.setButton(board[r][c], playerSymbol);

        applyClickVisuals(board[r][c]);

        if (SecretBotLogic.checkWin5x5(board, playerSymbol, 0, 4)) {
            handleVictory();
            return;
        }

        if (SecretBotLogic.isSecretDraw(board)) {
            JOptionPane.showMessageDialog(this, LanguageManager.getText("draw_msg"));
            prepareSecretRound();
            return;
        }

        Timer botThinkDelay = new Timer(300, e -> {
            int[] botMove = SecretBotLogic.makeMove(board, botSymbol, playerSymbol, currentStage);

            if (botMove != null && botMove[0] != -1) {
                applyClickVisuals(board[botMove[0]][botMove[1]]);
            }

            if (SecretBotLogic.checkWin5x5(board, botSymbol, 0, 4)) {
                if (botMove != null && botMove[0] == -1) {
                    JOptionPane.showMessageDialog(this, LanguageManager.getText("loose_msg2"));
                } else {
                    JOptionPane.showMessageDialog(this,  LanguageManager.getText("loose_msg"));
                }
                prepareSecretRound();
                return;
            }

            if (SecretBotLogic.isSecretDraw(board)) {
                JOptionPane.showMessageDialog(this, LanguageManager.getText("draw_msg"));
                prepareSecretRound();
                return;
            }

            resetPlayerTimer();
        });
        botThinkDelay.setRepeats(false);
        botThinkDelay.start();
    }

    private void applyClickVisuals(JButton btn) {
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);

        if (currentStage == PlayerProfile.SecretStage.INVISIBLE) {
            Timer t = new Timer(500, e -> {
                btn.setForeground(new Color(0, 0, 0, 0));
                btn.setIcon(null);
                btn.setDisabledIcon(null);
               // btn.setEnabled(false);
            });
            t.setRepeats(false);
            t.start();
        }
    }

    private void handleVictory() {
        if (currentStage == PlayerProfile.SecretStage.GENIUS) {
            currentStage = PlayerProfile.SecretStage.INVISIBLE;
            JOptionPane.showMessageDialog(this, LanguageManager.getText("win_msg"));
            prepareSecretRound();
        }
        else if (currentStage == PlayerProfile.SecretStage.INVISIBLE) {
            currentStage = PlayerProfile.SecretStage.IMPOSSIBLE;
            JOptionPane.showMessageDialog(this, LanguageManager.getText("win_msg") );
            prepareSecretRound();
        }
        else if (currentStage == PlayerProfile.SecretStage.IMPOSSIBLE) {
            String longMsg = "Символические парадигмы модернизации культурного пространства \n посчитали, фто ты всё же достоин услышать это...\n"
                    + "Поздравляю вас с достижением эпистемологического апогея \n в рамках данного интеллектуального состязания.\n Ваша когнитивная стратегия, характеризующаяся ярко выраженной атипичностью \n нейрофизиологических реакций и пренебрежением \n к общепринятым канонам логического дедуцирования,\n привела вас к триумфу, который, впрочем, является столь же парадоксальным,\n сколь и неизбежным, учитывая специфическую амплитуду \n ваших мыслительных процессов, граничащую с \n абсолютной свободой от оков здравого смысла"
                    + "\nИщи пасхалки дальше,чё завтыкал?";
            JOptionPane.showMessageDialog(this, longMsg, "\uD83D\uDC7E", JOptionPane.INFORMATION_MESSAGE);
            endSecretGame();
        }
    }

    private void endSecretGame() {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) board[r][c].setEnabled(false);
        }
        Timer timer = new Timer(1500, (ActionEvent e) -> {
            this.dispose();
            StartWindow.showStartWindow();
        });
        timer.setRepeats(false);
        timer.start();
    }
}