package evgenykravtsov.appblocker.data.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import evgenykravtsov.appblocker.domain.model.App;
import evgenykravtsov.appblocker.domain.model.AppRepository;
import evgenykravtsov.appblocker.external.android.AppBlockerController;

public class SqliteHelper extends SQLiteOpenHelper implements AppRepository {

    private static final String DATABASE_NAME =
            AppBlockerController.class.getSimpleName() + "_sqlite_database";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME_BLOCKED_APPLICATIONS = "blocked_applications";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_APPLICATION_NAME = "application_title";
    private static final String COLUMN_APPLICATION_PROCESS_NAME = "application_process_name";

    private static SqliteHelper instance;

    private int databaseClientCounter;
    private SQLiteDatabase database;

    ////

    private SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized SqliteHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SqliteHelper(context);
        }

        return instance;
    }

    ////

    @Override
    public List<App> getBlockedApps() {
        openDatabase();

        String statement = "SELECT * FROM " + TABLE_NAME_BLOCKED_APPLICATIONS;
        List<App> blockedApps = new ArrayList<>();

        Cursor cursor = select(statement);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            blockedApps.add(mapCursorToApp(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return blockedApps;
    }

    @Override
    public void addAppToBlockList(App app) {
        openDatabase();
        String sqlQuery = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",
                TABLE_NAME_BLOCKED_APPLICATIONS,
                COLUMN_APPLICATION_NAME,
                COLUMN_APPLICATION_PROCESS_NAME);

        SQLiteStatement statement = database.compileStatement(sqlQuery);
        String applicationName = app.getTitle();
        String applicationProcessName = app.getProcessName();

        statement.bindString(1, applicationName);
        statement.bindString(2, applicationProcessName);

        insert(statement);
        closeDatabase();
    }

    @Override
    public void removeAppFromBlockList(App app) {
        openDatabase();
        String sqlQuery = String.format("DELETE FROM %s WHERE %s = ?",
                TABLE_NAME_BLOCKED_APPLICATIONS,
                COLUMN_APPLICATION_NAME);

        SQLiteStatement statement = database.compileStatement(sqlQuery);
        String applicationName = app.getTitle();
        statement.bindString(1, applicationName);

        delete(statement);
        closeDatabase();
    }


    ////

    @Override
    public void onCreate(SQLiteDatabase database) {
        createBlockedApplicationsTable(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        dropBlockedApplicationsTable(database);
        onCreate(database);
    }

    ////

    private void createBlockedApplicationsTable(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + TABLE_NAME_BLOCKED_APPLICATIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_APPLICATION_NAME + " TEXT NOT NULL, " +
                COLUMN_APPLICATION_PROCESS_NAME + " TEXT NOT NULL)");
    }

    private void dropBlockedApplicationsTable(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BLOCKED_APPLICATIONS);
    }

    private synchronized SQLiteDatabase openDatabase() {
        databaseClientCounter++;

        if (databaseClientCounter == 1) {
            database = instance.getWritableDatabase();
        }

        return database;
    }

    private synchronized void closeDatabase() {
        databaseClientCounter--;

        if (databaseClientCounter == 0) {
            database.close();
        }
    }

    private void insert(SQLiteStatement statement) {
        statement.executeInsert();
    }

    private Cursor select(String statement) {
        return database.rawQuery(statement, null);
    }

    private int delete(SQLiteStatement statement) {
        return statement.executeUpdateDelete();
    }

    private App mapCursorToApp(Cursor cursor) {
        return new App(cursor.getString(1), cursor.getString(2));
    }
}
