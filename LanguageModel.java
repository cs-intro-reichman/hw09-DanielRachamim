import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of character data objects.
    private HashMap<String, List> CharDataMap;

    // The window length used in this model.
    private int windowLength;

    // The random number generator used by this model.
    private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
    public void train(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                for (int i = 0; i <= line.length() - windowLength; i++) {
                    String window = line.substring(i, i + windowLength);
                    char nextChar = (i + windowLength < line.length()) ? line.charAt(i + windowLength) : '\0';
                    List charDataList = CharDataMap.getOrDefault(window, new List());
                    charDataList.update(nextChar);
                    CharDataMap.put(window, charDataList);
                }
            }
            // Calculate probabilities for each window
            for (List charDataList : CharDataMap.values()) {
                calculateProbabilities(charDataList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Computes and sets the probabilities (p and cp fields) of all the
    // characters in the given list.
    public void calculateProbabilities(List charDataList) {
        int totalCount = charDataList.getTotalCount();
        double cumulativeProbability = 0.0;
        for (CharData charData : charDataList.toArray()) {
            double probability = (double) charData.getCount() / totalCount;
            cumulativeProbability += probability;
            charData.setP(probability);
            charData.setCp(cumulativeProbability);
        }
    }

    // Returns a random character from the given probabilities list.
    public char getRandomChar(List charDataList) {
        double randomValue = randomGenerator.nextDouble();
        for (CharData charData : charDataList.toArray()) {
            if (randomValue < charData.getCp()) {
                return charData.getChr();
            }
        }
        return charDataList.getLast().getChr(); // Fallback if randomValue exceeds cumulative probability
    }

    /**
     * Generates a random text, based on the probabilities that were learned during training.
     *
     * @param initialText     text to start with. If initialText's last substring of size numberOfLetters
     *                        doesn't appear as a key in Map, we generate no text and return only the initial text.
     * @param numberOfLetters the size of text to generate
     * @return the generated text
     */
    public String generate(String initialText, int textLength) {
        // If the initial text is shorter than the window length, return the initial text
        if (initialText.length() < windowLength) {
            return initialText;
        }

        // Set up the initial window
        String window = initialText.substring(initialText.length() - windowLength);

        // Initialize the generated text with the initial text
        StringBuilder generatedText = new StringBuilder(initialText);

        // Generate additional text until reaching the desired length
        while (generatedText.length() < textLength) {
            // Check if the current window exists in the map
            if (!CharDataMap.containsKey(window)) {
                // If the current window is not found, stop and return the generated text so far
                break;
            }

            // Retrieve the list of character data associated with the current window
            List charDataList = CharDataMap.get(window);

            // Get a random character from the list based on their probabilities
            char nextChar = getRandomChar(charDataList);

            // Append the selected character to the generated text
            generatedText.append(nextChar);

            // Update the window to the last windowLength characters of the generated text
            window = generatedText.substring(generatedText.length() - windowLength);
        }

        // Return the generated text
        return generatedText.toString();
    }

    /** Returns a string representing the map of this language model. */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String key : CharDataMap.keySet()) {
            List keyProbs = CharDataMap.get(key);
            str.append(key).append(" : ").append(keyProbs).append("\n");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        // Testing the LanguageModel class
        LanguageModel languageModel = new LanguageModel(2); // Window size of 2
        languageModel.train("corpus.txt");
        System.out.println(languageModel);
        System.out.println("Generated Text: " + languageModel.generate("you_", 50));
    }
}