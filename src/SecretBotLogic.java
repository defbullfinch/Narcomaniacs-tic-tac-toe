import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecretBotLogic {

    public static int[] makeMove(JButton[][] board, String bot, String player, PlayerProfile.SecretStage stage) {
        if (stage == PlayerProfile.SecretStage.GENIUS) {
            return makeGeniusMove(board, bot, player);
        } else if (stage == PlayerProfile.SecretStage.INVISIBLE) {
            return makeInvisibleMove(board, bot, player);
        } else if (stage == PlayerProfile.SecretStage.IMPOSSIBLE) {
            return makeImpossibleMove(board, bot, player);
        }
        return null;
    }

    // --- УРОВЕНЬ 1: ГЕНИЙ ---
    private static int[] makeGeniusMove(JButton[][] board, String bot, String player) {
        int[] attackMove = findWinningMove(board, bot, false);
        if (attackMove != null) { BotLogic.setButton(board[attackMove[0]][attackMove[1]], bot); return attackMove; }

        int[] defenseMove = findWinningMove(board, player, true);
        if (defenseMove != null) { BotLogic.setButton(board[defenseMove[0]][defenseMove[1]], bot); return defenseMove; }

        if (board[2][2].isEnabled() && board[2][2].getText().isEmpty()) {
            BotLogic.setButton(board[2][2], bot); return new int[]{2, 2};
        }

        List<int[]> secretMoves = new ArrayList<>();
        List<int[]> normalMoves = new ArrayList<>();
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                if (board[r][c].isEnabled() && board[r][c].getText().isEmpty()) {
                    if (r == 0 || r == 4 || c == 0 || c == 4) secretMoves.add(new int[]{r, c});
                    else normalMoves.add(new int[]{r, c});
                }
            }
        }

        if (!secretMoves.isEmpty()) {
            Collections.shuffle(secretMoves);
            BotLogic.setButton(board[secretMoves.get(0)[0]][secretMoves.get(0)[1]], bot);
            return secretMoves.get(0);
        }
        if (!normalMoves.isEmpty()) {
            Collections.shuffle(normalMoves);
            BotLogic.setButton(board[normalMoves.get(0)[0]][normalMoves.get(0)[1]], bot);
            return normalMoves.get(0);
        }
        return null;
    }

    // --- УРОВЕНЬ 2: НЕВИДИМКА (Снимок памяти) ---
    private static int[] makeInvisibleMove(JButton[][] board, String bot, String player) {
        JButton[] innerBoard = new JButton[9];
        int index = 0;

        // 1. Собираем центральные 9 кнопок
        for (int r = 1; r <= 3; r++) {
            for (int c = 1; c <= 3; c++) {
                innerBoard[index++] = board[r][c];
            }
        }

        // 2. ДЕЛАЕМ СНИМОК: Запоминаем, где было пусто до хода бота
        String[] stateBefore = new String[9];
        for (int i = 0; i < 9; i++) {
            stateBefore[i] = innerBoard[i].getText();
        }

        // 3. Отдаем массив твоему старому эксперту (он втихую сделает ход)
        BotLogic.makeExpertMove(innerBoard, bot);

        // 4. ИЩЕМ ИЗМЕНЕНИЯ: Сравниваем снимок с текущим полем
        for (int i = 0; i < 9; i++) {
            if (!stateBefore[i].equals(innerBoard[i].getText())) {
                // Нашли, куда он сходил! Конвертируем индекс (0-8) обратно в [r][c] (1-3)
                int r = (i / 3) + 1;
                int c = (i % 3) + 1;
                return new int[]{r, c}; // Возвращаем координаты окну!
            }
        }

        return null;
    }

    // --- УРОВЕНЬ 3: НЕВОЗМОЖНО ---
    private static int[] makeImpossibleMove(JButton[][] board, String bot, String player) {
        // 1. Атака (сразу выигрываем, если можем)
        int[] attack = findWinningMove(board, bot, true);
        if (attack != null) { BotLogic.setButton(board[attack[0]][attack[1]], bot); return attack; }

        // 2. ЛАЗЕЙКА "Троянский конь" (пасхалка для победы)
        boolean loopholeActive = board[2][1].getText().equals(player) &&
                board[3][2].getText().equals(player) &&
                board[3][1].getText().equals(player);

        if (loopholeActive) {
            // Бот "глючит" и делает глупый ход вместо защиты
            if (board[1][3].getText().isEmpty()) { BotLogic.setButton(board[1][3], bot); return new int[]{1,3}; }
        }
        int playerThreats = countThreats(board, player);

        if (playerThreats > 1) {
            if (loopholeActive) {
                // ЛАЗЕЙКА РАБОТАЕТ: бот игнорирует угрозы и делает глупый ход
                return new int[]{1, 3};
            } else {
                // ЛАЗЕЙКА ЗАКРЫТА: бот применяет ЧИТ-КОД
                // Тут твой код с заполнением верхнего ряда
                return new int[]{-1, -1};
            }
        }
        // 3. Защита (блокируем победу игрока)
        int[] defense = findWinningMove(board, player, true);
        if (defense != null) { BotLogic.setButton(board[defense[0]][defense[1]], bot); return defense; }
        System.out.println("Threats: " + playerThreats + ", Loophole: " + loopholeActive);
        // 4. ЧИТ-КОД (Если игрок как-то умудрился сделать вилку - бот "переворачивает стол")
//        int playerThreats = countThreats(board, player);
//        if (playerThreats > 1) {
//            // Больше никаких трех кнопок! Просто возвращаем код бага,
//            // а окно SecretGameWindow само покажет сообщение "Я МЕНЯЮ ПРАВИЛА"
//            return new int[]{-1, -1};
//        }

        // 5. УМНАЯ ИГРА ГРОССМЕЙСТЕРА
        // 5.1 Если центр свободен - забираем
        if (board[2][2].getText().isEmpty()) { BotLogic.setButton(board[2][2], bot); return new int[]{2,2}; }

        // 5.2 Продвинутая защита: Если игрок занял углы по диагонали (пытается сделать вилку)
        boolean diag1 = board[1][1].getText().equals(player) && board[3][3].getText().equals(player);
        boolean diag2 = board[1][3].getText().equals(player) && board[3][1].getText().equals(player);
        if (diag1 || diag2) {
            int[][] edges = {{1,2}, {2,1}, {2,3}, {3,2}};
            for (int[] e : edges) {
                if (board[e[0]][e[1]].getText().isEmpty()) { BotLogic.setButton(board[e[0]][e[1]], bot); return e; }
            }
        }

        // 5.3 Иначе занимаем любой свободный угол
        int[][] corners = {{1,1}, {1,3}, {3,1}, {3,3}};
        for (int[] c : corners) {
            if (board[c[0]][c[1]].getText().isEmpty()) { BotLogic.setButton(board[c[0]][c[1]], bot); return c; }
        }

        // 5.4 Если углов нет - занимаем ребро
        int[][] edges = {{1,2}, {2,1}, {2,3}, {3,2}};
        for (int[] e : edges) {
            if (board[e[0]][e[1]].getText().isEmpty()) { BotLogic.setButton(board[e[0]][e[1]], bot); return e; }
        }

        return null;
    }

    // --- Вспомогательные методы ---
    private static int[] findWinningMove(JButton[][] board, String symbol, boolean only3x3) {
        int min = only3x3 ? 1 : 0;
        int max = only3x3 ? 3 : 4;
        for (int r = min; r <= max; r++) {
            for (int c = min; c <= max; c++) {
                if (board[r][c].isEnabled() && board[r][c].getText().isEmpty()) {
                    board[r][c].setText(symbol);
                    boolean isWin = checkWin5x5(board, symbol, min, max);
                    board[r][c].setText("");
                    if (isWin) return new int[]{r, c};
                }
            }
        }
        return null;
    }

    private static int countThreats(JButton[][] board, String symbol) {
        int threats = 0;
        for (int r = 1; r <= 3; r++) {
            for (int c = 1; c <= 3; c++) {
                if (board[r][c].isEnabled() && board[r][c].getText().isEmpty()) {
                    board[r][c].setText(symbol);
                    if (checkWin5x5(board, symbol, 1, 3)) threats++;
                    board[r][c].setText("");
                }
            }
        }
        return threats;
    }

    public static boolean checkWin5x5(JButton[][] board, String symbol, int min, int max) {
        for (int r = min; r <= max; r++) {
            for (int c = min; c <= max; c++) {
                if (c + 2 <= max && board[r][c].getText().equals(symbol) && board[r][c+1].getText().equals(symbol) && board[r][c+2].getText().equals(symbol)) return true;
                if (r + 2 <= max && board[r][c].getText().equals(symbol) && board[r+1][c].getText().equals(symbol) && board[r+2][c].getText().equals(symbol)) return true;
                if (r + 2 <= max && c + 2 <= max && board[r][c].getText().equals(symbol) && board[r+1][c+1].getText().equals(symbol) && board[r+2][c+2].getText().equals(symbol)) return true;
                if (r + 2 <= max && c - 2 >= min && board[r][c].getText().equals(symbol) && board[r+1][c-1].getText().equals(symbol) && board[r+2][c-2].getText().equals(symbol)) return true;
            }
        }
        return false;
    }

    public static boolean isSecretDraw(JButton[][] board) {
        for (int r = 1; r <= 3; r++) {
            for (int c = 1; c <= 3; c++) {
                if (board[r][c].getText().isEmpty()) return false;
            }
        }
        return true;
    }
}