package com.app.game.quizee;

import android.text.Html;
import android.util.Log;
import com.app.game.quizee.BackEnd.BackEndManager;
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
    private static int q_id = 0;
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


    public static Question getQuestion(String categoryName) throws IOException, JSONException{

        JSONObject json = getJSON(categoryName);
        JSONArray questions = json.getJSONArray("results");
        if (questions.length() == 0){
            return null;
        }
        q_id +=1;
        JSONObject jsonQuestion = (JSONObject)questions.get(0);
        //String[] json_array = {"category", "question", "difficulty", "correct_answer"};
        Question question = new Question(q_id,BackEndManager.find_cate(Html.fromHtml(jsonQuestion.getString("category")).toString()),Html.fromHtml(jsonQuestion.getString("question")).toString(),Html.fromHtml(jsonQuestion.getString("difficulty")).toString(),Html.fromHtml(jsonQuestion.getString("correct_answer")).toString());
        JSONArray incorrectAnswers = jsonQuestion.getJSONArray("incorrect_answers");
        for (int i=0; i<incorrectAnswers.length(); i++){
            question.addIncorrectAnswers(Html.fromHtml((String)incorrectAnswers.get(i)).toString());
        }
        return question;
    }
}
