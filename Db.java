import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
* Db
*/
public class Db implements Closeable {
    
    private String fileName;
    private String encoding = "UTF8";
    private Map<String, String> store;
    
    public Db(String file) {
        this.fileName = file;
        this.store = new HashMap<>();
    }
    
    public void open() throws IOException {
        File f = new File(fileName);
        if (!f.exists()) {
            return;
        }
        try (FileInputStream in = new FileInputStream(fileName)) {
            int keyLen = -1;
            while ((keyLen = in.read()) != -1) {
                byte[] keyBytes = new byte[keyLen];
                in.read(keyBytes, 0, keyLen);
                String key = new String(keyBytes, encoding);
                
                int valueLen = in.read();
                if (valueLen == -1) continue;
                
                byte[] valueBytes = new byte[valueLen];
                in.read(valueBytes);
                String value = new String(valueBytes, encoding);
                store.put(key, value);
            }
        }
    }
    
    public void set(String key, String value) throws IOException {
        File f = new File(fileName);
        if (!f.exists()) {
            f.createNewFile();
        }
        try (FileOutputStream out = new FileOutputStream(f, true)) {
            byte[] keyBytes = key.getBytes(encoding);
            byte[] valueBytes = value.getBytes(encoding);
            out.write(keyBytes.length);
            out.write(keyBytes, 0, keyBytes.length);
            out.write(valueBytes.length);
            out.write(valueBytes, 0, valueBytes.length);
            out.flush();
        }
        store.put(key, value);
    }
    
    public String get(String key) {
        
        return store.get(key);
    }

    public Map<String, String> get() {
        return store;
    }

    public void close() {
        this.store.clear();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Db: filename=" + this.fileName + ", content={\n");
        for (String key : store.keySet()) {
            builder.append("\t" + key + "=" + store.get(key) + "\n");
        }
        builder.append("}\n");
        return builder.toString();
    }
}