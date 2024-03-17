package com.project.service_impl.user;

import com.project.service.user.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> hasUsername(String username) {
        return (root, query, cb) -> username == null ? null : cb.equal(root.get("username"), username);
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> email == null ? null : cb.equal(root.get("email"), email);
    }
}