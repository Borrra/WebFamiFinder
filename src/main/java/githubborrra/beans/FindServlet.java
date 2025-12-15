package githubborrra.beans;

import java.io.IOException;
import java.io.PrintWriter;

import githubborrra.game.GameListClass;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* 4. Это 4-ая "Страница" моего приложения или 2-ой Сервлет. Я перехожу сюда
по ссылке из Фейслета ввода слова для сортировки (searchPage.xhtlm)
* здесь мы выводим на экран отсортированный список игр (или издателей) либо список
Издателей, Мапперов или Годов (в зависимости от ввода). Для этого работает просто
логика этого Java класса */

/* Если мы хотим отсортировать и этот список, идем по ссылке 'Go to Search panel' и
* попадаем на Фейслет 'searchPage' (т.е. цикл searchPage-FindServlet как бы замыкается)
* либо идем по ссылке 'Go to Full List' и попадаем на Сервлет FirstServlet (с полным Списком) */


@WebServlet("/find")
public class FindServlet extends HttpServlet {

    /* осуществляем Инжекцию Бина в данный Сервлет */

    @Inject
    private HelloBean MyBean;

    /* Класс содержит один переопределенный метод (ниже), а осатльные
     * методы соответственно из супер класса HttpServlet (по умолчанию) */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        GameListClass Gamelist = MyBean.getGames();

        Gamelist = Gamelist.inputAnalyse(MyBean.getSearch());

        /* передаем наш отсортированный объект Класса GameListClass в MyBean (поле Games)
        * это и обеспечит как-бы цикл бесконечной сортировки */

        MyBean.setGames(Gamelist);

        /* сформируем строку Истории Поиска */

        if (MyBean.getPath() == null || MyBean.getPath().equals("")) {

            MyBean.setPath(MyBean.getSearch());

        } else {

            MyBean.setPath(MyBean.getPath() + " => " + MyBean.getSearch());
        }

        try (PrintWriter out = response.getWriter()) {

            //out.println("<p>Инфа по менеджеру:</p>");
            out.println("<p>Искали по: " + MyBean.getPath() + " </p>");

            /* если введенное - это Издатели, Мапперы или Годы, то выводим этот список на экран */

            if ( (MyBean.getSearch().equals("creators")) || (MyBean.getSearch().equals("издатели")) ||
                    (MyBean.getSearch().equals("mappers")) || (MyBean.getSearch().equals("мапперы")) ||
                    (MyBean.getSearch().equals("years")) || (MyBean.getSearch().equals("года")) || (MyBean.getSearch().equals("годы")) ) {

                out.println("<html>");
                out.println("<head><title>Field List Page</title></head>");
                out.println("<body>");

                /* используем список из поля FieldList. Там список формируется автоамтически, конструктором
                * при создании Объекта, если поиск идет по Издателям, Мапперам или Годам */

                out.println("<p>Рзмер списка: " + Gamelist.getFieldList().size() + " </p>");

                /* ссылки: на страницу поиска и на начальную страницу */

                out.println("<br/><a href='searchPage.xhtml'>Go to Search panel</a>");

                out.println("<br/>");

                out.println("<br/><a href='" + request.getContextPath() + "'>Go to Log/Pass panel</a>");

                out.println("<h2>" + MyBean.getSearch() + " Table</h2>");

                // Создаем простую HTML таблицу с 2-мя колонками

                out.println("<table border='1'>");
                out.println("<tr><th>No</th><th>Name</th></tr>");

                /* заведем двухмерный массив по нашу Таблицу */

                String[][] data = new String[Gamelist.getFieldList().size()][2];

                /* заполняем Массив данными из моего Списка Игр: за один цикл - одна строка */

                for (int i = 0; i < Gamelist.getFieldList().size(); i++) {

                    data[i][0] = String.valueOf(i + 1);
                    data[i][1] = Gamelist.getFieldList().get(i);
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

                /* если введенное - это не Издетели и т.д. происходит общий поиск */

            } else {

                out.println("<html>");
                out.println("<head><title>Sorted List Page</title></head>");
                out.println("<body>");

                out.println("<p>Рзмер списка: " + Gamelist.getGameList().size() + " </p>");

                /* ссылки: на страницу поиска и на начальную страницу */

                out.println("<br/><a href='hello'>Go to Full List</a>");

                out.println("<br/>");

                out.println("<br/><a href='searchPage.xhtml'>Go to Search panel</a>");

                out.println("<br/>");

               // out.println("<br/><a href='" + request.getContextPath() + "'>Go to Log/Pass panel</a>");

                out.println("<h2>Games Table</h2>");

                // Создаем простую HTML таблицу с 6-ю столбцами

                out.println("<table border='1'>");
                out.println("<tr><th>No</th><th>Name</th><th>Creator</th><th>Mapper</th><th>Year</th><th>Comment</th></tr>");

                /* заведем двухмерный массив по нашу Таблицу */

                String[][] data = new String[Gamelist.getGameList().size()][6];

                /* заполняем Массив данными из моего Списка Игр: за один цикл - одна строка */

                for (int i = 0; i < Gamelist.getGameList().size(); i++) {

                    data[i][0] = String.valueOf(i + 1);
                    data[i][1] = Gamelist.getGameList().get(i).getName();
                    data[i][2] = Gamelist.getGameList().get(i).getCreator();
                    data[i][3] = Gamelist.getGameList().get(i).getMapper();
                    data[i][4] = Gamelist.getGameList().get(i).getYear();
                    data[i][5] = Gamelist.getGameList().get(i).getComment();
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

                out.println("<br/>");

               // MyBean.setGames(Gamelist.getGameList());

                //out.println("<br/><a href='searchPage.xhtml'>Go to Search panel</a>");

                //out.println("<br/><a href='/webFamiThird/subsearchPage.xhtml'>Go to SubSearch panel</a>");
                out.println("<br/>");

            }

            /* ссылки: на страницу поиска и на начальную страницу */

            out.println("<br/><a href='searchPage.xhtml'>Go to Search panel</a>");

            out.println("<br/>");

            out.println("<br/><a href='hello'>Go to Full List</a>");

            out.println("<br/>");

            out.println("<br/><a href='" + request.getContextPath()+ "'>Go to Log/Pass panel</a>");

            out.println("<br/>");

            //out.println("<br/><a href='subsearchPage.xhtml'>Go to SubSearch panel</a>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}
