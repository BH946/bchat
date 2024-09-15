package org.example.v2.domain;

import java.sql.Timestamp;

public class ChatRoom {
  private Long chatRoom_id; //pk
  private String title;
  private Long user_id; //fk
  private Timestamp created_at;

  public ChatRoom(String title, Long userId) {
    this.title = title;
    user_id = userId;
  }
  public ChatRoom(Long chatRoomId, String title, Long userId, Timestamp createdAt) {
    chatRoom_id = chatRoomId;
    this.title = title;
    user_id = userId;
    created_at = createdAt;
  }

  public void setChatRoom_id(Long chatRoom_id) {
    this.chatRoom_id = chatRoom_id;
  }

  public void setCreated_at(Timestamp created_at) {
    this.created_at = created_at;
  }

  public Long getChatRoom_id() {
    return chatRoom_id;
  }

  public Long getUser_id() {
    return user_id;
  }

  public String getTitle() {
    return title;
  }

  public Timestamp getCreated_at() {
    return created_at;
  }

  @Override
  public String toString() {
    return "ChatRoom{" +
        "chatRoom_id=" + chatRoom_id +
        ", title='" + title + '\'' +
        ", user_id=" + user_id +
        ", created_at=" + created_at +
        '}';
  }
}
