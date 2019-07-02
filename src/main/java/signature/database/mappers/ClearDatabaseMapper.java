package signature.database.mappers;

import org.apache.ibatis.annotations.Delete;

public interface ClearDatabaseMapper {


    @Delete("DELETE FROM admin")
    void deleteAdmin();

    @Delete("DELETE FROM  client")
    void deleteClien();

    @Delete("DELETE FROM session")
    void deleteSession();

    @Delete("DELETE FROM  user")
    void deleteUser();
}
