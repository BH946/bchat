package org.example.v1.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.v1.domain.Message;

public class MessageRepository {
  private Connection connection;

  public MessageRepository(Connection connection) {
    this.connection = connection;
  }

  /**
   * 메시지 기록
   */
  public void save(Message message) throws SQLException {
    String[] keyCol ={"message_id"}; //키 값이 생성되는 컬럼 명
    String sql = "insert into Message (chatRoom_id, user_id, content) values (?,?,?)";
    try (PreparedStatement statement = connection.prepareStatement(sql, keyCol)) { // generatedKeys 반환
      statement.setLong(1, message.getChatRoom_id());
      statement.setLong(2, message.getUser_id());
      statement.setString(3, message.getContent());
      statement.executeUpdate(); //주로 반환이 필요없는 dml
      //생성된 키 조회
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) { //다음 행 이동 boolean
        message.setMessage_id(generatedKeys.getLong(1)); //생성된 message_id 저장
      }
    } //try 자동 자원 close
  }

  /**
   * 해당 채팅방 메시지 전부 조회
   */
  public List<Message> findAllByChatId(Long userId, Long chatRoomId) throws SQLException {
    List<Message> findMessages = new ArrayList<>();
    String sql = "select * from Message where user_id = ? and chatRoom_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, userId);
      statement.setLong(2, chatRoomId);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        Message findMessage = new Message(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getString(4), rs.getTimestamp(5));
        findMessages.add(findMessage);
      }
      return findMessages;
    }
  }

}
