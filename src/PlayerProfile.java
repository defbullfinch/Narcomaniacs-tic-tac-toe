import javax.swing.ImageIcon;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;



public class PlayerProfile {
    public static int winStreak = 0;
    public static Set<Integer> currentStreakLevels = new HashSet<>();
    public static Set<String> unlockedThemes = new HashSet<>();

    //тема по умолчанию (х/о)
    public static String activeTheme = "DEFAULT";

    public static void addWin(int level) {
        winStreak++;
        currentStreakLevels.add(level);
        checkUnlocks();
    }

    public static void resetStreak() {
        winStreak = 0;
        currentStreakLevels.clear();
        System.out.println("Стрик сброшен до 0.");
    }

    private static void checkUnlocks() {
        if (winStreak == 5) {
            String themeToUnlock;
            if (currentStreakLevels.size() > 1) {
                // Если побеждал на разных уровнях — даем рандомную
                String[] themes = {"М", "П", "Г"};
                themeToUnlock = themes[new Random().nextInt(themes.length)];
            } else if (currentStreakLevels.contains(1)) {
                themeToUnlock = "М"; // Для первого уровня
            } else if (currentStreakLevels.contains(2)) {
                themeToUnlock = "П"; // Для второго уровня
            } else {
                themeToUnlock = "Г"; // Для мастера
            }

            unlockedThemes.add(themeToUnlock);
            System.out.println("Разблокирована тема: " + themeToUnlock + "!");

            // После разблокировки можно сбросить стрик или оставить, чтобы открывать дальше.
            // Оставим сброс для нового челленджа.
            winStreak = 0;
            currentStreakLevels.clear();
        }
    }
}
