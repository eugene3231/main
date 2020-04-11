package cookbuddy.logic.parser;

import static cookbuddy.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static cookbuddy.logic.parser.CommandParserTestUtil.assertParseFailure;
import static cookbuddy.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static cookbuddy.testutil.TypicalIndexes.INDEX_FIRST_RECIPE;
import static cookbuddy.testutil.TypicalRecipes.EGGS_ON_TOAST;
import static cookbuddy.testutil.TypicalRecipes.HAM_SANDWICH;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cookbuddy.commons.core.index.Index;
import cookbuddy.logic.commands.FavCommand;
import cookbuddy.logic.commands.ModifyCommand;
import cookbuddy.logic.commands.UnFavCommand;
import cookbuddy.model.recipe.attribute.Fav;
import org.junit.jupiter.api.Test;

public class FavCommandParserTest {


    private static final String noIndex = "No index has been provided for the command!\n";
    private static final String invalidIndex = "Index must be a non-zero unsigned integer.\n";
    private static final String helpMessage = "For a command summary, type \"help fav\"";
    private static final String helpMessage_unfav = "For a command summary, type \"help unfav\"";
    private static final String invalidFormat = "Invalid command format! \n";

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ModifyCommand.MESSAGE_USAGE);

    private FavCommandParser parser = new FavCommandParser();
    private UnFavCommandParser new_parser = new UnFavCommandParser();


    @Test
    public void starting_recipe_equality_test() {
        assertTrue(EGGS_ON_TOAST.getFavStatus().equals(EGGS_ON_TOAST.getFavStatus()));
    }

    @Test
    public void starting_recipe_status_test() {
        assertTrue(EGGS_ON_TOAST.getFavStatus().equals(new Fav(false)));
    }

    @Test
    public void fav_recipe_success() {
        HAM_SANDWICH.favRecipe();
        assertTrue(HAM_SANDWICH.getFavStatus().equals(new Fav(true)));
    }

    @Test
    public void unFav_recipe_success() {
        HAM_SANDWICH.unFavRecipe();
        assertTrue(HAM_SANDWICH.getFavStatus().equals(new Fav(false)));
    }

    @Test
    public void parse_fav_invalidIndex_test() {
        assertParseFailure(parser, "abc", invalidFormat + invalidIndex + helpMessage);
    }

    @Test
    public void parse_unFav_invalidIndex_test() {
        assertParseFailure(new_parser, "abc", invalidFormat + invalidIndex + helpMessage_unfav);
    }

    @Test
    public void parse_fav_noIndex_test() {
        assertParseFailure(parser, "", invalidFormat + noIndex + helpMessage);
    }

    @Test
    public void parse_unFav_noIndex_test() {
        assertParseFailure(new_parser, "", invalidFormat + noIndex + helpMessage_unfav);
    }

    @Test
    public void parse_fav_success_test() {
        Index targetIndex = INDEX_FIRST_RECIPE;
        FavCommand expectedCommand = new FavCommand(targetIndex);
        assertParseSuccess(parser, "1", expectedCommand);
        
    }

    @Test
    public void parse_unFav_success_test() {
        Index targetIndex = INDEX_FIRST_RECIPE;
        UnFavCommand expectedCommand = new UnFavCommand(targetIndex);
        assertParseSuccess(new_parser, "1", expectedCommand);
    }
    
}
