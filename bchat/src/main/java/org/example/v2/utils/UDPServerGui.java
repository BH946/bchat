package org.example.v2.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.v2.SqlSessionTemplate;
import org.example.v2.domain.User;
import org.example.v2.repository.UserRepository;
import org.example.v2.service.UserService;

public class UDPServerGui implements Runnable {

  private final int port;
  private final UserService userService;

  public UDPServerGui(int port) {
    SqlSessionFactory sqlSessionFactory = SqlSessionTemplate.getSqlSessionFactory();
    UserRepository userRepository = new UserRepository(sqlSessionFactory);
    this.userService = new UserService(userRepository);
    this.port = port;
  }

  @Override
  public void run() {
    try {
      DatagramSocket socket = new DatagramSocket(port);
      byte[] buffer = new byte[512]; //250자 정도
      while (!Thread.currentThread().isInterrupted()) { //스레드 원할 때 종료하려고 isInterrupted()
        //수신(응답)
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet); //대기
        String[] data = new String(packet.getData(), 0, packet.getLength()).split(
            ","); //"id,pw,nickname,name,col,email"
        System.out.println("test: " + Arrays.toString(data));

        //회원가인 또는 로그인으로 분기
        String response = loginNRegister(data);

        //전송
        byte[] resBuf = response.getBytes();
        DatagramPacket resPacket = new DatagramPacket(resBuf, resBuf.length, packet.getAddress(),
            packet.getPort());
        socket.send(resPacket);
        System.out.println(response);
      }
    } catch (SocketException e) {
      //UDP서버가 이미 실행중이면 PASS
      System.out.println("UDP 서버 이미 실행 중");
      return;
    } catch (IOException e) { //receive
      e.printStackTrace();
    }
    System.out.println("UDP 서버 스레드 종료");
  }

  private String loginNRegister(String[] data) {
    String response = "";
    if (data.length == 2) {
      //로그인 진행
      User findUser = userService.login(data[0], data[1]); //id, pw
      if (findUser != null) {
        response = findUser.getUserId() + "," + ConstanctMsg.LOGIN_SUCCESS;
      } else {
        response = ConstanctMsg.LOGIN_FAIL;
      }
    } else {
      //회원가입 진행
      response = ConstanctMsg.REGISTER_SUCCESS;
      try {
        userService.register(data);
      } catch (IllegalStateException e) {
        response = e.getMessage(); //"이미 존재하는 회원입니다."
      }
    }
    return response;
  }


}
