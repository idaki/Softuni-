package bg.softuni.userservice.service.impl.user;

import bg.softuni.userservice.models.dto.UserDetailsDTO;
import bg.softuni.userservice.models.entity.user.User;
import bg.softuni.userservice.models.entity.user.UserProfile;
import bg.softuni.userservice.repository.UserRepository;

import bg.softuni.userservice.service.interfaces.user.UserInfoService;
import bg.softuni.userservice.utils.buiider.UserDetailsDTOBuilder.UserDetailsDTOBuilder;
import bg.softuni.userservice.utils.utills.UserFinderUtil.UserFinderUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;
    private final UserDetailsDTOBuilder userDetailsDTOBuilder;
    private final UserFinderUtil userFinderUtil;

    public UserInfoServiceImpl(UserRepository userRepository, UserDetailsDTOBuilder userDetailsDTOBuilder, UserFinderUtil userFinderUtil) {
        this.userRepository = userRepository;
        this.userDetailsDTOBuilder = userDetailsDTOBuilder;
        this.userFinderUtil = userFinderUtil;
    }


    @Override
    public UserDetailsDTO getUserDetails(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = user.getUserProfile();

        return userDetailsDTOBuilder
                .withUsername(user.getUsername())
                .withFirstName(profile.getFirstName())
                .withLastName(profile.getLastName())
                .withEmail(user.getEmail())
                .withAvatar(profile.getAvatar())
                .withId(user.getId())
                .build();
    }

    @Override
    public UserDetailsDTO findUserByDetails(String username, String firstName, String lastName) {
        Optional<User> userOpt = userFinderUtil.findUser(username, firstName, lastName);

        return userOpt.map(user ->
                userDetailsDTOBuilder
                        .withUsername(user.getUsername())
                        .withFirstName(user.getUserProfile().getFirstName())
                        .withLastName(user.getUserProfile().getLastName())
                        .withEmail(user.getEmail())
                        .withAvatar(user.getUserProfile().getAvatar())
                        .withId(user.getId())
                        .build()
        ).orElse(null);
    }
}
