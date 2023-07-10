package inspien.weather.Dto;

import lombok.Data;

@Data
public class WeatherDataDto {
    private Double temp;
    private Double feels_like_temp;
    private String Weather;
    private Integer humidity;
}
