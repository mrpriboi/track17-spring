package track.msgtest.messenger.messages;

/**
 *
 */
public class RegisterMessage extends Message {
    private String name;
    private String pass;

    public RegisterMessage(){
        type = Type.MSG_REGISTRATION;
    }

    public RegisterMessage(String name, String pass) {
        type = Type.MSG_LOGIN;
        this.name = name;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "RegisterMessage{" +
                "name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}