package signature.database.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;
import signature.model.Client;

public interface ClientMapper {

    @Insert("INSERT INTO user (firstName, lastName, patronymic, login, password, role) VALUES " +
            "( #{firstName}, #{lastName}, #{patronymic}, #{login}, #{password}, #{role.name} )")
    @Options(useGeneratedKeys = true)
    void insertUser(Client client);

    @Insert("INSERT INTO client ( userid, email, address, phone) VALUES " +
            "(#{id}, #{email}, #{address}, #{phone})")
    void insertClient(Client client);

    @Update("UPDATE user, client SET user.firstName = #{firstName}, user.lastName = #{lastName}, " +
            "user.patronymic = #{patronymic}, user.password = #{password}, client.email = #{email}, " +
            "client.address = #{address}, client.phone = #{phone}  WHERE user.id = #{id} and user.id = client.userid")
    void EditProfileClient(Client client);
}
