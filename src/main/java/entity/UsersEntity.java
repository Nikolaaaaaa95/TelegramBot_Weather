package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "users", schema = "telegram_bot")
public class UsersEntity {
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "city")
    private String city;

    @Basic
    @Column(name = "id_bot")
    private Long idBot;

    @Basic
    @Column(name = "weather_settings")
    private String weatherSettings;

    public Long getIdBot() {
        return idBot;
    }

    public void setIdBot(Long idBot) {
        this.idBot = idBot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeatherSettings() {
        return weatherSettings;
    }

    public void setWeatherSettings(String weatherSettings) {
        this.weatherSettings = weatherSettings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersEntity that = (UsersEntity) o;

        if (id != that.id) return false;
        if (!Objects.equals(username, that.username)) return false;
        return Objects.equals(city, that.city);
    }

    @Override
    public String toString() {
        return "Информация о вас:" + "\n" +
                "Username = " + username + "\n" +
                "City = " + city + "\n" +
                "Weather Settings = " + weatherSettings;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }
}
