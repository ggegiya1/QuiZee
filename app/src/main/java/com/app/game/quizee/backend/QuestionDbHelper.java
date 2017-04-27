package com.app.game.quizee.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by trist on 2017-04-27.
 */

//Adapté de https://developer.android.com/training/basics/data-storage/databases.html#DbHelper

public class QuestionDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    SQLiteDatabase db;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contacts.db";

    public QuestionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_QUESTION + " TEXT," +
                    FeedEntry.COLUMN_DIFFICULTY + " TEXT," +
                    FeedEntry.COLUMN_CATEGORY + " TEXT)" +
                    FeedEntry.COLUMN_ANSWER1 + " TEXT)"+
                    FeedEntry.COLUMN_ANSWER2 + " TEXT)"+
                    FeedEntry.COLUMN_ANSWER3 + " TEXT)"+
                    FeedEntry.COLUMN_CORRECT_ANSWER + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public void addPlayer(Question q) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_QUESTION, q.getTextQuestion());
        values.put(FeedEntry.COLUMN_ANSWER1, q.getAnswers(false).get(0).getText());
        values.put(FeedEntry.COLUMN_ANSWER2, q.getAnswers(false).get(1).getText());
        values.put(FeedEntry.COLUMN_ANSWER3, q.getAnswers(false).get(2).getText());
        values.put(FeedEntry.COLUMN_CORRECT_ANSWER, q.getAnswers(true).get(0).getText());

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);
    }




    public ArrayList getQuestions() {
        ArrayList questions = new ArrayList();
        if (db ==null) {
            db = getReadableDatabase();
        }
        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_QUESTION,
                FeedEntry.COLUMN_DIFFICULTY,
                FeedEntry.COLUMN_ANSWER1,
                FeedEntry.COLUMN_ANSWER2,
                FeedEntry.COLUMN_ANSWER3,
                FeedEntry.COLUMN_CORRECT_ANSWER,
        };

// Filter results WHERE "title" = 'My Title'
        String selection = FeedEntry.COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_LEVEL + " DESC";

        //TODO make cursor choose in certain categories only
        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                "RANDOM() LIMIT 10"                       // The sort order
        );

        while(cursor.moveToNext()) {
            String questionText = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_QUESTION));
            String questionCategory = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_QUESTION));
            String questionDifficulty = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_QUESTION));
            String answer0 = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_QUESTION));
            String answer1 = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_QUESTION));
            String answer2 = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_QUESTION));
            String correctAnswer = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_QUESTION));
            //TODO arranger le constructeur de questions
            //questions.add(new Question(playerId, playerName, playerImage, playerLevel));
        }
        cursor.close();

        return questions;
    }

    //TODO verifier si delete player fonctionne
    public void deleteQuestion(Question q) {
        if (db == null) {
            db = getReadableDatabase();
        };
        String[] selectionArgs = { q.getTextQuestion() };

        db.delete(FeedEntry.COLUMN_QUESTION, "LIKE ?", selectionArgs);
    }

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "questions";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_ANSWER1 = "answer1";
        public static final String COLUMN_ANSWER2 = "answer2";
        public static final String COLUMN_ANSWER3 = "answer3";
        public static final String COLUMN_CORRECT_ANSWER = "correct answer";
    }
}
