package cz.upce.fei.inptp.databasedependency.dao;

import com.google.inject.Inject;
import cz.upce.fei.inptp.databasedependency.entity.PersonRole;
import cz.upce.fei.inptp.databasedependency.entity.Person;
import cz.upce.fei.inptp.databasedependency.entity.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO object for PersonRole entity.
 */
public class PersonRolesDAO implements DAO<PersonRole> {

    @Inject
    public PersonRolesDAO() {
    }

    @Override
    public void save(PersonRole object) {
        try {
            Statement st = Database.getInstance().createStatement();

            st.execute("delete from role where id = " + object.getPerson().getId());

            for (Role role : object.getRoles()) {
                st.execute("insert into role values (" + object.getPerson().getId() + ", '" + role.getSection() + "', '" + role.getAccess() + "', '" + role.getModifier() + "')");
            }

        } catch (SQLException ex) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public PersonRole load(String parameters) {
        try {
            Statement st = Database.getInstance().createStatement();

            Person person = new PersonDAOImpl().load(parameters);
            if (person == null) {
                return null;
            }

            ResultSet rs = st.executeQuery("select * from role where id = " + person.getId());
            ArrayList<Role> roles = new ArrayList<>();

            while (rs.next()) {
                roles.add(new Role(
                        rs.getString("section"),
                        rs.getString("access"), 
                        rs.getString("modifier")
                ));
            }

            return new PersonRole(person, roles);
        } catch (SQLException ex) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
