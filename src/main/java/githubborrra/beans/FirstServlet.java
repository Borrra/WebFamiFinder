
package githubborrra.beans;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import githubborrra.game.GameClass;
import githubborrra.game.GameListClass;
import githubborrra.inet.AddressManager;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* 2. В этот мой Первый Сервлет мы попадаем с Приветственной страницы (с первого Фейслета).
В этот Сервлет я Инжектирую мой бин HelloBean, в котором должны быть заполненны поля
Login и Password, далее работает логика этого Сервлета (т.к. это по сути просто Java Класс)
и в зависимости от того, правильный Логин-Пароль мы ввели, на странице мы видим либо
сообщение об ощибке, либо Таблицу с Играми => т.е. Этот Сервлет:
1. проверяет Логин-Пароль,
2. при правильном вводе показывает Полную Таблиуц Игр
3. дает ссылку на следующий Фейслет (searchPage.xhtml) для продолжения сотировки данного
списка (Таблицы) */

@WebServlet("/hello")
public class FirstServlet extends HttpServlet {

    /* осуществляем Инжекцию Бина в данный Сервлет */

    @Inject
    private HelloBean MyBean;

    /* Класс содержит один переопределенный метод (ниже), а осатльные
    * методы соответственно из супер класса HttpServlet (т.е. присутствуют
    * по умолчанию) */

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        response.setContentType("text/html;charset=UTF-8");

        /* заводим Менеджер */

        AddressManager manag = new AddressManager();

        /* обновляем его актуальные адреса на компе */

        manag = manag.setting();

        /* заводим объект класса GameListClass, для того, чтобы воспользоваться его методом */

        GameListClass list = new GameListClass(manag);

        /* создаем список наших Игр, используя метод readGamesFormFile т.е. читаем с Компа */

        List<GameClass> myList = list.readGamesFromFile();

        /* если надо можем считать этот лист с Инета (GitHub) */

        //List<GameClass> myWebList = list.readGamesFromWeb();

        /* занесем в наш бин получившийся (полный) список */

        MyBean.setGames(list);
        MyBean.setPath("");

        /* заводим экземпляр Класса PrintWriter - out и через него формируем
        * веб-страницу и вставляем туда, нужную нам логику */

        try (PrintWriter out = response.getWriter()) {

            /* организуем логику проверки правильности Лагина и Пароля
            * если Логин и Пароль верны, на экран выведется таблица наших Игр,
            * с указаникм инфы по адресам */

            if (MyBean.getLogin().equals("Borrra") && MyBean.getPassword().equals("1985")) {

                out.println("<html>");
                out.println("<head><title>Full List Page</title></head>");
                out.println("<body>");

                out.println("<p>That's the right Pass Bro!</p>");

                out.println("<p>Инфа по менеджеру:</p>");
                out.println("<p>Адрес Веб-файла: " + manag.webFileAddress+ " </p>");
                out.println("<p>Адрес Комп-файла: " + manag.fileAddress+ " </p>");
                out.println("<p>Рзмер списка: " + myList.size()+ " </p>");

                /* ссылки: на страницу поиска и на начальную страницу */

                out.println("<br/><a href='searchPage.xhtml'>Go to Search panel</a>");

                out.println("<br/>");

                //out.println("<br/><a href='" + request.getContextPath()+ "'>Go to Log/Pass panel</a>");

                out.println("<h2>Game Table</h2>");

                // Create a simple HTML table
                out.println("<table border='1'>");
                out.println("<tr><th>No</th><th>Name</th><th>Creator</th><th>Mapper</th><th>Year</th><th>Comment</th></tr>");

                /* заведем двухмерный массив по нашу Таблицу */

                String [][] data = new String[myList.size()][6];

                /* заполняем Массив данными из моего Списка Игр: за один цикл - одна строка */

                for (int i=0; i<myList.size(); i++) {

                    data[i][0] = String.valueOf(i+1);
                    data[i][1] = myList.get(i).getName();
                    data[i][2] = myList.get(i).getCreator();
                    data[i][3] = myList.get(i).getMapper();
                    data[i][4] = myList.get(i).getYear();
                    data[i][5] = myList.get(i).getComment();
                }

                /* выводим Таблицу на Веб-страницу */

                for (String[] row : data) {

                    out.println("<tr>");

                    for (String cell : row) {
                        out.println("<td>" + cell + "</td>");
                    }

                    out.println("</tr>");
                }

                out.println("</table>");

                /* ссылки: на страницу поиска и на начальную страницу
                * т.е. ссылка на страницу поиска может быть только здесь, в условии
                * когда ввели правильные Пароль и Логин */

                out.println("<br/><a href='searchPage.xhtml'>Go to Search panel</a>");

                out.println("<br/>");

                //out.println("<br/><a href='" + request.getContextPath()+ "'>Go to Log/Pass panel</a>");

            } else { // если Логин или Пароль не верный

                out.println("<html>");
                out.println("<head><title>Wrong Pass Page</title></head>");
                out.println("<body>");

                out.println("<p>You are screwed Man!</p>");
                if (MyBean.getLogin().equals("Borrra")) {

                    out.println("<p>Login: " + MyBean.getLogin() + " is right! But</p>");
                    out.println("<p>Password: " + MyBean.getPassword() + " is wrong</p>");
                }
                else if (MyBean.getPassword().equals("1985")) {

                    out.println("<p>Password: " + MyBean.getPassword() + " is right! But</p>");
                    out.println("<p>Login: " + MyBean.getLogin() + " is wrong!</p>");
                }
                else {

                    out.println("<p>Login: " + MyBean.getLogin() + " are wrong</p>");
                    out.println("<p>Password: " + MyBean.getPassword() + " are wrong</p>");
                }

                /* ссылка на начальную страницу */

                out.println("<br/><a href='" + request.getContextPath()+ "'>Go to Log/Pass panel</a>");
            }

            out.println("</body>");
            out.println("</html>");
        }
    }
}
