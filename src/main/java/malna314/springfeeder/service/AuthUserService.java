package malna314.springfeeder.service;

import malna314.springfeeder.entity.AuthUser;

public interface AuthUserService {

    AuthUser findByEmail(String email);

}