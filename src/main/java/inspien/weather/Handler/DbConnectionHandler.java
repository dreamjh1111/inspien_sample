package inspien.weather.Handler;

import inspien.weather.Entity.WeatherData;
import inspien.weather.Repository.WeatherDataRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * DB에 관련된 로직을 수행하는 Handler
 */
@Component
public class DbConnectionHandler {
    private final WeatherDataRepository weatherDataRepository;
    private Map<String, WeatherData> weatherDataMap = new HashMap<>();

    public DbConnectionHandler(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }
    public Optional<WeatherData> findByCurrentTempByCity(String city) {
        return weatherDataRepository.findTopByCityOrderByCreatedAtDesc(city);
    }
    public WeatherData saveWeatherData(String city, double temp, double feelsLikeTemp, String weatherDescription, int humidity) {
        WeatherData weatherData = new WeatherData();
        weatherData.setCity(city);
        weatherData.setTemp(temp);
        weatherData.setFeels_like_temp(feelsLikeTemp);
        weatherData.setWeather(weatherDescription);
        weatherData.setHumidity(humidity);
        weatherData.setCreatedAt(LocalDateTime.now());
        System.out.println(city + " 날씨 데이터 저장 완료: " + weatherData);

        return weatherDataRepository.save(weatherData);
    }
}
