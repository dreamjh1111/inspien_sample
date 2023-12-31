package inspien.weather.Handler;

import inspien.weather.Entity.WeatherData;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 1. DB에 조회한 도시의 날씨데이터가 있는지 확인
 * 2. DB에 날씨 데이터가 있다면 5분이내의 날씨인지 Validation
 * -> DB에 5분이내의 해당 도시 데이터가 있다면 ture 반환
 */
@Component
public class DbDataValidator {
    public boolean DbPeriodvalidator(Optional<WeatherData> weatherData) {
        if (weatherData.isPresent()) {  // 해당 도시에 대한 데이터가 DB에 저장되어 있을 경우
            LocalDateTime now = LocalDateTime.now();  // 현재 시각
            // 현재 시각과 데이터의 생성 시각(createdAt)의 차이를 계산 (단위: 분)
            long diffInMinutes = Duration.between(weatherData.get().getCreatedAt(), now).toMinutes();

            if (diffInMinutes <= 5) {  // 데이터의 생성 시각이 5분 이내인 경우
                // DB에서 조회한 데이터를 반환
                return true;
            }
        }
        return false;
    }
}
