package org.example.domain;

public class Profile {
  private Long profile_id; //pk (시퀀스x. user_id 사용) - 어차피 1:1
  private String nickname;
  private String name;
  private String col;
  private String email;

  public Profile(Long id, String nickname, String name, String col, String email) {
    this.profile_id=id;
    this.nickname=nickname; this.name=name;
    this.col=col; this.email=email;
  }
  public Long getProfileId() {
    return profile_id;
  }
  public String getNickname() {
    return nickname;
  }
  public String getName() {
    return name;
  }
  public String getCol() {
    return col;
  }
  public String getEmail() {
    return email;
  }
}
