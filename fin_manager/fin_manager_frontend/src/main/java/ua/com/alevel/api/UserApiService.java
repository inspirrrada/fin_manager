package ua.com.alevel.api;

import ua.com.alevel.model.UserModel;

import java.util.Collection;

public interface UserApiService {

    Collection<UserModel> findAll();
}
