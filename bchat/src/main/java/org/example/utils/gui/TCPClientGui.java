package org.example.utils.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.example.MainChatApp;

public class TCPClientGui implements Runnable{
  private final String serverAddress;
  private final int port;
  private final Long userId;
  public Socket socket;
  public PrintWriter out;
  public BufferedReader input;

  public TCPClientGui(String serverAddress, int port, Long userId) {
    this.serverAddress = serverAddress;
    this.port = port;
    this.userId = userId;
  }

  @Override
  public void run() {
    try {
      socket = new Socket(serverAddress, port);
      System.out.println("소켓 되냐?"+socket); //debug
      out = new PrintWriter(socket.getOutputStream(), true);
      input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      System.out.println("Stream 세팅 - 소켓(서버), 클라"); //debug
      String message;

      // (입장알림)최초 전송 -> userIdList 관리위해 초기 SET
      out.println(userId);
      System.out.println(userId+":최초 전송");

      // 응답
      while (true) {
        message = input.readLine();
        if (message.split(":")[0].equals("유저리스트 업데이트")) {
          List<Long> userIdList = dataToList(message);
          MainChatApp.UserListUp(userIdList);
          continue;
        }
//        else if (message.split(":")[1].equals(" 님이 퇴장하셨습니다.")) {
//          message = input.readLine();
//          List<Long> userIdList = dataToList(message);
//          MainChatApp.UserListUp(userIdList);
//        }
        MainChatApp.getChatArea().append(message + "\n"); // 채팅 영역에 메시지 추가
        System.out.println("응답 정상"); //debug
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public List<Long> dataToList(String message) {
    List<Long> dataList = new ArrayList<>();
    message = message.split(":")[1];
    if (message.contains(",")) {
      String[] datas = message.split(", ");
      for (String data : datas) {
        dataList.add(Long.parseLong(data)); // 각 부분을 Long으로 변환 후 리스트에 추가
      }
    } else {
      dataList.add(Long.parseLong(message));
    }
    return dataList;
  }

}
