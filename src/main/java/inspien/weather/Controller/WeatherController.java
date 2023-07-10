package inspien.weather.Controller;

import inspien.weather.Entity.WeatherData;
import inspien.weather.Service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * URL: https://inspientest.duckdns.org/weather/{city}
 * Method: GET
 * 해당 도시의 날씨정보를 조회
 */
@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{city}")
    public WeatherData getWeatherData(@PathVariable String city) {
        return weatherService.getCurrentWeatherData(city);
    }
}
