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
    private final long id;
    private final int from;
    private final int to;
    private final int volume;
    private final long ts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transfer transfer = (Transfer) o;

        if (id != transfer.id) return false;
        if (from != transfer.from) return false;
        if (to != transfer.to) return false;
        if (volume != transfer.volume) return false;
        return ts == transfer.ts;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + from;
        result = 31 * result + to;
        result = 31 * result + volume;
        result = 31 * result + (int) (ts ^ (ts >>> 32));
        return result;
    }
}
