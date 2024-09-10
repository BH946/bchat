package org.example;


import org.example.service.UserService;

public class Main {
  //테스트용 데이터 삽입
  public static String[] testData = {"test123", "1234", "admin", "관리자", "010-1234-5678", "admin@gmail.com"};
  public static String[] testData1 = {"test1234", "1234", "admin1", "관리자", "010-1234-5678", "admin@gmail.com"};
  public static String[] testData2 = {"test12345", "1234", "admin", "관리자", "010-1234-5678", "admin@gmail.com"};
  public static String[] testDataL = {"test123", "1234"};
  public static String[] testDataLL = {"abcd", "1111"};

  public static void main(String[] args) {
    // 서버 스레드 시작 (무한 루프)
    int port = 9876;
    Thread serverThread = new Thread(new UDPServer(port));
    serverThread.start();

    // main 스레드는 계속..
//    udpRegisterTest(port);
    udpLoginTest(port);
  }

  public static void udpRegisterTest(int port) {
    //정상
    UDPClient udpClient = new UDPClient("localhost", port, testData);
    System.out.println("회원가입 시도.");
    udpClient.udpClientRegister();
    System.out.println("회원가입 완료.");
    //중복 id유도
    udpClient = new UDPClient("localhost", port, testData);
    System.out.println("회원가입 시도.");
    udpClient.udpClientRegister();
    System.out.println("회원가입 완료.");
    //중복 닉네임유도
    udpClient = new UDPClient("localhost", port, testData1);
    System.out.println("회원가입 시도.");
    udpClient.udpClientRegister();
    System.out.println("회원가입 완료.");
    //정상
    udpClient = new UDPClient("localhost", port, testData2);
    System.out.println("회원가입 시도.");
    udpClient.udpClientRegister();
    System.out.println("회원가입 완료.");
  }

  public static void udpLoginTest(int port) {
    //정상
    UDPClient udpClient = new UDPClient("localhost", port, testDataL);
    System.out.println("로그인 시도.");
    udpClient.udpClientLogin();
    //실패
    udpClient = new UDPClient("localhost", port, testDataLL);
    System.out.println("로그인 시도.");
    udpClient.udpClientLogin();
  }
}