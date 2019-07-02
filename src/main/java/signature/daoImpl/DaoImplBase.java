package signature.daoImpl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import signature.database.MyBatisUtils;
import signature.database.mappers.*;

@Service
public class DaoImplBase {

    protected SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    protected UserMapper getUserMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(UserMapper.class);
    }

    protected AdminMapper getAdministratorMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AdminMapper.class);
    }

    protected ClientMapper getClientMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(ClientMapper.class);
    }


    protected SessionMapper getSessionMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(SessionMapper.class);
    }

    protected ClearDatabaseMapper getClearDataBaseMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(ClearDatabaseMapper.class);
    }
}
