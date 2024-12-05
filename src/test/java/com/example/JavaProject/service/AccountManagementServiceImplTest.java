package com.example.JavaProject.service;

import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.entity.Role;
import com.example.JavaProject.entity.User;
import com.example.JavaProject.exception.ProfileHiddenException;
import com.example.JavaProject.mapper.UserMapper;
import com.example.JavaProject.repository.RoleRepository;
import com.example.JavaProject.repository.UserRepository;
import com.example.JavaProject.service.implementation.AccountManagementServiceImpl;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountManagementServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AccountManagementServiceImpl accountManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenValidScenario_thenPromoteUserToAdmin() {
        User user = new User();
        user.setId(2L);
        user.setRole(new Role(1L,"ROLE_USER", null));

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(authenticationService.getCurrentUserId()).thenReturn(1L);
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(new Role(2L, "ROLE_ADMIN", null));

        String result = accountManagementService.promote(2L);

        assertEquals("USER WITH ID 2 SUCCESSFULY PROMOTED TO ADMIN", result);
        verify(userRepository).save(user);
    }

    @Test
    void whenUserIdDoesNotExist_thenErrorMessageApears() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        String result = accountManagementService.promote(2L);

        assertEquals("User with id2 doesn't exist", result);
    }

    @Test
    void whenUserTriesTpPromoteOneself_thenCorrespondingMessageApear() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(authenticationService.getCurrentUserId()).thenReturn(1L);

        String result = accountManagementService.promote(1L);

        assertEquals("User cannot promote oneself", result);
    }

    @Test
    void whenUserAlreadyIsAdmin_thenCorrespondingMessageApear() {
        User user = new User();
        user.setId(2L);
        user.setRole(new Role(2L, "ROLE_ADMIN", null));

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(authenticationService.getCurrentUserId()).thenReturn(1L);

        String result = accountManagementService.promote(2L);

        assertEquals("User with id2 already is an admin", result);
    }

    @Test
    void whanValidUserCredentials_thenUpdateAccountSuccessfully() {
        User user = new User();
        user.setId(1L);
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("newUsername");
        registerDto.setAge(25);
        registerDto.setIsMan(true);
        registerDto.setPassword("newPassword");

        when(authenticationService.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(userMapper.mapToDto(user)).thenReturn(registerDto);

        RegisterDto result = accountManagementService.updateAccount(registerDto);

        assertEquals(registerDto, result);
        verify(userRepository).save(user);
    }

    @Test
    void whenUserNotFound_thenRuntimeExceptionIsThrown() {
        RegisterDto registerDto = new RegisterDto();

        when(authenticationService.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                            () -> accountManagementService.updateAccount(registerDto));

        assertEquals("No user found with currently logged account.", exception.getMessage());
    }

    @Test
    void whenUserProdileIsHidden_thenProfileHiddenExceptionIsThrown() {
        User user = new User();
        user.setId(1L);
        user.setHidden(true);
        RegisterDto registerDto = new RegisterDto();

        when(authenticationService.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ProfileHiddenException exception = assertThrows(ProfileHiddenException.class,
                            () -> accountManagementService.updateAccount(registerDto));

        assertEquals("Profile has been deleted", exception.getMessage());
    }

    @Test
    void whenValidScenario_thenUserDeleteAccountSuccessfully() {
        User user = new User();
        user.setId(1L);

        when(authenticationService.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String result = accountManagementService.deleteAccount();

        assertEquals("User has been successfully deleted", result);
        verify(userRepository).save(user);
    }

    @Test
    void whenUserIdNotFound_thenRuntimeExceptionIsThrown() {
        when(authenticationService.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> accountManagementService.deleteAccount());

        assertEquals("No user found with currently logged account.", exception.getMessage());
    }

    @Test
    void whenUserProdileIsHidden_thenCorrespondingMessageApreas() {
        User user = new User();
        user.setId(1L);
        user.setHidden(true);

        when(authenticationService.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String result = accountManagementService.deleteAccount();

        assertEquals("User has already been deleted", result);
    }

    @Test
    void whenValidEmailIsGiven_thenGetInfoByEmailSuccessfully() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        String result = accountManagementService.getInfoByEmail("test@example.com");

        assertEquals("User profile is visible", result);
    }

    @Test
    void whenUserWithGivenEmailNotFound_thenRuntimeExceptionIsThrown() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> accountManagementService.getInfoByEmail("test@example.com"));

        assertEquals("User not found with email: test@example.com", exception.getMessage());
    }

    @Test
    void whenUserWithGivenEmailIsHidden_thenProfileHiddenExceptionIsThrown() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setHidden(true);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        ProfileHiddenException exception = assertThrows(ProfileHiddenException.class,
                        () -> accountManagementService.getInfoByEmail("test@example.com"));

        assertEquals("Profile is hidden", exception.getMessage());
    }

    @Test
    void whenValidIdIsGiven_thenUserAccountIsHiddenSuccessfully() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String result = accountManagementService.hideAccount(1L);

        assertEquals("User account has been successfully hidden.", result);
        verify(userRepository).save(user);
    }

    @Test
    void whenUserWithGivenIdNotFound_thenRuntimeExceptionIsThrown() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                        () -> accountManagementService.hideAccount(1L));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void whenAccountIsAlreadyHidden_thenCorrespondingMessageIsShown() {
        User user = new User();
        user.setId(1L);
        user.setHidden(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String result = accountManagementService.hideAccount(1L);

        assertEquals("User's account is already hidden.", result);
    }
}