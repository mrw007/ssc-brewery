package guru.sfg.brewery.security.google;

import com.warrenstrange.googleauth.ICredentialRepository;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class GoogleCredentialRepository implements ICredentialRepository {

    public final UserRepository userRepository;

    @Override
    public String getSecretKey(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return user.getGoogle2FASecret();
    }

    @Override
    public void saveUserCredentials(String username, String secretKey, int validationCode, List<Integer> scratchCodes) {
        User user = userRepository.findByUsername(username).orElseThrow();
        user.setGoogle2FASecret(secretKey);
        user.setUserGoogle2FA(true);
        userRepository.save(user);
    }
}
