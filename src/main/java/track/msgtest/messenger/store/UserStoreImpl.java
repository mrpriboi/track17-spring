package track.msgtest.messenger.store;

import track.msgtest.messenger.User;

import java.util.ArrayList;

/**
 * Created by priboi on 20.04.17.
 */
public class UserStoreImpl implements UserStore {

    static User user1 = new User("admin", "password");
    static User user2 = new User("user", "password");
    static ArrayList<User> users = new ArrayList<User>();
    static {
        users.add(user1);
        users.add(user2);
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User getUser(String login, String pass) {
        for (User user : users) {
            if (user.getName().equals(login)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }
}
