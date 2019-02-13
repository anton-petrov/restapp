import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.jshell.spi.ExecutionControl;

import java.text.SimpleDateFormat;
import java.util.*;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

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
        public User getUser(String id) {
            return null;
        }

        @Override
        public User editUser(User user) throws ExecutionControl.UserException {
            return null;
        }

        @Override
        public void deleteUser(String id) {

        }

        @Override
        public boolean userExist(String id) {
            return false;
        }
    };

    private static String getData() {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var data = new ArrayList<>(
                Arrays.asList(
                        new User(1, "John Doe", "john@doe.com"),
                        new User(2, "Артем Шепелев", "rw@gmail.ru"),
                        new User(3, "Баба Валя", "babkavalya@microsoft.com")
                )
        );
        return gson.toJson(data);
    }

    private static void init() {
        // ...
        userService.addUser(new User(0, "User", "user@user.com"));
    }

    public static void main(String[] args) {
        init();
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
            return null;
        });
        put("/users/:id", (request, response) -> {
            return null;
        });
        delete("/users/:id", (request, response) -> {
            return null;
        });
        options("/users/:id", (request, response) -> {
            return null;
        });
        System.out.println(getData());
    }
}