package accounts.rest;

import accounts.Account;
import accounts.Bank;
import accounts.Transfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * Created by miraculis on 06.05.2017.
 */
@Slf4j
@RestController
public class Api {
    @Autowired
    private Bank bank;

    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.GET, produces = "application/json")
    public Account findAccount(@PathVariable int id) {
        log.info("find account " + id);
        return bank.find(id);
    }

    @RequestMapping(value = "/api/accounts/{id}/transfers", method = RequestMethod.GET, produces = "application/json")
    public TransfersResponse listTransfers(@PathVariable int id) {
        return new TransfersResponse(bank.listTransfers(id));
    }

    @RequestMapping(value = "/api/transfer", method = RequestMethod.POST, consumes = "application/json")
    public int transfer(@RequestBody Transfer transfer) {
        return bank.transfer(transfer);
    }
}
