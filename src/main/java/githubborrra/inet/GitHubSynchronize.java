
package githubborrra.inet;

import githubborrra.service.ServiceMethods;
import githubborrra.service.MyWebNameComp;
import githubborrra.service.MyStringNameComp;
import githubborrra.service.ConsoleWindow;
import githubborrra.game.GameFolderClass;


import java.awt.Frame;
import javax.swing.SwingUtilities;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/* Класс, методы которого осуществляют взамиосвязь программы с GitHub, для
* обновления информации */

public class GitHubSynchronize {

	/////////////* В этом Классе нет полей *////////////
	
	/* 1. этот метод считывает инфу из моего текстового файла хранящегося на GitHub
	 * и заводит в моей папке текстовый файл (если его нету, а если есть,
	 * перезаписывать его) со всей скачанной инфой, т.е. он ничего не принимает и не
	 * возвращает, он Обновляет Текстовый Файл проекта (или создает его, если его
	 * вообще не было) Будет использован при вводе "refreshFile" */

	public String refreshTextFile (AddressManager manag) {

		List <String> myList = new ArrayList <String> ();
		
		// адрес моего файла на GitHub

		String fileUrl = manag.webFileAddress;

		try {

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

				// Читаем файл строка за строкой и добавляем строки в наш Список

				while ((line = in.readLine()) != null) {

					myList.add(line);
				}

				// Close the BufferedReader
				in.close();

			} else {

				System.out.println("Failed to fetch the file: " + connection.getResponseCode());
			}

			// Disconnect the connection
			connection.disconnect();

		} catch (IOException e) {

			e.printStackTrace();
		}

		/* Обработаем тут наш Список, для записи его в Файл */

		String[] myArray = myList.toArray(new String[0]); // переводим в Массив Строк

		// Service_Class.windowShow(myArray); // выводим в Окно

		File myTextFile; // файл под создаваемый Текст Файл
		
		String fileAdr = ""; // этот адрес будет возвращать
	
		if (manag.fileAddress.equals("")) {
			
			/* если Менеджер не нашел ТекстФайла вообще ниеге, нужно завести папку проекта
			 * (либо попытаться это сделать и понять, что она уже есть). */
			
			ServiceMethods.windowShow("Создадим (если ее нет) папку Проекта на РабСтоле");
			
			/* создаем папку Проекта на Рабочем Столе */
			
			fileAdr = manag.createProjectFolder();
					
			myTextFile = new File(fileAdr + "/" + manag.textFileName);
			
		} else {
			
			/* если fileAddress не равен нулю, значит ТекстФайл должен существовать
			 * а значит его нужно просто обновить */
			
			myTextFile = new File(manag.fileAddress);
			
			/* проверим файл на существование */
			
			if (myTextFile.exists()) {
				
				/* если существует - хорошо - ничего не делаем */
				
			} else { // если не существует
				
				/* создаем папку Проекта на Рабочем Столе */
				
				fileAdr = manag.createProjectFolder();
						
				myTextFile = new File(fileAdr + "/" + manag.textFileName);
			}
		}
		
		String flAddr = myTextFile.getAbsolutePath();
		
		/* создаем Текстовый Файл на рабочем столе, если он еще не создан - случай когда
		 * файла изначально не было */

		try { // первое try для первого создания файла

			/* в нашей Директории (рабочий стол) создаем наш файл. Только не совсем на
			 * рабочем. Он создается в папке MyGameSearcher на рабочем, даже если ее до
			 * этого не было - это над проверить */

			if (myTextFile.createNewFile()) { // если файла не было, создаем

				System.out.println("Text file created good");

			} else { // если файл есть, перезаписываем

				// System.out.println ("Text file is already exists");
			}

		} catch (IOException e) { // конец первого try по созданию первого файла

			System.err.println("Error creating: " + e.getMessage());
		}

		/* если наш Текстовый файл существует, пишем туда инфу (а он должен
		 * существовать, т.к. в предыдущем участке кода мы его создавали) */

