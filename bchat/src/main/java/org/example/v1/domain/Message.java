package org.example.v1.domain;

import java.sql.Timestamp;

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
    message_id = messageId;
    chatRoom_id = chatRoomId;
    user_id = userId;
    this.content = content;
    created_at = createdAt;
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

  @Override
  public String toString() {
    return "Message{" +
        "message_id=" + message_id +
        ", chatRoom_id=" + chatRoom_id +
        ", user_id=" + user_id +
        ", content='" + content + '\'' +
        ", created_at=" + created_at +
        '}';
  }
}
