package org.example.v2.repository;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.v2.domain.Profile;
import org.example.v2.domain.User;

public class UserRepository {
  private SqlSessionFactory sqlSessionFactory;

  public UserRepository(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  /**
   * 회원가입
   */
  public User save(User user) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      UserMapper mapper = session.getMapper(UserMapper.class);
      mapper.saveUser(user);
      session.commit(); //트랜잭션 commit
      return user; //생성된 generatedKeys 포함
    }
  }
  public Profile save(Profile profile) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      UserMapper mapper = session.getMapper(UserMapper.class);
      mapper.saveProfile(profile);
      session.commit(); //트랜잭션 commit
      return profile; //생성된 generatedKeys 포함X
    }
  }

  /**
   * 로그인
   */
  public User findByIdNPw(String id, String pw) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      UserMapper mapper = session.getMapper(UserMapper.class);
      return mapper.findByIdNPw(id, pw);
    }
  }

  /**
   * 조회
   */
  public User findById(String id) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      UserMapper mapper = session.getMapper(UserMapper.class);
      return mapper.findById(id);
    }
  }

  public User findByNickname(String nickname) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      UserMapper mapper = session.getMapper(UserMapper.class);
      return mapper.findByNickname(nickname);
    }
  }

}
