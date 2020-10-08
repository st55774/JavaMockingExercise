package cz.upce.fei.inptp.databasedependency.business;

import cz.upce.fei.inptp.databasedependency.dao.PersonDAO;
import cz.upce.fei.inptp.databasedependency.entity.Person;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    private AuthenticationService authenticationService;

    private static final String USER_NAME = "user";
    private static final String CORRECT_PASSWORD = "pass";
    private static final String INCORRECT_PASSWORD = "invalid";

    @Spy
    @InjectMocks
    private PersonDAO personDAO;
    private Person authenticatedPerson;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        personDAO = mock(PersonDAO.class);
        authenticationService = new AuthenticationService(personDAO);
        authenticatedPerson = new Person(0, USER_NAME, AuthenticationService.encryptPassword(CORRECT_PASSWORD));
    }

    @org.junit.jupiter.api.Test
    void authenticateCorrectUserWithCorrectPassword() {
        when(personDAO.load(String.format("name = '%s'", USER_NAME))).thenReturn(authenticatedPerson);

        assertEquals(true, authenticationService.Authenticate(USER_NAME, CORRECT_PASSWORD));
    }

    @org.junit.jupiter.api.Test
    void authenticateCorrectUserWithIncorrectPassword() {
        when(personDAO.load(String.format("name = '%s'", USER_NAME))).thenReturn(authenticatedPerson);

        assertEquals(false, authenticationService.Authenticate(USER_NAME, INCORRECT_PASSWORD));
    }

    @org.junit.jupiter.api.Test
    void authenticateNonExistingUser() {
        when(personDAO.load(Mockito.any())).thenReturn(null);

        assertEquals(false, authenticationService.Authenticate(USER_NAME, CORRECT_PASSWORD));
    }
}