		if (myTextFile.exists()) {

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(flAddr, false))) {
				// try (PrintWriter writer = new PrintWriter(new FileWriter(fileAddres, false)))
				// {
				for (int i = 0; i < myArray.length; i++) {

					/* записываем в файл весь наш Массив строк, считанных с Репозатория */

					writer.write(myArray[i]);
					writer.newLine();
					// writer.write("\n"); // это вставил из-за PrintWriter
				}

				/* это "закрытие" я ввел по аналогии с закрытием потока при чтении инфы с инета.
				 * Я это ввел, и стал работать следующий блок - сейчас текстовый файл удаляется,
				 * но т.к. временного нет, он не переиминовывается */

				writer.close(); // придумал сам

				ServiceMethods.windowShow("Текст Файл обновлен.");

			} catch (IOException e) {

				e.printStackTrace();
			}

			return myTextFile.getAbsolutePath();
			
		} else {
			
			return "";
		}

	} // конец метода 1. fileFromWebCreator
	
	/* 2. Этот Метод обрабатывает Текстовый файл проекта лежащий на Git Hub и
	 * формирует из него список Объектов данного класса, т.е. список всех Папок
	 * фотографий проекта со списком файлов (фотографий) содержащихся в них. Т.е. по
	 * этому списку и будем создавать папки на компе и по этому же списку будем
	 * качать в них Фотографии */

	public GameFolderClass[] checkGitHubFolder (AddressManager manag) {

		Set<String> setList = new HashSet<>(); // сюда и будем собирать все названия фоток (1.jpg, 2.jpg)

		GameFolderClass[] gitHubArr; // будем возвращать этот массив

		/* заводим Список, в который будем записывать считанные объекты данного Класса */

		List<GameFolderClass> folders = new ArrayList<>();

		try {

			// адрес моего Текстового файла на GitHub

			String fileUrl = manag.webFileAddress;
			
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

					/* разделяем Строку на подстроки, "," - разделитель и упаковываем их в Массив
					 * Строк */
					
					/* т.е создаем Массив Строк, в котором каждый элемент (каждая строка) это
					 * значение нашего Поля передаваемое Конструктору. В зависимости от количества
					 * полей (переменная h), выбирается соответствующий Конструктор, из всего 11 */

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

					//////////////////// блок формирования Объектов из считаных строк /////////////////////////

					/* в зависимости от количества считанных частей (h) применяем соответствующий
					 * Конструктор */

					if (h > 5) { // если в папке есть фотки

						/* блок для выяснения (подсчета) есть ли в адресе фоток ссылка на папку General
						 * (чтобы не копировать лишние фотки). Если содержит, то k + 1 и в дальнейшем
						 * вычтем это число из кол-ва элементов массива адресов фоток одной игры
						 * ((h-5)-k1)  */

						int k1 = 0;

						for (int i = 5; i < parts.length; i++) {

							if (parts[i].substring(0, parts[i].indexOf('/')).equals("1. General")) {

								k1++;

								/* здесь будет формироваться массив строк названий фоток в папке General,
								 * специально в формате "1.jpg", "10.jpg" и др. чтобы мой Компаратор смог их
								 * отсортировать, а затем добавляем их в список set для дальнейшей обработки за
								 * пределами этого цикла */

								String p = parts[i].substring(parts[i].indexOf('/') + 1);

								setList.add(p); // собираем уникальные названия фоток папки General в виде "3.jpg"
							}
						}

						/* дополнительно добавляю название фоток из папки General, которые не указаны в
						 * текстовом файле, а прописаны в конструкторе, и поэтому не могут быть скачены
						 * автоматически */

						setList.add("1.jpg");
						setList.add("2.jpg");
						setList.add("3.jpg");

//						String name    = parts[0]; // это чтобы понимать почему счет идет с 5-го элемента
//						String creator = parts[1];
//						String mapper  = parts[2];
//						String year    = parts[3];
//						String comment = parts[4];

						/* создаем массив строк для Адресов фоток, с числом строк на столько меньше,
						 * сколько адресов ссылаются на папку General */

						String[] pik = new String[(h - 5) - k1]; // создаем массив строк для Адресов фоток

						String FoldName = parts[5].substring(0, parts[5].indexOf('/')); // берем только название Папки

						/* заполняем Массив pik только Адресами фоток, считанных из файла, но если адрес
						 * ссылается на папку General, его не записываем */

						for (int i = 5; i < parts.length; i++) {

							if (!(parts[i].substring(0, parts[i].indexOf('/')).equals("1. General"))) {

								/* переделываем формат строки с "Dizzy/1.jpg" в "1.jpg" */
								
								pik[i - 5] = parts[i].substring(parts[i].indexOf('/')+1, parts[i].length());
								
							}
						}

						/* создаем объект используя "универсальный" конструктор */

						GameFolderClass gameFolder = new GameFolderClass(FoldName, pik);

						folders.add(gameFolder); // добавляем объект в список folders

					} // конец if (h>5)

				} // конец основного цикла while

				// Close the BufferedReader
				in.close();

			} else {

				System.out.println("Failed to fetch the file: " + connection.getResponseCode());
			}

			// Disconnect the connection
			connection.disconnect();

		} catch (IOException e) {

			e.printStackTrace();
			System.out.println("Some shit is going on with the Internet");
		}

		//////////////// блок обработки папки 1. General /////////////////////

		/* создаем один объект GitHubSynchronize, т.е под папку 1. General и добавим его
		 * в список папок на Git Hub */

		String[] GeneralFolder = setList.toArray(new String[0]); // массив Адресов фоток из папки General в формате
																	// "1.jpg"

		Arrays.sort(GeneralFolder, new MyStringNameComp()); // сортируем массив строк (адресов) моим супер компаратором

		/* формируем Один объект класса GitHubSynchronize т.е. под папку General и
		 * добавляем его в наш Список */

		GameFolderClass forGeneralFolder = new GameFolderClass("1. General", GeneralFolder);

		folders.add(forGeneralFolder); // добавляем объект 1. General в список GitHubList

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		gitHubArr = folders.toArray(new GameFolderClass[0]);

		/* вызываем Метод sort и передаем ему наш Компаратор MyWebNameComp(), он делает
		 * так, что наши элементы массива или Списка имели возможность сравниваться друг
		 * с другом, например в методе removeAll в дальнейшем */

		Arrays.sort (gitHubArr, new MyWebNameComp());

		return gitHubArr;

	} // конец Метода № 2 по формированию Списка Папок на Git Hub

	 /* 3. Метод который будет чекать локальную Папку с фотографиями Проекта и
	 * составлять список папок и фотографий в них, для того чтобы потом сравнить со
	 * списком содержащим Папки с Git Hub и пороизвести обновление. Т.е. метод только
	 * составляет список содержимого Папки Проекта находящейся на Компьютере
	 * (включая Папку 1. General ) */

	private GameFolderClass[] checkLocalFolder (AddressManager manag) {

		GameFolderClass[] compArr; // этот Массив будет возвращать Метод

		/* заводим Список, в который будем записывать считанные объекты данного Класса */

		List<GameFolderClass> folders = new ArrayList<>();

		File projectFolder = new File(manag.photoFolderAddress);

		/* проверим папку projectFolder на существование */

		if (projectFolder.exists()) {

			/* составим список только папок в папке фоток моего проекта */
			
			File[] localGameFolders = projectFolder.listFiles(File::isDirectory);

			String foldName; // строка под название Папки игры

			String[] addrPhoto; // массив строк под Адреса фоток Игры

			for (int i = 0; i < localGameFolders.length; i++) {

				foldName = localGameFolders[i].getName(); // берем название Папки игры

				/* записываем имена (названия фоток в папке) в Массив строк, сортируем его и
				 * добавляем путь к папке и ее название */

				/* берем адреса фоток содержащихся в Папке игры (формат 1.jpg, 2.jpg и др.) */
				
				addrPhoto = localGameFolders[i].list();

				Arrays.sort(addrPhoto, new MyStringNameComp()); // наконец-то я отсортировал нормально адреса фоток!

				for (int k = 0; k < addrPhoto.length; k++) {

					//addrPhoto[k] = foldName + "/" + addrPhoto[k];
					addrPhoto[k] = addrPhoto[k]; // экспериментирую
				}

				/* создаем объект используя "универсальный" конструктор */

				GameFolderClass gameFolder = new GameFolderClass(foldName, addrPhoto);

				folders.add(gameFolder); // добавляем объект в список folders
			}

			compArr = folders.toArray(new GameFolderClass[0]);

		} else {

			/* если папка Проекта не существует, то метод вернет пустой массив (не null) */

			compArr = new GameFolderClass[0];
		}

		/* смотрим что за список получился */

		return compArr;

	} // конец Метода № 3 checkLocalFolder()
	
	/* 4. Метод, применяет методы checkGitHubFolder и checkLocalFolder и создает массив,
	 * отсутвстующих на Компе объектов (папок) (точнее папок, в которых присутствуют расхождения)
	 * который уже в другом методе, мы будем загружать с GitHub */

	private GameFolderClass[] differArray (AddressManager manag) {

		///////// * формируем Список файлов на Git Hub *////////

		/* создаем Массив Папок на Git Hub используя наш Метод № 3 readWebFolders() */

		GameFolderClass[] gitHubArray = checkGitHubFolder(manag);

		/* заводим Список и добавляем в него предидущий Массив GitArray */

		Set<GameFolderClass> GitHubSet = new HashSet<GameFolderClass>();

		for (int i = 0; i < gitHubArray.length; i++) {

			GitHubSet.add(gitHubArray[i]);
		}

		////////// * формируем Список файлов на Компе *///////////

		GameFolderClass[] compArray = checkLocalFolder(manag);

		Set<GameFolderClass> compSet = new HashSet<GameFolderClass>();

		for (int i = 0; i < compArray.length; i++) {

			compSet.add(compArray[i]);
		}

		/* применяем метод removeAll чтобы удалить из списка на Git Hub элементы,
		 * которые есть на компьютере */

		GitHubSet.removeAll(compSet);

		/* в итоге мы получили список элементов нашего Списка, список адресов фотографий
		 * которых отличается от списка адресов фоток на компе.*/

		/* получем "разничный" Массив */

		GameFolderClass[] difArray = GitHubSet.toArray(new GameFolderClass[0]);

		Arrays.sort (difArray, new MyWebNameComp());
		
		return difArray;
		
	} // конец Метода № 4 differArray ()
	
	/* 5. Метод, который скачивает фотки "разничного" массива */
	
	public String downloadDiffArray (AddressManager manag) {
		
		GameFolderClass[] arr;
		
		/* Метод вызывается как принудительно, так и при запуске программы,
		 * когда при старте проги, на Компе папка с Фотками не нашлась, поэтому
		 * первое условие, это для случая, когда при старте Папи с фотками нет и
		 * мы создаем папку Проекта (если ее нет) и в ней папку Фотографий и фотки
		 * уже будут обновляться в нее */
		
		if ( manag.photoFolderAddress.equals("") ) {
			
			 //manag.createProjectFolder();
			 
			 manag.photoFolderAddress = manag.createPhotoFolder();
			 
			/* создаем массив Папок с расхождениями, он будет максимальный, т.е.
			 * фотки будут загружаться все */
				
			arr = differArray (manag);
			
		} else {
		
			/* создаем массив Папок с расхождениями */
		
			arr = differArray (manag);
		
		}
		
		/* если его длина не больше нуля, т.е. расхождения есть - загружаем файлы */
		
		if (arr.length>0) {
			
			/* здесь открываем мое Консоль-Окно для показа скачиаемых файлов */
				
			ServiceMethods.windowShoww(arr, "Папки с расхождениями: ");

			//if (ServiceMethods.yesNoWindow("Обновляем папку с фотками (Yes) или оставляем как есть (No)")==0) {
				
				ConsoleWindow.startWindow("Скачиваем файлы:"); // открытие окна-консоли
				
				for (int i=0; i<arr.length; i++) {
				
					arr[i].download(manag);
				}
				
			//////////////////Блок закрытия окна-консоли ////////////////////

			SwingUtilities.invokeLater(() -> {

				for (Frame frame : Frame.getFrames()) { frame.dispose(); }
			});	
			
			//////////////////////////////////////////////////////////////////
			
			//} else {
				
				//ServiceMethods.windowShow("Оставляем папку с фотками как есть");
			//}
	
			/* здесь нужно добавить слеш, так как он нужен в Менеджере,
			 * без него фотки не будут показываться (путь неправильный) */
			
			return manag.photoFolderAddress + "/";
			
		} else {
			
			ServiceMethods.windowShow("Качать нечего. Полное совпадение.");
			
			return manag.photoFolderAddress;
		}
		
	} // конец метода 5. downloadDiffArray (AddressManager manag)

} // конец Метода GitHubSynchronize

