# SYT Dezentrale Systeme GK862 Spring Data & ORM

## Questions

## Spring Data with MySQL

I created this project with the Spring Initializr with the dependencies: 
Spring Web, Spring Data JPA, and MySQL Driver.

### MySQL database

For the database, I used a docker container: 

```bash
# start the container
docker run \
    --name mysql \
    -e MYSQL_ROOT_PASSWORD=password \
    -p 3306:3306 \
    -d mysql

# create a database

docker exec -it mysql mysql --password
# enter 'password'

```

```sql
create database db_example;
create user 'springuser'@'%' identified by 'password';
grant all on db_example.* to 'springuser'@'%'; -- Gives all privileges to the new user on the newly created database
```

### application.properties

For the db connection, I wrote into the file `src/main/resources/application.properties`:

```
spring.application.name=spring-orm

spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/db_example
spring.datasource.username=springuser
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql: true
```

### User.java

For this exercise, I will save User.java objects into the database.

The class has to be annotated with `@Entity`, and the ID with 
`@ID` and `@GeneratedValue(strategy = GenerationType.AUTO)`

### UserRepository.java

```java
public interface UserRepository extends CrudRepository<User, Integer> {
    // this will be auto implemented
}
```

### Controller

```java
@Controller
public class MainController {
    // autowire the generated userRepository bean to this
    @Autowired
    private UserRepository userRepository;

    // i want to also use GET requests, for simplicity
    @RequestMapping("/add")
    public @ResponseBody String addNewUser(
            @RequestParam String name,
            @RequestParam String email) {


        // create a new user
        User n = new User();
        n.setName(name);
        n.setEmail(email);

        // save the user to the db
        userRepository.save(n);

        return "Saved";
    }

    @RequestMapping("/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // get all users
        return userRepository.findAll();
    }
}
```

### Testing

test this with [adding a user](http://localhost:8080/add?name=Simon&email=sgao)
and [getting all users](http://localhost:8080/all)

## Sources

- [Spring Boot "Accessing Data with MySQL" tutorial](https://spring.io/guides/gs/accessing-data-mysql)

