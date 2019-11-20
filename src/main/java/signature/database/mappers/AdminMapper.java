package signature.database.mappers;


import org.apache.ibatis.annotations.*;
import signature.model.Admin;
import signature.model.Client;

import java.util.List;

public interface AdminMapper {

    @Insert("INSERT INTO user (firstName, lastName, patronymic, login, password, role) VALUES " +
            "( #{firstName}, #{lastName}, #{patronymic}, #{login}, #{password}, #{role.name} )")
    @Options(useGeneratedKeys = true)
    void insertUser(Admin admin);

    @Insert("INSERT INTO admin_access (admin_code) VALUES (#{code})")
    void setAdminCode(int code);

    @Insert("INSERT INTO admin (userid, position) VALUES " +
            "( #{id}, #{position} )")
    void insertAdministrator(Admin admin);

    @Select({"SELECT user.firstName, user.lastName, user.role, user.patronymic, user.id, client.userid, client.email, client.address, client.phone" +
            " FROM user, client WHERE user.id = client.userid and user.role = 'CLIENT'"})
    List<Client> getClientInfo();

    @Select({"SELECT * FROM admin_access"})
    int getAdminCode();

    @Update("UPDATE user, admin SET user.firstName = #{firstName}, user.lastName = #{lastName}, user.patronymic = #{patronymic}, user.password = #{password}, admin.position = #{position}  WHERE user.id = #{id} and user.id = admin.userid")
    void editProfileAdmin(Admin admin);

}
