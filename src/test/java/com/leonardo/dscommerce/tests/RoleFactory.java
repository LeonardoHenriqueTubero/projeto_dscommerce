package com.leonardo.dscommerce.tests;

import com.leonardo.dscommerce.entities.Role;

public class RoleFactory {

    public static Role createRoleClient() {
        return new Role(1L, "ROLE_CLIENT");
    }

    public static Role createRoleAdmin() {
        return new Role(1L, "ROLE_ADMIN");
    }
}
