package track.msgtest.messenger.net;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;

import java.lang.Object;

public class Worker implements Runnable {
    private Socket clientSocket = null;
    private boolean isWorking = true;
    private ArrayList<User> userTable = null;
    private Map<Long, OutputStream> usersOnline = null;
    private boolean isAuthorized = false;
    private StringProtocol protocol;
    private String userName;

    public Worker(Socket clientSocket, ArrayList<User> table, Map<Long, OutputStream> base) {
        System.out.println("Started new worker!");
        this.clientSocket = clientSocket;
        this.userTable = table;
        this.usersOnline = base;
        this.protocol = new StringProtocol();
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
        while (isWorking) {
            try {
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();
                //ObjectInputStream in = new ObjectInputStream(input);
                //ObjectOutputStream out = new ObjectOutputStream(output);
                int read = input.read(buf);
                if (!isAuthorized) {
                    LoginMessage msg = new LoginMessage();
                    try {
                        if (read > 0) {
                            msg = (LoginMessage) protocol.decode(Arrays.copyOf(buf, read ));
                        }
                    } catch (ProtocolException exc) {
                        System.out.println("Invalid login");
                        sendMessageToUser("Invalid login,try again", output);
                    }
                    for (User user : userTable) {
                        if ((!isAuthorized) && user.checkName(msg.getName()) && user.checkPassword(msg.getPass())) {
                            isAuthorized = true;
                            userName = msg.getName();
                            usersOnline.put(user.getId(), output);
                            read = 0;
                            sendMessageToUser("SUCCESS,YOU ADDED TO CHAT", output);
                            sendMessageToChat("<Server>" + userName + " connected to chat", usersOnline);
                        }
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
