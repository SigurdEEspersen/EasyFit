package eugen.enterprise.easyfit.model;

import android.content.Context;

import androidx.room.Room;

public class DatabaseAccess {
    private static DatabaseAccess instance;
    private static AppDatabase appDatabase;
    private static final Object LOCK = new Object();
    private static Context context;

    private DatabaseAccess() {
    }

    public static DatabaseAccess getInstance() {
        if (instance == null) {
            instance = new DatabaseAccess();
        }
        return instance;
    }

    public AppDatabase getDatabase(Context c) {
        if (appDatabase == null) {
            context = c;
            synchronized (LOCK) {
                if (appDatabase == null) {
                    appDatabase = Room.databaseBuilder(c,
                            AppDatabase.class, "easy-fit-database")
                            .build();
                }
            }
        }
        return appDatabase;
    }
}
