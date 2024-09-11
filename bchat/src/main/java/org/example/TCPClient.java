package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {
  private final String serverAddress;
  private final int port;

  public TCPClient(String serverAddress, int port) {
    this.serverAddress = serverAddress;
    this.port = port;
  }

  public void start() {
    try (Socket socket = new Socket(serverAddress, port)) {
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

      String inputLine;
      while ((inputLine = input.readLine()) != null) {
        out.println(inputLine); //서버 전송
        System.out.println("서버 응답: "+br.readLine()); //서버 응답 수신
      }
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
