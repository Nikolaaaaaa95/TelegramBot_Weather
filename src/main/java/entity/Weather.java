package entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    public static StringBuilder getWeather(String message, String day) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=" + message + "&units=metric&appid=35ab5c2d657c4736dc14b4dafbf869d3");

        Scanner sc = new Scanner((InputStream) url.getContent());
        ModelWeather model = new ModelWeather();
        String resultJSON = "";
        StringBuilder result;
        while (sc.hasNext()) {
            resultJSON += sc.nextLine();
        }

        JSONObject jsonObject = new JSONObject(resultJSON);
        model.setCity(message);
        result = new StringBuilder("Город: " + model.getCity() + "\n" +
                "Погода на " + day + ": ");
        for (int i = 0; i < 33; i += 8) {
            if (day.equals("Завтра")) {
                i = 8;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            JSONObject jsonObjectList = jsonArray.getJSONObject(i);
            if (day.equals("5 дней"))
            {
                model.setData(jsonObjectList.getString("dt_txt"));
                result.append("\n").append(model.getData()).append(": ");
            }
            JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
            JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
            JSONObject jsonWeather = jsonArrayWeather.getJSONObject(0);
            model.setMain(jsonWeather.getString("main"));
            model.setDescription(jsonWeather.getString("description"));
            model.setTemp(jsonObjectMain.getDouble("temp"));
            model.setHumidity(jsonObjectMain.getDouble("humidity"));


            result.append(model.getMain()).append("\n").append("Описание: ").append(model.getDescription()).append("\n").append("Температура: ").append(model.getTemp()).append("C").append("\n").append("Влажность: ").append(model.getHumidity()).append("\n");
            if (day.equals("Сегодня") & i == 0) return result;
            if (day.equals("Завтра") & i == 8) return result;
            result.append("\n");
        }
        return result;
    }
}
