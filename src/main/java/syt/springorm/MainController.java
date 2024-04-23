package syt.springorm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import syt.springorm.data.Product;
import syt.springorm.data.WarehouseData;

@Controller
public class MainController {
    // autowire the generated userRepository bean to this
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseDataRepository whRepo;

    @Autowired
    private ProductRepository pRepo;

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
        // whRepo.save(d);

        pRepo.save(p);

        return "saved";
    }

}
