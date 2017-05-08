package accounts;

import org.apache.ignite.*;
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

    public Bank() {
    }

    public int transfer(Transfer t0) {

        IgniteCache<Integer, Account> accounts = Holder.ignite.cache("accounts");
        IgniteCache<Long, Transfer> transfers = Holder.ignite.cache("transfers");

        IgniteTransactions transactions = Holder.ignite.transactions();
        try (Transaction tx = transactions.txStart()) {
            Account a1 = accounts.get(t0.getFrom()), a2 = accounts.get(t0.getTo());

            if ((t0.getVolume()>0 ? a1 : a2).check(t0)) {
                IgniteAtomicSequence seq = Holder.ignite.atomicSequence("transfers-seq", 0, true);

                Transfer t = new Transfer(seq.getAndIncrement(), t0.getFrom(), t0.getTo(),
                        t0.getVolume(), System.currentTimeMillis());
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
        IgniteCache<Long, Transfer> cache = Holder.ignite.cache("transfers");

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
