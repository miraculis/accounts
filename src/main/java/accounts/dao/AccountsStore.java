package accounts.dao;

import accounts.Account;
import org.apache.ignite.IgniteException;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lifecycle.LifecycleAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.HashMap;
import java.util.Map;

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
        jdbc.update("INSERT INTO ACCOUNTS(ID,AMOUNT) VALUES (:ID,:AMOUNT);", parameterMap);
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
}
