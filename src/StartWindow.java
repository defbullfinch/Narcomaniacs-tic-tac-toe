import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartWindow {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartWindow::showStartWindow);
    }

    public static void showStartWindow() {
        JFrame startFrame = new JFrame( PlayerProfile.winStreak+ " \uD83D\uDC40");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(600, 450);
        startFrame.setLocationRelativeTo(null);

        createStartWindow(startFrame);

        startFrame.setVisible(true);
    }

        public static void createStartWindow(JFrame startFrame) {
            startFrame.getContentPane().removeAll();
            startFrame.setLayout(new BorderLayout());
            startFrame.setLayout(new BorderLayout());

//            JLabel label = new JLabel(LanguageManager.getText("label_level_choice"), SwingConstants.CENTER);
//            label.setFont(new Font("Arial", Font.BOLD, 20));
//            startFrame.add(label, BorderLayout.NORTH);

            JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JRadioButton uaBtn = new JRadioButton("UA", LanguageManager.currentLang.equals("UA"));
            JRadioButton engBtn = new JRadioButton("ENG", LanguageManager.currentLang.equals("ENG"));
            JRadioButton ruBtn = new JRadioButton("RU", LanguageManager.currentLang.equals("RU"));

            ButtonGroup langGroup = new ButtonGroup();
            langGroup.add(ruBtn);
            langGroup.add(uaBtn);
            langGroup.add(engBtn);

            langPanel.add(ruBtn);
            langPanel.add(uaBtn);
            langPanel.add(engBtn);

            java.awt.event.ActionListener langListener = e -> {
                String selected = e.getActionCommand();
                if (selected.equals("RU")) LanguageManager.currentLang = "RU";
                if (selected.equals("UA")) LanguageManager.currentLang = "UA";
                if (selected.equals("ENG")) LanguageManager.currentLang = "ENG";
                createStartWindow(startFrame);
            };

            ruBtn.addActionListener(langListener);
            uaBtn.addActionListener(langListener);
            engBtn.addActionListener(langListener);

            JLabel label = new JLabel(LanguageManager.getText("label_level_choice"), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 20));

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(langPanel, BorderLayout.NORTH); // Языки в самом верху
            topPanel.add(label, BorderLayout.CENTER);    // Заголовок под ними
            startFrame.add(topPanel, BorderLayout.NORTH);

            //панель с тремя уровнями
            JPanel levelsPanel = new JPanel(new GridLayout(1, 3, 10, 0)); //(1 строка, 3 колонки, отступ 10, отступ 0)
            levelsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Отступы от краев окна

            //блоки: кнопка + картинка
            levelsPanel.add(createLevelBlock(LanguageManager.getText("level_1"), "src/images/level/dibil_static.jpg", "src/images/level/dibil.gif", () -> {
                startFrame.dispose();
                new GameWindow(LanguageManager.getText("level_1_gameWindow"), 1);
            }));

            levelsPanel.add(createLevelBlock(LanguageManager.getText("level_2"), "src/images/level/normal_static.png", "src/images/level/normal.gif", () -> {
                startFrame.dispose();
                new GameWindow(LanguageManager.getText("level_2_gameWindow"), 2);
            }));

            levelsPanel.add(createLevelBlock(LanguageManager.getText("level_3"), "src/images/level/sigma_static.jpg", "src/images/level/sigma.gif", () -> {
                startFrame.dispose();
                new GameWindow(LanguageManager.getText("level_3_gameWindow"), 3);
            }));


            startFrame.add(levelsPanel, BorderLayout.CENTER);

            //НИЗ: Панель тем
            JPanel themePanel = new JPanel();
            themePanel.setBorder(BorderFactory.createTitledBorder(LanguageManager.getText("themes")));

            if (PlayerProfile.unlockedThemes.isEmpty()) {
                JLabel lockLabel = new JLabel(LanguageManager.getText("lock_hint"));
                themePanel.add(lockLabel);
            } else {
                for (String theme : PlayerProfile.unlockedThemes) {
                    JButton themeBtn = new JButton(LanguageManager.getText("theme") + theme);
                    themeBtn.addActionListener(e -> {
                        PlayerProfile.activeTheme = theme;
                        JOptionPane.showMessageDialog(startFrame, (LanguageManager.getText("chosen_theme")) + theme);
                    });
                    themePanel.add(themeBtn);
                }
                JButton defaultBtn = new JButton(LanguageManager.getText("default_"));
                defaultBtn.addActionListener(e -> PlayerProfile.activeTheme = "DEFAULT");
                themePanel.add(defaultBtn);
            }
            if (PlayerProfile.unlockedThemes.size() >= 3) {
                JButton secretBtn = new JButton("n#!А1р*к0Кт");
                secretBtn.setBackground(Color.BLACK);
                secretBtn.setForeground(Color.RED);
                secretBtn.addActionListener(e -> {
                    startFrame.dispose();
                    new SecretGameWindow();
                });
                levelsPanel.add(secretBtn);
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