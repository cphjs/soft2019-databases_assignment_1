import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
* Db
*/
public class Db implements Closeable {
    
    private String fileName;
    private String encoding = "UTF8";
    private Map<String, Integer> store;
    
    public Db(String file) {
        this.fileName = file;
        this.store = new HashMap<>();
    }
    
    public void open() throws IOException {
        File f = new File(fileName);
        if (!f.exists()) {
            return;
        }
        try (RandomAccessFile in = new RandomAccessFile(fileName, "r")) {
            try {
                while (true) {
                    int keyLen = in.readInt();
                    byte[] keyBytes = new byte[keyLen];
                    in.read(keyBytes);
                    String key = new String(keyBytes, encoding);

                    store.put(key, (int)in.getFilePointer());

                    int valueLen = in.readInt();
                    in.skipBytes(valueLen);
                } 
            } catch (EOFException e) {

            }
        }
    }
    
    public void set(String key, String value) throws IOException {
        File f = new File(fileName);
        if (!f.exists()) {
            f.createNewFile();
        }
        try (RandomAccessFile out = new RandomAccessFile(f, "rw")) {
            out.seek(out.length());
            byte[] keyBytes = key.getBytes(encoding);
            byte[] valueBytes = value.getBytes(encoding);
            out.writeInt(keyBytes.length);
            out.write(keyBytes, 0, keyBytes.length);
            long offset = out.getFilePointer();
            out.writeInt(valueBytes.length);
            out.write(valueBytes, 0, valueBytes.length);
            
            
            store.put(key, (int)offset);
        }
    }
    
    public String get(String key) throws IOException {
        try (RandomAccessFile in = new RandomAccessFile(fileName, "r")) {
            Integer offset = store.get(key);
            if (offset == null) return null;
            in.seek(offset);
            int valueLen = in.readInt();
            byte[] valueBytes = new byte[valueLen];
            in.read(valueBytes);
            return new String(valueBytes, encoding);
        }
    }
    
    public void close() {
        this.store.clear();
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder("Db: filename=" + this.fileName + ", content={\n");
        for (String key : store.keySet()) {
            try {
                builder.append("\t" + key + "=" + this.get(key) + "\n");
            } catch (IOException e) {
                System.err.println("Could not read value for key " + key + ": " + e.getMessage());
            }
        }
        builder.append("}\n");
        return builder.toString();
    }
}