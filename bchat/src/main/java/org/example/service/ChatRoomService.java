package org.example.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.example.domain.ChatRoom;
import org.example.domain.Profile;
import org.example.domain.User;
import org.example.repository.ChatRoomRepository;
import org.example.repository.UserRepository;

public class ChatRoomService {
  private final String DRIVER = "oracle.jdbc.driver.OracleDriver";
  private final String URL = "jdbc:oracle:thin:@localhost:1521:STR";
  private final String DB_ID = "testUser";
  private final String DB_PW = "1234";
  
  public ChatRoomService() {};

  /**
   * 채팅방 조회
   */
  public ChatRoom findByIdNTitle(Long id, String title) {
    ChatRoom findChatRoom = null;
    try{
      Class.forName(DRIVER); // 드라이버를 메모리 로드
      Connection connection = DriverManager.getConnection(URL, DB_ID, DB_PW);
      ChatRoomRepository chatRoomRepository = new ChatRoomRepository(connection);

      //GUI 에서 얻은.. 정보..
      findChatRoom = chatRoomRepository.findByIdNTitle(id, title);
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return findChatRoom;
  }

  /**
   * 채팅방 생성 -> "중복검증을 하고, 이미 DB에 있으면 있는값을 반환"
   */
  public ChatRoom create(Long id, String title) {
    ChatRoom newChatRoom=null;
    try{
      Class.forName(DRIVER); // 드라이버를 메모리 로드
      Connection connection = DriverManager.getConnection(URL, DB_ID, DB_PW);
      ChatRoomRepository chatRoomRepository = new ChatRoomRepository(connection);

      newChatRoom = chatRoomRepository.findByIdNTitle(id, title);
      if(newChatRoom!=null) return newChatRoom;

      //GUI 에서 얻은.. 정보..
      ChatRoom chatRoom = new ChatRoom(title, id);
      newChatRoom = chatRoomRepository.save(chatRoom);

      System.out.println("채팅방이 성공적으로 저장되었습니다. 생성된 ID: " + chatRoom.getChatRoom_id());
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return newChatRoom;
  }


}
