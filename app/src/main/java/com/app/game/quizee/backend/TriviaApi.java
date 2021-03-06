package com.app.game.quizee.backend;
import android.text.Html;
import android.util.Log;

import com.app.game.quizee.backend.Answer;
import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.CategoryManager;
import com.app.game.quizee.backend.Question;
import com.app.game.quizee.backend.Questions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TriviaApi {

    private static final String API_URL = "https://opentdb.com/api.php?category=%s&type=multiple&amount=%s";
    private final OkHttpClient httpClient;
    private final List<Category> categories;
    private Questions questions;
    private final int amount;
    private final boolean repeatForever;

    /**
     * Constructor of class TriviaAPI
     */
    public TriviaApi(Collection<Category> categories, int amount, boolean repeatForever) {
        this.categories = new ArrayList<>(categories);
        this.amount = amount;
        this.repeatForever = repeatForever;
        this.httpClient = new OkHttpClient();
    }

    /**
     * Send a HTTP request to the Trivia API and get JSON response
     */
    private JSONObject getJSON(int categoryId, double amount) throws IOException, JSONException {

        // NOTE:  category id = 0 will return a question in any category
        String url = String.format(API_URL, categoryId, amount);
        Log.i("trivia.api", "loading url: " + url);
        Request request = new Request.Builder().url(url).build();
        Response response = httpClient.newCall(request).execute();
        return new JSONObject(response.body().string());
    }

    /**
     * Populate questions with given category
     */
    private void fetchQuestions(Questions questions, Category category, double amount){
        try {
            JSONObject json = getJSON(category.getId(), amount);
            JSONArray questionsJson = json.getJSONArray("results");
            for (int i=0; i<questionsJson.length(); i++){
                questions.withQuestion(fromJson((JSONObject) questionsJson.get(i)));
            }
        }catch (Exception ex){
            Log.e("trivia.api", String.format("error fetching questions for category : [%s]", category.getName()), ex);
        }
    }

    /**
     * If multiple categories given, then try to fetch equal amount of questions for each category
     */
    private void fetchQuestions(List<Category> categories, int amount){
        Map<Category, Double> categorySize = new HashMap<>();
        int remainCategories = categories.size();
        double remainAmount = amount;
        double minQuestionsPerCategory;
        // try to calculate equal amount of questions for each category
        while (remainCategories > 0){
            minQuestionsPerCategory = Math.floor(remainAmount / remainCategories);
            categorySize.put(categories.get(remainCategories - 1), minQuestionsPerCategory);
            remainCategories--;
            remainAmount -= minQuestionsPerCategory;
        }
        questions = new Questions();
        for (Map.Entry<Category, Double> entry: categorySize.entrySet()) {
           fetchQuestions(questions, entry.getKey(), entry.getValue());
        }
    }
    /**
     * Get one question.
     * The method performs a lazy reading, it fetches multiple questions to the cache and then returns them one by one
     */
    public Question getQuestion(){
        // fetch questions when called first time
        if (questions == null){
            fetchQuestions(categories, amount);
        }
        //  fetch more questions if all questions have been viewed
        if (questions.isEmpty() && repeatForever){
            fetchQuestions(categories, amount);
        }

        return questions.nextQuestion();
    }

    /**
     * Map the JSON response to the Question object
     */
    private Question fromJson(JSONObject jsonQuestion) throws JSONException{
        Question question = new Question(CategoryManager.getInstance().getCategoryByName(
                Html.fromHtml(jsonQuestion.getString("category")).toString()),
                Html.fromHtml(jsonQuestion.getString("question")).toString(),
                Html.fromHtml(jsonQuestion.getString("difficulty")).toString());
        question.addAnswer(new Answer(Html.fromHtml(jsonQuestion.getString("correct_answer")).toString(), true));
        JSONArray incorrectAnswers = jsonQuestion.getJSONArray("incorrect_answers");
        for (int i=0; i<incorrectAnswers.length(); i++){
            question.addAnswer(new Answer(Html.fromHtml((String)incorrectAnswers.get(i)).toString(), false));
        }
        return question;
    }
}
