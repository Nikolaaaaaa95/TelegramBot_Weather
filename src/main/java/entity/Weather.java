package entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    public static String getWeather(String message, String day) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=" + message + "&units=metric&appid=35ab5c2d657c4736dc14b4dafbf869d3");

        Scanner sc = new Scanner((InputStream) url.getContent());
        ModelWeather model = new ModelWeather();
        String resultJSON = "";
        String result = "";
        while (sc.hasNext()) {
            resultJSON += sc.nextLine();
        }

        JSONObject jsonObject = new JSONObject(resultJSON);
        model.setCity(message);
        result = "Город: " + model.getCity() + "\n" +
                "Погода на " + day + ": ";
        for (int i = 0; i < 33; i += 8) {
            if (day.equals("Завтра")) {
                i = 8;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            JSONObject jsonObjectList = jsonArray.getJSONObject(i);
            if (day.equals("5 дней"))
            {
                model.setData(jsonObjectList.getString("dt_txt"));
                result += "\n" + model.getData() + ": ";
            }
            JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
            JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
            JSONObject jsonWeather = jsonArrayWeather.getJSONObject(0);
            model.setMain(jsonWeather.getString("main"));
            model.setDescription(jsonWeather.getString("description"));
            model.setTemp(jsonObjectMain.getDouble("temp"));
            model.setHumidity(jsonObjectMain.getDouble("humidity"));


            result += model.getMain() + "\n" +
                    "Описание: " + model.getDescription() + "\n" +
                    "Температура: " + model.getTemp() + "C" + "\n" +
                    "Влажность: " + model.getHumidity() + "\n";
            if (day.equals("Сегодня") & i == 0) return result;
            if (day.equals("Завтра") & i == 8) return result;
            result += "\n";
        }
        return result;
    }
}
