package accounts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * Created by miraculis on 04.05.2017.
 */
@Getter
@Setter
@AllArgsConstructor
public class Transfer {
    private final int id;
    private final int from;
    private final int to;
    private final int volume;
    private final long ts;
}
