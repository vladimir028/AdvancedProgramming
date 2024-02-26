package VtorKolokvum_Ispit.WeatherDispatcher;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
//            System.out.println();
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            System.out.println();
            if(parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if(operation==1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if(operation==2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if(operation==3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if(operation==4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}
interface Display{
    void display();
}
interface Update{
    void setMeasurements(float temperature, float humidity, float pressure);
}

interface Subject{
    void register(Update display);
    void remove(Update display);
    void notifyDisplay();
}

class WeatherDispatcher implements Subject{

    Set<Update> updates;
    float temperature;
    float humidity;
    float pressure;

    public WeatherDispatcher() {
        updates = new HashSet<>();
        temperature = 0;
        humidity = 0;
        pressure = 0;
    }



    @Override
    public void register(Update display) {
        updates.add(display);
    }

    @Override
    public void remove(Update display) {
        updates.remove(display);
    }

    @Override
    public void notifyDisplay() {
        for (Update update : updates) {
            update.setMeasurements(temperature, humidity, pressure);
        }
    }
    public void setMeasurements(float temperature, float humidity, float pressure){
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

    private void measurementsChanged() {
        notifyDisplay();
    }
}

class CurrentConditionsDisplay implements Display, Update{

    float temperature;
    float humidity;
    WeatherDispatcher weatherDispatcher;
    public CurrentConditionsDisplay(WeatherDispatcher weatherDispatcher) {
        this.weatherDispatcher = weatherDispatcher;
        weatherDispatcher.register(this);

    }

    @Override
    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        display();

    }

    @Override
    public void display() {
        System.out.println("Temperature: " + temperature + "F");
        System.out.println("Humidity: " + humidity + "%");
    }
}

class ForecastDisplay implements Display, Update{
    float currentPressure;
    float previousPressure;
    WeatherDispatcher weatherDispatcher;

    public ForecastDisplay(WeatherDispatcher weatherDispatcher) {
        this.weatherDispatcher = weatherDispatcher;
        weatherDispatcher.register(this);
        this.currentPressure = 0;
        this.previousPressure = 0;
    }



    @Override
    public void display() {
        System.out.print("Forecast: ");
        if (currentPressure > previousPressure) {
            System.out.println("Improving");
        } else if (currentPressure == previousPressure) {
            System.out.println("Same");
        } else if (currentPressure < previousPressure) {
            System.out.println("Cooler");
        }
    }

    @Override
    public void setMeasurements(float temperature, float humidity, float pressure) {
        previousPressure = currentPressure;
        currentPressure = pressure;
        display();
    }
}
