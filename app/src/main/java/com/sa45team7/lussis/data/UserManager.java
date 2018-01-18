package com.sa45team7.lussis.data;

import com.sa45team7.lussis.rest.model.Employee;

/**
 * Created by nhatton on 1/17/18.
 */

class UserManager {

    private Employee employee;

    private static final UserManager ourInstance = new UserManager();

    static UserManager getInstance() {
        return ourInstance;
    }

    private UserManager() {
    }
}
