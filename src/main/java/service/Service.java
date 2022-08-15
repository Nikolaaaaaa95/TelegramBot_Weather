package service;

import entity.Message;
import entity.UsersEntity;
import entity.Weather;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.Random;

public class Service {

    UsersEntity userEntity;

    public static String updateMessage(String message, Update update) throws IOException {
        Service service = new Service();
        Message messageInfo = new Message();
        StringBuilder resultSB = new StringBuilder();
        String day = "Сегодня";

        if (message != null && !message.isEmpty()) {
            switch (message) {
                case "Погода", "погода" -> {
                    String str = service.getCityUser(update.getMessage().getFrom().getId());
                    day = service.getWeatherSettings(update.getMessage().getFrom().getId());
                    if (str != null & day != null)
                        resultSB = Weather.getWeather(str, day);
                    else
                        resultSB = new StringBuilder("Погода? Я умею искать прогноз погоды! Напиши мне название города.");
                }
                case "/start" -> {
                    resultSB = new StringBuilder("Привет! Я умею искать погоду. Введи название города!");
                }
                case "/settings", "Settings" -> {
                    resultSB = new StringBuilder("Что будем настраивать?");
                }
                case "UserName", "/username" -> {
                    messageInfo.writeOnFile(update.getMessage().getMessageId(), "UN");
                    resultSB = new StringBuilder("Введи свое имя!");
                }
                case "City", "/city", "Город" -> {
                    messageInfo.writeOnFile(update.getMessage().getMessageId(), "C");
                    resultSB = new StringBuilder("Введи свой город!");
                }
                case "Мяу", "мяу", "мур", "мрр", "мяяу", "мрррр", "мурр" -> {
                    Random random = new Random();
                    int i = random.nextInt(2);
                    if (i == 0)
                        resultSB = new StringBuilder("Мяу");
                    else resultSB = new StringBuilder("Не передергивай меня!");
                }
                case "/info", "Info" -> {
                    UsersEntity usersEntity = service.getUserInfo(update.getMessage().getFrom().getId());
                    if (usersEntity == null)
                        resultSB = new StringBuilder("О вас нет никакой информации. Зайдите в Settings и добавте информацию о себе!");
                    else
                        resultSB = new StringBuilder(usersEntity.toString());
                }
                case "Back" -> {
                    resultSB = new StringBuilder("Главное меню");
                }
                case "Weather Settings" -> {
                    resultSB = new StringBuilder("На какой день вам необходима погода?");
                }
                case "Сегодня", "Завтра", "5 дней" -> {
                    service.addOrUpdateWeatherSettings(message, update.getMessage().getFrom().getId());
                    resultSB = new StringBuilder("Done!");
                }
                default -> {
                    try {
                        resultSB = Weather.getWeather(message, day);
                        messageInfo.cleanFile();
                    } catch (IOException e) {
                        resultSB = new StringBuilder("Я не знаю такого города!");
                    }
                }
            }

            if (messageInfo.checkAPI(update.getMessage().getMessageId() - 2 + "UN")) {
                service.addOrUpdateUsername(message, update.getMessage().getFrom().getId());
                resultSB = new StringBuilder("Done!");
            }
            if (messageInfo.checkAPI(update.getMessage().getMessageId() - 2 + "C")) {
                service.addOrUpdateCity(message, update.getMessage().getFrom().getId());
                resultSB = new StringBuilder("Done!");
            }

        }

        return String.valueOf(resultSB);
    }

    public UsersEntity getUserInfo(long id) {

        try (Session session = getSession()) {
            session.beginTransaction();

            Query<UsersEntity> query = session.createQuery("from UsersEntity where idBot = :idMethod", UsersEntity.class).setParameter("idMethod", id);
            userEntity = query.getSingleResult();
            session.getTransaction().commit();
        }
        return userEntity;
    }

    public void addOrUpdateUsername(String message, long id) {
        try (Session session = getSession()) {
            session.beginTransaction();
            Query<UsersEntity> query = session.createQuery("from UsersEntity where idBot = :idMethod", UsersEntity.class).setParameter("idMethod", id);
             if (query.stream().findAny().isEmpty()) {
                userEntity = new UsersEntity();
                userEntity.setIdBot(id);
            }
             else userEntity = query.getSingleResult();
            userEntity.setUsername(message);
            session.persist(userEntity);
            session.getTransaction().commit();
        }
    }

    public void addOrUpdateCity(String message, long id) {
        try (Session session = getSession()) {
            session.beginTransaction();

            Query<UsersEntity> query = session.createQuery("from UsersEntity where idBot = :idMethod", UsersEntity.class).setParameter("idMethod", id);
            if (query.stream().findAny().isEmpty()) {
                userEntity = new UsersEntity();
                userEntity.setIdBot(id);
            }
            else userEntity = query.getSingleResult();
            userEntity.setCity(message);
            session.persist(userEntity);
            session.getTransaction().commit();
        }
    }

    public void addOrUpdateWeatherSettings(String message, long id) {
        try (Session session = getSession()) {
            session.beginTransaction();

            Query<UsersEntity> query = session.createQuery("FROM UsersEntity where idBot =: idMethod", UsersEntity.class).setParameter("idMethod", id);
            if (query.stream().findAny().isEmpty()) {
                userEntity = new UsersEntity();
                userEntity.setIdBot(id);
            }
            else userEntity = query.getSingleResult();
            userEntity.setWeatherSettings(message);
            session.persist(userEntity);
            session.getTransaction().commit();
        }
    }

    public String getWeatherSettings(long id) {
        try (Session session = getSession()) {
            session.beginTransaction();
            Query<UsersEntity> list = session.createQuery("FROM UsersEntity where idBot =: idMethod", UsersEntity.class).setParameter("idMethod", id);
            userEntity = list.getSingleResult();
            session.getTransaction().commit();
        }
        return userEntity.getWeatherSettings();
    }

    public String getCityUser(long id) {
        try (Session session = getSession()) {
            session.beginTransaction();
            Query<UsersEntity> query = session.createQuery("from UsersEntity where idBot = :idMethod", UsersEntity.class).setParameter("idMethod", id);
            userEntity = query.getSingleResult();
            session.getTransaction().commit();

            return userEntity.getCity();
        }

    }

    public Session getSession() {
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(UsersEntity.class).buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        return session;
    }
}
