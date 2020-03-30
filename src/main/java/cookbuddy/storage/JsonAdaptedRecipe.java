package cookbuddy.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import cookbuddy.commons.exceptions.IllegalValueException;
import cookbuddy.logic.parser.ParserUtil;
import cookbuddy.model.recipe.Recipe;
import cookbuddy.model.recipe.attribute.Calorie;
import cookbuddy.model.recipe.attribute.Difficulty;
import cookbuddy.model.recipe.attribute.IngredientList;
import cookbuddy.model.recipe.attribute.InstructionList;
import cookbuddy.model.recipe.ImagePath;
import cookbuddy.model.recipe.attribute.Name;
import cookbuddy.model.recipe.attribute.Rating;
import cookbuddy.model.recipe.attribute.Serving;
import cookbuddy.model.recipe.attribute.Tag;

/**
 * Jackson-friendly version of {@link Recipe}.
 */
class JsonAdaptedRecipe {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Recipe's %s field is missing!";

    private final String name;
    private final String ingredients;
    private final String instructions;
    private final String filePath;
    private final String calorie;
    private final int serving;
    private final int rating;
    private final int difficulty;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedRecipe} with the given recipe details.
     */
    @JsonCreator
    public JsonAdaptedRecipe(@JsonProperty("name") String name, @JsonProperty("ingredients") String ingredients,
            @JsonProperty("instructions") String instructions, @JsonProperty("filePath") String filePath,
                             @JsonProperty("calorie") String calorie,
                             @JsonProperty("serving") int serving, @JsonProperty("rating") int rating,
                             @JsonProperty("difficulty") int difficulty,
                                     @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.filePath = filePath;
        this.calorie = calorie;
        this.serving = serving;
        this.rating = rating;
        this.difficulty = difficulty;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Recipe} into this class for Jackson use.
     */
    public JsonAdaptedRecipe(Recipe source) {
        name = source.getName().name;
        ingredients = source.getIngredients().toString();
        instructions = source.getInstructions().toString();
        filePath = source.getFilePath().toString();
        calorie = source.getCalorie().calorie;
        serving = source.getServing().serving;
        rating = source.getRating().rating;
        difficulty = source.getDifficulty().difficulty;
        tagged.addAll(source.getTags().stream().map(JsonAdaptedTag::new).collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted recipe object into the model's
     * {@code Recipe} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted recipe.
     */
    public Recipe toModelType() throws IllegalValueException {
        final List<Tag> recipeTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            recipeTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (ingredients == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, IngredientList.class.getSimpleName()));
        }
        // if (!IngredientList.isValidIngredients(ingredients)) {
        // throw new IllegalValueException(IngredientList.MESSAGE_CONSTRAINTS);
        // }
        final IngredientList modelIngredients = ParserUtil.parseIngredients(ingredients);

        if (instructions == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, InstructionList.class.getSimpleName()));
        }
        // if (!InstructionList.isValidInstructions(instructions)) {
        // throw new IllegalValueException(InstructionList.MESSAGE_CONSTRAINTS);
        // }
        final InstructionList modelInstructions = ParserUtil.parseInstructions(instructions);

        if (filePath == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                ImagePath.class.getSimpleName()));
        }

        if (!ImagePath.isValidFilePath(filePath)) {
            throw new IllegalValueException(ImagePath.MESSAGE_CONSTRAINTS);
        }

        final ImagePath modelUrl = new ImagePath(filePath);

        final Calorie modelCalorie = new Calorie(calorie);
        final Serving modelServe = new Serving(serving);
        final Rating modelRating = new Rating(rating);
        final Difficulty modelDifficulty = new Difficulty(difficulty);
        final Set<Tag> modelTags = new HashSet<>(recipeTags);
        return new Recipe(modelName, modelIngredients, modelInstructions, modelUrl, modelCalorie, modelServe,
                modelRating, modelDifficulty, modelTags);
    }
}
