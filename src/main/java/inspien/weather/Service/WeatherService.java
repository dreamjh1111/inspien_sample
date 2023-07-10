package inspien.weather.Service;

import inspien.weather.Dto.WeatherDataDto;
import inspien.weather.Entity.WeatherData;
import inspien.weather.Handler.DbDataValidator;
import inspien.weather.Handler.DbConnectionHandler;
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
    private final DbConnectionHandler weatherSaveForDB;
    private final DbDataValidator dbDataValidator;
    @Autowired
    public WeatherService(WeatherDataRepository weatherDataRepository,
                          @Value("${openweatherapi.key}") String openWeatherApiKey,
                          RestTemplate restTemplate,
                          DbConnectionHandler weatherSaveForDB, DbDataValidator dbDataValidator) {
        this.weatherDataRepository = weatherDataRepository;
        this.openWeatherApiKey = openWeatherApiKey;
        this.restTemplate = restTemplate;
        this.weatherSaveForDB = weatherSaveForDB;
        this.dbDataValidator = dbDataValidator;
    }

    /**
     * @param city
     * @return WeatherData
     * 해당 도시에 Validator를 적용하여 상황에 따라 DB의 데이터를 반환
     * 또는 실시간 날씨를 갱신하여 반환한다 -> (5분이내의 데이터가 DB에 있을경우 바로 반환하므로써 중복 데이터 저장 방지)
     */
    public WeatherData getCurrentWeatherData(String city) {
        Optional<WeatherData> weatherDataDB = weatherSaveForDB.findByCurrentTempByCity(city);
        System.out.println(weatherDataDB);
        if (dbDataValidator.DbPeriodvalidator(weatherDataDB)){
            System.out.println("DB에서 날씨 데이터를 불러왔습니다. :" + weatherDataDB);
            var res = weatherDataDB.get();
            return res;
        }
        var nowWeatherData = getWeatherDataAtOpenAPI(city);
        var weatherData = weatherSaveForDB.saveWeatherData(
                city,
                nowWeatherData.getTemp(),
                nowWeatherData.getFeels_like_temp(),
                nowWeatherData.getWeather(),
                nowWeatherData.getHumidity());

        return weatherData;
    }

    /**
     * 10분마다 주기적으로 cities 리스트에 있는 도시들의 온도를 DB에 저장한다.
     */
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

    /**
     * OpenAPI를 사용하여 해당 도시의 실시간 날씨정보 조회
     */
    private WeatherDataDto getWeatherDataAtOpenAPI(String city){
        var apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&lang=kr&appid=" + openWeatherApiKey;
        ResponseEntity<Map> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, null, Map.class);
        var response = responseEntity.getBody();
        var mainData = (Map<String, Object>) response.get("main");
        var weatherList = (List) response.get("weather");
        var weatherMap = (Map) weatherList.get(0); // 현재 날씨 정보를 포함하는 Map 객체

        // 반환 데이터 저장
        var res = new WeatherDataDto();
        res.setTemp((Double) mainData.get("temp"));
        res.setWeather((String) weatherMap.get("description"));
        res.setFeels_like_temp((Double) mainData.get("feels_like"));
        res.setHumidity((int) mainData.get("humidity"));
        return res;
    }
}
