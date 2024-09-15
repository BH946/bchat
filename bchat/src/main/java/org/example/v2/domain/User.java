package org.example.v2.domain;

public class User {
  private Long user_id; //pk (시퀀스) + fk(프로필)
  private String id;
  private String pw;
  private String nickname;

  protected User() {} //생성자 Protected 패턴

  public Long getUserId() {
    return user_id;
  }
  public String getId() {
    return id;
  }
  public String getPw() {
    return pw;
  }
  public String getNickname() {
    return nickname;
  }

  public void setUserId(Long userId) {
    this.user_id=userId;
  }

  // 연관관계 메서드
  public static User createUser(String id, String pw, String nickname) {
    User user = new User();
    user.id=id; user.pw=pw; user.nickname=nickname;
    return user;
  }

  @Override
  public String toString() {
    return "User{" +
        "user_id=" + user_id +
        ", id='" + id + '\'' +
        ", nickname='" + nickname + '\'' +
        '}';
  }
}
