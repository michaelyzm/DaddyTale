package com.michaelyzm.daddytale.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.michaelyzm.daddytale.DaddyTaleApplication;
import com.michaelyzm.daddytale.model.Book;

import java.util.ArrayList;


/**
 * Created by zhoyu on 8/8/2016.
 */
public class DatabaseHelper {

    private static DatabaseHelper instance;
    final String DATABASE_NAME="daddy_tale.db";

    private SQLiteDatabase db;
    Context context;
    public static DatabaseHelper getInstance()
    {
        if(instance == null)
        {
            instance = new DatabaseHelper(DaddyTaleApplication.ApplicationContext);
        }
        return instance;
    }
    private DatabaseHelper(Context context)
    {
        this.context = context;
    }

    public void createAndInitDatabase()
    {
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        try{
            db.execSQL("CREATE TABLE book(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, photo VARCHAR, audio VARCHAR, tag VARCHAR");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addNewBook(String bookName, String photoPath, String audioPath)
    {
        try {
            db.execSQL("INSERT INTO book VALUES (NULL, ?, ?, ?)", new Object[]{bookName, photoPath, audioPath});
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void removeBook(int id)
    {
        try{
            db.execSQL("DELETE FROM book WHERE _id="+id);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateBook(int id, String bookName, String photoPath, String audioPath)
    {
        try{
            db.execSQL("UPDATE book SET name=?, photo=?, audio=? WHERE _id=?", new Object[]{bookName, photoPath, audioPath, id});
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> getAllBooks()
    {
        ArrayList<Book> result = new ArrayList<>();
        Cursor cursor = db.query("book", null, null, null, null, null, null);
        while(cursor.moveToNext())
        {
            Book book = new Book();
            book.id = cursor.getInt(cursor.getColumnIndex("_id"));
            book.bookName = cursor.getString(cursor.getColumnIndex("name"));
            book.photoPath = cursor.getString(cursor.getColumnIndex("photo"));
            book.audioPath = cursor.getString(cursor.getColumnIndex("audio"));
            result.add(book);
        }
        return result;
    }
}
