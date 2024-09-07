package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.example.domain.Profile;
import org.example.domain.User;
import org.example.repository.UserRepository;

public class UDPServer {
  //test
  private final String TEST_ID = "test123";
  private final String TEST_PW = "1234";
  private final String TEST_NICKNAME = "admin";
  private final String TEST_DB_CONNECTION = "";

  /**
   * 회원 가입
   */
  public void register() {
    // 1. 중복 회원 검증(필수)
    validateDuplicateUser(TEST_ID, TEST_NICKNAME);

    // 2. 회원 등록
    try{
      Connection connection = DriverManager.getConnection(TEST_DB_CONNECTION);
      UserRepository userRepository = new UserRepository(connection);

      //GUI 에서 얻은.. 정보..
      User user = User.createUser(TEST_ID, TEST_PW, TEST_NICKNAME);
      user = userRepository.save(user);
      Profile profile = new Profile(user.getUserId(), "관리자", "010-1234-5678", "admin@gmail.com");
      userRepository.save(profile);

      System.out.println("사용자가 성공적으로 저장되었습니다. 생성된 ID: " + user);
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void validateDuplicateUser(String id, String nickname) {
    // find함수를 레퍼지토리에 먼저 구현 후 체크 -> 쿠러ㅣ튜닝필ㅇㄹ
    try{
      Connection connection = DriverManager.getConnection(TEST_DB_CONNECTION);
      UserRepository userRepository = new UserRepository(connection);
      User findUser = userRepository.findById(id);
      if (findUser != null) {
        // IllegalStateException 예외를 호출
        throw new IllegalStateException("이미 존재하는 회원입니다.");
      }

      findUser = userRepository.findByNickname(nickname);
      if (findUser != null) {
        // IllegalStateException 예외를 호출
        throw new IllegalStateException("이미 존재하는 닉네임입니다.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
