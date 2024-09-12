package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.example.domain.ChatRoom;
import org.example.domain.Message;
import org.example.service.ChatRoomService;
import org.example.service.MessageService;

public class TCPServer implements Runnable {
  private final int port;
  private final String title;
  private final Long userId;
  private static Long chatRoomId;
  private static final MessageService messageService = new MessageService();
  private static final List<ClientHandler> clientHandlers = new ArrayList<>();

  public TCPServer(int port, String title, Long userId) {
    this.port = port;
    this.title = title;
    this.userId = userId;
    //최초 db에 채팅방 생성
    ChatRoomService chatRoomService = new ChatRoomService();
    ChatRoom newChatRoom = chatRoomService.create(userId, title);
    chatRoomId = newChatRoom.getChatRoom_id();
  }

  // 이전대화내용
  public List<Message> findPrevMessages() {
    List<Message> messages = new ArrayList<>();
    messages = messageService.findAllByChat(userId, chatRoomId);
    System.out.println(messages); //debug
    return messages;
  }

  @Override
  public void run() {
    try{
      ServerSocket serverSocket = new ServerSocket(port);
      while (true) {
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

    public ClientHandler(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try {
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true); //stream사용, autoFlush:true
        String message;
        System.out.println("여기 오나?"); //debug
        while ((message = input.readLine()) != null) {
          //br.readLine()으로 매번 입력을 대기 + 이미 연결 성공했으니 이 부분만 무한반복(while)
          //이 내부를 전체 클라에게 메시지 주는것으로 바꿔야 함. -> db는 1번만 저장
          System.out.println("받은 메시지: " + message);
//          output.println("메시지 수신: " + message); //응답 메시지 전송
          // 모든 클라이언트에게 메시지 전송
          broadcastMessage(message);
          //매번 db 기록
          String[] data = message.split(",", 2); //[userId,content]
          messageService.create(chatRoomId, Long.parseLong(data[0]), data[1]);
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
          try{
            socket.close();
            clientHandlers.remove(this);
          } catch (IOException e) {
            e.printStackTrace();
          }
      }

    }
    private void broadcastMessage(String message) {
      for (ClientHandler clientHandler : clientHandlers) {
        clientHandler.output.println("메시지 수신: " + message); //응답 메시지 전송
      }
    }

  }

}
