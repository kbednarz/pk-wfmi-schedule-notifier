package pk.edu.pl.pk_wfmi_schedule_notificator.storage;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class Storage {
    private static final String PREFS_NAME = "STORAGE.tmp";

    public void saveList(List<String> list) throws IOException {
        FileOutputStream fos = new FileOutputStream(PREFS_NAME);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(list);
        oos.close();
    }

    public List<String> readList() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(PREFS_NAME);
        ObjectInputStream ois = new ObjectInputStream(fis);
        List<String> list = (List<String>) ois.readObject();
        ois.close();

        return list;
    }
}
