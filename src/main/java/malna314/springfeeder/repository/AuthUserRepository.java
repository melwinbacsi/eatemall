package malna314.springfeeder.repository;

import malna314.springfeeder.entity.AuthUser;
import org.springframework.data.repository.CrudRepository;

public interface AuthUserRepository extends CrudRepository<AuthUser,Long> {
    AuthUser findByEmail(String email);
}