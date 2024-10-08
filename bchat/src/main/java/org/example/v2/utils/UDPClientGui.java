package org.example.v2.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClientGui {

  private final String serverAddress;
  private final int serverPort;
  private String[] data;
  private String response; //예상 답변: "아이디 또는 비밀번호가 잘못되었습니다.", "회원가입을 완료 했습니다.", "로그인에 성공하셨습니다.", "이미 존재하는 회원입니다."

  public UDPClientGui(String serverAddress, int serverPort, String[] data) {
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;
    this.data = data;
  }

  public String getResponse() {
    return response;
  }

  /**
   * 회원가입
   */
  public void udpClientRegister() {
    try {
      //전송
      DatagramSocket socket = new DatagramSocket();
      String input =
          data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + data[5];
      byte[] buffer = input.getBytes();
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
          InetAddress.getByName(serverAddress), serverPort);
      socket.send(packet);

      //응답
      byte[] resBuf = new byte[512];
      DatagramPacket resPacket = new DatagramPacket(resBuf, resBuf.length);
      socket.receive(resPacket); //대기
      response = new String(resPacket.getData(), 0, resPacket.getLength());
      System.out.println(response); //debug

      socket.close();
    } catch (SocketException e) {
      e.printStackTrace();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 로그인
   */
  public void udpClientLogin() {
    try {
      //전송
      DatagramSocket socket = new DatagramSocket();
      String input = data[0] + "," + data[1];
      byte[] buffer = input.getBytes();
      System.out.println(input);
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
          InetAddress.getByName(serverAddress), serverPort);
      socket.send(packet);

      //응답
      byte[] resBuf = new byte[512];
      DatagramPacket resPacket = new DatagramPacket(resBuf, resBuf.length);
      socket.receive(resPacket); //대기
      response = new String(resPacket.getData(), 0, resPacket.getLength());
      System.out.println("로그인 응답: " + response); //debug

      socket.close();
    } catch (SocketException e) {
      e.printStackTrace();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
