package org.example.v1.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.v1.repository.MessageRepository;
import org.example.v1.domain.Message;

public class MessageService {
  private final String DRIVER = "oracle.jdbc.driver.OracleDriver";
  private final String URL = "jdbc:oracle:thin:@localhost:1521:STR";
  private final String DB_ID = "testUser";
  private final String DB_PW = "1234";
  
  public MessageService() {};

  /**
   * 메시지 조회
   */
  public List<Message> findAllByChat(Long userId, Long chatRoomId) {
    List<Message> findMessages = new ArrayList<>();
    try{
      Class.forName(DRIVER); // 드라이버를 메모리 로드
      Connection connection = DriverManager.getConnection(URL, DB_ID, DB_PW);
      MessageRepository messageRepository = new MessageRepository(connection);

      //GUI 에서 얻은.. 정보..
      findMessages = messageRepository.findAllByChatId(userId, chatRoomId);
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return findMessages;
  }

  /**
   * 메시지 기록
   */
  public void create(Long chatRoomId, Long userId, String content) {
    try{
      Class.forName(DRIVER); // 드라이버를 메모리 로드
      Connection connection = DriverManager.getConnection(URL, DB_ID, DB_PW);
      MessageRepository messageRepository = new MessageRepository(connection);

      //GUI 에서 얻은.. 정보..
      Message message = new Message(chatRoomId, userId, content);
      messageRepository.save(message);

      System.out.println("채팅 내용이 저장되었습니다. 생성된 ID: " + message.getMessage_id());
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }


}
