package com.leonardo.dscommerce.services;

import com.leonardo.dscommerce.dto.UserDTO;
import com.leonardo.dscommerce.entities.User;
import com.leonardo.dscommerce.projections.UserDetailsProjection;
import com.leonardo.dscommerce.repositories.UserRepository;
import com.leonardo.dscommerce.tests.UserFactory;
import com.leonardo.dscommerce.utils.CustomUserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private CustomUserUtil util;

    private User user;
    private String existingEmail;
    private String nonExistingEmail;
    private UserDetailsProjection projection;
    private List<UserDetailsProjection> list;

    @BeforeEach
    void setUp() throws Exception {
        user = UserFactory.createUserClient();
        existingEmail = user.getEmail();
        nonExistingEmail = "teste@gmail.com";
        projection = Mockito.mock(UserDetailsProjection.class);
        list = new ArrayList<>(List.of(projection));

        Mockito.when(projection.getUsername()).thenReturn(existingEmail);
        Mockito.when(projection.getPassword()).thenReturn(user.getPassword());
        Mockito.when(projection.getRoleId()).thenReturn(1L);
        Mockito.when(projection.getAuthority()).thenReturn("ROLE_CLIENT");

        Mockito.when(repository.searchUserAndRolesByEmail(existingEmail)).thenReturn(list);
        Mockito.when(repository.searchUserAndRolesByEmail(nonExistingEmail)).thenReturn(List.of());
        Mockito.when(repository.findByEmail(existingEmail)).thenReturn(Optional.of(user));
        Mockito.when(repository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());
    }

    @Test
    void loadUserByUsernameShouldReturnUserDetailsWhenUsernameExists() {
        UserDetails result = service.loadUserByUsername(existingEmail);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingEmail, result.getUsername());
        Assertions.assertEquals(user.getPassword(), result.getPassword());
    }

    @Test
    void loadUserByUsernameShouldUsernameNotFoundExceptionWhenUsernameDoesNotExist() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            UserDetails result = service.loadUserByUsername(nonExistingEmail);
        });
    }

    @Test
    void authenticatedShouldReturnUserWhenUserExists() {
        Mockito.when(util.getLoggedUsername()).thenReturn(existingEmail);

        User result = service.authenticated();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingEmail, result.getUsername());
    }

    @Test
    void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        Mockito.doThrow(ClassCastException.class).when(util).getLoggedUsername();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
           service.authenticated();
        });
    }

    @Test
    void getMeShouldReturnUserDTOWhenUserExists() {

        UserService serviceSpy = Mockito.spy(service);
        Mockito.doReturn(user).when(serviceSpy).authenticated();

        UserDTO result = serviceSpy.getMe();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingEmail, result.getEmail());
    }

    @Test
    void getMeShouldThrowUsernameNotFoundExceptionWhenUserNotAuthenticated() {
        UserService serviceSpy = Mockito.spy(service);
        Mockito.doThrow(UsernameNotFoundException.class).when(serviceSpy).authenticated();

        Assertions.assertThrows(UsernameNotFoundException.class, serviceSpy::getMe);
    }
}
