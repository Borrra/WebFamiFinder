
package githubborrra.game;

import githubborrra.service.ServiceMethods;
import githubborrra.service.MyNameCompNew;
import githubborrra.service.NumberedListCellRenderer;
import githubborrra.inet.AddressManager;
import githubborrra.inet.GitHubSynchronize;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import javax.swing.*;

/* Класс с одним Методом, который организует Список Объектов, который он считывает из текстового файла, расположенного
 * на компьютере. Т.е. по адресу текстового файла читает его и формирует из него Список Объектов (Игр). Применяется
 * в главном методе в Классе MyGameSearcher при формировании Массива Объектов */

public class GameListClass {

	private static int ind; // это переменная для вывода номера выбранного элемента из приемника
	
	/////////////////////////////// Поля //////////////////////////////////////

	private List<GameClass> GameList; // список Игр

	private List<String> FieldList;   // список отсортированный по Полю (Издатели, Мапперы и др.)

	public boolean inet;		 // откуда берутся файлы
	
	public int level;          	 // уровень сортировки
	public int starCode;       	 // наличие введенного кода *_ _

	public String fieldName;   	 // под "издетелей"
	public String searchTrace; 	 // для отслуживания "истории" поиска
	public String key;         	 // то, по чему сортируем список

	public String fileListAddress;  // текущий адрес Текст файла
	public String photoListAddress; // текущий адрес папки с фотками

	/* Конструктор Основной и единственный для формирования Полного Объекта */

	public GameListClass(AddressManager manag) {

		level = 1;
		starCode = 0;
		inet = manag.readFrom;

		fieldName = "";
		searchTrace = "All Games";
		key = "All Games";

		FieldList = new ArrayList<String>();
		
		/* если читаем с Инета и Инет есть */
		
		if ( manag.readFrom && manag.isInetHere )  {

			fileListAddress = manag.webFileAddress;
			photoListAddress = manag.webPhotoAddress;
			GameList = readGamesFromWeb();  // читаем с Инета
			
		} else { // либо Инета нет, либо читаем с Компа
			
			inet = false; // читаем с компа
			
			/* если оба из адресов отсутствует надо обновлять Менеджер */
			
			if ( manag.fileAddress.equals("") && manag.photoFolderAddress.equals("") ) {
				
				this.key = "refreshManag"; // чтобы попасть на обновление Менеджера в main
				
			} else { // если все адреса есть 
				
				fileListAddress = manag.fileAddress;
				photoListAddress = manag.photoFolderAddress;
				GameList = readGamesFromFile(); // читаем с Компа
			}
		}
	}

	/* 4 Strrer-а */
	
	/* 1. Сеттер "Издетелей" - буду использовать в InputAnalyse, когда есть
	 * совпадение по Полям (creators, mappers, years) */
	
	GameListClass setFieldList (List<GameClass> a, String c, String b, int d) {
		
		GameListClass list = this;
		
		list.level = d;
		list.starCode = 0;

		list.fieldName = c;
		list.searchTrace = b;
		list.key = "";
		
		list.GameList = a;
		list.FieldList = ServiceMethods.getFieldListNew(a, c);
		
		return list;
	}
	
	/* 2. Сеттер Списка Игр - буду использовать в методе objectSorting */
	
	GameListClass setGameList (List<GameClass> a, int b, String c, String d) {
		
		GameListClass list = this;
		
		list.level = b;
		list.starCode = 0;

		list.fieldName = "";
		list.searchTrace = d;
		list.key = c;

		list.GameList = a;
		list.FieldList = new ArrayList<String>();
		
		return list;
	}
	
	/* 3. Метод восстанавливающий Полный список игр.
	 * Буду использовать в mouseChooseWindow() */
	
	GameListClass setFullList () {
		
		GameListClass list = this;
		
		list.level = 1;
		list.starCode = 0;

		list.fieldName = "";
		list.searchTrace = "All Games";
		list.key = "All Games";

		list.FieldList = new ArrayList<String>();
		
		/* если читаем с Инета и инет есть */
		
		if ( list.inet && AddressManager.isInetAvalible() ) {
			
			GameList = readGamesFromWeb();  // читаем с Инета
			
		} else { // если читаем с Компа или Инета нет

			GameList = readGamesFromFile(); // читаем с Компа
		}
		
		return list;
	}
	
	/* 4. Метод "Пустой" использую для закрытия программы
	 * если ТекстФайл не нашелся в методе inputAnalyse */
	
	GameListClass setEndList () {
		
		GameListClass list = this;
		
		list.key = "end";
		
		return list;
	}
	
	/* Getter-ы */

	public List <GameClass> getGameList () {
		
		return this.GameList;
	}
	
	public List <String> getFieldList () {
		
		return this.FieldList;
	}
	
	public String getKey() {

		return key;
	}

	public int getLevel() {

		return level;
	}

	/* Setter-ы (пока не использую, т.к. все поля открыты) */

	public void setAddInput(String k) {

		this.fieldName = k;
	}

	public void setKey(String k) {

		this.key = k;
	}

	/* 1.1. Наш метод который будет считывать данные (с файла на компе),
	 * формировать Коллекцию Игр. Применяю в Конструкторе №1 */

