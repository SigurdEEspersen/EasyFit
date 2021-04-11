package eugen.enterprise.easyfit.model.data_objects;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SetResultDao {
    @Query("SELECT * FROM SetResultData")
    SetResultData[] loadSavedSetResults();

    @Insert
    void saveSetResult(SetResultData setResult);
}
