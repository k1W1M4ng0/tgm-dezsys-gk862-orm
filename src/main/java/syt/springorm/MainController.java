package syt.springorm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
