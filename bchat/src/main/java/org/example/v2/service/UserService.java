package org.example.v2.service;

import org.example.v2.domain.Profile;
import org.example.v2.domain.User;
import org.example.v2.repository.UserRepository;

public class UserService {
  private UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  };

  /**
   * 로그인
   */
  public User login(String id, String pw) {
    return userRepository.findByIdNPw(id, pw);
  }
  /**
   * 회원가입 -> Profile도 추가
   */
  public void register(String[] data) {
    User user = dataToUser(data); //db에서 자동생성 필드 제외하고 create
    //1. 중복회원 검증(필수)
    validateDuplicateUser(user.getId(), user.getNickname());
    //2. 회원등록
    user = userRepository.save(user); //user_id 흭득
    Profile profile = dataToProfile(user.getUserId(), data);
    userRepository.save(profile);
  }
  private void validateDuplicateUser(String id, String nickname) {
    User findUser = userRepository.findById(id);
    if (findUser != null) {
      // IllegalStateException 예외를 호출
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }

    findUser = userRepository.findByNickname(nickname);
    if (findUser != null) {
      // IllegalStateException 예외를 호출
      throw new IllegalStateException("이미 존재하는 닉네임입니다.");
    }
  }


  /**
   * 데이터 변환 함수
   */
  private User dataToUser(String[] data) {
    User user = User.createUser(data[0], data[1], data[2]);
    return user;
  }
  private Profile dataToProfile(Long profile_id, String[] data) {
    Profile profile = Profile.createProfile(profile_id, data[3], data[4], data[5]);
    return profile;
  }
}
