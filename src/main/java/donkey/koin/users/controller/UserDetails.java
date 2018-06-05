package donkey.koin.users.controller;

import donkey.koin.entities.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserDetails {

    private final String username;
    private final String email;
    private final List<Transaction> transactions;
}