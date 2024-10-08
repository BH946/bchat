package org.example.v1.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.example.v1.domain.Profile;
import org.example.v1.domain.User;
import org.example.v1.repository.UserRepository;
import org.example.v2.utils.ConstanctMsg;

public class UserService {
  //test
//  private final String TEST_ID = "test123";
//  private final String TEST_PW = "1234";
//  private final String TEST_NICKNAME = "admin";

  private final String DRIVER = "oracle.jdbc.driver.OracleDriver";
  private final String URL = "jdbc:oracle:thin:@localhost:1521:STR";
  private final String DB_ID = "testUser";
  private final String DB_PW = "1234";

  public UserService() {};

  /**
   * 로그인
   */
  public User login(String id, String pw) {
    User findUser = null;
    try{
      Class.forName(DRIVER); // 드라이버를 메모리 로드
      Connection connection = DriverManager.getConnection(URL, DB_ID, DB_PW);
      UserRepository userRepository = new UserRepository(connection);

      //GUI 에서 얻은.. 정보..
      findUser = userRepository.findByIdNPw(id,pw);
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return findUser;
  }

  /**
   * 회원 가입
   */
  public void register(String[] data) {
    User user = dataToUser(data);
    // 1. 중복 회원 검증(필수)
    validateDuplicateUser(user.getId(), user.getNickname());

    // 2. 회원 등록
    try{
      Class.forName(DRIVER); // 드라이버를 메모리 로드
      Connection connection = DriverManager.getConnection(URL, DB_ID, DB_PW);
      UserRepository userRepository = new UserRepository(connection);

      //GUI 에서 얻은.. 정보..
      User newUser = User.createUser(user.getId(), user.getPw(), user.getNickname());
      newUser = userRepository.save(newUser);
      Profile newProfile = dataToProfile(newUser.getUserId(), data);
//      Profile profile = new Profile(newUser.getUserId(), "관리자", "010-1234-5678", "admin@gmail.com");
      userRepository.save(newProfile);

      System.out.println("사용자가 성공적으로 저장되었습니다. 생성된 ID: " + newUser.getUserId());
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void validateDuplicateUser(String id, String nickname) {
    // find함수를 레퍼지토리에 먼저 구현 후 체크 -> 쿠러ㅣ튜닝필ㅇㄹ
    try{
      Class.forName(DRIVER); // 드라이버를 메모리 로드
      Connection connection = DriverManager.getConnection(URL, DB_ID, DB_PW);
      UserRepository userRepository = new UserRepository(connection);
      User findUser = userRepository.findById(id);
      if (findUser != null) {
        // IllegalStateException 예외를 호출
        throw new IllegalStateException(ConstanctMsg.REGISTER_FAIL);
      }

      findUser = userRepository.findByNickname(nickname);
      if (findUser != null) {
        // IllegalStateException 예외를 호출
        throw new IllegalStateException("이미 존재하는 닉네임입니다.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private User dataToUser(String[] data) {
    User user = User.createUser(data[0], data[1], data[2]);
    return user;
  }
  private Profile dataToProfile(Long id, String[] data) {
    Profile profile = new Profile(id, data[3], data[4], data[5]);
    return profile;
  }
}
