package accounts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by miraculis on 04.05.2017.
 */
@Getter
@AllArgsConstructor
@ToString
public class Account implements Serializable{
    private final int id;
    private final int amount;

    public boolean check(Transfer t) {
        return amount - t.getVolume() >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) return false;
        return amount == account.amount;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + amount;
        return result;
    }
}
