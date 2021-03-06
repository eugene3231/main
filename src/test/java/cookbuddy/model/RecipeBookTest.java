package cookbuddy.model;

import static cookbuddy.logic.commands.CommandTestUtil.VALID_TAG_LUNCH;
import static cookbuddy.testutil.Assert.assertThrows;
import static cookbuddy.testutil.TypicalRecipes.HAM_SANDWICH;
import static cookbuddy.testutil.TypicalRecipes.getTypicalRecipeBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import cookbuddy.model.recipe.Recipe;
import cookbuddy.model.recipe.exceptions.DuplicateRecipeException;
import cookbuddy.testutil.RecipeBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RecipeBookTest {

    private final RecipeBook recipeBook = new RecipeBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), recipeBook.getRecipeList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> recipeBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        RecipeBook newData = getTypicalRecipeBook();
        recipeBook.resetData(newData);
        assertEquals(newData, recipeBook);
    }

    @Test
    public void resetData_withDuplicateRecipes_throwsDuplicateRecipeException() {
        // Two recipes with the same identity fields
        Recipe editedHamSandwich = new RecipeBuilder(HAM_SANDWICH).withTags(VALID_TAG_LUNCH).build();
        List<Recipe> newRecipes = Arrays.asList(HAM_SANDWICH, editedHamSandwich);
        RecipeBookStub newData = new RecipeBookStub(newRecipes);

        assertThrows(DuplicateRecipeException.class, () -> recipeBook.resetData(newData));
    }

    @Test
    public void hasRecipe_nullRecipe_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> recipeBook.hasRecipe(null));
    }

    @Test
    public void hasRecipe_recipeNotInRecipeBook_returnsFalse() {
        assertFalse(recipeBook.hasRecipe(HAM_SANDWICH));
    }

    @Test
    public void hasRecipe_recipeInRecipeBook_returnsTrue() {
        recipeBook.addRecipe(HAM_SANDWICH);
        assertTrue(recipeBook.hasRecipe(HAM_SANDWICH));
    }

    @Test
    public void hasRecipe_recipeWithSameIdentityFieldsInRecipeBook_returnsTrue() {
        recipeBook.addRecipe(HAM_SANDWICH);
        Recipe editedAlice = new RecipeBuilder(HAM_SANDWICH).withTags(VALID_TAG_LUNCH).build();
        assertTrue(recipeBook.hasRecipe(editedAlice));
    }

    @Test
    public void getRecipeList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> recipeBook.getRecipeList().remove(0));
    }

    /**
     * A stub ReadOnlyRecipeBook whose recipes list can violate interface constraints.
     */
    private static class RecipeBookStub implements ReadOnlyRecipeBook {
        private final ObservableList<Recipe> recipes = FXCollections.observableArrayList();

        RecipeBookStub(Collection<Recipe> recipes) {
            this.recipes.setAll(recipes);
        }

        @Override
        public ObservableList<Recipe> getRecipeList() {
            return recipes;
        }
    }
}
