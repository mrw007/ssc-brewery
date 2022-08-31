package guru.sfg.brewery.web.controllers;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(UserController.USER_URL)
@Controller
public class UserController {
    public static final String USER_URL = "/user";
    public static final String USER_REGISTER_2_FA = "user/register2fa";

    private final UserRepository userRepository;
    private final GoogleAuthenticator googleAuthenticator;

    @GetMapping("/register2fa")
    public String register2FA(Model model) {

        User user = getUser();
        String url = GoogleAuthenticatorQRGenerator.getOtpAuthURL("SFG", user.getUsername(),
                googleAuthenticator.createCredentials(user.getUsername()));

        log.debug("Google QR URL: " + url);

        model.addAttribute("googleurl", url);
        return USER_REGISTER_2_FA;
    }

    @PostMapping
    public String confirm2FA(@RequestParam Integer verifyCode) {

        User user = getUser();

        log.debug("Entered Code is: " + verifyCode);


        if (googleAuthenticator.authorizeUser(user.getUsername(), verifyCode)) {

            User savedUser = userRepository.findById(user.getId()).orElseThrow();
            savedUser.setUserGoogle2FA(true);
            userRepository.save(savedUser);

            return "index";
        } else {
            // bad code
            return USER_REGISTER_2_FA;
        }


    }

    private static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}