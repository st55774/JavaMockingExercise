package cz.upce.fei.inptp.databasedependency.dao;

import com.google.inject.Inject;
import cz.upce.fei.inptp.databasedependency.entity.Person;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO object for Person entity.
 */
public class PersonDAOImpl implements PersonDAO {

    @Inject
    public PersonDAOImpl() {
    }

    @Override
    public void save(Person object) {
        try {
            Statement st = Database.getInstance().createStatement();

            st.execute("delete from person where id = " + object.getId());
            st.execute("insert into person values (" + object.getId() + ", '" + object.getName() + "', '" + object.getPassword() + "')");

        } catch (SQLException ex) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Person load(String parameters) {
        try {
            Statement st = Database.getInstance().createStatement();

            ResultSet rs = st.executeQuery("select * from person where " + parameters);
            if (!rs.next()) {
                return null;
            }
            
            Person p = new Person(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("password")
            );
            
            return p;
        } catch (SQLException ex) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    @Override
    public String getRoleWhereStringFor(Person person) {
        return "id = " + person.getId();
    }

}
