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

    @RequestMapping("/warehouse")
    public @ResponseBody String test() {
        Product p = new Product();
        p.setProductID(1);
        p.setProductName("name2");
        p.setProductUnit("unit3");
        p.setProductQuantity(4);
        p.setProductCategory("cat5");
        // pRepo.save(p);

        WarehouseData d = new WarehouseData();
        d.setProductData(List.of(p));
        whRepo.save(d);
        
        return "saved";
    }

}
