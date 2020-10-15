package cz.upce.fei.inptp.databasedependency.dao;

import com.google.inject.AbstractModule;

public class PersonDAOModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PersonDAO.class).to(PersonDAOImpl.class);
    }
}
