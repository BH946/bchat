package org.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.domain.ChatRoom;
import org.example.domain.User;

public class ChatRoomRepository {
  private Connection connection;

  public ChatRoomRepository(Connection connection) {
    this.connection = connection;
  }

  /**
   * 회원가입
   */
  public ChatRoom save(ChatRoom chatRoom) throws SQLException {
    String[] keyCol ={"chatRoom_id"}; //키 값이 생성되는 컬럼 명
    String sql = "insert into ChatRoom (title, created_id) values (?,?)";
    try (PreparedStatement statement = connection.prepareStatement(sql, keyCol)) { // generatedKeys 반환
      statement.setString(1, chatRoom.getTitle());
      statement.setLong(2, chatRoom.getUser_id());
      statement.executeUpdate(); //주로 반환이 필요없는 dml
      //생성된 키 조회
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) { //다음 행 이동 boolean
        chatRoom.setChatRoom_id(generatedKeys.getLong(1)); //생성된 chatRoom_id 저장
      }
      return chatRoom;
    } //try 자동 자원 close
  }

  public ChatRoom findByIdNTitle(Long id, String title) throws SQLException {
    String sql = "select * from ChatRoom where created_id = ? and title = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, id);
      statement.setString(2, title);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        ChatRoom findChatRoom = new ChatRoom(rs.getString(2), rs.getLong(3));
        System.out.println(findChatRoom); //debug
        return findChatRoom;
      }
      return null;
    }
  }

}