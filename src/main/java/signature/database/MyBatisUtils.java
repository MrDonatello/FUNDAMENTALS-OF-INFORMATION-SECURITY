package signature.database;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Reader;

@Service
public class MyBatisUtils {


    private static SqlSessionFactory sqlSessionFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisUtils.class);


    public MyBatisUtils() {
    }

    public static boolean initSqlSessionFactory() {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error loading mybatis-config.xml", e);
            return false;
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public static SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }
}