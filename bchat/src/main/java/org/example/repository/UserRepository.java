package org.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.domain.Profile;
import org.example.domain.User;

public class UserRepository {
  private Connection connection;

  public UserRepository(Connection connection) {
    this.connection = connection;
  }

  public User save(User user) throws SQLException {
    String sql = "insert into User (id, pw, nickname) values (?,?,?)";
    try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) { // generatedKeys 반환
      statement.setString(1, user.getId());
      statement.setString(2, user.getPw());
      statement.setString(2, user.getNickname());
      statement.executeUpdate(); //주로 반환이 필요없는 dml
      //생성된 키 조회
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) { //다음 행 이동 boolean
        user.setId(generatedKeys.getLong(1)); //생성된 users_id 저장
      }
      return user;
    } //try 자동 자원 close
  }

  public void save(Profile profile) throws SQLException {
    String sql = "insert into Profile (profile_id, name, col, email) values (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, profile.getProfileId());
      statement.setString(2, profile.getName());
      statement.setString(3, profile.getCol());
      statement.setString(4, profile.getEmail());
      statement.executeUpdate(); //주로 dml - int 반환
    } //try 자동 자원 close
  }

  public User findById(String id) throws SQLException {
    String sql = "select * from User where id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, id);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        //중복
        User findUser = User.createUser(rs.getString(1),rs.getString(2),rs.getString(3));
        System.out.println("test: user_id값 나오나? : "+rs.getLong(1));
        return findUser;
      }
      return null;
    }
  }
  public User findByNickname(String nickname) throws SQLException {
    String sql = "select * from User where nickname = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, nickname);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        //중복
        User findUser = User.createUser(rs.getString(1),rs.getString(2),rs.getString(3));
        System.out.println("test: user_id값 나오나? : "+rs.getLong(1));
        return findUser;
      }
      return null;
    }
  }
}
