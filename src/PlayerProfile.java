import javax.swing.*;
import java.util.*;


public class PlayerProfile {
    public static int winStreak = 0;
    public static Set<Integer> currentStreakLevels = new HashSet<>();
    public static Set<String> unlockedThemes = new HashSet<>();

    //тема по умолчанию (х/о)
    public static String activeTheme = "DEFAULT";
    public static int secretLevelStage = 0;
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
    public enum SecretStage {
        GENIUS("Гений"),
        INVISIBLE("Невидимка"),
        IMPOSSIBLE("Невозможно");

        private final String title;
        SecretStage(String title) { this.title = title; }
        public String getTitle() { return title; }
    }

    private static void checkUnlocks() {

        System.out.println("Текущий стрик: " + winStreak);
        System.out.println("Уровни, на которых были победы: " + currentStreakLevels);

        if (winStreak >= 5) {
            Set<String> potentialThemes = new HashSet<>();
            if (currentStreakLevels.contains(1)) potentialThemes.add("M");
            if (currentStreakLevels.contains(2)) potentialThemes.add("P");
            if (currentStreakLevels.contains(3)) potentialThemes.add("G");
            System.out.println("Темы, подходящие под пройденные уровни: " + potentialThemes);
            //list of unlock themes
            List<String> availableToUnlock = new ArrayList<>();
            for (String theme : potentialThemes) {
                if (!unlockedThemes.contains(theme)) {
                    availableToUnlock.add(theme);
                }else {
                    System.out.println("Тема " + theme + " пропущена (уже разблокирована ранее).");
                }
            }
            //checking&unlocking
            if (!availableToUnlock.isEmpty()) {
                // Если в списке несколько тем (например, играл на 1 и 2 уровнях и обе закрыты),
                // выберется рандомная из них.
                // Если одна уже была открыта, то в списке останется только одна (100% шанс).
                String themeToUnlock = availableToUnlock.get(new Random().nextInt(availableToUnlock.size()));

                unlockedThemes.add(themeToUnlock);
               // System.out.println("УСПЕХ! Система выбрала и разблокировала тему: " + themeToUnlock);
               // System.out.println("Разблокирована тема: " + themeToUnlock + "!");
               // JOptionPane.showMessageDialog(null, "Разблокирована тема: " + themeToUnlock + "!");
                JOptionPane.showMessageDialog(null, LanguageManager.getText("unlock_msg") + themeToUnlock + "!");
                System.out.println("Данные стрика очищены для нового цикла.");
                winStreak = 0;
                currentStreakLevels.clear();
            } else {
                // Если все темы для пройденных в этом стрике уровней уже открыты
                System.out.println("Новых тем не разблокировано, так как они уже есть в коллекции.");
            }
        }
    }
}
