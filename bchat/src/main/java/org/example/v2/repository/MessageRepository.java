package org.example.v2.repository;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.v2.domain.Message;
import org.example.v2.repository.mapper.MessageMapper;

public class MessageRepository {

  private SqlSessionFactory sqlSessionFactory;

  public MessageRepository(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  /**
   * 메시지 기록
   */
  public Message save(Message message) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      MessageMapper mapper = session.getMapper(MessageMapper.class);
      mapper.save(message);
      session.commit(); //트랜잭션 commit
      return message;
    }
  }

  /**
   * 해당 채팅방 메시지 전부 조회
   */
  public List<Message> findAllByChatId(Long chatRoomId) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      MessageMapper mapper = session.getMapper(MessageMapper.class);
      return mapper.findAllByChatId(chatRoomId);
    }
  }
}