	public List<GameClass> readGamesFromFile() {

		List<GameClass> games = new ArrayList<>();
		
		/* здесь нужно вообще сделать проверку адреса и если файл существует
		 * только тогда, начинать считывание */
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileListAddress))) {

			/* заводим 3 переменные, перед циклом, значит они "глобальные" по отношению к
			 * циклу. В result будем объеденять строки, относящиеся к одному объекту */

			String line;
			String result = "";
			int count = 0;

			/* каждая итерация цикла это чтение одной стоки. Соответственно, ставим условия
			 * и тем самым что-то игнорируем и начинаем цикл заново, что-то обрезаем и т.д. */

			while ((line = br.readLine()) != null) {

				// считываем Строку без учета пробелов вначале или вконце

				line = line.trim();

				// если Строка пустая или начинается с // (но не заканчивается на "*/") -
				// пропускаем ее

				if (line.isEmpty() || (line.startsWith("//") && (!line.endsWith("*/")))) {
					continue;
				}

				///////////// Блок, реализующий игнорирование кода в /* .... */ /////////////

				if ((line.startsWith("/*")) && (line.endsWith("*/"))) {

					continue;
				}

				if (line.startsWith("/*")) {

					count = 1;
					continue;
				}

				if (((count == 1) && (line.endsWith(","))) ||

						((count == 1) && (line.endsWith(";"))) ||

						((count == 1) && (line.isEmpty())) ||

						((count == 1) && (line.startsWith("//")) && (!line.endsWith("*/")))) {

					continue;
				}

				if ((count == 1) && (line.endsWith("*/"))) {

					count = 0;
					continue;
				}

				///////////////////////////////////////////////////////////////////////////////

				// если Строка кончается на "," значит дальше будет адрес Фотки, надо объеденять
				// строки

				if (line.endsWith(",")) {
					result += line.trim() + " ";
					continue;
				}

				// если Строка кончается на ";" значит это окончательный конец строки, надо
				// заканчивать объеденение

				if (line.endsWith(";")) {
					line = line.substring(0, line.length() - 1); // удаляем эти ;
					result += line.trim() + " ";
					line = result;
					result = "";
					// continue;
				}

				/* разделяем Строку на подстроки, "," - разделитель и упаковываем их в Массив
				 * Строк */
				/* т.е создаем Массив Строк, в котором каждый элемент (каждая строка) это
				 * значение нашего Поля передаваемое Конструктору. В зависимости от количества
				 * полей (переменная h), выбирается соответствующий Конструктор, их всего 6 */

				String[] parts = line.split(",");

				int h = parts.length; // переменная для отслеживания количества Полей считанных для одного Объекта.

				// в каждой Подстроке удаляем Пробелы (если были) вначале или вконце

				for (int i = 0; i < h; i++) {
					parts[i] = parts[i].trim();
				}

				// если Подстрока начинается с ", удаляем этот символ

				for (int i = 0; i < h; i++) {
					if (parts[i].startsWith("\"")) {
						parts[i] = parts[i].substring(1);
					}
				}

				// если Подстрока кончается на ", удаляем этот символ

				for (int i = 0; i < h; i++) {
					if (parts[i].endsWith("\"")) {
						parts[i] = parts[i].substring(0, parts[i].length() - 1);
					}
				}

				/////////// блок формирования Объектов из считаных строк /////////////////

				/* в зависимости от количества считанных частей (h) применяем соответствующий
				 * Конструктор */

				if (h == 1) { // 3 фотки по умолчанию

					String name = parts[0];

					GameClass person = new GameClass(photoListAddress, name);
					games.add(person);
				}

				else if (h == 2) { // 3 фотки по умолчанию

					String name = parts[0];
					String creator = parts[1];

					GameClass person = new GameClass(photoListAddress, name, creator);
					games.add(person);
				}

				else if (h == 3) { // 3 фотки по умолчанию

					String name = parts[0];
					String creator = parts[1];
					String year = parts[2];

					GameClass person = new GameClass(photoListAddress, name, creator, year);
					games.add(person);
				}

				else if (h == 4) { // 3 фотки по умолчанию

					String name = parts[0];
					String creator = parts[1];
					String mapper = parts[2];
					String year = parts[3];

					GameClass person = new GameClass(photoListAddress, name, creator, mapper, year);
					games.add(person);
				}

				else if (h == 5) { // 3 фотки по умолчанию

					String name = parts[0];
					String creator = parts[1];
					String mapper = parts[2];
					String year = parts[3];
					String comment = parts[4];

					GameClass person = new GameClass(photoListAddress, name, creator, mapper, year, comment);
					games.add(person);
				}

				/*
				 * благодаря этому блоку else стало возможно просматривать бесконечное число
				 * фоток одной игры. Т.е. сколько фоток будет столько и прочитаем
				 */

				else { // 1 и более кастомных фотки

					String name = parts[0];
					String creator = parts[1];
					String mapper = parts[2];
					String year = parts[3];
					String comment = parts[4];

					String[] pik = new String[h - 5]; // создаем массив строк для Адресов фоток

					/* заполняем Массив pik только Адресами фоток, считанных из файла */

					for (int i = 5; i < parts.length; i++) {

						//pik[i - 5] = manag.photoFolderAddress + parts[i];
						pik[i - 5] = parts[i];
					}

					/* создаем объект используя "универсальный" конструктор */

					GameClass person = new GameClass(photoListAddress, name, creator, mapper, year, comment, pik);
					games.add(person); // добавляем объект в список games
				}

			} // конец цикла while

			/*
			 * пробовал вводить это условие по аналогии с закрытием потока при чтени инфы из
			 * интернета
			 */

			// br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// вызываем Метод sort и передаем ему наш Компаратор, это сортирует нашу
		// Коллекцию (либо по Алфавиту либо по Году выпуска)

		games.sort(new MyNameCompNew());
		// games.sort(new MyYearComp());

		return games;

	} // конец Метода 1.1.
	
	/* 1.2 Наш метод который будет считывать данные (с файла на GitHub),
	 * формировать Коллекцию Игр. Применяю в Конструкторе №3 */
	
	public List<GameClass> readGamesFromWeb () {

		List<GameClass> games = new ArrayList<>();

		try {

			//String fileUrl = GameClass.webFileAddres; // веб адрес моего текст файла на Git Hub

			String fileUrl = fileListAddress;
			
			// Создаем URL объект используя мой адрес

			URL url = new URL(fileUrl);

			// Открываем соединение к URL

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET"); // Set the request method to GET

			// Check if the response code is HTTP_OK (200)

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

				// Create a BufferedReader to read the input stream
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String line;
				String result = "";
				int count = 0;

				// Читаем файл строка за строкой и добавляем строки в наш Список

				while ((line = in.readLine()) != null) {

					// считываем Строку без учета пробелов вначале или вконце

					line = line.trim();

					// если Строка пустая или начинается с // (но не заканчивается на "*/") -
					// пропускаем ее

					if (line.isEmpty() || (line.startsWith("//") && (!line.endsWith("*/")))) {

						continue;
					}

					///////////// Блок, реализующий игнорирование кода в /* .... */ /////////////

					if ((line.startsWith("/*")) && (line.endsWith("*/"))) {

						continue;
					}

					if (line.startsWith("/*")) {

						count = 1;
						continue;
					}

					if (((count == 1) && (line.endsWith(","))) ||

							((count == 1) && (line.endsWith(";"))) ||

							((count == 1) && (line.isEmpty())) ||

							((count == 1) && (line.startsWith("//")) && (!line.endsWith("*/")))) {

						continue;
					}

					if ((count == 1) && (line.endsWith("*/"))) {

						count = 0;
						continue;
					}

					///////////////////////////////////////////////////////////////////////////////

					// если Строка кончается на "," значит дальше будет адрес Фотки, надо объеденять
					// строки

					if (line.endsWith(",")) {
						result += line.trim() + " ";
						continue;
					}

					// если Строка кончается на ";" значит это окончательный конец строки, надо
					// заканчивать объеденение

					if (line.endsWith(";")) {
						line = line.substring(0, line.length() - 1); // удаляем эти ;
						result += line.trim() + " ";
						line = result;
						result = "";
						// continue;
					}

					/*
					 * разделяем Строку на подстроки, "," - разделитель и упаковываем их в Массив
					 * Строк
					 */
					/*
					 * т.е создаем Массив Строк, в котором каждый элемент (каждая строка) это
					 * значение нашего Поля передаваемое Конструктору. В зависимости от количества
					 * полей (переменная h), выбирается соответствующий Конструктор, из всего 11
					 */

					String[] parts = line.split(",");

					int h = parts.length; // переменная для отслеживания количества Полей считанных для одного Объекта.

					// в каждой Подстроке удаляем Пробелы (если были) вначале или вконце

					for (int i = 0; i < h; i++) {
						parts[i] = parts[i].trim();
					}

					// если Подстрока начинается с ", удаляем этот символ

					for (int i = 0; i < h; i++) {

						if (parts[i].startsWith("\"")) {
							parts[i] = parts[i].substring(1);
						}
					}

					// если Подстрока кончается на ", удаляем этот символ

					for (int i = 0; i < h; i++) {

						if (parts[i].endsWith("\"")) {
							parts[i] = parts[i].substring(0, parts[i].length() - 1);
						}
					}

					///////////////////////// блок формирования Объектов из считаных строк
					///////////////////////// ////////////////////

					/* в зависимости от количества считанных частей (h) применяем соответствующий
					 * Конструктор */

					if (h == 1) {

						String name = parts[0];

						GameClass person = new GameClass(photoListAddress, name);
						games.add(person);
					}

					else if (h == 2) {

						String name = parts[0];
						String creator = parts[1];

						GameClass person = new GameClass(photoListAddress, name, creator);
						games.add(person);
					}

					else if (h == 3) {

						String name = parts[0];
						String creator = parts[1];
						String year = parts[2];

						GameClass person = new GameClass(photoListAddress, name, creator, year);
						games.add(person);
					}

					else if (h == 4) {

						String name = parts[0];
						String creator = parts[1];
						String mapper = parts[2];
						String year = parts[3];

						GameClass person = new GameClass(photoListAddress, name, creator, mapper, year);
						games.add(person);
					}

					else if (h == 5) {

						String name = parts[0];
						String creator = parts[1];
						String mapper = parts[2];
						String year = parts[3];
						String comment = parts[4];

						GameClass person = new GameClass(photoListAddress, name, creator, mapper, year, comment);
						games.add(person);
					}

					/*
					 * благодаря этому блоку else стало возможно просматривать бесконечное число
					 * фоток одной игры. Т.е. сколько фоток будет столько и прочитаем
					 */

					else { // 1 и более кастомных фотки

						String name = parts[0];
						String creator = parts[1];
						String mapper = parts[2];
						String year = parts[3];
						String comment = parts[4];

						String[] pik = new String[h - 5]; // создаем массив строк для Адресов фоток

						/* заполняем Массив pik только Адресами фоток, считанных из файла */

						for (int i = 5; i < parts.length; i++) {

							//pik[i - 5] = manag.photoFolderAddress + parts[i];
							pik[i - 5] = parts[i];
						}

						/* создаем объект используя "универсальный" конструктор */

						GameClass person = new GameClass(photoListAddress, name, creator, mapper, year, comment, pik);
						games.add(person); // добавляем объект в список games
					}

				} // конец while

				// Close the BufferedReader
				in.close();

			} else {

				//System.out.println("Failed to fetch the file: " + connection.getResponseCode());
			}

			// Disconnect the connection
			connection.disconnect();

		} catch (IOException e) {

			// System.out.println("Fuck!");
			e.printStackTrace();
		}

		// вызываем Метод sort и передаем ему наш Компаратор, это сортирует нашу
		// Коллекцию (либо по Алфавиту либо по Году выпуска)

		games.sort(new MyNameCompNew());
		// games.sort(new MyYearComp());
		
		return games;

	} // конец Метода readGamesFromWeb
	
	/* 2. Метод принимает от пользователя Фразу и обрабатывает ее, выбирая тот или
	 * иной Конструктор, т.е. формируя объект с новыми данными в полях и новыми
	 * списками - переделываю под нужды Вэб-Проложения */

	public GameListClass inputAnalyse(AddressManager manag, String finder) {

		/* если например предыдущий метод (mouseChooseWindow) применит setFullList,
		 * там key равен "refreshManag" и чтобы этот метод (inputAnalyse) сразу
		 * передавал объект дальше в блок обновления Менеджера и применяется это
		 * условие */
		
//		if ( key.equals("refreshManag") ) {
//
//			return this;
//		}
		
		/* если список Игр пуст, значит ничего не нашлось, а значит нужно
		 * обновлять список */
		
		if (this.GameList.size()==0) {

			GameListClass list = this.setFullList();
			
			return list;
		}

		/* вводим что-либо с клавиатуры */
		
//		key = JOptionPane.showInputDialog(null, searchTrace + "->", "Input Window",
//				JOptionPane.QUESTION_MESSAGE); // вводим слово в окно

		key = finder;

		/* записываем введенное с клавы в "историю поиска", если:
		 * это не старКод, не listInfo и не managInfo */

		if ( !key.startsWith("*") && !key.equals("listInfo") && !key.equals("managInfo") && !key.equals("Info") ) {
			
			searchTrace = searchTrace + "->" + key;
		}
		
		/* Первые 3 условия, сформируют в объекте еще один список с уникальными названиями Издателей,
		 * Мапперов либо Годов, присутствующий в основном списке */
		
		// 1. если вводим creators или "издатели" - в addInput пишем "creator"

		if (key.equals("creators") || (key.equals("издатели"))) {

			/* используем Сеттер "Издателей" */

			GameListClass list = this.setFieldList(this.GameList, "creator", searchTrace, level);
			
			return list;
		}

		// 2. если вводим "mappers" или "мапперы" - в addInput пишем "mapper"

		else if ( (key.equals("mappers")) || (key.equals("мапперы")) ) {

			/* используем Сеттер "Издателей" */

			GameListClass list = this.setFieldList(this.GameList, "mapper", searchTrace, level);

			return list;
		}

		// 3. если вводим "years", "года" или "годы" - в addInput пишем "year"

		else if ( (key.equals("years")) || (key.equals("года")) || (key.equals("годы")) ) {

			/* используем Сеттер "Издателей" */

			GameListClass list = this.setFieldList(this.GameList, "year", searchTrace, level);

			return list;
		}
		
		/* Следующие 2 условия - это условия, когда мы можем менять место, откуда считываем инфу -
		 * либо с Компа, било с Инета (GitHub-а) */
		
		/* 4. если вводим "InetOff"  */

		else if (key.equals("InetOff")) {

			/* используя метод setReadFrom устанавливаем значение поля readFrom менеджера = false
			 * и передаем получившийся Менеджер в Конструктор №1 => поиск с Компа */
			
			//manag = manag.setReadFrom(0);
			
			/* присваиваем "refreshManag" key, чтобы Менеджер в main обновился,
			 * т.е. заполнил поля fileAddress and photoAddress, найдя их на компе или скачав
			 * их с Инета и там же manag = manag.setReadFrom(0) - т.е. поиск с Компа */
			
			this.key = "refreshManag";

			return this;
		}

		/* 5. если вводим "InetOn" переменная webOrNot = 0 и как-бы включаем закачку
		 * даных из Инета */

		else if (key.equals("InetOn")) {

			/* используя метод setReadFrom устанавливаем значение поля readFrom менеджера = true
			 * и передаем получившийся Менеджер в Конструктор №1 => поиск с Инета */
			
			manag = manag.setReadFrom(1);
			
			/* используем Конструктор № 1 */
			
			GameListClass list = new GameListClass(manag);

			return list;
		}
		
		/* Следующие 2 условия, это вызов окна с Информацией о Менеджере Адресов
		 * и о состоянии Списка */
		
		/* 6. при вводе managInfo - показываем состояние Менеджера Адресов */
		
		else if ( key.equals("managInfo") ) {
			
			manag.showManagInfo();;

			return this; // возвращаем этот же Список
		}
		
		/* 7. при вводе listInfo - показываем состояние Списка */
		
		else if ( key.equals("listInfo") ) {
			
			this.getInfo();

			return this; // возвращаем этот же Список
		}

		/* 8. при вводе Info - показываем состояние Списка */
		
		else if ( key.equals("Info") ) {
			
			ServiceMethods.informWindow();

			return this; // возвращаем этот же Список
		}
		
		/* 8. Условие по обновлению Списка до полного */
		
		else if ( key.equals("") ) {

			/* используем Конструктор № 1 */
			
			GameListClass list = new GameListClass(manag);

			return list;
		}

		/* 9. Условие по заполнению поля starCode и последующему выбору по этому полю
		 * одного элемента из Списка */
		
		else if (ServiceMethods.isStarCodeOk(key)) {

			/* если мы ввели старКод, а поле "Издателей" заполнено, то
			 * по старКоду выбираем из списка "Издателей", а возвращаем объект
			 * со списком Игр, отсортированным по выбранному элементу из списка
			 * "Издателей" */
			
			if ( !this.fieldName.equals("") ) {
				
				/* переводим старКод в цифру */
				
				int ku = Integer.parseInt(key.substring(1, key.length()));
				
				/* присваиваем key значение "издателя" под номером старКода */
				
				this.key = this.getFieldList().get(ku-1);

				searchTrace = searchTrace + "->" + key;

				/* далее идет "стандартная" сортировка списка Игр по key */
				
				GameListClass xlist = this.objectSorting();
				
				return xlist;
				
			} else {
			
				/* это случай, когда по старКоду выбираем из Списка Игр, т.е.
				 * когда список "Издателей" был пуст */
				
				this.starCode = Integer.parseInt(key.substring(1, key.length()));
				this.key = "";

				return this;		
			}
		}

		/* 10. Условие по обновлению ТекстФайла по вводу "refreshFile" */
		
		//else if ( key.equals("refreshFile") && manag.isInetHere && !manag.fileAddress.equals("") ) {
		else if ( key.equals("refreshFile") && manag.isInetHere ) {
			
			GitHubSynchronize synch = new GitHubSynchronize ();
			
			synch.refreshTextFile(manag);
			
			GameListClass list = new GameListClass(manag);

			return list;
		}
		
		/* 11. Условие по обновлению папок с Фотками по вводу "refreshPhoto" */
		
		else if ( key.equals("refreshPhoto") && manag.isInetHere) {
			
			GitHubSynchronize synch = new GitHubSynchronize ();
			
			synch.downloadDiffArray(manag);
			
			GameListClass list = new GameListClass(manag);

			return list;
		}
		
		/* 12. Условие когда ввели "refreshFile" или "refreshPhoto", но Инета нет */
		
		else if ( (key.equals("refreshFile") || key.equals("refreshPhoto")) && !manag.isInetHere) {
			
			//ServiceMethods.windowShow("Не получится обновить данные. Отсутствует интернет");

			return this;
		}

		/* 13. Дефолтное условие, если предидущие условия не выполены, значит
		 * проводим Сортировку */
		
		else {

			GameListClass list = this;

			list = list.objectSorting();
			//ServiceMethods.windowShow(list.getGameList().get(0).getPics()[0]);
			
			return list;
		}

	} // конец метода inputAnalyse

	/* 2* Метод принимает от пользователя Фразу и обрабатывает ее, выбирая тот или
	 * иной Конструктор, т.е. формируя объект с новыми данными в полях и новыми
	 * списками - переделываю под нужды Вэб-Проложения */

	public GameListClass inputAnalyse(String finder) {

		/* если например предыдущий метод (mouseChooseWindow) применит setFullList,
		 * там key равен "refreshManag" и чтобы этот метод (inputAnalyse) сразу
		 * передавал объект дальше в блок обновления Менеджера и применяется это
		 * условие */

//		if ( key.equals("refreshManag") ) {
//
//			return this;
//		}

		/* если список Игр пуст, значит ничего не нашлось, а значит нужно
		 * обновлять список */

		if (this.GameList.size()==0) {

			GameListClass list = this.setFullList();

			return list;
		}

		/* вводим что-либо с клавиатуры */

//		key = JOptionPane.showInputDialog(null, searchTrace + "->", "Input Window",
//				JOptionPane.QUESTION_MESSAGE); // вводим слово в окно

		key = finder;

		/* записываем введенное с клавы в "историю поиска", если:
		 * это не старКод, не listInfo и не managInfo */

		if ( !key.startsWith("*") && !key.equals("listInfo") && !key.equals("managInfo") && !key.equals("Info") ) {

			searchTrace = searchTrace + "->" + key;
		}

		/* Первые 3 условия, сформируют в объекте еще один список с уникальными названиями Издателей,
		 * Мапперов либо Годов, присутствующий в основном списке */

		// 1. если вводим creators или "издатели" - в addInput пишем "creator"

		if (key.equals("creators") || (key.equals("издатели"))) {

			/* используем Сеттер "Издателей" */

			GameListClass list = this.setFieldList(this.GameList, "creator", searchTrace, level);

			return list;
		}

		// 2. если вводим "mappers" или "мапперы" - в addInput пишем "mapper"

		else if ( (key.equals("mappers")) || (key.equals("мапперы")) ) {

			/* используем Сеттер "Издателей" */

			GameListClass list = this.setFieldList(this.GameList, "mapper", searchTrace, level);

			return list;
		}

		// 3. если вводим "years", "года" или "годы" - в addInput пишем "year"

		else if ( (key.equals("years")) || (key.equals("года")) || (key.equals("годы")) ) {

			/* используем Сеттер "Издателей" */

			GameListClass list = this.setFieldList(this.GameList, "year", searchTrace, level);

			return list;
		}

		/* Следующие 2 условия - это условия, когда мы можем менять место, откуда считываем инфу -
		 * либо с Компа, било с Инета (GitHub-а) */

		/* 4. если вводим "InetOff"  */

		else if (key.equals("InetOff")) {

			/* используя метод setReadFrom устанавливаем значение поля readFrom менеджера = false
			 * и передаем получившийся Менеджер в Конструктор №1 => поиск с Компа */

			//manag = manag.setReadFrom(0);

			/* присваиваем "refreshManag" key, чтобы Менеджер в main обновился,
			 * т.е. заполнил поля fileAddress and photoAddress, найдя их на компе или скачав
			 * их с Инета и там же manag = manag.setReadFrom(0) - т.е. поиск с Компа */

			this.key = "refreshManag";

			return this;
		}


		/* 7. при вводе listInfo - показываем состояние Списка */

		else if ( key.equals("listInfo") ) {

			this.getInfo();

			return this; // возвращаем этот же Список
		}

		/* 8. при вводе Info - показываем состояние Списка */

		else if ( key.equals("Info") ) {

			ServiceMethods.informWindow();

			return this; // возвращаем этот же Список
		}

		/* 9. Условие по заполнению поля starCode и последующему выбору по этому полю
		 * одного элемента из Списка */

		else if (ServiceMethods.isStarCodeOk(key)) {

			/* если мы ввели старКод, а поле "Издателей" заполнено, то
			 * по старКоду выбираем из списка "Издателей", а возвращаем объект
			 * со списком Игр, отсортированным по выбранному элементу из списка
			 * "Издателей" */

			if ( !this.fieldName.equals("") ) {

				/* переводим старКод в цифру */

				int ku = Integer.parseInt(key.substring(1, key.length()));

				/* присваиваем key значение "издателя" под номером старКода */

				this.key = this.getFieldList().get(ku-1);

				searchTrace = searchTrace + "->" + key;

				/* далее идет "стандартная" сортировка списка Игр по key */

				GameListClass xlist = this.objectSorting();

				return xlist;

			} else {

				/* это случай, когда по старКоду выбираем из Списка Игр, т.е.
				 * когда список "Издателей" был пуст */

				this.starCode = Integer.parseInt(key.substring(1, key.length()));
				this.key = "";

				return this;
			}
		}

		/* 13. Дефолтное условие, если предидущие условия не выполены, значит
		 * проводим Сортировку */

		else {

			GameListClass list = this;

			list = list.objectSorting();
			//ServiceMethods.windowShow(list.getGameList().get(0).getPics()[0]);

			return list;
		}

	} // конец метода inputAnalyse*

	/* 3. Метод сортирует Список Объекта и вызвращает Объект с отсортированным
	 * Списком Игр. Используется в предыдущем методе. */

	private GameListClass objectSorting() {

		int w = 0; // переменная для отслеживания того, какой список возвращаем

		/* создадим Список, который и будет возвращать Метод */

		List<GameClass> objectList = new ArrayList<GameClass>();

		this.level++; // вызываем этот метод и t+1 сразу - для отображения этапа Сортировки

		/* при вводе "allGames" поиск уже не производим, возвращаем объект как есть, так
		 * как метод по анализу ввода (inputAnalyse) заполнил это поле, а значит и
		 * вернул полный список, т.е. в этом объекте присутствует полный список */

		if (this.key.equals("All Games")) {

			searchTrace = "All Games";

			return this;
		}

		/* если вводим стар код - номер идет в поле starCode, и объект возвращается как
		 * есть */

		if (this.starCode != 0) {

			return this;
		}

		/* на данный момент у нас key - это то, что ввели с клавиатуры. Здесь можно
		 * передать наше key в спец метод, в котором это значение обработается и
		 * проверится на соответ- ствие условиям: - начинается ли оно с "*" - содержит
		 * ли слова "creators", "mappers" и т.д. т.е. здесь будет аналог блока
		 * "блок ввода спец. команд в начальное окно ввода" из старого Класса
		 * MyGameSearch */

		///////////////////// сортировка по полному совпадению по Полям /////////////////////

		// проходим по списку, и собираем в отдельный список Объекты, которые
		// соответствуют условиям

		for (int i = 0; i < GameList.size(); i++) {

			if ((this.key.equals(GameList.get(i).getCreator())) || (this.key.equals(GameList.get(i).getMapper()))
					|| (this.key.equals(GameList.get(i).getYear()))) {

				objectList.add(GameList.get(i)); // добавляем Объект с совпадением в наш Список

				w++;
			}
		}

		if (w > 0) {

			/* используем Сеттер Списка Игр */

			GameListClass list = this.setGameList(objectList, level, this.key, searchTrace);
			
			return list;
		}
		
		// если что-то нашлось - возвращаем этот список

		///////////////////////////// Блок поиска непосредственно в Именах //////////////////////////

		if (this.key.length() > 0) {

			char n = this.key.charAt(0); // n - первый символ введенного слова

			///////////////////// блок для сортировки по Одной введеной Букве ////////////////////////

			/*
			 * если введенное слово - это Один символ и это Буква, то делаем эту букву
			 * Заглавной (даже если она и была заглавной) и отсортировываем объекты,
			 * названия которых начинается на эту Букву
			 */

			if ((this.key.length() == 1) && (Character.isAlphabetic(n))) {

				char n1 = Character.toUpperCase(n);

				this.key = String.valueOf(n1);

				for (int i = 0; i < GameList.size(); i++) {

					if (this.key.equals(String.valueOf(GameList.get(i).getName().charAt(0)))) {

						objectList.add(GameList.get(i)); // добавляем Объект с совпадением в наш Список

						w++;
					}
				}

				if (w > 0) {

					/* используем Сеттер Списка Игр */

					GameListClass list = this.setGameList(objectList, level, this.key, searchTrace);
					
					return list;

				}
				
				// если что-то нашлось - возвращаем этот список

				/////////////////////////////////////////////////////////////////////////////////////////////////////

				//////////////////// если больше одной буквы или символы //////////////////////////

			} else {

				/* ищем введеное в названиях Игр и считаем кол-во найденого */

				for (int i = 0; i < GameList.size(); i++) {

					if (GameList.get(i).getName().contains(this.key)) {

						objectList.add(GameList.get(i)); // добавляем Объект с совпадением в наш Список

						w++;
					}
				}

				if (w > 0) {

					/* используем Сеттер Списка Игр */

					GameListClass list = this.setGameList(objectList, level, this.key, searchTrace);
					
					return list;
				}
				
				// если что-то нашлось - возвращаем этот список
			}

		} // конец if (a.length()>0) - основного

		//////////////////////////////////////////////////////////////////////////////////////////////////////

		/* используем Сеттер Списка Игр */

		GameListClass list = this.setGameList(objectList, level, this.key, searchTrace);

		return list;

	} // конец Метода 3. objectSorting
	
	/* 4. Метод по выводу Списка GameClass в Окно (JOptionPane) со
	 * Слайдером и возможностью выбора элементов из этого списка Мышью.
	 * Вот над созданием этого метода (Окна) я бился долгие месяцы! */
	
	public GameListClass mouseChooseWindow() {

		/* если метод видит "end" - ничего не показывает, и в main - конец проги */
		
		if ( this.key.equals("end") || this.key.equals("конец") ) {

			//End-Сеттер
			
			GameListClass list = setEndList();
			
			return list;
		}

		/* если посмотрели спиок, а потом ввели starCode (*_ _) значит показываем фотки
		 * этой игры с индексом СтарКода (-1, т.к. индексация с 0) */
		
		if ( this.starCode!=0 ) {
			
			this.GameList.get(this.starCode-1).showPics(this.inet);
			
			this.starCode = 0;
			
			/* благодаря возврату this.mouseChooseWindow мы можем выбирать старКодом
			 * сколько угодно раз из одного Списка */
			
			return this.mouseChooseWindow();
		}

		/* создаем Список под строки: либо названий Игр, либо Издателей */
		
		List <String> y = new ArrayList<String>();
		
		/* если поле fieldName заполненно, берем этот список */
		
		if (!this.fieldName.equals("")) {
			
			for (int i = 0; i < this.getFieldList().size(); i++) {

				y.add(this.getFieldList().get(i));
			}
			
		/* если поле fieldName пусто, берем список Названий Игр */
			
		} else {
		
			if ( this.getGameList()!=null ) {
				
				// создаем Массив Строк "y" из нашего Массива Объектов

				for (int i = 0; i < this.getGameList().size(); i++) {

					y.add(this.getGameList().get(i).getName());
				}		
			} 
		}
		
		// добавляем наш Массив Строк 'y' в наш JList

		DefaultListModel<String> listModel = new DefaultListModel<>();

		// добавляем наш Массив Строк в наш JList

		for (String item : y) {

			listModel.addElement(item);
		}

		// создаем JList - myList

		JList<String> myList = new JList<>(listModel);

		// создаем типо "Счетчик" к нашму JList

		myList.setCellRenderer(new NumberedListCellRenderer());

		// создаем Панель Прокрутки и добавляем туда наш Список myList

		JScrollPane scrollPane = new JScrollPane(myList);

		/* создадим Списки list и clist для передачи их далее в приемник, т.к. если просто взять
		 * их из объекта this.getGameList - в приемнике их не видно (this) */
		
		List <GameClass> list = this.getGameList();
		List <String>   clist = this.getFieldList();
		boolean subList = this.inet;
			
		////////////////////////// Регистрация Приемника ///////////////////////////////

		myList.addListSelectionListener(new ListSelectionListener() { // начало Метода регистрации Приемника

			public void valueChanged(ListSelectionEvent e) { // начало Метода регистрации События
				
				if (!e.getValueIsAdjusting()) {

					// получаем один выбранный Мышкой Элемент (Строку)

					String selectedElement = myList.getSelectedValue();

					// получаем Индекс (номер выбранной Строки)

					int index = -1;

					ListModel<String> model = myList.getModel();

					/* при выборе мышью ввел минимальное значение i = 1, даже если выбираем
					 * первый элемент (i=0), i будет равно 1, тем самым будет видно, что
					 * выбор сделан мышью, потому что если ничего не выбираем, i=0, а почему
					 * я не понял, думал, что должно быть -1. Поэтому далее при использовании
					 * index и ind вычитаю 1 */
					
					for (int i = 0; i < model.getSize(); i++) {
						
						if (model.getElementAt(i).equals(selectedElement)) {
							index = i+1; // ввел +1 для отслеживания выбора мышью
							break;
						}
					}
				
					/* если список clist ("Издателей") пуст, значит перед нами список Игр,
					 * заначит, выбираем игру по индексу и показываем ее фотки */
					
				  //if ( clist.size()==0 ) { // здесь почеу-то работало и так..
					if ( clist.size()==0 && index!=0) {
						
						// по Индексу выбранной Строки, выбираем Объект из нашего Массива Объектов

						GameClass shit = list.get(index-1);

						//shit.showPics(subList); // показываем фотографии Объекта (игры)
						
					/* в обратном случае - в списке "Издателей" что-то есть, а значит берем индекс,
					 * пишем в глобальную переменную и закрываем это окно */
						
					} else {
						
						ind = index;	
						
						/* Эту часть подсказал GPT, это мгновенно закрывает Окно при нажатии на один
						 * из элементов Списка */
						
						Window window = SwingUtilities.getWindowAncestor (myList);
						
						if (window instanceof JDialog) {
							JDialog dialog = (JDialog) window;
							dialog.dispose();
						}
						
						else if (window instanceof JFrame) {
							JFrame frame = (JFrame) window;
							frame.dispose();
						}
					}
	
			///////////////////////////////////////////////////////////////////////////////////////////////////
					
				} // конец if
				
			} // конец метода Внутреннего Абстрактного Класса, т.е. конец Метода регистрации События
			
		}); // конец нашего Внутреннего Абстрактного Класса, т.е. Метода регистрации Приемника

		//ServiceMethods.windowShow(Integer.toString(ind)); // смотрел значение ind
		/* почему-то если ничего не выбираем мышью из списка, ind равно 0, а я думал лолжно быть -1 */
		
		/////////////////////// Конец Регистрации Приемника //////////////////////////////

		scrollPane.setPreferredSize(new Dimension(500, 500));

		/* здесь формируется наше Главное окно: заголовок и др. */
		
		/* заводим перменную listSize, для указания в заголовке кол-ва элементов,
		 * если игры то их, если это "издатели" то их */
		
		int listSize = 0;
		
		/* если в списке издателей что-то есть */
		
		if (this.FieldList.size()!=0) {
			
			listSize = this.FieldList.size();
			
		} 
		
		/* если в списке издателей ничего и в играх ничего */
		
		else if (this.FieldList.size()==0  && this.GameList.size()!=0) {
			
			listSize = this.GameList.size();
		}
	
		JOptionPane.showMessageDialog(null, scrollPane, listSize + " pcs : " + searchTrace, JOptionPane.PLAIN_MESSAGE);
		
		/* после показа списка, если в нем 1 Игра, показываем ее без лишних движений */
	
		if (this.GameList.size()==1) {
			
			this.GameList.get(0).showPics(this.inet);
			
			/* используем Конструктор №1 */
			
			//GameListClass retList = new GameListClass(manag);

			GameListClass retList = this.setFullList();
					
			return retList;
		}

		/* наш метод вызвращает объект нашего Класса, вот это блок по возвращению:
		 * 
		 * если Список "Издателей" был, то в key записываем то, что выбрали мышью,
		 * обновляем "историю" поиска searchTrace, обнуляем статическую ind и
		 * формируем объект для возвращения - этот объект будет с отсортированным
		 * списком Игр по переданому key и он сразу выведет на экран список Игр этим
		 * же методом, т.к. список "Издателей" у него уже пуст и вернет этот объект */
		
		if ( !this.fieldName.equals("") && ind!=0) {
			
			this.key = this.getFieldList().get(ind-1);

			searchTrace = searchTrace + "->" + key;
			
			ind = 0; 

			GameListClass xlist = this.objectSorting().mouseChooseWindow();
			
			return xlist;
			
		/* если же в Объекте не было списка "Издателей", метод после показа (и выбора
		 * мышью) Списка Игр, вернет этот же Объект */
			
		} else {
			
			return this;
		}

	} // конец Метода 4.2 mouseChooseWindow()
	
	/* 5. Метод для чека - что содержит наш Оьъект */

	public void getInfo() {
		
		if (this.key.equals("end") ) return;

		String text = "Level: " + level + "\n"

				+ "Key: " + key + "\n" + "fieldName: " + fieldName + "\n" + "searchTrace: " + searchTrace + "\n"
				+ "starCode: " + starCode + "\n" + "read from Inet: " + inet + "\n" + "GameListSize: "
				+ GameList.size() + "\n" + "FieldListSize: " + FieldList.size() + "\n"
				+ "fileListAddress: " + fileListAddress + "\n"
				+ "photoFolderAddress: " + photoListAddress + "\n";

		JTextArea textArea = new JTextArea(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, "That's what we have here: ",
				JOptionPane.PLAIN_MESSAGE);
		
	} // конец 5 Метода getInfo()

} // конец Класса GameListClass

