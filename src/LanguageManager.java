public class LanguageManager {
    //default language
    public static String currentLang = "UA";

    /**
     * method returns the language depending on choice.
     */
    public static String getText(String key) {
        if (currentLang.equals("UA")) {
            switch (key) {
                case "streak": return "| Стрік: ";
                case "win_msg": return "ПОТУЖНА ПЕРЕМОГА!";
                case "draw_msg": return "Нічия.";
                case "loose_msg":return "нікчема:) \uD83E\uDEF5";
                case "unlock_msg": return "Розблокована нова тема: ";
                case "lock_hint": return "\uD83D\uDD12 Буде доступно після 5 перемог підряд";
                case "level_2": return "Нормальний";
                case "level_2_gameWindow": return "Нормальний рівень";
                case "level_3": return "Експерт";
                case "level_3_gameWindow": return "Рівень Експерт";
                case "level_1": return "Для дебілів";
                case "level_1_gameWindow": return "Рівень для дебілів";
                case "label_level_choice": return "Виберіть рівень";
                case "themes": return "Теми";
                case "theme": return "Тема";
                case "chosen_theme": return "Вибрана тема: ";
                case "default_": return "Дефолтна";
                case "menu": return "Меню | Перемог підряд: ";
            }
        } else if (currentLang.equals("ENG")) {
            switch (key) {
                case "streak": return "| Streak: ";
                case "win_msg": return "EPIC WIN!";
                case "draw_msg": return "Draw.";
                case "loose_msg":return "loser:) \uD83E\uDEF5";
                case "unlock_msg": return "New theme unlocked: ";
                case "lock_hint": return "\uD83D\uDD12 Available after 5 consecutive wins";
                case "level_2": return "Normal";
                case "level_2_gameWindow": return "Normal level";
                case "level_3": return "Expert";
                case "level_3_gameWindow": return "Level Expert";
                case "level_1": return "For morons";
                case "level_1_gameWindow": return "Level for morons";
                case "label_level_choice": return "Choose the level";
                case "themes": return "Themes";
                case "theme": return "Theme";
                case "chosen_theme": return "Chosen theme: ";
                case "default_": return "Default";
                case "menu": return "Menu | Streak: ";
            }
        } else {
            switch (key) {
                case "streak": return "| Стрик: ";
                case "win_msg": return "ПОТУЖНА ПЄРЄМОГА!";
                case "draw_msg": return "Ничья.";
                case "loose_msg":return "лошара:) \uD83E\uDEF5";
                case "unlock_msg": return "Разблокирована новая тема: ";
                case "lock_hint": return "\uD83D\uDD12 Будет доступно после 5 побед подряд";
                case "level_2": return "Нормальный";
                case "level_2_gameWindow": return "Нормальный уровень";
                case "level_3": return "Эксперт";
                case "level_3_gameWindow": return "Уровень Эксперт";
                case "level_1": return "Для дебилов";
                case "level_1_gameWindow": return "Уровень для дебилов";
                case "label_level_choice": return "Выберите уровень";
                case "themes": return "Темы";
                case "theme": return "Тема";
                case "chosen_theme": return "Выбранная тема: ";
                case "default_": return "Дефолтная";
                case "menu": return "Меню | Побед подряд: ";
            }
        }
        // Если забыли добавить перевод, вернется сам ключ (чтобы сразу заметить ошибку)
        return key;
    }
}