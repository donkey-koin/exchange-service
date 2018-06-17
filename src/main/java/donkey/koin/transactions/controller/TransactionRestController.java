package donkey.koin.transactions.controller;

import donkey.koin.krypto.PotentialTransaction;
import donkey.koin.transactions.transaction.TransactionDetails;
import donkey.koin.transactions.transaction.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static donkey.koin.dictionaries.WebServicesDictionary.TRANSACTION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(TRANSACTION)
public class TransactionRestController {

    @Autowired
    private final TransactionService transactionService;

    @RequestMapping(value = "/purchase", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PotentialTransaction> purchaseKoins(@RequestBody @Valid TransactionDetails transactionDetails) {
        PotentialTransaction purchase = transactionService.purchase(transactionDetails);
        return ResponseEntity.ok().body(purchase);
    }

    @RequestMapping(value = "/sell", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PotentialTransaction> sellKoins(@RequestBody @Valid TransactionDetails transactionDetails) {
        return ResponseEntity.ok().body(transactionService.sell(transactionDetails));
    }
}
