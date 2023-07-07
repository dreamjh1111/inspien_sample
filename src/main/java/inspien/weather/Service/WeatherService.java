package inspien.weather.Service;

import inspien.weather.Entity.WeatherData;
import inspien.weather.Handler.DbDataValidator;
import inspien.weather.Handler.WeatherSaveForDB;
import inspien.weather.Repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WeatherService {
    private final WeatherDataRepository weatherDataRepository;
    private final String openWeatherApiKey;
    private final RestTemplate restTemplate;
    private final WeatherSaveForDB weatherSaveForDB;
    private final DbDataValidator dbDataValidator;
    @Autowired
    public WeatherService(WeatherDataRepository weatherDataRepository,
                          @Value("${openweatherapi.key}") String openWeatherApiKey,
                          RestTemplate restTemplate,
                          WeatherSaveForDB weatherSaveForDB, DbDataValidator dbDataValidator) {
        this.weatherDataRepository = weatherDataRepository;
        this.openWeatherApiKey = openWeatherApiKey;
        this.restTemplate = restTemplate;
        this.weatherSaveForDB = weatherSaveForDB;
        this.dbDataValidator = dbDataValidator;
    }

    public WeatherData getCurrentWeatherData(String city) {

        Optional<WeatherData> weatherData_test = weatherSaveForDB.findByCity(city);
        System.out.println(weatherData_test);
        if (dbDataValidator.DbPeriodvalidator(weatherData_test)){
            System.out.println("이건 DB에 저장된 데이터 불러온거 :" + weatherData_test);
            var res = weatherData_test.get();
            return res;
        }

        var apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&lang=kr&appid=" + openWeatherApiKey;
        ResponseEntity<Map> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, null, Map.class);
        var response = responseEntity.getBody();
        var res_main = (Map<String, Object>) response.get("main");
        var weatherList = (List) response.get("weather");
        var weatherMap = (Map) weatherList.get(0); // 현재 날씨 정보를 포함하는 Map 객체
        var weather = (String) weatherMap.get("description"); // 날씨 정보 중 description을 String 형태로 가져옴
        var temp = (Double) res_main.get("temp");
        var feelsLikeTemp = (Double) res_main.get("feels_like");

        var weatherData = weatherSaveForDB.saveWeatherData(city, temp, feelsLikeTemp, weather);
        System.out.println(city + " 날씨 데이터 저장 완료: " + weatherData);

        return weatherData;
    }


    @Scheduled(fixedRate = 600 * 1000) // 10분마다 실행
    public void saveCurrentWeatherDataPeriodically() {
        List<String> cities = List.of("seoul","London");

        for (String city : cities) {
            try {
                var weatherData = getCurrentWeatherData(city);
            } catch (Exception e) {
                System.out.println(city + " 날씨 데이터 저장 중 오류 발생: " + e.getMessage());
            }
        }
    }
}
