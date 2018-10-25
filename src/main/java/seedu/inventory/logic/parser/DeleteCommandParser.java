package seedu.inventory.logic.parser;

import static seedu.inventory.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.inventory.commons.core.index.Index;
import seedu.inventory.logic.commands.DeleteItemCommand;
import seedu.inventory.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteItemCommand object
 */
public class DeleteCommandParser implements Parser<DeleteItemCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteItemCommand
     * and returns an DeleteItemCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteItemCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteItemCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteItemCommand.MESSAGE_USAGE), pe);
        }
    }

}
