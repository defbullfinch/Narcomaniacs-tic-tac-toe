import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartWindow {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartWindow::createStartWindow);
    }

        public static void createStartWindow() {
            JFrame startFrame = new JFrame("Меню | Побед подряд: " + PlayerProfile.winStreak);
            startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            startFrame.setSize(600, 450);
            startFrame.setLocationRelativeTo(null);
            startFrame.setLayout(new BorderLayout());

            JLabel label = new JLabel("Выберите уровень", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 20));
            startFrame.add(label, BorderLayout.NORTH);

            //панель с тремя уровнями
            JPanel levelsPanel = new JPanel(new GridLayout(1, 3, 10, 0)); //(1 строка, 3 колонки, отступ 10, отступ 0)
            levelsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Отступы от краев окна

            //блоки: кнопка + картинка
            levelsPanel.add(createLevelBlock("Для дибилов", "src/images/level/dibil_static.jpg", "src/images/level/dibil.gif", () -> {
                startFrame.dispose();
                new GameWindow("Уровень для дибилов", 1);
            }));

            levelsPanel.add(createLevelBlock("Нормальный", "src/images/level/normal_static.jpg", "src/images/level/normal.gif", () -> {
                startFrame.dispose();
                new GameWindow("Нормальный уровень", 2);
            }));

            levelsPanel.add(createLevelBlock("Эксперт", "src/images/level/sigma_static.jpg", "src/images/level/sigma.gif", () -> {
                startFrame.dispose();
                new GameWindow("Уровень Эксперт", 3);
            }));

            startFrame.add(levelsPanel, BorderLayout.CENTER);

            //НИЗ: Панель тем
            JPanel themePanel = new JPanel();
            themePanel.setBorder(BorderFactory.createTitledBorder("Темы"));

            if (PlayerProfile.unlockedThemes.isEmpty()) {
                JLabel lockLabel = new JLabel("\uD83D\uDD12 Будет доступно после 5 побед подряд");
                themePanel.add(lockLabel);
            } else {
                for (String theme : PlayerProfile.unlockedThemes) {
                    JButton themeBtn = new JButton("Тема " + theme);
                    themeBtn.addActionListener(e -> {
                        PlayerProfile.activeTheme = theme;
                        JOptionPane.showMessageDialog(startFrame, "Выбрана тема: " + theme);
                    });
                    themePanel.add(themeBtn);
                }
                JButton defaultBtn = new JButton("Обычная");
                defaultBtn.addActionListener(e -> PlayerProfile.activeTheme = "DEFAULT");
                themePanel.add(defaultBtn);
            }// Допустим, всего тем 3 (М, П, Г).
            if (PlayerProfile.unlockedThemes.size() >= 3) {
                JButton secretBtn = new JButton("n#!А1р*к0Кт");
                secretBtn.setBackground(Color.BLACK);
                secretBtn.setForeground(Color.RED);
                secretBtn.addActionListener(e -> {
                    startFrame.dispose();
                    new SecretGameWindow();
                });
                levelsPanel.add(secretBtn); // Добавляем на панель к уровням
            }
            startFrame.add(themePanel, BorderLayout.SOUTH);

            startFrame.setVisible(true);
        }

        //создание блока "Кнопка + Картинка"
        private static JPanel createLevelBlock(String btnText, String staticPicPath, String gifPicPath, Runnable action) {
            JPanel panel = new JPanel(new BorderLayout());

            JButton btn = new JButton(btnText);
            //отображается картинка
            JLabel imgLabel = new JLabel(new ImageIcon(staticPicPath), SwingConstants.CENTER);

            //при наведении на кнопку или на картинку срабатывает гифффка
            MouseAdapter hoverAdapter = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    imgLabel.setIcon(new ImageIcon(gifPicPath)); // Включаем гифку
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    imgLabel.setIcon(new ImageIcon(staticPicPath)); // Возвращаем статику
                }
            };

            btn.addMouseListener(hoverAdapter);
            imgLabel.addMouseListener(hoverAdapter);
            btn.addActionListener(e -> action.run());

            panel.add(btn, BorderLayout.NORTH);
            panel.add(imgLabel, BorderLayout.CENTER);

            return panel;
        }
    }
//
//        // --- НИЗ: Темы ---
//        JPanel themePanel = new JPanel();
//        themePanel.setBorder(BorderFactory.createTitledBorder("Темы"));
//
//        if (PlayerProfile.unlockedThemes.isEmpty()) {
//            JLabel lockLabel = new JLabel("Замочек: Будет доступно после 10 побед подряд");
//            // lockLabel.setIcon(new ImageIcon("src/lock.png")); // Добавь иконку замочка
//            themePanel.add(lockLabel);
//        } else {
//            // Если темы открыты, показываем их
//            for (String theme : PlayerProfile.unlockedThemes) {
//                JButton themeBtn = new JButton("Тема " + theme);
//                themeBtn.addActionListener(e -> {
//                    PlayerProfile.activeTheme = theme;
//                    JOptionPane.showMessageDialog(startFrame, "Выбрана тема: " + theme);
//                });
//                themePanel.add(themeBtn);
//            }
//            JButton defaultBtn = new JButton("Обычная");
//            defaultBtn.addActionListener(e -> PlayerProfile.activeTheme = "DEFAULT");
//            themePanel.add(defaultBtn);
//        }
//        startFrame.add(themePanel, BorderLayout.SOUTH);
//
//        // Обработчики запуска игры
//        easyBtn.addActionListener(e -> { startFrame.dispose(); new GameWindow("Уровень для дибилов", 1); });
//        normalBtn.addActionListener(e -> { startFrame.dispose(); new GameWindow("Нормальный уровень", 2); });
//        expertBtn.addActionListener(e -> { startFrame.dispose(); new GameWindow("Уровень Мастер", 3); });
//
//        startFrame.setVisible(true);
//    }
