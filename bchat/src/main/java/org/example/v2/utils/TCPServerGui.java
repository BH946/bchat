package org.example.v2.utils;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.v2.SqlSessionTemplate;
import org.example.v2.domain.ChatRoom;
import org.example.v2.domain.Message;
import org.example.v2.repository.ChatRoomRepository;
import org.example.v2.repository.MessageRepository;
import org.example.v2.service.ChatRoomService;
import org.example.v2.service.MessageService;

public class TCPServerGui implements Runnable {

  private final int port;
  private static Long chatRoomId;
  private static final List<ClientHandler> clientHandlers = new ArrayList<>();
  public static final List<Long> userIdList = new ArrayList<>();
  public static List<Message> messages = new ArrayList<>();
  private final ChatRoomService chatRoomService;
  private static MessageService messageService;

  public TCPServerGui(int port, String title, Long userId) {
    SqlSessionFactory sqlSessionFactory = SqlSessionTemplate.getSqlSessionFactory();
    ChatRoomRepository chatRoomRepository = new ChatRoomRepository(sqlSessionFactory);
    MessageRepository messageRepository = new MessageRepository(sqlSessionFactory);
    this.chatRoomService = new ChatRoomService(chatRoomRepository);
    this.messageService = new MessageService(messageRepository);
    this.port = port;
    //db에 최초 채팅방 생성
    ChatRoom chatRoom = ChatRoom.createChatRoom(title, userId);
    chatRoomService.create(chatRoom);
    chatRoom = chatRoomService.findByIdNTitle(userId, title);
    this.chatRoomId = chatRoom.getChatRoomId();
  }

  @Override
  public void run() {
    try (ServerSocket serverSocket = new ServerSocket(port);) {
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

        setupStreams();
        //수신 + 요청(응답)
        handleMessages();

      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        System.out.println("일반유저 채팅 종료시 이부분 수행");
        closeConnection();
      }

    }

    private void setupStreams() throws IOException {
      input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      output = new PrintWriter(socket.getOutputStream(), true); //stream사용, autoFlush:true(바로 전송)
    }

    private void handleMessages() throws IOException {
      String message;
      while ((message = input.readLine()) != null) {
        //br.readLine()으로 매번 입력을 대기 + 이미 연결 성공했으니 이 부분만 무한반복(while)
        //이 내부를 전체 클라에게 메시지 주는것으로 바꿔야 함. -> db는 1번만 저장
        addResponseChat(message);
      }
    }

    /**
     * 클라로부터 요청 받았을 때 응답 함수
     */
    private void addResponseChat(String message) {
      System.out.println("받은 메시지: " + message); //debug
      if (message.equals(ConstanctMsg.PREV_CHAT_FLAG)) {
        messages = messageService.findAllByChatId(chatRoomId);
        messagePrevChat(messages);
      } else if (message.split(":")[0].equals(ConstanctMsg.USER_LIST_FLAG)) {
        //최초 전송 + 최초 수신 부분임 -> "입장 때 유저리스트 업뎃 할거라서"
        handleUserListUpdate(message);
      } else { //위 2가지 외: 일반 메시지를 의미
        broadcastMessage(message);
        saveMessage(message); //매번 db 기록 -> "일반 메시지만"
      }
    }

    private void handleUserListUpdate(String message) {
      this.userId = Long.parseLong(message.split(":")[1]);
      userIdList.add(this.userId);
      System.out.println(userId + ": 최초 수신");
      broadcastMessage(this.userId + ",님이 입장했습니다."); //->TCPClientGui
      broadcastMessageUserList(userIdList); //->TCPClientGui (유저리스트 업데이트)
    }

    private void saveMessage(String message) {
      String[] data = message.split(",", 2); //[userId,content]
      Message msg = Message.createMessage(chatRoomId, Long.parseLong(data[0]), data[1]);
      messageService.create(msg);
    }


    /**
     * 메시지 전송 함수 - 기본, 유저조회(GSON 미사용), 채팅조회(GSON 사용)
     */
    private void broadcastMessage(String message) {
      String[] messages = message.split(",", 2);
      for (ClientHandler clientHandler : clientHandlers) {
        clientHandler.output.println(messages[0] + ": " + messages[1]); //응답 메시지 전송
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
        clientHandler.output.println(ConstanctMsg.USER_LIST_FLAG + ":" + sb);
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
      this.output.println(ConstanctMsg.PREV_CHAT_FLAG + ":" + sb);

      System.out.println(sb); //debug
    }

    /**
     * 사용자 퇴장 부분 + 자원 반환 함수
     */
    private void closeConnection() {
      try {
        userIdList.remove(userId);
        broadcastMessage(this.userId + ",님이 퇴장하셨습니다."); //->TCPClientGui
        broadcastMessageUserList(userIdList); //->TCPClientGui (유저리스트 업데이트)
        if (socket != null) {
          socket.close();
        }
        if (input != null) {
          input.close();
        }
        if (output != null) {
          output.close();
        }
        clientHandlers.remove(this);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
