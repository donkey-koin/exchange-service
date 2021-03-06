package donkey.koin.wallets.controller;

import donkey.koin.entities.wallet.Wallet;
import donkey.koin.wallets.wallet.WalletDetails;
import donkey.koin.wallets.wallet.WalletService;
import donkey.koin.wallets.wallet.WalletUpdateDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static donkey.koin.dictionaries.WebServicesDictionary.WALLET;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(WALLET)
public class WalletRestController {

    private final WalletService walletService;

    @RequestMapping(value = "/content", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getContent(@RequestBody @Valid WalletDetails walletDetails) {
        Wallet wallet = walletService.getCurrentWallet(walletDetails.getUsername());
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("amountEuro", "" + wallet.getAmountEuro());
        stringMap.put("amountBtc", "" + wallet.getAmountBtc());
        return ResponseEntity.status(HttpStatus.OK).body(stringMap);
    }

    @RequestMapping(value = "/deposit", method = POST, consumes = APPLICATION_JSON_VALUE)
    public void deposit(@RequestBody @Valid WalletDetails walletDetails) {
        walletService.deposit(walletDetails.getUsername(), walletDetails.getMoneyToDeposit());
    }

    @RequestMapping(value = "/withdrawn", method = POST, consumes = APPLICATION_JSON_VALUE)
    public void withdrawn(@RequestBody @Valid WalletDetails walletDetails) {
        walletService.withdrawn(walletDetails.getUsername(), walletDetails.getMoneyToWithdrawn());
    }

    @RequestMapping(value = "/update", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity updateContent(@RequestBody List<WalletUpdateDetails> walletsToUpdateWithAmount) {
        walletService.updateWallets(walletsToUpdateWithAmount);
        return ResponseEntity.ok().build();
    }
}
