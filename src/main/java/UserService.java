import jdk.jshell.spi.ExecutionControl;

import java.util.Collection;

public interface UserService {
    void addUser(User user);

    Collection<User> getUsers();

    User getUser(Integer id);

    User editUser(User user)
            throws ExecutionControl.UserException;

    void deleteUser(Integer id);

    boolean userExist(Integer id);
}
