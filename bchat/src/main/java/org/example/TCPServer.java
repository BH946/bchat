package org.example;

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
  private final Long chatRoomId;
  private final MessageService messageService = new MessageService();

  public TCPServer(int port, String title, Long userId) {
    this.port = port;
    this.title = title;
    this.userId = userId;
    //최초 db에 채팅방 생성
    ChatRoomService chatRoomService = new ChatRoomService();
    ChatRoom newChatRoom = chatRoomService.create(userId, title);
    this.chatRoomId = newChatRoom.getChatRoom_id();
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
          //매번 db 기록
          String[] data = message.split(",",2); //[userId,content]
          messageService.create(this.chatRoomId, Long.parseLong(data[0]), data[1]);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
