package track.msgtest.messenger;

/**
 *
 */
public class User {
    private static long idCount = 0;
    private String name;
    private String pass;
    private long id;

    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
        this.id = idCount++;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }

    public boolean checkName(String name) {
        return (this.name.equals(name));
    }

    public boolean checkPassword(String password) {
        return (this.pass.equals(password));
    }

    public long getId() {
        return this.id;
    }
}
