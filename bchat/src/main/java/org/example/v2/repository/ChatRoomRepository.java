package org.example.v2.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.v2.domain.ChatRoom;
import org.example.v2.repository.mapper.ChatRoomMapper;

public class ChatRoomRepository {
  private SqlSessionFactory sqlSessionFactory;

  public ChatRoomRepository(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  /**
   * 채팅방 생성
   */
  public ChatRoom save(ChatRoom chatRoom) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      ChatRoomMapper mapper = session.getMapper(ChatRoomMapper.class);
      mapper.save(chatRoom);
      session.commit(); //트랜잭션 commit
      return chatRoom;
    }
  }

  /**
   * 채팅방 조회
   */
  public ChatRoom findByIdNTitle(Long userId, String title) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      ChatRoomMapper mapper = session.getMapper(ChatRoomMapper.class);
      return mapper.findByIdNTitle(userId, title);
    }
  }

}
