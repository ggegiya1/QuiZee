package com.app.game.quizee;

import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ggegiya1 on 2/15/17.
 */

public class TriviaApi {

    private static final String API_URL = "https://opentdb.com/api.php?amount=1&category=%s&type=multiple";

    private static OkHttpClient http;

    private static JSONObject getJSON(String categoryId) throws IOException, JSONException {

        String url = String.format(API_URL, categoryId);

        Log.i("trivia.api", "loading url: " + url);
        Request request = new Request.Builder().url(url).build();

        if(http == null)
            http = new OkHttpClient();

        Response response = http.newCall(request).execute();

        return new JSONObject(response.body().string());
    }


    public static Question getQuestion(String categoryId) throws IOException, JSONException{

        JSONObject json = getJSON(categoryId);
        JSONArray questions = json.getJSONArray("results");
        if (questions.length() == 0){
            return null;
        }
        JSONObject jsonQuestion = (JSONObject)questions.get(0);
        Question question = new Question();
        question.setCategory(Html.fromHtml(jsonQuestion.getString("category")).toString());
        question.setQuestion(Html.fromHtml(jsonQuestion.getString("question")).toString());
        question.setDifficulty(Html.fromHtml(jsonQuestion.getString("difficulty")).toString());
        question.setCorrectAnswer(Html.fromHtml(jsonQuestion.getString("correct_answer")).toString());
        JSONArray incorrectAnswers = jsonQuestion.getJSONArray("incorrect_answers");
        for (int i=0; i<incorrectAnswers.length(); i++){
            question.addIncorrectAnswers(Html.fromHtml((String)incorrectAnswers.get(i)).toString());
        }
        return question;
    }
}
