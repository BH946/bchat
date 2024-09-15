package org.example.v2.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.example.MainChatApp;
import org.example.v2.domain.Message;

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
          List<Long> userIdList = dataToUserList(message);
          MainChatApp.UserListUp(userIdList);
          continue;
        } else if (message.split(":")[0].equals("이전채팅기록요청")) {
          List<Message> prevChatList = dataToPrevChat(message);
          MainChatApp.PrevChatUp(prevChatList);
//          MainChatApp.PrevChatUp(message.split(":")[1]);
          continue;
        }
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

  /**
   * data 가공 함수 - 유저리스트, 채팅기록(Gson 활용)
   */
  public List<Long> dataToUserList(String message) {
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
  public List<Message> dataToPrevChat(String message) {
    List<Message> dataList = new ArrayList<>();
    message = message.split(":",2)[1];
    Gson gson = new Gson();
    Type messageListType = new TypeToken<List<Message>>(){}.getType();
    return gson.fromJson(message, messageListType);
  }
}
