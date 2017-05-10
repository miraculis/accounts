package accounts;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Светлана on 10.05.2017.
 */
@Getter
public class Transfers implements Serializable {
    private int id;
    private List<Transfer> transfers = new LinkedList<>();

    public Transfers(int id) {
        this.id = id;
    }
}
