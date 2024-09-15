package org.example.utils.gui;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import org.example.MainChatApp;
import org.example.domain.ChatRoom;
import org.example.domain.Message;
import org.example.service.ChatRoomService;
import org.example.service.MessageService;

public class TCPServerGui implements Runnable {
  private final int port;
  private final String title;
  private static Long userId; //방장
  private static Long chatRoomId;
  private static final MessageService messageService = new MessageService();
  private static final List<ClientHandler> clientHandlers = new ArrayList<>();
  public static final List<Long> userIdList = new ArrayList<>();
  public static List<Message> messages = new ArrayList<>();

  public TCPServerGui(int port, String title, Long userId) {
    this.port = port;
    this.title = title;
    this.userId = userId;
    //db에 최초 채팅방 생성
    ChatRoomService chatRoomService = new ChatRoomService();
    ChatRoom newChatRoom = chatRoomService.create(userId, title);
    chatRoomId = newChatRoom.getChatRoom_id();
  }
  
  /**
   * 채팅기록 업데이트 함수
   */
  public static void updatePrevMessages() {
    messages = messageService.findAllByChat(userId, chatRoomId);
  }

  @Override
  public void run() {
    try{
      ServerSocket serverSocket = new ServerSocket(port);
      while (!Thread.currentThread().isInterrupted()) {
        System.out.println("서버가 포트=" + port + " 에서 대기중");
        Socket socket = serverSocket.accept(); //clientSocket
        System.out.println("클라 연결 성공");

        ClientHandler clientHandler = new ClientHandler(socket); //new Thread
        clientHandlers.add(clientHandler);
        new Thread(clientHandler).start();
        }
      } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private Long userId;

    public ClientHandler(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true); //stream사용, autoFlush:true(바로 전송)
        String message;

        // 최초 수신+최초 응답
        message = input.readLine();
        this.userId=Long.parseLong(message);
        userIdList.add(this.userId);
        System.out.println(userId+":최초 수신");
        broadcastMessage(this.userId+",님이 입장했습니다."); //->TCPClientGui
        broadcastMessageUserList(userIdList); //->TCPClientGui (유저리스트 업데이트)
        System.out.println(userId+":최초 응답");
        
        while (true) {
          //br.readLine()으로 매번 입력을 대기 + 이미 연결 성공했으니 이 부분만 무한반복(while)
          //이 내부를 전체 클라에게 메시지 주는것으로 바꿔야 함. -> db는 1번만 저장
          message = input.readLine();
          System.out.println("받은 메시지: " + message); //debug
          if (message.equals("이전채팅기록요청")) {
            updatePrevMessages();
            messagePrevChat(messages);
            continue;
          }
          // 모든 클라이언트에게 메시지 전송
          broadcastMessage(message);
          //매번 db 기록
          String[] data = message.split(",", 2); //[userId,content]
          messageService.create(chatRoomId, Long.parseLong(data[0]), data[1]);
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          System.out.println("일반유저 채팅 종료시 이부분 수행");
          userIdList.remove(userId);
          broadcastMessage(this.userId+",님이 퇴장하셨습니다."); //->TCPClientGui
          broadcastMessageUserList(userIdList); //->TCPClientGui (유저리스트 업데이트)

          socket.close();
          clientHandlers.remove(this);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    }

    /**
     * 메시지 전송 함수 - 기본, 유저조회, 채팅조회(GSON 활용)
     */
    private void broadcastMessage(String message) {
      String[] messages = message.split(",",2);
      for (ClientHandler clientHandler : clientHandlers) {
        clientHandler.output.println(messages[0]+": "+messages[1]); //응답 메시지 전송
      }
    }
    private void broadcastMessageUserList(List<Long> messages) {
      // List<Long>를 문자열로 변환
      StringBuilder sb = new StringBuilder();
      for (Long message : messages) {
        sb.append(message).append(", "); // 각 Long을 문자열로 변환
      }

      // 마지막 쉼표 및 공백 제거
      if (sb.length() > 0) {
        sb.setLength(sb.length() - 2);
      }

      // 클라이언트에게 메시지 전송
      for (ClientHandler clientHandler : clientHandlers) {
        clientHandler.output.println("유저리스트 업데이트:"+sb);
      }
    }
    private void messagePrevChat(List<Message> messages) {
      Gson gson = new Gson();
      StringBuilder sb = new StringBuilder();
      sb.append("["); //배열 시작(JSON)

      for (int i = 0; i < messages.size(); i++) {
        Message msg = messages.get(i);
        String jsonMsg = gson.toJson(msg);
        sb.append(jsonMsg);
        // 마지막 메시지가 아닐 경우 쉼표 추가 -> 빈 공백 데이터 끝에 방지
        if (i < messages.size() - 1) {
          sb.append(", "); //Json 형태 쉼표
        }
      }

      sb.append("]"); //배열 끝(JSON)
      // 클라이언트에게 메시지 전송
      this.output.println("이전채팅기록요청:"+sb);

      System.out.println(sb); //debug
    }
  }

}
