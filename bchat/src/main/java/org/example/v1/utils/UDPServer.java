package org.example.v1.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import org.example.v1.domain.User;
import org.example.v1.service.UserService;

public class UDPServer implements Runnable {
  private final int port;

  public UDPServer(int port) {
    this.port = port;
  }

  @Override
  public void run() {
    try {
      DatagramSocket socket = new DatagramSocket(port);
      byte[] buffer = new byte[512]; //250자 정도
      while (!Thread.currentThread().isInterrupted()) { //스레드 원할 때 종료하려고 isInterrupted()
        //응답
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet); //대기
        String[] data = new String(packet.getData(), 0, packet.getLength()).split(
            ","); //"id,pw,nickname,name,col,email"
        System.out.println("test1: " + Arrays.toString(data));

        //회원가인 또는 로그인으로 분기
        String response = "";
        UserService userService = new UserService();
        if (data.length == 2) {
          //로그인 진행
          response = "로그인에 성공하셨습니다.";
          User findUser = userService.login(data[0], data[1]); //id, pw
          if (findUser == null)
            response = "로그인에 실패했습니다.";
        } else {
          //회원가입 진행
          response = "회원가입을 완료 했습니다.";
          try {
            userService.register(data);
          } catch (IllegalStateException e) {
            response = e.getMessage(); //"이미 존재하는 회원입니다."
          }
        }
        byte[] resBuf = response.getBytes();
        DatagramPacket resPacket = new DatagramPacket(resBuf, resBuf.length, packet.getAddress(),
            packet.getPort());
        socket.send(resPacket);
        System.out.println(response);
      }
    } catch (SocketException e) {
      e.printStackTrace();
    } catch (IOException e) { //receive
      e.printStackTrace();
    }
    System.out.println("스레드 종료");
  }

}
