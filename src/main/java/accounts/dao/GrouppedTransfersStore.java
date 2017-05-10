package accounts.dao;

import accounts.Transfer;
import accounts.Transfers;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Светлана on 10.05.2017.
 */
public class GrouppedTransfersStore  extends CacheStoreAdapter<Integer, Transfers> implements LifecycleAware {
    private NamedParameterJdbcTemplate jdbc;

    @Override
    public Transfers load(Integer key) throws CacheLoaderException {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("id", key);
        Transfers t = new Transfers(key);
        List<Map<String, Object>> load = jdbc.queryForList("SELECT * FROM TRANSFERS WHERE fromId=:id or toId=:id", inputParam);
        load.forEach((map) -> {
            long id = (Long) map.get("ID");
            t.getTransfers().add(new Transfer(id, (Integer) map.get("FROMID"),
                    (Integer) map.get("TOID"), (Integer) map.get("AMOUNT"), (Long) map.get("TS")));
        });
        return t;
    }

    @Override
    public void write(Cache.Entry<? extends Integer, ? extends Transfers> entry) throws CacheWriterException {
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
