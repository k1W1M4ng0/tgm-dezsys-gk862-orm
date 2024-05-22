package syt.springorm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import syt.springorm.data.Product;
import syt.springorm.data.WarehouseData;

@Controller
@CrossOrigin
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
        // also, saving it throws an exception (likely because pRepo.save tries to save this object too)
        // whRepo.save(d);

        pRepo.save(p);

        // however, you can save a new one 
        whRepo.save(new WarehouseData());

        return "saved";
    }

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

    static class UpdateRequest {
        public String warehouseApplicationID;
        public String warehouseName;
        public String warehouseAddress;
        public String warehousePostalCode;
        public String warehouseCity;
        public String warehouseCountry;
    }

}
