package pk.edu.pl.pk_wfmi_schedule_notificator.validation;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

public class ChangeValidator {
    private Storage storage = new Storage();

    public List<String> validate(List<String> itemsToValidate) throws IOException, ClassNotFoundException {
        List<String> oldList = storage.readList();

        List<String> substractList = new LinkedList<>(itemsToValidate);

        substractList.removeAll(oldList);

        return substractList;
    }
}
