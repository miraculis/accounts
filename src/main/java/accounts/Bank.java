package accounts;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteTransactions;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.transactions.Transaction;
import org.springframework.stereotype.Component;

import javax.cache.Cache;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by miraculis on 04.05.2017.
 */
@Component
public class Bank {
    private int id = 0;

    public Bank() {
    }

    public int transfer(Transfer t0) {
        Transfer t = new Transfer(id++, t0.getFrom(), t0.getTo(),
                t0.getVolume(), System.currentTimeMillis());

        IgniteCache<Integer, Account> accounts = Holder.ignite.cache("accounts");
        IgniteCache<Integer, Transfer> transfers = Holder.ignite.cache("transfers");

        IgniteTransactions transactions = Holder.ignite.transactions();
        try (Transaction tx = transactions.txStart()) {
            Account a1 = accounts.get(t.getFrom()), a2 = accounts.get(t.getTo());

            if ((t.getVolume()>0 ? a1 : a2).check(t)) {
                accounts.put(t.getFrom(), new Account(t.getFrom(), a1.getAmount() - t.getVolume()));
                accounts.put(t.getTo(), new Account(t.getTo(), a2.getAmount() + t.getVolume()));
                transfers.put(t.getId(), t);
                tx.commit();
                return 0;
            } else {
                tx.rollback();
                return -1;
            }
        }
    }

    public List<Transfer> listTransfers(int accountId) {
        List<Transfer> res = new LinkedList<>();
        IgniteCache<Integer, Transfer> cache = Holder.ignite.cache("transfers");

        try (QueryCursor cursor = cache.query(new ScanQuery((id,t) -> {
            Transfer x = (Transfer) t;
            return x.getFrom() == accountId || x.getTo() == accountId;
        }))) {
           cursor.forEach((z)->res.add((Transfer)((Cache.Entry)z).getValue()));
        }
        return res;
    }

    public Account find(int id) {
        IgniteCache<Integer, Account> cache = Holder.ignite.cache("accounts");
        return cache.get(id);
    }

    public void createAccount(Account a) {
        IgniteCache<Integer, Account> cache = Holder.ignite.cache("accounts");
        cache.put(a.getId(), a);
    }

    private static class Holder {
        static Ignite ignite = init();
        private static Ignite init() {
            Ignite ignite = Ignition.start("ignite/example-hello-server.xml");
            ignite.cache("transfers").loadCache((k, v)->true);
            return ignite;
        }
    }
}
