package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.example.domain.Profile;
import org.example.domain.User;
import org.example.repository.UserRepository;

public class UDPServer {

  public void register() {
    try{
      Connection connection = DriverManager.getConnection("");
      UserRepository userRepository = new UserRepository(connection);

      //GUI 에서 얻은.. 정보..
      User user = User.createUser("test123", "1234");
      user = userRepository.save(user);
      Profile profile = new Profile(user.getUserId(), "admin", "관리자", "010-1234-5678", "admin@gmail.com");
      userRepository.save(profile);

      System.out.println("사용자가 성공적으로 저장되었습니다. 생성된 ID: " + user);
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
