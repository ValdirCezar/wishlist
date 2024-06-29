package br.com.valdircezar.wishlist.clients;

import br.com.valdircezar.wishlist.models.exceptions.ObjectNotFoundException;
import br.com.valdircezar.wishlist.models.responses.UserResponse;

import java.util.List;

public class UserClientMock {

    public static void findById(final String userId) {
        mockUserResponse()
                .stream().filter(user -> user.id().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("User not found by id: " + userId));

    }

    private static List<UserResponse> mockUserResponse() {
        return List.of(
                new UserResponse("606b3efb74e106091aae50d8", "Valdir Cezar", "valdir@mail.com"),
                new UserResponse("607f2e1a9f5cf6e2ca4beae7", "Allan Turing", "turing@mail.com"),
                new UserResponse("607f2e1a9f5cf6e2ca4becae", "Margaret Hamilton", "hamilton@mail.com"),
                new UserResponse("618abc510dcef9143eefa7f1", "Linus Torvalds", "torvalds@mail.com"));
    }

}
