package track.msgtest.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import track.msgtest.messenger.messages.LoginMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.messages.Type;


/**
 * Простейший протокол передачи данных
 */
public class StringProtocol implements Protocol {

    static Logger log = LoggerFactory.getLogger(StringProtocol.class);

    public static final String DELIMITER = ";";

    @Override
    public Message decode(byte[] bytes) throws ProtocolException {
        String str = new String(bytes);
        log.info("decoded: {}", str);
        String[] tokens = str.split(DELIMITER);
        Type type = Type.valueOf(tokens[0]);
        switch (type) {
            case MSG_TEXT:
                TextMessage textMsg = new TextMessage();
                textMsg.setSenderId(parseLong(tokens[1]));
                textMsg.setText(tokens[2]);
                textMsg.setType(type);
                return textMsg;
            case MSG_LOGIN:
                LoginMessage loginMsg = new LoginMessage();
                loginMsg.setName(tokens[1]);
                loginMsg.setPass(tokens[2]);
                return loginMsg;
            default:
                throw new ProtocolException("Invalid type: " + type);
        }
    }

    @Override
    public byte[] encode(Message msg) throws ProtocolException {
        StringBuilder builder = new StringBuilder();
        Type type = msg.getType();
        builder.append(type).append(DELIMITER);
        switch (type) {
            case MSG_TEXT:
                TextMessage sendMessage = (TextMessage) msg;
                builder.append(String.valueOf(sendMessage.getSenderId())).append(DELIMITER);
                builder.append(sendMessage.getText()).append(DELIMITER);
                break;
            case MSG_LOGIN:
                LoginMessage loginMsg = (LoginMessage) msg;
                builder.append(String.valueOf(loginMsg.getName())).append(DELIMITER);
                //System.out.println("SEE HERE!!!!" + builder.toString());
                builder.append(String.valueOf(loginMsg.getPass())).append(DELIMITER);
                //System.out.println("SEE HERE!!!!" + builder.toString());
                break;

            default:
                throw new ProtocolException("Invalid type: " + type);
        }
        log.info("encoded: {}", builder.toString());
        return builder.toString().getBytes();
    }

    private Long parseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            // who care
        }
        return null;
    }
}