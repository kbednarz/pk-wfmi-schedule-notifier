package pk.edu.pl.pk_wfmi_schedule_notificator.validation;


import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

public class ChangeValidator {
    private Storage storage = new Storage();
    // todo: fix method to work on Timetable object
/*
    public List<String> validate(List<String> itemsToValidate) throws IOException, ClassNotFoundException {
        List<String> oldList = storage.readTimetable();

        List<String> substractList = new LinkedList<>(itemsToValidate);

        substractList.removeAll(oldList);

        return substractList;
    }*/
}
