package accounts.rest;

import accounts.Account;
import accounts.Bank;
import accounts.Transfer;
import accounts.Transfers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    public Account findAccount(@PathVariable int id, HttpServletResponse response) {
        setHeaders(response);
        return bank.find(id);
    }

    private void setHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
    }

    @RequestMapping(value = "/api/accounts/{id}/transfers", method = RequestMethod.GET, produces = "application/json")
    public Transfers listTransfers(@PathVariable int id, HttpServletResponse  response) {
        setHeaders(response);
        return bank.listTransfers(id);
    }

    @RequestMapping(value = "/api/transfer", method = RequestMethod.POST, consumes = "application/json")
    public int transfer(@RequestBody Transfer transfer, HttpServletResponse  response) {
        setHeaders(response);
        return bank.transfer(transfer);
    }
}
