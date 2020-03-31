package cookbuddy.model.recipe;

import static cookbuddy.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import cookbuddy.model.recipe.exceptions.DuplicateRecipeException;
import cookbuddy.model.recipe.exceptions.RecipeNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A list of recipes that enforces uniqueness between its elements and does not allow nulls.
 * A recipe is considered unique by comparing using {@code Recipe#isSameRecipe(Recipe)}. As such, adding and updating of
 * recipes uses Recipe#isSameRecipe(Recipe) for equality so as to ensure that the recipe being added or updated is
 * unique in terms of identity in the UniqueRecipeList. However, the removal of a recipe uses Recipe#equals(Object) so
 * as to ensure that the recipe with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Recipe#isSameRecipe(Recipe)
 */
public class UniqueRecipeList implements Iterable<Recipe> {

    private final ObservableList<Recipe> internalList = FXCollections.observableArrayList();
    private final ObservableList<Recipe> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent recipe as the given argument.
     */
    public boolean contains(Recipe toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameRecipe);
    }

    /**
     * Returns the total number of recipes in the list.
     */
    public long count() {
        return internalList.size();
    }

    /**
     * Adds a recipe to the list.
     * The recipe must not already exist in the list.
     */
    public void add(Recipe toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateRecipeException();
        }
        internalList.add(toAdd);
    }

    public void favRecipe(Recipe recipe) {
        recipe.favRecipe();
    }

    /**
     * Replaces the recipe {@code target} in the list with {@code editedRecipe}.
     * {@code target} must exist in the list.
     * The recipe identity of {@code editedRecipe} must not be the same as another existing recipe in the list.
     */
    public void setRecipe(Recipe target, Recipe editedRecipe) {
        requireAllNonNull(target, editedRecipe);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new RecipeNotFoundException();
        }

        if (!target.isSameRecipe(editedRecipe) && contains(editedRecipe)) {
            throw new DuplicateRecipeException();
        }
        if(target.getFavStatus().toString().equals("\2665") == true) {
            editedRecipe.favRecipe();
        }

        internalList.set(index, editedRecipe);

    }

    /**
     * Removes the equivalent recipe from the list.
     * The recipe must exist in the list.
     */
    public void remove(Recipe toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new RecipeNotFoundException();
        }
    }

    public void setRecipes(UniqueRecipeList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code recipes}.
     * {@code recipes} must not contain duplicate recipes.
     */
    public void setRecipes(List<Recipe> recipes) {
        requireAllNonNull(recipes);
        if (!recipesAreUnique(recipes)) {
            throw new DuplicateRecipeException();
        }

        internalList.setAll(recipes);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Recipe> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Recipe> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueRecipeList // instanceof handles nulls
                    && internalList.equals(((UniqueRecipeList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code recipes} contains only unique recipes.
     */
    private boolean recipesAreUnique(List<Recipe> recipes) {
        for (int i = 0; i < recipes.size() - 1; i++) {
            for (int j = i + 1; j < recipes.size(); j++) {
                if (recipes.get(i).isSameRecipe(recipes.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
