package org.example.domain;

import java.sql.Timestamp;
import oracle.sql.TIMESTAMP;

public class Message {
  private Long message_id; //pk
  private Long chatRoom_id; //fk
  private Long user_id; //fk
  private String content;
  private Timestamp created_at;

  public Message(Long chatRoomId, Long userId, String content) {
    chatRoom_id = chatRoomId;
    user_id = userId;
    this.content = content;
  }
  public Message(Long messageId, Long chatRoomId, Long userId, String content, Timestamp createdAt) {
    messageId = message_id;
    chatRoom_id = chatRoomId;
    user_id = userId;
    this.content = content;
    createdAt = created_at;
  }

  public void setMessage_id(Long message_id) {
    this.message_id = message_id;
  }

  public void setCreated_at(Timestamp created_at) {
    this.created_at = created_at;
  }

  public Long getMessage_id() {
    return message_id;
  }

  public Long getChatRoom_id() {
    return chatRoom_id;
  }

  public Long getUser_id() {
    return user_id;
  }

  public Timestamp getCreated_at() {
    return created_at;
  }

  public String getContent() {
    return content;
  }
}
