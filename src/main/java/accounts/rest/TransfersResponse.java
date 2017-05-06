package accounts.rest;

import accounts.Transfer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by miraculis on 06.05.2017.
 */
@Getter
@AllArgsConstructor
public class TransfersResponse implements Serializable {
    private List<Transfer> transfers;
}
