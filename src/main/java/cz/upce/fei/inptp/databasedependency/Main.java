package cz.upce.fei.inptp.databasedependency;

import com.google.inject.Guice;
import com.google.inject.Injector;
import cz.upce.fei.inptp.databasedependency.business.*;
import cz.upce.fei.inptp.databasedependency.dao.*;
import cz.upce.fei.inptp.databasedependency.entity.PersonRole;
import cz.upce.fei.inptp.databasedependency.entity.Person;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        Injector injector = Guice.createInjector(
                new AuthenticationServiceModule(),
                new AuthorizationServiceModule(),
                new DatabaseModule(),
                new PersonDAOModule(),
                new PersonRolesDAOModule()
        );

        Database database = new Database();
        database.open();

        PersonDAO personDao = new PersonDAOImpl();
        
        // create person
        Person person = new Person(10, "Peter", AuthenticationService.encryptPassword("rafanovsky"));
        personDao.save(person);

        // load person
        person = personDao.load("id = 10");
        System.out.println(person);

        // test authentication
        AuthenticationService authentication = new AuthenticationServiceImpl(injector.getInstance(PersonDAO.class));
        System.out.println(authentication.Authenticate("Peter", "rafa"));
        System.out.println(authentication.Authenticate("Peter", "rafanovsky"));

        // check user roles
        PersonRole pr = new PersonRolesDAO().load("name = 'yui'");
        System.out.println(pr);

        // test authorization
        person = personDao.load("id = 2");
        AuthorizationService authorization = new AuthorizationService(injector.getInstance(PersonDAO.class), injector.getInstance(PersonRolesDAO.class));
        boolean authorizationResult = authorization.Authorize(person, "/finance/report", AccessOperationType.Read);
        System.out.println(authorizationResult);
        
        
        // load all persons from db
        try {
            Statement statement = database.createStatement();
            ResultSet rs = statement.executeQuery("select * from person");
            while (rs.next()) {
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        database.close();
    }
}
