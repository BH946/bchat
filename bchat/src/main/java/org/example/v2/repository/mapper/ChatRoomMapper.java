package org.example.v2.repository.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.v2.domain.ChatRoom;

public interface ChatRoomMapper {

  /**
   * 채팅방 생성
   */
  void save(ChatRoom chatRoom);

  /**
   * 채팅방 조회
   */
//  ChatRoom findByIdNTitle(@Param("created_id") Long userId, @Param("title") String title);
  ChatRoom findByIdNTitle(@Param("user_id") Long userId, @Param("title") String title);
}
