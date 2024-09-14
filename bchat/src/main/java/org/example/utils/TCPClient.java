package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TCPClient implements Runnable{
  private final String serverAddress;
  private final int port;
  private final Long userId;
  private Socket socket;

  public TCPClient(String serverAddress, int port, Long userId) {
    this.serverAddress = serverAddress;
    this.port = port;
    this.userId = userId;
  }

  @Override
  public void run() {
    try {
      socket = new Socket(serverAddress, port);
      System.out.println("소켓 되냐?"+socket); //debug
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Stream 세팅 - 소켓(서버), 클라"); //debug

      while (true) {
        //br.readLine(), input.readLine()으로 매번 입력을 대기 + 이미 연결 성공했으니 이 부분만 무한반복(while)
        out.println(userId+","+input.readLine()); //서버 전송 (content+userId)
        System.out.println("서버 응답: "+br.readLine()); //서버 응답 수신
        System.out.println("응답 정상"); //debug
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
