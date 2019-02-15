import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import static spark.Spark.*;

public class Main {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String URL = "http://10.0.0.100:4567/data/";
    private static UserService userService = new UserService() {

        HashMap<Integer, User> dbUsers = new HashMap<>();

        @Override
        public void addUser(User user) {
            dbUsers.put(user.getId(), user);
        }

        @Override
        public Collection<User> getUsers() {
           return dbUsers.values();
        }

        @Override
        public User getUser(Integer id) {
            return dbUsers.get(id);
        }

        @Override
        public User editUser(User user) {
            try {
                if (user.getId() == null)
                    throw new Exception("ID cannot be blank");

                User editUser = dbUsers.get(user.getId());
                if (editUser == null)
                    throw new Exception("User not found");

                if (user.getEmail() != null) {
                    editUser.setEmail(user.getEmail());
                }
                if (user.getName() != null) {
                    editUser.setName(user.getName());
                }
                if (user.getPosition() != null) {
                    editUser.setPosition(user.getPosition());
                }
                editUser.setAge(user.getAge());
                editUser.setSalary(user.getSalary());
                return editUser;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        public void deleteUser(Integer id) {
            dbUsers.remove(id);
        }

        @Override
        public boolean userExist(Integer id) {
            return dbUsers.containsKey(id);
        }
    };

    private static void init() {
        userService.addUser(new User(0, "User", "user@user.com"));
    }

    private static void startRestService() {
        get("/hello", (req, res) -> "Hello form REST service! All working. " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date()));

        get("/users", (req, res) -> {
            res.type("application/json");
            return gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJsonTree(userService.getUsers())));
        });

        post("/users", (req, res) -> {
            res.type("application/json");
            var user = new Gson().fromJson(req.body(), User.class);
            userService.addUser(user);
            return new GsonBuilder().setPrettyPrinting().create().toJson(new StandardResponse(StatusResponse.SUCCESS));
        });

        get("/users/:id", (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, new Gson()
                            .toJsonTree(userService.getUser(Integer.parseInt(request.params(":id")))))
            );
        });
        put("/users/:id", (request, response) -> {
            response.type("application/json");
            User toEdit = new Gson().fromJson(request.body(), User.class);
            User editedUser = userService.editUser(toEdit);

            if (editedUser != null) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.SUCCESS, new Gson()
                                .toJsonTree(editedUser)));
            } else {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, new Gson()
                                .toJson("User not found or error in edit")));
            }
        });
        delete("/users/:id", (request, response) -> {
            response.type("application/json");
            userService.deleteUser(Integer.parseInt(request.params(":id")));
            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, "user deleted"));
        });
        options("/users/:id", (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,
                            (userService.userExist(
                                    Integer.parseInt(request.params(":id"))) ? "User exists" : "User does not exists"))
            );
        });
    }

    public static void main(String[] args) {
        init();
        startRestService();
    }
}