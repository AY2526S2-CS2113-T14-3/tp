package seedu.duke.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.ui.ErrorUi;

public final class CommandSupport {
    private static final Logger logger = Logger.getLogger(CommandSupport.class.getName());

    private CommandSupport() {}

    public static void saveData(AppContainer container) {
        try {
            if (container.getCategories() != null && container.getStorage() != null) {
                container.getStorage().save(container.getCategories());
            }
        } catch (java.io.IOException e) {
            ErrorUi.printFileSaveError();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Internal error during save", e);
        }
    }

    public static int getCategoryIndex(AppContainer container, String[] sentence) {
        int categoryIndex = Integer.parseInt(sentence[2]) - 1;
        if (categoryIndex < 0 || categoryIndex >= container.getCategories().getAmount()) {
            throw new IndexOutOfBoundsException("Category " + sentence[2] + " does not exist.");
        }
        return categoryIndex;
    }
}
