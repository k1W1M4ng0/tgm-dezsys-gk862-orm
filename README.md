# SYT Dezentrale Systeme GK862 Spring Data & ORM

## Questions

    What is ORM and how is JPA used?
    What is the application.properties used for and where must it be stored?
    Which annotations are frequently used for entity types? Which key points must be observed?
    What methods do you need for CRUD operations?

ORM stands for Object Relational Mapping, and the JPA is used to use ORM in java.
It supports annotations (such as @Entity), that tell the driver how to map a class to a table.

The application.properties file is generally used as config file for spring, and it can also be used
to store database credentials and its address. It is stored in src/main/resources

Frequently used annotations are:  
- Id
- GeneratedValue
- ManyToOne
    Also OneToMany, OneToOne, etc.
- JoinColumn

You can use the methods found in the CrudRepository interface for CRUD operations. They are:  
- count
- delete
- deleteById
- deleteAll
- deleteAllById
- existsById
- findAll
- findAllById
- findById
- save
- saveAll

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

## WarehouseData with MySQL

For saving WarehouseData (+ Product) objects, they have to be turned into entity models.

First, I changed the id to be an Integer instead of a String. I then set the strategy to AUTO.

I changed the timestamp in Warehouse to a LocalDateTime, for some later tasks.

The most important bit is the relation between the classes:

```java
// in WarehouseData
    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private List<Product> productData;


// in Product
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouseID")
    private WarehouseData warehouse;
```

One WarehouseData saves multiple Products, so this side gets a @OneToMany annotation.

mappedBy is the name of the attribute in Product, which maps back to the warehouse.

Multiple products can belong to a warehouse, so i put a @ManyToOne annotation there.

@JoinColumn is the name of the column in the database

### Testing

for testing, i first manually added data to the tables

```sql
USE DATABASE db_example;

-- clear tables
DELETE FROM product;
INSERT INTO product VALUES
    (1, 'Electronics', 'Smartphone', 100, 'Piece'),
    (2, 'Electronics', 'Laptop', 50, 'Piece'),
    (3, 'Books', 'Novel', 200, 'Piece'),
    (4, 'Books', 'Textbook', 150, 'Piece'),
    (5, 'Stationery', 'Pencil', 500, 'Box'),
    (6, 'Stationery', 'Notebook', 300, 'Piece'),
    (7, 'Clothing', 'T-Shirt', 250, 'Piece'),
    (8, 'Clothing', 'Jeans', 100, 'Piece'),
    (9, 'Groceries', 'Bread', 1000, 'Loaf'),
    (10, 'Groceries', 'Milk', 500, 'Gallon');
-- products generated by ai since i was lazy coming up with examples

DELETE FROM warehouse_data;
INSERT INTO warehouse_data VALUES
    (1, '2023-11-15 10:00:00', '123 Main St', 'APP123', 'New York', 'USA', 'Warehouse A', '10001'),
    (2, '2023-11-15 11:00:00', '456 Market St', 'APP456', 'Los Angeles', 'USA', 'Warehouse B', '90001');
-- again generated by ai


select * from product;
select * from warehouse_data;
DELETE FROM warehouse_data;
DELETE FROM product;
```

I also added an api to add a predefined set of product + warehousedata.

```java
// in MainController
    @Autowired
    private ProductRepository pRepo;

    @RequestMapping("/add/warehouseProduct")
    public @ResponseBody String addWarehouseProduct() {
        Product p = new Product();
        p.setProductID(1);
        p.setProductName("name2");
        p.setProductUnit("unit3");
        p.setProductQuantity(4);
        p.setProductCategory("cat5");

        WarehouseData d = new WarehouseData();

        p.setWarehouse(d);
        d.setProductData(List.of(p));

        // saving in whrepo is not necessary thanks to cascade type all in Product
        // also, saving it throws an exception (likely because pRepo.save tries to save this object too)
        // whRepo.save(d);

        pRepo.save(p);

        // however, you can save a new one 
        whRepo.save(new WarehouseData());


        return "saved";
    }
```

## Collecting data

The next task was to implement various ways to collect data, with CrudRepository methods.


- Collect all data of one data warehouse specified by datawarehouseID.
- Collect a single product of a data warehouse specified by datawarehouseID and productID.
- Update a data warehouse using datawarehouseID.


For the first two, i added new request mappings to MainController:

