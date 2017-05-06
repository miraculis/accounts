package accounts;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * Created by miraculis on 04.05.2017.
 */
@Getter
@AllArgsConstructor
public class Account implements Serializable{
    private final int id;
    private final int amount;

    public boolean check(Transfer t) {
        return amount + t.getVolume() >= 0;
    }
}
