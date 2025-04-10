package kr.hhplus.be.server.domain.user;

import lombok.Getter;

@Getter
public class User {
    private Long id;

    public User(Long id) {
        this.id = id;
    }
}
