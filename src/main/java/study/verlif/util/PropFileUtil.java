package study.verlif.util;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by 16508 on 2018/5/10.
 */
public final class PropFileUtil extends Properties {

    private File file;

    public PropFileUtil(String filePath) {
        file = new File(filePath);
        loadFile(file);
    }
    private void loadFile(File file) {
        try {
            if (this.file.exists()) {
                FileInputStream inputStream = new FileInputStream(this.file);
                load(inputStream);
            }
            else {
                if (!this.file.createNewFile()) {
                    throw new IOException("无法创建该文件: " + file.getPath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 添加一个key-value对，放入此对象映射的property文件中
     * 成功返回true，失败返回false
     */
    public Boolean setProperty(String key, String Value){
        try {
            setProperty(key, Value);
            PrintStream print = new PrintStream(this.file);
            list(print);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * 返回所查询property文件中所有的key和value值，并添加进HashMap中
     * 当文件初始化异常时，返回null
     */
    public HashMap<String, String> getProperties() {
        HashMap<String, String> properties = new HashMap<>();
        Enumeration enu = propertyNames();
        while(enu.hasMoreElements())
        {
            String key = (String)enu.nextElement();
            properties.put(key, getProperty(key));
        }
        return properties;
    }

    public boolean store(String FilePath) {
        try {
            store(new FileOutputStream(FilePath), "new PropertiesFile");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
