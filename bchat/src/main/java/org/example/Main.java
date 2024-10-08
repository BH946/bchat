package org.example;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.example.v1.utils.TCPClient;
import org.example.v1.utils.TCPServer;
import org.example.v1.utils.UDPServer;

/**
 * GUI 없이 테스트 Main 함수 -> v1 패키지
 */
public class Main {
  public static void main(String[] args) throws IOException {
    int udpPort = 9876; int tcpPort = 9785;
    // 서버 스레드 시작 (무한 루프)
    Thread serverThread = new Thread(new UDPServer(udpPort));
//    serverThread.start();

    // main 스레드는 계속.. -> 아래 코드들 GUI 쪽으로 옮겨지게 될 거임. (아마)
//    udpRegisterTest(port);
//    udpLoginTest(port);
    // 로그인 했다면??
//    serverThread.interrupt(); //블로킹 상태일 위 스레드는 이제 종료 -> 종료 안 할거임.
//    try {
//      serverThread.join(); // 스레드 종료 대기
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }

    // 서버 스레드 시작 (무한 루프) -> userId 임의로.. title 임의로.. 테스트 중
    // 방장이라면??
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String input = br.readLine();
    if (input.equals("방장")) {
      System.out.println("방장임돠");
      serverThread = new Thread(new TCPServer(tcpPort, "제목", 47L));
      serverThread.start();
      Thread clientThread = new Thread(new TCPClient("localhost", tcpPort, 47L));
      clientThread.start();
    } else if(input.equals("일반")) {
      System.out.println("일반유저임돠");
      Thread clientThread = new Thread(new TCPClient("localhost", tcpPort, 48L));
      clientThread.start();
    }

  }


  /**
   * 로그인, 회원가입 Test code
   */
  //테스트용 데이터 삽입
//  public static String[] testData = {"test123", "1234", "admin", "관리자", "010-1234-5678", "admin@gmail.com"};
//  public static String[] testData1 = {"test1234", "1234", "admin1", "관리자", "010-1234-5678", "admin@gmail.com"};
//  public static String[] testData2 = {"test12345", "1234", "admin", "관리자", "010-1234-5678", "admin@gmail.com"};
//  public static String[] testDataL = {"test123", "1234"};
//  public static String[] testDataLL = {"abcd", "1111"};

//  public static void udpRegisterTest(int port) {
//    //정상
//    UDPClient udpClient = new UDPClient("localhost", port, testData);
//    System.out.println("회원가입 시도.");
//    udpClient.udpClientRegister();
//    System.out.println("회원가입 완료.");
//    //중복 id유도
//    udpClient = new UDPClient("localhost", port, testData);
//    System.out.println("회원가입 시도.");
//    udpClient.udpClientRegister();
//    System.out.println("회원가입 완료.");
//    //중복 닉네임유도
//    udpClient = new UDPClient("localhost", port, testData1);
//    System.out.println("회원가입 시도.");
//    udpClient.udpClientRegister();
//    System.out.println("회원가입 완료.");
//    //정상
//    udpClient = new UDPClient("localhost", port, testData2);
//    System.out.println("회원가입 시도.");
//    udpClient.udpClientRegister();
//    System.out.println("회원가입 완료.");
//  }
//
//  public static void udpLoginTest(int port) {
//    //정상
//    UDPClient udpClient = new UDPClient("localhost", port, testDataL);
//    System.out.println("로그인 시도.");
//    udpClient.udpClientLogin();
//    //실패
//    udpClient = new UDPClient("localhost", port, testDataLL);
//    System.out.println("로그인 시도.");
//    udpClient.udpClientLogin();
//  }
}