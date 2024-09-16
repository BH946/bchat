package org.example.v2.domain;

import java.sql.Timestamp;

public class Message {

  private Long message_id; //pk
  private Long chatRoom_id; //fk
  private Long user_id; //fk
  private String content;
  private Timestamp created_at;

  protected Message() {
  }


  public static Message createMessage(Long chatRoomId, Long userId, String content) {
    Message message = new Message();
    message.chatRoom_id = chatRoomId;
    message.user_id = userId;
    message.content = content;
    return message;
  }

  public void setMessage_id(Long messageId) {
    this.message_id = messageId;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.created_at = createdAt;
  }

  public Long getMessageId() {
    return message_id;
  }

  public Long getChatRoomId() {
    return chatRoom_id;
  }

  public Long getUserId() {
    return user_id;
  }

  public Timestamp getCreatedAt() {
    return created_at;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return "Message{" + "message_id=" + message_id + ", chatRoom_id=" + chatRoom_id + ", user_id="
        + user_id + ", content='" + content + '\'' + ", created_at=" + created_at + '}';
  }
}
