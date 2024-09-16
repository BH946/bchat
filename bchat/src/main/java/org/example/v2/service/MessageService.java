package org.example.v2.service;

import java.util.List;
import org.example.v2.domain.Message;
import org.example.v2.repository.MessageRepository;

public class MessageService {

  private MessageRepository messageRepository;

  public MessageService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  /**
   * 메시지 조회
   */
  public List<Message> findAllByChatId(Long chatRoomId) {
    return messageRepository.findAllByChatId(chatRoomId);
  }

  /**
   * 메시지 기록 -> "중복 검증 없어도 됨"
   */
  public void create(Message message) {
    messageRepository.save(message);
  }


}
