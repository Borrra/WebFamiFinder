package githubborrra.beans;

/* в данном Классе (Бине) содержаться только 6 полей и Геттеры/Сеттеры к ним
* т.е. эти поля можно заполнять и получать. Соответственно заполняем мы их
* в greeting.xhtml, а получаем эти значения в MyServlet и применяем к ним какую-то
* логику */

/* Т.е. этот Класс-бин нигде не "виден" он является как-бы большой Глобальной
* переменной, которую видят все элементы приложения, а именно 2 моих Фейслета
* и 2 моих Сервлета. Т.е. служит для хранения и обновления данных: вводим (т.е.
* обновляем) данные в Фейслетах, видим что получилось в Сервлетах, а остальные
* Классы моего приложения (3 пакета и 13 Классов в них) взаимодействуют с данными
* этого бина и осуществляют Логику приложения */

import githubborrra.game.GameListClass;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@SessionScoped
@Named
public class HelloBean implements Serializable {

    /* у нас есть всего 6 полей */

    private String login;
    private String password;
    private boolean cool;
    private String search;
    private String path;
    private GameListClass games;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public GameListClass getGames() {
        return games;
    }

    public void setGames(GameListClass games) {
        this.games = games;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
    /* на каждое поле делаем по Геттеру и Сеттеру */

    public boolean isCool() {
        return cool;
    }

    public void setCool(boolean cool) {
        this.cool = cool;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // //////////// конец гетторов-сеттеров ///////////////// //

    /* далее могут идти различные Методы */

    /* т.е. наш бин не содержит никаких Методов, только 3 поля и геттеры-сеттеры к ним,
    * мы просто заполняем поля этого бина, а потом считываем их когда надо */


}
