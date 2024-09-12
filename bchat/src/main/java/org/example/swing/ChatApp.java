package org.example.swing;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatApp {
  private JFrame frame;
  private CardLayout cardLayout;

  public ChatApp() {
    frame = new JFrame("Chat Application");
    cardLayout = new CardLayout();
    frame.setLayout(cardLayout);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 300);

    // 로그인 화면
    JPanel loginPanel = createLoginPanel();

    // 회원 가입 화면
    JPanel registerPanel = createRegisterPanel();

    // 채팅방 선택 화면
    JPanel homePanel = createHomePanel();

    // 메시지 창
    JPanel messagerPanel = createMessagerPanel();

    // 각 화면을 카드 레이아웃에 추가
    frame.add(loginPanel, "login");
    frame.add(registerPanel, "register");
    frame.add(homePanel, "home");
    frame.add(messagerPanel, "messager");

    // 기본 화면 설정
    frame.setVisible(true);
    cardLayout.show(frame.getContentPane(), "login");
  }

  private JPanel createLoginPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 여백 추가

    JLabel titleLabel = new JLabel("Login");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // 제목 스타일
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬
    panel.add(titleLabel);

    panel.add(Box.createRigidArea(new Dimension(0, 20))); // 간격 추가

    JTextField idField = new JTextField(20);
    JPasswordField pwField = new JPasswordField(20);
    JButton loginButton = new JButton("로그인");
    JButton registerButton = new JButton("회원가입");

    idField.setMaximumSize(idField.getPreferredSize()); // 필드 크기 조정
    pwField.setMaximumSize(pwField.getPreferredSize());

    panel.add(new JLabel("ID:"));
    panel.add(idField);
    panel.add(Box.createRigidArea(new Dimension(0, 10))); // 간격 추가
    panel.add(new JLabel("PW:"));
    panel.add(pwField);
    panel.add(Box.createRigidArea(new Dimension(0, 20))); // 간격 추가

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(loginButton);
    buttonPanel.add(registerButton);
    panel.add(buttonPanel);

    loginButton.addActionListener(e -> {
      // 로그인 처리 로직 추가
      cardLayout.show(frame.getContentPane(), "home");
    });

    registerButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "register"));

    return panel;
  }

  private JPanel createRegisterPanel() {
    JPanel panel = new JPanel(new GridLayout(7, 2));
    JTextField regIdField = new JTextField();
    JPasswordField regPwField = new JPasswordField();
    JTextField nicknameField = new JTextField();
    JTextField nameField = new JTextField();
    JTextField phoneField = new JTextField();
    JTextField emailField = new JTextField();
    JButton confirmButton = new JButton("확인");
    JButton cancelButton = new JButton("취소");

    panel.add(new JLabel("ID:"));
    panel.add(regIdField);
    panel.add(new JLabel("PW:"));
    panel.add(regPwField);
    panel.add(new JLabel("닉네임:"));
    panel.add(nicknameField);
    panel.add(new JLabel("이름:"));
    panel.add(nameField);
    panel.add(new JLabel("전화번호:"));
    panel.add(phoneField);
    panel.add(new JLabel("이메일:"));
    panel.add(emailField);
    panel.add(confirmButton);
    panel.add(cancelButton);

    confirmButton.addActionListener(e -> {
      // 회원가입 처리 로직 추가
      cardLayout.show(frame.getContentPane(), "login");
    });

    cancelButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "login"));

    return panel;
  }

  private JPanel createHomePanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 여백 추가
    panel.setBackground(new Color(230, 230, 250)); // 연한 보라색 배경

    JLabel titleLabel = new JLabel("채팅방 선택");
    titleLabel.setFont(new Font("맑은고딕", Font.BOLD, 24));
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(titleLabel);

    panel.add(Box.createRigidArea(new Dimension(0, 20))); // 간격 추가

    JButton createRoomButton = new JButton("채팅방 생성");
    JButton joinRoomButton = new JButton("채팅방 입장");

    // 버튼 스타일 설정
    createRoomButton.setFont(new Font("맑은고딕", Font.PLAIN, 18));
    joinRoomButton.setFont(new Font("맑은고딕", Font.PLAIN, 18));

    createRoomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    joinRoomButton.setAlignmentX(Component.CENTER_ALIGNMENT);

    panel.add(createRoomButton);
    panel.add(Box.createRigidArea(new Dimension(0, 10))); // 간격 추가
    panel.add(joinRoomButton);

    panel.add(Box.createRigidArea(new Dimension(0, 20))); // 간격 추가

    // 버튼 클릭 이벤트 처리
    createRoomButton.addActionListener(e -> {
      // 채팅방 생성 입력 화면
      createRoom();
    });

    joinRoomButton.addActionListener(e -> {
      // 채팅방 입장 입력 화면
      joinRoom();
    });

    return panel;
  }


  private void createRoom() {
    JDialog createRoomDialog = new JDialog(frame, "채팅방 생성", true);
    createRoomDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    createRoomDialog.setSize(300, 150);
    createRoomDialog.setLocationRelativeTo(frame);

    JPanel createRoomPanel = new JPanel(new GridLayout(3, 2));
    JTextField titleField = new JTextField();
    JTextField portField = new JTextField();
    JButton confirmButton = new JButton("확인");
    JButton cancelButton = new JButton("취소");

    createRoomPanel.add(new JLabel("제목:"));
    createRoomPanel.add(titleField);
    createRoomPanel.add(new JLabel("Port:"));
    createRoomPanel.add(portField);
    createRoomPanel.add(confirmButton);
    createRoomPanel.add(cancelButton);

    confirmButton.addActionListener(e -> {
      // 채팅방 생성 로직 추가
      cardLayout.show(frame.getContentPane(), "messager");
      createRoomDialog.dispose(); // createRoom 창 닫기
    });

    cancelButton.addActionListener(e -> {
      cardLayout.show(frame.getContentPane(), "home");
      createRoomDialog.dispose(); // createRoom 창 닫기
    });

    createRoomDialog.add(createRoomPanel);
    createRoomDialog.setVisible(true); // 다이얼로그 표시
  }


  private void joinRoom() {
    JDialog joinRoomDialog = new JDialog(frame, "채팅방 입장", true);
    joinRoomDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    joinRoomDialog.setSize(300, 150);
    joinRoomDialog.setLocationRelativeTo(frame);

    JPanel joinRoomPanel = new JPanel(new GridLayout(3, 2));
    JTextField ipField = new JTextField();
    JTextField portField = new JTextField();
    JButton confirmButton = new JButton("확인");
    JButton cancelButton = new JButton("취소");

    joinRoomPanel.add(new JLabel("IP:"));
    joinRoomPanel.add(ipField);
    joinRoomPanel.add(new JLabel("Port:"));
    joinRoomPanel.add(portField);
    joinRoomPanel.add(confirmButton);
    joinRoomPanel.add(cancelButton);

    confirmButton.addActionListener(e -> {
      // 채팅방 입장 로직 추가
      cardLayout.show(frame.getContentPane(), "messager");
      joinRoomDialog.dispose(); // joinRoom 창 닫기
    });

    cancelButton.addActionListener(e -> {
      cardLayout.show(frame.getContentPane(), "home");
      joinRoomDialog.dispose(); // joinRoom 창 닫기
    });

    joinRoomDialog.add(joinRoomPanel);
    joinRoomDialog.setVisible(true); // 다이얼로그 표시
  }


  private JPanel createMessagerPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(new Color(240, 240, 255));
    JTextArea chatArea = new JTextArea();
    chatArea.setEditable(false);
    chatArea.setLineWrap(true);
    chatArea.setWrapStyleWord(true);
    JTextField messageField = new JTextField();
    JTextArea userArea = new JTextArea(5, 10);
    userArea.setEditable(false);
    userArea.setText("접속한 유저:\n");

    panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
    panel.add(userArea, BorderLayout.EAST);

    // 채팅 입력 필드와 버튼을 포함할 패널 생성
    JPanel inputPanel = new JPanel(new BorderLayout());
    inputPanel.add(messageField, BorderLayout.CENTER);

    // "채팅기록" 버튼 생성
    JButton chatHistoryButton = new JButton("채팅기록");
    inputPanel.add(chatHistoryButton, BorderLayout.EAST); // 버튼을 오른쪽에 추가

    panel.add(inputPanel, BorderLayout.SOUTH); // 입력 패널을 하단에 추가

    messageField.addActionListener(e -> {
      String message = messageField.getText();
      chatArea.append("나: " + message + "\n");
      messageField.setText("");
      // 서버로 메시지 전송 로직 추가
    });

    chatHistoryButton.addActionListener(e -> {
      // 채팅 기록 보여주는 로직 추가 (예: JOptionPane 사용)
      JOptionPane.showMessageDialog(panel, chatArea.getText(), "채팅 기록", JOptionPane.PLAIN_MESSAGE);
    });

    return panel;
  }



  public static void main(String[] args) {
    SwingUtilities.invokeLater(ChatApp::new);
  }
}