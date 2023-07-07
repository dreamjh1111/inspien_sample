package inspien.weather.Handler;

import inspien.weather.Entity.WeatherData;
import inspien.weather.Repository.WeatherDataRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class WeatherSaveForDB {
    private final WeatherDataRepository weatherDataRepository;
    private Map<String, WeatherData> weatherDataMap = new HashMap<>();

    public WeatherSaveForDB(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }
    public Optional<WeatherData> findByCity(String city) {
        return weatherDataRepository.findTopByCityOrderByCreatedAtDesc(city);
    }
    public WeatherData saveWeatherData(String city, double temp, double feelsLikeTemp, String weatherDescription) {
        WeatherData weatherData = new WeatherData();
        weatherData.setCity(city);
        weatherData.setTemp(temp);
        weatherData.setFeels_like_temp(feelsLikeTemp);
        weatherData.setWeather(weatherDescription);
        weatherData.setCreatedAt(LocalDateTime.now());

        return weatherDataRepository.save(weatherData);
    }

}
