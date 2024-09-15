package org.example.v2.repository;

import org.apache.ibatis.annotations.Param;
import org.example.v2.domain.Profile;
import org.example.v2.domain.User;

public interface UserMapper {

  /**
   * 회원가입
   */
  void saveUser(User user);
  void saveProfile(Profile profile);

  /**
   * 로그인
   */
  User findByIdNPw(@Param("id") String id, @Param("pw") String pw); //id,pw로 찾기

  /**
   * 조회
   */
  User findById(String id); //아이디로 찾기
  User findByNickname(String nickname); //닉네임으로 찾기
}
