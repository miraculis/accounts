package accounts.dao;

import accounts.Account;
import accounts.Transfer;
import org.apache.ignite.IgniteException;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.lifecycle.LifecycleAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by miraculis on 06.05.2017.
 */
public class AccountsStore extends CacheStoreAdapter<Integer, Account> implements LifecycleAware {
    private NamedParameterJdbcTemplate jdbc;


    @Override
    public Account load(Integer key) throws CacheLoaderException {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("id", key);
        return jdbc.queryForObject("SELECT * FROM ACCOUNTS WHERE id=:id", inputParam, (rs, i) -> new Account(rs.getInt(1), rs.getInt(2)));
    }

    @Override
    public void write(Cache.Entry<? extends Integer, ? extends Account> entry) throws CacheWriterException {
        Account account = entry.getValue();
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("ID", account.getId());
        parameterMap.put("AMOUNT", account.getAmount());
        jdbc.update("UPDATE ACCOUNTS SET AMOUNT = :AMOUNT WHERE ID = :ID", parameterMap);
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
    }

    @Override
    public void start() throws IgniteException {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("jdbc-context.xml");
        jdbc = context.getBean(NamedParameterJdbcTemplate.class);
    }

    @Override
    public void stop() throws IgniteException {
    }

    @Override
    public void loadCache(IgniteBiInClosure<Integer, Account> clo, Object... args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future f = executorService.submit(() -> {
            List<Map<String, Object>> load = jdbc.queryForList("SELECT * FROM ACCOUNTS", new HashMap());
            load.forEach((map) -> {
                int id = (Integer) map.get("ID");
                clo.apply(id, new Account(id, (Integer) map.get("AMOUNT")));
            });
            executorService.shutdown();
        });
    }

}
