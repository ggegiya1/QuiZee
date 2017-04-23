package com.app.game.quizee.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by trist on 2017-04-23.
 * Database tiré de l'exemple sur https://developer.android.com/training/basics/data-storage/databases.html#DbHelper
 */

public class ContactDbHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contacts.db";

    public ContactDbHelper(Context context) {
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
                    FeedEntry.COLUMN_PLAYER_NAME + " TEXT," +
                    FeedEntry.COLUMN_AVATAR + " TEXT," +
                    FeedEntry.COLUMN_LEVEL + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public void addPlayer(Player p) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_AVATAR, p.getImage());
        values.put(FeedEntry.COLUMN_LEVEL, p.getLevel());
        values.put(FeedEntry.COLUMN_PLAYER_NAME, p.getName());

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);
    }




    public ArrayList getPlayers() {
        ArrayList players = new ArrayList();
        if (db ==null) {
            db = getReadableDatabase();
        }
        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_PLAYER_NAME,
                FeedEntry.COLUMN_LEVEL,
                FeedEntry.COLUMN_AVATAR
        };

// Filter results WHERE "title" = 'My Title'
        String selection = FeedEntry.COLUMN_PLAYER_NAME + " = ?";
        String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedEntry.COLUMN_LEVEL + " DESC";

        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        while(cursor.moveToNext()) {
            String playerId = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry._ID));
            String playerName = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_PLAYER_NAME));
            String playerImage = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_AVATAR));
            int playerLevel = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_LEVEL));
            players.add(new Player(playerId, playerName, playerImage, playerLevel));
        }
        cursor.close();

        return players;
    }

    //TODO verifier si delete player fonctionne
    public void deletePlayer(Player p) {
        if (db == null) {
            db = getReadableDatabase();
        };
        String[] selectionArgs = { p.getId() };

        db.delete(FeedEntry.TABLE_NAME, "LIKE ?", selectionArgs);
    }

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_PLAYER_NAME = "name";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_AVATAR = "avatar";
    }
}
