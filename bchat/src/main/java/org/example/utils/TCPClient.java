package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TCPClient {
  private final String serverAddress;
  private final int port;
  private final Long userId;
  private Socket socket;

  public TCPClient(String serverAddress, int port, Long userId) {
    this.serverAddress = serverAddress;
    this.port = port;
    this.userId = userId;
  }

  public void start() {
    System.out.println("함수 실행으 ㄴ하겠지"); //debug
    try {
      socket = new Socket(serverAddress, port);
      System.out.println("소켓 되긴 하냐?"+socket); //debug
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      System.out.println("여기까지 오냐1"); //debug
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("여기까지 오냐2"); //debug

      String inputLine;
      while ((inputLine = input.readLine()) != null) {
        System.out.println("여기까지 오냐3"); //debug
        out.println(userId+","+inputLine); //서버 전송 (content+userId)
        System.out.println("여기까지 오냐4"); //debug
        System.out.println("서버 응답: "+br.readLine()); //서버 응답 수신
        System.out.println("여기까지 오냐5"); //debug => 여기부터 멈췄네
      }
      System.out.println("여기까지 오냐6"); //debug
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
