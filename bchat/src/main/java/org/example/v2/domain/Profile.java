package org.example.v2.domain;

public class Profile {
  private Long profile_id; //pk (시퀀스x. user_id 사용) - 어차피 1:1
  private String name;
  private String col;
  private String email;

  protected Profile() {};

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

  // 연관관계 메서드
  public static Profile createProfile(Long profileId, String name, String col, String email) {
    Profile profile = new Profile();
    profile.profile_id=profileId; profile.name=name;
    profile.col=col; profile.email=email;
    return profile;
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
