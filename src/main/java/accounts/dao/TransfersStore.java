package accounts.dao;

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

/**
 * Created by miraculis on 06.05.2017.
 */
public class TransfersStore extends CacheStoreAdapter<Long, Transfer> implements LifecycleAware {
    private NamedParameterJdbcTemplate jdbc;

    @Override
    public Transfer load(Long key) throws CacheLoaderException {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("id", key);
        return jdbc.queryForObject("SELECT * FROM TRANSFERS WHERE id=:id", inputParam,
                (rs, i) -> new Transfer(rs.getLong(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getLong(5)));
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends Transfer> entry) throws CacheWriterException {
        Transfer account = entry.getValue();
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("ID", account.getId());
        parameterMap.put("FROM", account.getFrom());
        parameterMap.put("TO", account.getTo());
        parameterMap.put("AMOUNT", account.getVolume());
        parameterMap.put("TS", account.getTs());
        jdbc.update("INSERT INTO TRANSFERS(ID,FROMID,TOID,AMOUNT,TS) VALUES (:ID,:FROM,:TO,:AMOUNT,:TS);", parameterMap);
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
    public void loadCache(IgniteBiInClosure<Long, Transfer> clo, Object... args) {
        List<Map<String,Object>> load = jdbc.queryForList("SELECT * FROM TRANSFERS", new HashMap());
        load.forEach((map)-> {
            long id = (Long)map.get("ID");
            clo.apply(id, new Transfer(id, (Integer)map.get("FROMID"),
                    (Integer)map.get("TOID"), (Integer)map.get("AMOUNT"), (Long)map.get("TS")));
        });
    }
}

