package cz.upce.fei.inptp.databasedependency.business;

import com.google.inject.Inject;
import cz.upce.fei.inptp.databasedependency.dao.PersonDAO;
import cz.upce.fei.inptp.databasedependency.entity.Person;

/**
 * Authentication service - used for authentication of users stored in db.
 * Authentication should success if login and password (hashed) matches.
 */
public class AuthenticationServiceImpl implements AuthenticationService {
    private PersonDAO persondao;

    @Inject
    public AuthenticationServiceImpl() {
    }

    public AuthenticationServiceImpl(PersonDAO personDAO) {
        this.persondao = personDAO;
    }

    @Override
    public boolean Authenticate(String login, String password) {
        Person person = persondao.load("name = '" + login + "'");
        if (person == null) {
            return false;
        }

        return person.getPassword().equals(AuthenticationService.encryptPassword(password));
    }

}
