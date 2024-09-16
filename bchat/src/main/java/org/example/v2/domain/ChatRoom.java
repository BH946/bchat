package org.example.v2.domain;

import java.sql.Timestamp;

public class ChatRoom {

  private Long chatRoom_id; //pk
  private String title;
  private Long user_id; //fk
  private Timestamp created_at; //db default

  protected ChatRoom() {
  }

  public Long getChatRoomId() {
    return chatRoom_id;
  }

  public Long getUserId() {
    return user_id;
  }

  public String getTitle() {
    return title;
  }

  public Timestamp getCreatedAt() {
    return created_at;
  }

  // 연관관계 메서드
  public static ChatRoom createChatRoom(String title, Long userId) {
    ChatRoom chatRoom = new ChatRoom();
    chatRoom.title = title;
    chatRoom.user_id = userId;
    return chatRoom;
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
