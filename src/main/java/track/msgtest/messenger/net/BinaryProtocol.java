package track.msgtest.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.SerializationUtils;

import track.msgtest.messenger.messages.*;


/**
 * Простейший протокол передачи данных
 */
public class BinaryProtocol implements Protocol {

    static Logger log = LoggerFactory.getLogger(BinaryProtocol.class);

    public static final String DELIMITER = ";";

    @Override
    public Message decode(byte[] bytes) throws ProtocolException {
        Message msg = (Message) SerializationUtils.deserialize(bytes);
        log.info("decoded: {}", msg.toString());
        Type type = msg.getType();
        switch (type) {
            case MSG_TEXT:
                TextMessage textMsg = (TextMessage) msg;
                return textMsg;
            case MSG_LOGIN:
                LoginMessage loginMsg = (LoginMessage) msg;
                return loginMsg;
            case MSG_REGISTRATION:
                RegisterMessage regMsg = (RegisterMessage) msg;
                return regMsg;
            default:
                throw new ProtocolException("Invalid type: " + type);
        }
    }

    @Override
    public byte[] encode(Message msg) throws ProtocolException {
        Type type = msg.getType();
        byte[] data = new byte[1024 * 64];
        switch (type) {
            case MSG_REGISTRATION:
                RegisterMessage regMsg = (RegisterMessage) msg;
                data = SerializationUtils.serialize(regMsg);
                break;
            case MSG_TEXT:
                TextMessage sendMessage = (TextMessage) msg;
                data = SerializationUtils.serialize(sendMessage);
                break;
            case MSG_LOGIN:
                LoginMessage loginMsg = (LoginMessage) msg;
                data = SerializationUtils.serialize(loginMsg);
                //System.out.println("SEE HERE!!!!" + builder.toString());
                //System.out.println("SEE HERE!!!!" + builder.toString());
                break;
            default:
                throw new ProtocolException("Invalid type: " + type);
        }
        log.info("encoded: {}", msg.toString());
        return data;
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