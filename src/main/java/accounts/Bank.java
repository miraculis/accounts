package accounts;

import org.apache.ignite.*;
import org.apache.ignite.transactions.Transaction;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by miraculis on 04.05.2017.
 */
@Component
public class Bank {

    public Bank() {
    }

    public int transfer(Transfer t0) {
        if (t0.getFrom() == t0.getTo() || t0.getFrom() <= 0 || t0.getTo() <= 0)
            return -2;

        IgniteCache<Integer, Account> accounts = Holder.ignite.cache("accounts");
        IgniteCache<Long, Transfer> transfers = Holder.ignite.cache("transfers");
        IgniteCache<Integer, Transfers> transfersG = Holder.ignite.cache("transfers-grouped-by-account");


        IgniteTransactions transactions = Holder.ignite.transactions();
        try (Transaction tx = transactions.txStart()) {
            Account a1 = accounts.get(t0.getFrom()), a2 = accounts.get(t0.getTo());
            Transfers t1 = transfersG.get(t0.getFrom()), t2 = transfersG.get(t0.getTo());

            if ((t0.getVolume() > 0 ? a1 : a2).check(t0)) {
                Transfer t = new Transfer(getId(), t0.getFrom(), t0.getTo(),
                        t0.getVolume(), System.currentTimeMillis());
                accounts.put(t.getFrom(), new Account(t.getFrom(), a1.getAmount() - t.getVolume()));
                accounts.put(t.getTo(), new Account(t.getTo(), a2.getAmount() + t.getVolume()));
                transfers.put(t.getId(), t);
                t1.getTransfers().add(t);
                t2.getTransfers().add(t);
                tx.commit();
                return 0;
            } else {
                tx.rollback();
                return -1;
            }
        }
    }

    public Transfers listTransfers(int accountId) {
        IgniteCache<Integer, Transfers> cache = Holder.ignite.cache("transfers-grouped-by-account");
        return cache.get(accountId);
    }

    public Account find(int id) {
        IgniteCache<Integer, Account> cache = Holder.ignite.cache("accounts");
        return cache.get(id);
    }

    public void createAccount(Account a) {
        IgniteCache<Integer, Account> cache = Holder.ignite.cache("accounts");
        cache.put(a.getId(), a);
    }

    private long getId() {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] x = md5.digest(UUID.randomUUID().toString().getBytes());
            long result = 0;
            for (int i = 0; i < 8; i++) {
                result <<= 8;
                result |= ((x[i] ^ x[15-i]) & 0xFF);
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            return (long)(Math.random() * Long.MAX_VALUE);
        }
    }

    private static class Holder {
        static Ignite ignite = init();
        private static Ignite init() {
            return Ignition.start("ignite/example-hello-server.xml");
        }
    }
}
