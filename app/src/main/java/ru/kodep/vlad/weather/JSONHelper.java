package ru.kodep.vlad.weather;

/**
 * Created by vlad on 21.12.17
 */
import android.content.Context;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

class JSONHelper {

    private static final String FILE_NAME = "data.json";

    static boolean exportToJSON(Context context, List<ForeCast> dataList) {

        Gson gson = new Gson();

        ru.kodep.vlad.weather.JSONHelper.DataItems dataItems = new ru.kodep.vlad.weather.JSONHelper.DataItems();
        dataItems.setName(dataList);
        String jsonString = gson.toJson(dataItems);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    static List<ForeCast> importFromJSON(Context context) {

        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(FILE_NAME);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            ru.kodep.vlad.weather.JSONHelper.DataItems dataItems = gson.fromJson(streamReader, ru.kodep.vlad.weather.JSONHelper.DataItems.class);
            return dataItems.getName();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private static class DataItems {
        private List<ForeCast> users;

        List<ForeCast> getName() {
            return users;
        }

        void setName(List<ForeCast> users) {
            this.users = users;
        }
    }
}