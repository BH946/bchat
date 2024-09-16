package org.example.v2.service;

import org.example.v2.domain.ChatRoom;
import org.example.v2.repository.ChatRoomRepository;

public class ChatRoomService {

  private ChatRoomRepository chatRoomRepository;

  public ChatRoomService(ChatRoomRepository chatRoomRepository) {
    this.chatRoomRepository = chatRoomRepository;
  }

  /**
   * 채팅방 조회
   */
  public ChatRoom findByIdNTitle(Long userId, String title) {
    return chatRoomRepository.findByIdNTitle(userId, title);
  }

  /**
   * 채팅방 생성 -> "중복검증 필수"
   */
  public void create(ChatRoom chatRoom) {
    try {
      //1. 중복 검증
      validateDuplicateChatRoom(chatRoom);
      //2. 채팅방 등록
      chatRoomRepository.save(chatRoom);
    } catch (IllegalStateException e) {
      System.out.println(e.getMessage());
      return; //중복이면 그냥 오류없이 종료 -> 어차피 해당 방 사용할거임
    }
  }

  private void validateDuplicateChatRoom(ChatRoom chatRoom) {
    ChatRoom findChatRoom = chatRoomRepository.findByIdNTitle(chatRoom.getUserId(),
        chatRoom.getTitle());
    if (findChatRoom != null) {
      // IllegalStateException 예외를 호출
      throw new IllegalStateException("이미 존재하는 채팅방입니다.");
    }
  }

}
