package seedu.inventory.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.inventory.logic.commands.ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT;

import org.junit.Rule;
import org.junit.Test;

import seedu.inventory.commons.events.ui.ExitAppRequestEvent;
import seedu.inventory.logic.CommandHistory;
import seedu.inventory.model.Model;
import seedu.inventory.model.ModelManager;
import seedu.inventory.ui.testutil.EventsCollectorRule;

public class ExitCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_exit_success() {
        CommandResult result = new ExitCommand().execute(model, commandHistory);
        assertEquals(MESSAGE_EXIT_ACKNOWLEDGEMENT, result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ExitAppRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }
}
