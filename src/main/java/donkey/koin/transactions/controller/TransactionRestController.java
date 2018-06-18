package donkey.koin.transactions.controller;

import com.google.gson.GsonBuilder;
import donkey.koin.krypto.PotentialTransaction;
import donkey.koin.transactions.transaction.InitTransaction;
import donkey.koin.transactions.transaction.TransactionDetails;
import donkey.koin.transactions.transaction.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static donkey.koin.dictionaries.WebServicesDictionary.TRANSACTION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@RestController
@RequestMapping(TRANSACTION)
@AllArgsConstructor
public class TransactionRestController {

    private final TransactionService transactionService;

    @RequestMapping(value = "/purchase", method = POST, consumes = APPLICATION_JSON_VALUE)
    public String purchaseKoins(@RequestBody @Valid TransactionDetails transactionDetails) {
        PotentialTransaction purchase = transactionService.purchase(transactionDetails);
        return new GsonBuilder().setPrettyPrinting().create().toJson(purchase);
    }

    @RequestMapping(value = "/sell", method = POST, consumes = APPLICATION_JSON_VALUE)
    public String sellKoins(@RequestBody @Valid TransactionDetails transactionDetails) {
        PotentialTransaction sale = transactionService.sell(transactionDetails);
        return new GsonBuilder().setPrettyPrinting().create().toJson(sale);
    }

    @RequestMapping(value = "/init", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity init(@RequestBody @Valid InitTransaction initTransaction) {
        transactionService.init(initTransaction);
        return ResponseEntity.ok().build();
    }
}