```java

    @RequestMapping("/id/warehouse/{id}")
    public @ResponseBody WarehouseData getWarehouseById(@PathVariable int id) {
        return whRepo.findById(id).orElse(null);
    }

    @RequestMapping("/id/warehouse/{warehouseID}/product/{productID}")
    public @ResponseBody Product getSingleProductFromWarehouse(@PathVariable int warehouseID, @PathVariable int productID) {
        return pRepo.findById(productID)
            .filter(p -> p.getWarehouse().getWarehouseID() == warehouseID)
            .orElse(null);
    }
```

For the third one, i added a new request mapping:  

```java

    @RequestMapping("/update/warehouse/{id}")
    public @ResponseBody UpdateRequest updateWarehouse(@PathVariable int id, @RequestBody UpdateRequest req) {
        var newObject = whRepo.findById(id).orElseThrow();

        if(req.warehouseApplicationID.length() > 0) {
            newObject.setWarehouseApplicationID(req.warehouseApplicationID);
        }
        if(req.warehouseName.length() > 0) {
            newObject.setWarehouseName(req.warehouseName);
        }
        if(req.warehouseAddress.length() > 0) {
            newObject.setWarehouseAddress(req.warehouseAddress);
        }
        if(req.warehousePostalCode.length() > 0) {
            newObject.setWarehousePostalCode(req.warehousePostalCode);
        }
        if(req.warehouseCity.length() > 0) {
            newObject.setWarehouseCity(req.warehouseCity);
        }
        if(req.warehouseCountry.length() > 0) {
            newObject.setWarehouseCountry(req.warehouseCountry);
        }

        whRepo.save(newObject);

        return req;
    }
```

as well as a html site with a form:

```html
<!-- src: phind.com, asked on 2024-05-21 20:50,
query: "i have a class, and i want you to write a website including a simple html form, to update each value (excluding the ip). i want to send them via rest with a button (axios) [and then the warehouse data class]"
i then added an input field for entering server details, and for a warehouseID
-->

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update Warehouse Data</title>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
    <h2>Update Warehouse Information</h2>
    <form id="updateForm">
        <label for="warehouseID">WarehouseID to change:</label><br>
        <input type="text" id="warehouseID" name="warehouseID" required><br>

        <hr>

        <label for="warehouseApplicationID">Warehouse Application ID:</label><br>
        <input type="text" id="warehouseApplicationID" name="warehouseApplicationID"><br>
        <label for="warehouseName">Warehouse Name:</label><br>
        <input type="text" id="warehouseName" name="warehouseName"><br>
        <label for="warehouseAddress">Warehouse Address:</label><br>
        <input type="text" id="warehouseAddress" name="warehouseAddress"><br>
        <label for="warehousePostalCode">Warehouse Postal Code:</label><br>
        <input type="text" id="warehousePostalCode" name="warehousePostalCode"><br>
        <label for="warehouseCity">Warehouse City:</label><br>
        <input type="text" id="warehouseCity" name="warehouseCity"><br>
        <label for="warehouseCountry">Warehouse Country:</label><br>
        <input type="text" id="warehouseCountry" name="warehouseCountry"><br>

        <hr>

        <input type="text" id="server" name="server"><br>
        <label for="server">Server (ip + port)</label><br>

        <hr>

        <button type="submit">Submit</button>
    </form>

    <script>
        document.getElementById('updateForm').addEventListener('submit', function(event) {
            event.preventDefault(); // Prevent the default form submission behavior
            const formData = new FormData(this);

            axios.post('http://' + formData.get('server') + '/update/warehouse/' + formData.get('warehouseID'), Object.fromEntries(formData))
               .then(response => console.log(response.data))
               .catch(error => console.error(error));
        });
    </script>
</body>
</html>
```

## Switching to PostgreSQL

For switching to a postgresql dbms, i had to change `src/main/resources/application.properties`:

```diff
-spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/db_example
-spring.datasource.username=springuser
-spring.datasource.password=password
-spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
+spring.datasource.url=jdbc:postgresql://localhost:5432/db_example
+spring.datasource.username=postgres
+spring.datasource.password=Pass2023!
+spring.datasource.driver-class-name=org.postgresql.Driver
```

Then I added the driver as a dependency in `pom.xml`:

```xml
<!-- https://mvnrepository.com/artifact/org.postgresql/postgresql -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.3</version>
</dependency>
```

I also had to create the database:

```bash
docker exec -it postgres bash
su postgres
psql 

# sql
CREATE DATABASE db_example;
```

## Sources

- [Spring Boot "Accessing Data with MySQL" tutorial](https://spring.io/guides/gs/accessing-data-mysql)
- [API documentation for Spring CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
- [PostgreSQL driver](https://mvnrepository.com/artifact/org.postgresql/postgresql/42.7.3)
