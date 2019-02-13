public class User implements Cloneable {
    private Integer id;
    private String email;
    private String name;
    private String position;
    private int age;
    private int salary;

    @Override
    public User clone() {
        User user = null;
        try {
           user = (User) super.clone();
        } catch (CloneNotSupportedException e) {
            user = new User(this.id, this.name, this.email, this.position, this.age, this.salary);
        }
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User(int id, String name, String email, String position, int age, int salary) {
       this(id, name, email);
       this.age = age;
       this.salary = salary;
       this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


}
