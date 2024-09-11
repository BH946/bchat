package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable {
  private final int port;

  public TCPServer(int port) {
    this.port = port;
  }

  @Override
  public void run() {
    try{
      ServerSocket serverSocket = new ServerSocket(port);
      System.out.println("서버가 포트="+port+" 에서 대기중");
      //clientSocket
      try(Socket socket = serverSocket.accept()){
        System.out.println("클라 연결 성공");
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true); //stream사용, autoFlush:true
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String message;
        while ((message = br.readLine()) != null) {
          System.out.println("받은 메시지: "+message);
          out.println("메시지 수신: " + message); //응답 메시지 전송
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
