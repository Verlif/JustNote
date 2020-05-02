package study.verlif.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.verlif.util.ConsoleUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Result implements Serializable {

    public static final int CODE_SUCCESS = 200;
    public static final int CODE_FAIL = 400;
    public static final int CODE_BAD_NW = 500;

    protected int code;
    protected String msg;
    protected String data;

    public Result(Object object) {
        addObject(object);
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean ok() {
        return code == CODE_SUCCESS;
    }

    public <T> void addList(String key, List<T> list) {
        JSONArray array = new JSONArray();
        array.addAll(list);
        addObject(key, array);
    }

    public void addObject(Object object) {
        addObject(object.getClass().getSimpleName(), object);
    }

    public void addObject(String key, Object object) {
        JSONObject json = JSON.parseObject(data);
        if (json == null) {
            json = new JSONObject();
        }
        json.put(key, object);
        data = json.toJSONString();
    }

    public <T> T getDataObject(String key) {
        JSONObject object = JSON.parseObject(data);
        return (T) object.get(key);
    }

    public <T> T getDataObject(Class<T> cl) {
        JSONObject object = JSON.parseObject(data);
        return object.getObject(cl.getSimpleName(), cl);
    }

    public <T> ArrayList<T> getDataList(String key, Class<T> cl) {
        JSONObject object = JSON.parseObject(data);
        if (object != null) {
            JSONArray array = object.getJSONArray(key);
            ArrayList<T> list = new ArrayList<>();
            int size = array.size();
            for (int i = 0; i < size; i++) {
                JSONObject o = array.getJSONObject(i);

                ConsoleUtil.println(o);

                list.add(o.toJavaObject(cl));
            }
            return list;
        } else return new ArrayList<>();
    }
}
