package com.leonardo.dscommerce.tests;

import com.leonardo.dscommerce.entities.User;

import java.time.LocalDate;

public class UserFactory {

    public static User createUserClient() {
        User user = new User(1L, "Leo", "leo@gmail.com", "123456789", LocalDate.parse("2004-04-30"), "123456");
        user.getRoles().add(RoleFactory.createRoleClient());
        return user;
    }

    public static User createOtherUserClient() {
        User user = new User(2L, "Julia", "julia@gmail.com", "123456789", LocalDate.parse("2004-04-30"), "123456");
        user.getRoles().add(RoleFactory.createRoleClient());
        return user;
    }

    public static User createUserAdmin() {
        User user = new User(3L, "Leo", "leo@gmail.com", "123456789", LocalDate.parse("2004-04-30"), "123456");
        user.getRoles().add(RoleFactory.createRoleAdmin());
        return user;
    }
}
