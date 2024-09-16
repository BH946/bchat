package org.example.v2.repository.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.example.v2.domain.Message;

public interface MessageMapper {

  /**
   * 메시지 기록
   */
  void save(Message message);

  /**
   * 해당 채팅방 메시지 전부 조회
   */
  List<Message> findAllByChatId(@Param("chatRoom_id") Long chatRoomId);
}
