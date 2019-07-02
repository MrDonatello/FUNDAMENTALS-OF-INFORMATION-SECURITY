package signature.database.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import signature.model.Admin;
import signature.model.Client;
import signature.model.Role;

public interface UserMapper {

    @Select({"SELECT user.firstName, user.lastName, user.role, user.patronymic, user.id, admin.position, admin.userid" +
            " FROM user, admin WHERE user.login = #{login} and user.password = #{password} and user.id = admin.userid"})
    Admin loginAdmin(@Param("login") String login, @Param("password") String password);

    @Select({"SELECT user.firstName, user.lastName, user.role, user.patronymic, user.id, admin.position, admin.userid" +
            " FROM user, admin WHERE user.id = #{id} and user.id = admin.userid"})
    Admin infoAdmin(int id);

    @Select({"SELECT user.role FROM user WHERE user.login = #{login} and user.password = #{password}"})
    Role getRole(@Param("login") String login, @Param("password") String password);

    @Select({"SELECT user.role FROM user WHERE user.id = #{id}"})
    Role getRoleById(int id);

    @Select("SELECT user.firstName, user.lastName, user.patronymic, user.role, user.id AS idd, user.id, client.email, client.address, client.phone,  client.userid " +
            "FROM user, client WHERE user.login = #{login} and user.password = #{password} and user.id = client.userid ")
    Client loginClient(@Param("login") String login, @Param("password") String password);

    @Select("SELECT user.firstName, user.lastName, user.patronymic, user.role, user.id AS idd, user.id, client.email, client.address, client.phone,  client.userid " +
            "FROM user, client WHERE user.id = #{id} and user.id = client.userid ")
    Client infoClient(int id);

}
