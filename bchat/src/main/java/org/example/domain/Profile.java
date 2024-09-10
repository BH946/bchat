package org.example.domain;

public class Profile {
  private Long profile_id; //pk (시퀀스x. user_id 사용) - 어차피 1:1
  private String name;
  private String col;
  private String email;

  public Profile(Long id, String name, String col, String email) {
    this.profile_id=id; this.name=name;
    this.col=col; this.email=email;
  }
  public Long getProfileId() {
    return profile_id;
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

  @Override
  public String toString() {
    return "Profile{" +
        "profile_id=" + profile_id +
        ", name='" + name + '\'' +
        ", col='" + col + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}
