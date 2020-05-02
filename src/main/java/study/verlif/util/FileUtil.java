package study.verlif.util;

import java.io.*;

public class FileUtil {

    /**
     * 将文本输出到文件
     * @param text  目标文本字符串
     * @param file  目标输出文件
     * @return  操作是否成功
     */
    public boolean saveTextToFile(String text, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(text.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveObjectToFile(Serializable s, File file) {
        try {
            ObjectOutputStream fileOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            fileOutputStream.writeObject(s);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getStringFromFile(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            return br.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public Object getObjectFromFile(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            Object object = objIn.readObject();
            objIn.close();
            return object;
        } catch (FileNotFoundException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
