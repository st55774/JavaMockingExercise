package cz.upce.fei.inptp.databasedependency.business;

import com.google.inject.AbstractModule;

public class AuthenticationServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
    }
}
