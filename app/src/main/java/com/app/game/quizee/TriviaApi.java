package com.app.game.quizee;

import android.text.Html;
import android.util.Log;
import com.app.game.quizee.backend.Category;
import com.app.game.quizee.backend.CategoryManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ggegiya1 on 2/15/17.
 */

public class TriviaApi {

    private static final String API_URL = "https://opentdb.com/api.php?category=%s&type=multiple&amount=%s";
    private static OkHttpClient http;


    private static JSONObject getJSON(int categoryId, int amount) throws IOException, JSONException {

        // NOTE:  category id = 0 will return a question in any category
        String url = String.format(API_URL, categoryId, amount);

        Log.i("trivia.api", "loading url: " + url);
        Request request = new Request.Builder().url(url).build();

        if(http == null)
            http = new OkHttpClient();

        Response response = http.newCall(request).execute();
        return new JSONObject(response.body().string());
    }

    /**
     * Return questions in given category
     * @param category
     * @param amount
     * @return
     */
    static List<Question> getQuestions(Category category, int amount){
        List<Question> questions = new ArrayList<>();
        try {
            JSONObject json = getJSON(category.getId(), amount);
            JSONArray questionsJson = json.getJSONArray("results");
            for (int i=0; i<questionsJson.length(); i++){
                questions.add(fromJson((JSONObject) questionsJson.get(i)));
            }
        }catch (Exception ex){
            Log.e("trivia.api", String.format("error fetching questions for category : [%s]", category.getName()));
        }
        return questions;
    }

    /**
     * Return one questions in given category
     * @param category
     * @return
     */
    static Question getQuestion(Category category){
        try {
            JSONObject json = getJSON(category.getId(), 1);
            JSONArray questions = json.getJSONArray("results");
            if (questions.length() == 0){
                return null;
            }
            JSONObject jsonQuestion = (JSONObject)questions.get(0);
            return fromJson(jsonQuestion);
        }catch (Exception ex){
            Log.e("trivia.api", String.format("error fetching questions for category : [%s]", category.getName()));
        }
        return null;
    }

    private static Question fromJson(JSONObject jsonQuestion) throws JSONException{
        Question question = new Question(CategoryManager.getInstance().getCategoryByName(
                Html.fromHtml(jsonQuestion.getString("category")).toString()),
                Html.fromHtml(jsonQuestion.getString("question")).toString(),
                Html.fromHtml(jsonQuestion.getString("difficulty")).toString(),
                Html.fromHtml(jsonQuestion.getString("correct_answer")).toString());
        JSONArray incorrectAnswers = jsonQuestion.getJSONArray("incorrect_answers");
        for (int i=0; i<incorrectAnswers.length(); i++){
            question.addIncorrectAnswers(Html.fromHtml((String)incorrectAnswers.get(i)).toString());
        }
        return question;
    }
}
