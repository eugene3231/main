package cookbuddy.logic.commands;

import static cookbuddy.logic.commands.CommandTestUtil.assertCommandSuccess;
import static cookbuddy.testutil.TypicalRecipes.getTypicalRecipeBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import cookbuddy.commons.core.index.Index;
import cookbuddy.model.Model;
import cookbuddy.model.ModelManager;
import cookbuddy.model.UserPrefs;
import cookbuddy.model.recipe.Recipe;
import org.junit.jupiter.api.Test;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class RandomCommandTest {

    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());
    private Model expectedModel;

    
    @Test
    public void equalityCheck() {
       RandomCommand randomCom = new RandomCommand();
       RandomCommand sameVal = rc;
       RandomCommand newRandom = new RandomCommand();
       assertEquals(randomCom, sameVal);
       if (randomCom.getTargetIndex().equals(newRandom.getTargetIndex())) {
           assertEquals(randomCom, newRandom);
       } else {
           assertNotEquals(randomCom, newRandom);
       }
    }
    
}
