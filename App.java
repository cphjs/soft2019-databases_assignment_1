import java.io.IOException;
import java.util.Date;

/**
* App
*/
public class App {
    
    public static void main(String[] args) {
        String file = "thing.db";
        if (args.length > 0) {
            file = args[0];
        }
        try (Db db = new Db(file)) {
            db.open();
            System.out.println("Created database file: " + file);
            db.set("key1", "Value1");
            db.set("date", new Date().toString());
            db.set("key3", "value3");
            System.out.println(db);

            System.out.println("Re-reading database file");
            db.close();
            db.open();
            
            System.out.println("Value for date: " + db.get("date"));
            System.out.println(db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}