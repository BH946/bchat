package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class UDPClient {
  private final String serverAddress;
  private final int serverPort;
  private String[] data;

  public UDPClient(String serverAddress, int serverPort, String[] data) {
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;
    this.data = data;
  }

  /**
   * 회원가입
   */
  public void udpClientRegister() {
    try {
      //전송
      DatagramSocket socket = new DatagramSocket();
      String str = data[0]+","+data[1]+","+data[2]+","+data[3]+","+data[4]+","+data[5];
      byte[] buffer = str.getBytes();
      System.out.println(str);
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverAddress), serverPort);
      socket.send(packet);

      //응답
      byte[] resBuf = new byte[512];
      DatagramPacket resPacket = new DatagramPacket(resBuf, resBuf.length);
      socket.receive(resPacket); //대기
      String response = new String(resPacket.getData(),0, resPacket.getLength());
      System.out.println(response); //debug

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
      String str = data[0]+","+data[1];
      byte[] buffer = str.getBytes();
      System.out.println(str);
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverAddress), serverPort);
      socket.send(packet);

      //응답
      byte[] resBuf = new byte[512];
      DatagramPacket resPacket = new DatagramPacket(resBuf, resBuf.length);
      socket.receive(resPacket); //대기
      String response = new String(resPacket.getData(),0, resPacket.getLength());
      System.out.println(response); //debug

    } catch (SocketException e) {
      e.printStackTrace();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
