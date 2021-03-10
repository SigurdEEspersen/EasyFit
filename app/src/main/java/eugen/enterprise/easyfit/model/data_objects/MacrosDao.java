package eugen.enterprise.easyfit.model.data_objects;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MacrosDao {
    @Query("SELECT * FROM Macros")
    Macros[] loadSavedMacros();

    @Insert
    void saveMacros(Macros macros);

    @Query("DELETE FROM Macros")
    void deleteSavedMacros();
}
