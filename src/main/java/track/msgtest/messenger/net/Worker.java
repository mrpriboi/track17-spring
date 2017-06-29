package track.msgtest.messenger.net;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.*;

import javax.xml.transform.Result;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;

import java.lang.Object;

public class Worker implements Runnable {
    private Socket clientSocket = null;
    private boolean isWorking = true;
    private Connection connection = null;
    private Map<Long, OutputStream> usersOnline = null;
    private boolean isAuthorized = false;
    private Protocol protocol;
    private String userName;

    public Worker(Socket clientSocket, Connection connection, Map<Long, OutputStream> base) {
        System.out.println("Started new worker!");
        this.clientSocket = clientSocket;
        this.connection = connection;
        this.usersOnline = base;
        this.protocol = new BinaryProtocol();
    }

    public void sendMessageToUser(String text, OutputStream address) {
        try {
            TextMessage mesg = new TextMessage();
            mesg.setType(Type.MSG_TEXT);
            mesg.setText(text);
            address.write(protocol.encode(mesg));
            address.flush();
        } catch (ProtocolException | IOException e) {
            System.out.println("Error during sending message <" + text + "> to user " + userName);
        }
    }

    public void sendMessageToChat(String text, Map<Long, OutputStream> base) {
        if (!base.isEmpty()) {
            try {
                TextMessage mesg = new TextMessage();
                mesg.setType(Type.MSG_TEXT);
                mesg.setText(text);
                for (Map.Entry<Long, OutputStream> entry : base.entrySet()) {
                    entry.getValue().write(protocol.encode(mesg));
                    entry.getValue().flush();
                }
            } catch (ProtocolException | IOException e) {
                System.out.println("Error during sending message<" + text + ">");
            }
        } else {
            System.out.println("Chat is empty");
        }
    }

    @Override
    public void run() {
        byte[] buf = new byte[1024 * 64];
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connectionx = DriverManager.getConnection("jdbc:mysql://localhost:3306/Messenger", "root", "Mysql97");
            while (isWorking) {
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();
                //ObjectInputStream in = new ObjectInputStream(input);
                //ObjectOutputStream out = new ObjectOutputStream(output);
                int read = input.read(buf);
                if (!isAuthorized) {
                    Message msg = null;
                    if (read > 0) {
                        msg = (Message) protocol.decode(Arrays.copyOf(buf, read));
                    }
                    switch (msg.getType()) {
                        case MSG_REGISTRATION:
                            System.out.println("I am registering!");
                            RegisterMessage regMsg = (RegisterMessage) msg;
                            Statement st = connectionx.createStatement();
                            String str = "INSERT INTO users (login, password) VALUES ('" + regMsg.getName() + "', '" + regMsg.getPass() + "');";
                            System.out.println(str);
                            st.executeUpdate(str);
                            str = "SELECT id,login,password FROM users WHERE login='" + regMsg.getName() + "' AND password='" + regMsg.getPass() + "';";
                            ResultSet result = st.executeQuery(str);
                            if (result.next() && result.getString("login").equals(regMsg.getName()) && result.getString("password").equals(regMsg.getPass())){
                                sendMessageToUser("SUCCESS,YOU'VE REGISTERED NEW USER WITH LOGIN:" + regMsg.getName() + " AND PASSWORD:" + regMsg.getPass(), output);
                            } else {
                                sendMessageToUser("ERROR:(", output);
                            }
                            break;
                        case MSG_LOGIN:
                            LoginMessage loginMsg = (LoginMessage) msg;
                            Statement lgn = connectionx.createStatement();
                            String sql = "SELECT id,login,password FROM users WHERE login='" + loginMsg.getName() + "' AND password='" + loginMsg.getPass() + "';";
                            System.out.println(sql);
                            ResultSet data = lgn.executeQuery(sql);
                            if ( data.next() && (!isAuthorized) && data.getString("login").equals(loginMsg.getName()) && data.getString("password").equals(loginMsg.getPass())) {
                                isAuthorized = true;
                                userName = loginMsg.getName();
                                usersOnline.put(data.getLong("id"), output);
                                read = 0;
                                sendMessageToUser("SUCCESS,YOU ADDED TO CHAT", output);
                                sendMessageToChat("<Server>" + userName + " connected to chat", usersOnline);
                            }
                            break;
                        default:
                            sendMessageToUser("Invalid input, try again", output);
                            break;
                    }
                } else if (read > 0) {
                    TextMessage textMsg = new TextMessage();
                    try {
                        textMsg = (TextMessage) protocol.decode(Arrays.copyOf(buf, read));
                        sendMessageToChat("<" + userName + ">" + textMsg.getText(), usersOnline);
                    } catch (ProtocolException exc) {
                        System.out.println("Invalid message");
                        sendMessageToUser("Invalid message,error", output);
                    }
                }
                //String line = in.readUTF(); // ожидаем пока клиент пришлет строку текста.
                //System.out.println("The dumb client just sent me this line : " + line);
                //System.out.println("I'm sending it back...");
                //out.writeUTF(line); // отсылаем клиенту обратно ту самую строку текста.
                //out.flush(); // заставляем поток закончить передачу данных.
                //System.out.println("Waiting for the next line...");
                //System.out.println();
            }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException exc) {
                // ok
            } catch (ClassNotFoundException ex) {
                // ok :(
                ex.getMessage();
            } catch (ProtocolException exc) {
            System.out.println("Invalid login");
            }

    }
}
