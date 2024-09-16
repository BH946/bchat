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

public class TCPClientGui implements Runnable {

  private final String serverAddress;
  private final int port;
  private final Long userId;
  private PrintWriter output; //외부에서 사용 (MainChatApp)
  private volatile boolean blocking = true;

  public boolean isBlocking() {
    return blocking;
  }

  public TCPClientGui(String serverAddress, int port, Long userId) {
    this.serverAddress = serverAddress;
    this.port = port;
    this.userId = userId;
  }

  public PrintWriter getOutput() {
    return output;
  }

  @Override
  public void run() {
    try (Socket socket = new Socket(serverAddress, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader input = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));) {
      this.output = out;
      this.blocking = false; //블로킹 해제
      System.out.println("소켓: " + socket); //debug
      System.out.println("Stream 세팅 - 소켓(서버), 클라"); //debug
      String message;

      // 수신 -> 요청은 MainChatApp 담당
      while (true) {
        message = input.readLine();
        addResponseChat(message);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 서버로부터 응답받았을 때 수행 함수
   */
  private void addResponseChat(String message) {
    if (message.split(":")[0].equals(ConstanctMsg.USER_LIST_FLAG)) {
      List<Long> userIdList = dataToUserList(message);
      MainChatApp.UserListUp(userIdList);
    } else if (message.split(":")[0].equals(ConstanctMsg.PREV_CHAT_FLAG)) {
      List<Message> prevChatList = dataToPrevChat(message);
      MainChatApp.PrevChatUp(prevChatList);
    } else { //위 2가지 외: 일반 메시지를 의미
      MainChatApp.getChatArea().append(message + "\n"); // 채팅 영역에 메시지 추가
      System.out.println("응답 정상"); //debug
    }
  }

  /**
   * data 가공 함수 - 유저리스트, 채팅기록(Gson 활용)
   */
  private List<Long> dataToUserList(String message) {
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

  private List<Message> dataToPrevChat(String message) {
    message = message.split(":", 2)[1];
    Gson gson = new Gson();
    Type messageListType = new TypeToken<List<Message>>() {
    }.getType();
    return gson.fromJson(message, messageListType);
  }
}
