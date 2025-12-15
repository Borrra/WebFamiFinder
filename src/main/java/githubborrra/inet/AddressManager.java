
package githubborrra.inet;

import githubborrra.service.ServiceMethods;
import githubborrra.service.MyWindow;

import java.awt.Frame;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

/* формирую Класс, который будет заведывать всеми Адресами */

public class AddressManager {
	
	private ArrayList <String> findList  = new ArrayList<String>();
	private ArrayList <String> photoList = new ArrayList<String>();

	/* заводим статическую переменную для подсчета количества вызовов Метода и
	 * регулировки глубины вложения */

	private int j = 0;

	/* заводим статическую переменную для установки глубины вложения */

	private int h = 5; // глубина вложения 5: С:/Stuff/Stuff/Stuff/Stuff
	
	/* 6 основный Полей */
	
	public boolean readFrom;
	
	public boolean isInetHere;
	
	public String fileAddress;
	
	public String photoFolderAddress;

	public String webPhotoAddress;
	
	public String webFileAddress;

	/* заведем переменную под название папки Проекта */

	final String projectFolderName = "MyGameSearcher";
	
	/* переменная с именем Текстового файла с данными Проекта */
	
	final String textFileName = "GamesFile.txt";
	
	/* переменная с именем папки с Фотографиями Проекта */

	final String photoFolderName = "GamesPhoto\\";
	
	/* переменная под Путь к Рабочему Столу */
	
	final String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";

	/* переменная для адреса фоток хранящихся на Git Hub */
	
	final String webPhAddr = "https://raw.githubusercontent.com/Borrra/GamesPhoto/main/";

	/* переменная для адреса текстового файла хранящегося на Git Hub */

	final String webFlAddr = "https://raw.githubusercontent.com/Borrra/GamesFile/main/GamesFile.txt";

	/* переменная для адреса Интернет сайта, по которому проверяем наличие Интернета */

	static String inetChecking = "http://www.google.com";
	
	/* это переменная для названия тестового файла, который создается, чтобы хранить
	 * адреса файлов  */

	final String v = "InfoFile.txt";

	/* 1. Первый Конструктор, начальный */
	
	public AddressManager () {
		
		readFrom = true;
		
		isInetHere = isInetAvalible ();
		
		fileAddress = "";
		
		photoFolderAddress = "";
		
		webPhotoAddress = webPhAddr;
		
		webFileAddress = webFlAddr;
	}
	
	/* 2. Второй Конструктор, когда есть адрес Текст Файла, а адреса Папки с фотками нет */
	
	public AddressManager (String txtFile, int a) {
		
		readFrom = true;
		
		isInetHere = isInetAvalible ();
		
		fileAddress = txtFile;
		
		photoFolderAddress = "";
		
		webPhotoAddress = webPhAddr;
		
		webFileAddress = webFlAddr;
	}
	
	/* 3. Третий Конструктор, когда есть адрес Папки с фотками, а текст файла нет */
	
	public AddressManager (int a, String photoAdr) {
		
		readFrom = true;
		
		isInetHere = isInetAvalible ();
		
		fileAddress = "";
		
		photoFolderAddress = photoAdr;
		
		webPhotoAddress = webPhAddr;
		
		webFileAddress = webFlAddr;
	}
	
	/* 4. Четвертый Конструктор, для передачи адреса Текст Файла и адреса Папки с фотками */
	
	public AddressManager (String txtAdr, String photoAdr) {
		
		readFrom = true;
		
		isInetHere = isInetAvalible ();
		
		fileAddress = txtAdr;
		
		photoFolderAddress = photoAdr;
		
		webPhotoAddress = webPhAddr;
		
		webFileAddress = webFlAddr;
	}

	/* Setters */
	
	/* Метод проверяющий наличие Интернета и используемый в Конструкторах для
	 * заполнения поля isInetHere */
	
	public static boolean isInetAvalible () {

		try {

			URL url = new URL(inetChecking); // передаем адрес Интернет Ресурса
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			// urlConnection.setDefaultRequestProperty("User-Agent", "Test");
			// urlConnection.setDefaultRequestProperty("Connection", "close");
			urlConnection.setConnectTimeout(1000);
			urlConnection.connect();

			//this.isInetHere = (urlConnection.getResponseCode() == 200);
			
			return (urlConnection.getResponseCode() == 200);

		} catch (Exception e) {

			return false;
		}
		
	} // конец Метода 
	
	/* 1. Метод определяющий содержится ли одна Строка в Другой или нет */
	
	private boolean isContainMethod(String a, String b) {

		boolean k = false;

		if (a.equals(b)) { // ищем только Полное совпадение Имени

			k = true;
		}

		return k;

	} // конец метода 1. isContainMethod()
	
	/* 2.1 следующий метод принимает Директорию (массив файлов) и Строку, которую
	 * ищет в названиях файлов/папок директории если находится совпадение, путь к
	 * этому файлу/папке записывается в Список (статический). Т.е. Метод добавляет в
	 * Список адреса найденых файлов/папок из одной Директории, т.е. он не умеет
	 * углубляться, в отличии от следующего Метода, в котором данный метод и будет
	 * применяться */

	private void isContainList(File[] a, String b) {

		/* перебираем имена файлов/папок переданной директории (массива типа File) и
		 * сравниваем с искомой строкой, используя предидущий метод isContainMethod,
		 * если есть совпадение, пишем адрес этого файла/папки в список. */

		String kk;

		for (int i = 0; i < a.length; i++) {

			if (isContainMethod(a[i].getName(), b)) {

				kk = a[i].toString();

				/* адрес файла/папки, название которого совпадает с переданным словом,
				 * записывается в Список */

				findList.add(kk);
			}

		} // конец нашего цикла for

	} // конец Метода 2.1 isContainList()

	/* 2.2 Это перегуруженный Предидущий метод. Он принимает 2 строки для нахождения
	 * их в Директориях, и формирует 2 списка: Список адресов Папок с фотками и
	 * Список адресов Текстовых Файлов */

	private void IsContainList(File[] a, String b, String c) { // b - текст файл, с - папка фоток

		/* перебираем имена файлов/папок переданной директории (массива типа File) и
		 * сравниваем с искомой строкой, используя предидущий метод isContainMethod,
		 * если есть совпадение, пишем адрес этого файла/папки в список */

		String kk;

		for (int i = 0; i < a.length; i++) {

			if (isContainMethod(a[i].getName(), b)) {

				kk = a[i].toString();

				/* адрес файла/папки, название которого совпадает с переданным словом,
				 * записывается в Список */

				findList.add(kk);
			}

			if (isContainMethod(a[i].getName(), c)) {

				kk = a[i].toString();

				/*  адрес файла/папки, название которого совпадает с переданным словом,
				 * записывается в Список */

				photoList.add(kk);
			}

		} // конец нашего цикла for

	} // конец Метода 2.2 IsContainList
	
	/* 3.1 Метод получает директорию и имя файла/папки и осуществляет их поиск в
	 * этой директории с заданной глубиной вложения, т.е. использует Рекурсию и
	 * формирует Список со всеми адресами найденых совпадеий. Метод просто формирует
	 * Список, а уже сам список будет применяться в следующем Методе - , в которм из
	 * этого списка будут формироваться Статические переменные адресов файлов */

	private void inDirectorySearching(File[] arr, String bb) {

		j++; // при каждом вызове метода, j+1

		/*
		 * передаваемый Массив файлов помещаем в цикл, где к каждому элементу применяем
		 * метод listFiles(), т.е. раскладываем каждую Папку на ее содержимое и уже
		 * содержимое проверяем на соответствие искомому слову
		 */

		for (int i = 0; i < arr.length; i++) {

			/*
			 * в итоге мы имеем новый массив файлов (за каждую итерацию), содержащийся в
			 * каждой папке передаваемого Массива
			 */

			File[] arra = arr[i].listFiles(); // массив всех элементов содержащихся в каждой папке

			if (arra != null) { // если это не файл и если это папка с открытим доступом

				/*
				 * пробегаемся по всем элементам директории (и папкам и файлам), если есть
				 * совпадение записяваем этот путь в лист и углубляемся дальше
				 */

				isContainList(arra, bb);

			}

			/* отсеиаем только Папки чтобы перейти на уровень глубже */

			File[] arra1 = arr[i].listFiles(File::isDirectory); // массив только Директорий, это для углубления

			/*
			 * если к папке имеется доступ, то мы применяем Рекурсию, т.е. передаем эту
			 * директорию в наш метод аргументом и также ищем там нашу строку, как-бы
			 * погружаемся на уровень грубже
			 */

			if (arra1 != null) { // если к папке имеется доступ

				if (j < h - 1) { // условие для регулировки определенной глубины вложения

					inDirectorySearching(arra1, bb); // Рекурсивный вызов нашего Метода (самого себя)

					j--; // регулятор глубины вложния - надо разгадать тайну этого выражения) почему это
							// работает?
				}
			}

		} // конец цикла for

	} // конец 3.1 метода inDirectorySearching

	/* 3.2 Это предидущий перегруженный Метод, только принимает 2 строки для поиска
	 * в директории. Метод получает директорию и имя файла/папки и осуществляет их
	 * поиск в этой директории с заданной глубиной вложения, т.е. использует
	 * Рекурсию и формирует Список со всеми адресами найденых совпадеий. Просто
	 * формирует Список, а уже сам список будет применяться в следующем Методе, в
	 * которм из этого списка будут формироваться Статические переменные адресов
	 * файлов */

	private void InDirectorySearching(File[] arr, String bb, String cc) {

		j++; // при каждом вызове метода, j+1

		/* передаваемый Массив файлов помещаем в цикл, где к каждому элементу применяем
		 * метод listFiles(), т.е. раскладываем каждую Папку на ее содержимое и уже
		 * содержимое проверяем на соответствие искомому слову */

		for (int i = 0; i < arr.length; i++) {

			/* в итоге мы имеем новый массив файлов (за каждую итерацию), содержащийся в
			 * каждой папке передаваемого Массива */

			File[] arra = arr[i].listFiles(); // массив всех элементов содержащихся в каждой папке

			if (arra != null) { // если это не файл и если это папка с открытим доступом

				/* пробегаемся по всем элементам директории (и папкам и файлам), если есть
				 * совпадение записяваем эти пути в соответствующие списка (findList и
				 * photoList) и углубляемся дальше */

				IsContainList(arra, bb, cc); //
			}

			/* отсеиаем только Папки чтобы перейти на уровень глубже */

			File[] arra1 = arr[i].listFiles(File::isDirectory); // массив только Директорий, это для углубления

			/* если к папке имеется доступ, то мы применяем РЕКУРСИЮ, т.е. передаем эту
			 * директорию в наш метод аргументом и также ищем там нашу строку, как-бы
			 * погружаемся на уровень грубже */

			if (arra1 != null) { // если к папке имеется доступ

				if (j < h - 1) { // условие для регулировки определенной глубины вложения

					InDirectorySearching(arra1, bb, cc); // Рекурсивный вызов нашего Метода (самого себя)

					j--; // регулятор глубины вложния - надо разгадать тайну этого выражения) почему это
							// работает?
				}
			}

		} // конец цикла for

	} // конец 3.2 метода InDirectorySearching
	
	/* 4. Метод анализирующий нахождение файлов на Рабочем столе и по адресу в скрытом
	 * текстовом файле на рабочем столе (Первый из 3-х главных методов этого Класса)
	 * Используется в Методе №7.*/
	
	public AddressManager compFilesAnalyzer () {

		/* создадим Объекты класса File с нужными нам адресами, а затем проверим их на
		 * существование, если все существуют используем Конструктор № 3, если нет то будем
		 * искать есть ли
		 * скрытый текстовый файл на рабочем столе (с адресами наших 2-х файлов) - метод
		 * № 5 checkingTheAddresTextFile() и если его нет (или есть, но по этим адресам
		 * не те файлы или их нет) ищем файлы уже на всем компьютере, используя
		 * Предидущий метод № 3 inDirectorySearching() */

		String txtAdr = desktopPath + File.separator + projectFolderName
				+ File.separator + textFileName;
		String photoAdr = desktopPath + File.separator + projectFolderName
				//+ File.separator + photoFolderName.substring(0, photoFolderName.length()-1);
				+ File.separator + photoFolderName;
		
		File find_TextFile = new File(txtAdr); // текст файл
		File find_PhotoFolder = new File(photoAdr); // фото-папка

		////////////////////////////////////////////////////////////////////////////////////////////////////

		/* это блок в котором определяется что мы имеем. Есть 4 случая:
		 * 
		 * 1. Оба файла нашлись на Рабочем Столе
		 * 2. Оба файла существуют по адресам, считанным из скрытого файла на Раб. Столе
		 * 3. Текст файл есть на рабочем, Папки с фотками нет - ищем фотки в скрытом
		 * 4. Папка с фотками есть на рабочем, Текст файла нет - ищем Текст в скрытом */

		/* 1-ый случай: все есть на Рабочем - это работает */
		
		if (find_TextFile.exists() && find_PhotoFolder.exists()) {

			AddressManager manager = new AddressManager (txtAdr, photoAdr); // Конструктор №4
			
			return manager;
		}

		/* 2-ой случай: если Текст Файл на рабочем есть, а Фоток нет (искать нужно будет только папку с
		 * Фотками) */

		else if (find_TextFile.exists() && !find_PhotoFolder.exists()) {

			/* Текст файл есть на Рабочем, значит нужно чтобы метод checkHiddenFile чекнул
			 * в скрытом файле Адрес только Папки с фотками */

			/* заведем менеджер с адресом текст файла, на случай если checkHiddenFile
			 * ничего не найдет, тогда нам вернется менеджер с текстовым адресом */
			
			//ServiceMethods.windowShow("файл есть, фоток нет");
			
			AddressManager manager = new AddressManager (txtAdr, 1); // Конструктор №2
			
			manager = manager.checkHiddenFile(1); // нужен адрес фоток

			return manager;
		}

		/* 3-ий случай: если папка с Фотками на рабочем есть, а Текст Файла нет (искать нужно будет только
		 * Текст Файл) */

		else if (find_PhotoFolder.exists() && !find_TextFile.exists()) {

			/* Папка с фотками есть на Рабочем, значит нужно чтобы метод checkHiddenFile чекнул
			 * в скрытом файле Адрес только Текстового файла */

			/* заведем менеджер с адресом папки с фотками, на случай если checkHiddenFile
			 * ничего не найдет, тогда нам вернется менеджер с адресов фоток */
			
			//ServiceMethods.windowShow("фотки есть, текста нет");
			
			AddressManager manager = new AddressManager (1, photoAdr); // Конструктор №3
			
			manager = manager.checkHiddenFile(2); // нужен адрес текст файла

			return manager;
		}

		/* 4-ый случай: если на рабочем ничего нет и будем чекать Скрытый Текст файл - это рабоатает */
		
		else {

			/* если нечего не нашли на рабочем, чекаем стрытый файл*/

			AddressManager manager = new AddressManager (); // Конструктор №1
			
			manager = manager.checkHiddenFile(0);

			return manager;
		}

	} // конец Метода 4. compFilesAnalyzer
	
	/* 5. Метод, который будет проверять наличие на рабочем столе (или еще где-то)
	 * текстового документа, с 2-мя нужными мне адресами, и если документа нет, то
	 * возвращается тот же Менеджер, если же файл существует, метод считывает из
	 * него адреса файлов и записывает в Менеджер. Используется в
	 * предидущем Методе № 4 (второй из 3-х главных методов этого Класса) */

	private AddressManager checkHiddenFile(int a) {

		/* проверим наличие на рабочем столе скрытого текстового файла (для записи в
		 * него адресов нужных нам файлов) и при отсутствии заведм его Создаем объект
		 * класса File со следующим Конструктором */

		/* метод cerateNewFile() может выбрасывать Исключения, соответственно его нельзя
		 * употреблять без конструкции try-catch */

		File deskTop = new File(desktopPath); // директория - рабочий стол

		File myTextFile = new File(deskTop, v); // скрытый текстовый файл с адресами на рабочем столе

		if (myTextFile.exists()) { // если файл существует

			/* русский язык в текстовом файле shit не читает, викидывает исключение */

			try {

				/* класс Path, это класс пакета java.nio.file. Поэтому создаем Объект класса
				 * Path и используя метод get вводим адрес расположения нашего текстового файла */

				Path path = Paths.get(desktopPath + File.separator + v);

				/* используем класс File и его метод readAllLines() чтобы прочитать текстовый
				 * файл и сформировать из этих данных список Строк */

				List <String> stuff = Files.readAllLines(path);

				// GameClass.windowShowString("Размер считываемого файла - " + stuff.size());

				String[] a23 = stuff.toArray(new String[0]); // список переводим в Массив

				/* считываем адреса из скрытого текстового файла на рабочем столе */

				if ( stuff.size() == 2 && a==0) { // для случая поиска обоих файлов, а в скрытом 2 строки

					File findFile1 = new File(a23[0]); // формируем объект класса File с адресом из нашего массива найденых файлов
					File findFile2 = new File(a23[1]);

					if (findFile1.exists() && a23[0].contains(textFileName) && findFile2.exists() && a23[1]
							.contains(photoFolderName.substring(0, photoFolderName.length() - 1))) {

						AddressManager manah = new AddressManager (a23[0], a23[1]); // конструктор №4
		
						//System.out.println("Fist version");

						return manah;
					}

					else if ( findFile1.exists() && a23[0].contains( photoFolderName.substring(0, photoFolderName.length() - 1))
							&& findFile2.exists() && a23[1].contains(textFileName)) {
						
						AddressManager manah = new AddressManager (a23[1], a23[0]); // конструктор №4
						
						//System.out.println("Second version");
						
						return manah;
					}
					
					else {
					
					/* сюда попадаем, если в скрытый файл записали один адрес, но система считывает из как 2,
					 * поэтому из этих 2-х адресов выбираем один, содержащий имя либо Текст файла либо папки с фотками */

						if ( findFile1.exists() ) {

							if ( a23[0].contains(textFileName) ) {
								
								AddressManager manah = new AddressManager (a23[0], 1); // конструктор № 2
		
								return manah;
							}
							
							else if ( a23[0].contains(photoFolderName.substring(0, photoFolderName.length() - 1)) ) {
								
								AddressManager manah = new AddressManager (1, a23[0]); // конструктор № 3
		
								return manah;
							}
			
						}

						else if ( findFile2.exists() ) {

							if (  a23[1].contains(textFileName) )  {
								
								AddressManager manah = new AddressManager (a23[1], 1); // конструктор №2
	
								return manah;
							
							}
							
							else if ( a23[1].contains(photoFolderName.substring(0, photoFolderName.length() - 1)) ) {
								
								AddressManager manah = new AddressManager (1, a23[1]); // конструктор № 3

								return manah;
							}

						}

					} // конец else когда, в файле одна строка, но система считает, что 2

				} // конец if ( stuff.size() == 2 && a==0)
				
				else if ( stuff.size() == 2 && a==1 ) { // нужна только папка с фотками, а в скрытом 2 строки
					
					File findFile1 = new File(a23[0]); // формируем объект класса File с адресом из нашего массива найденых файлов
					File findFile2 = new File(a23[1]);
					
					if ( findFile1.exists() && a23[0].contains( photoFolderName.substring(0, photoFolderName.length() - 1)) ) {
						
						AddressManager manah = new AddressManager (this.fileAddress, a23[0]); // конструктор №4
						
						//System.out.println("i need photos, and in hidden there's two lines");

						return manah;
					}
					
					else if ( findFile2.exists() && a23[1].contains( photoFolderName.substring(0, photoFolderName.length() - 1))) {
						
						AddressManager manah = new AddressManager (this.fileAddress, a23[1]); // конструктор №4
						
						//System.out.println("i need photos, and in hidden there's two lines");

						return manah;	
					}
				}
				
				else if ( stuff.size() == 2 && a==2 ) { // нужен только текст файл, а в скрытом 2 строки
					
					File findFile1 = new File(a23[0]); // формируем объект класса File с адресом из нашего массива найденых файлов
					File findFile2 = new File(a23[1]);
					
					if ( findFile1.exists() && a23[0].contains(textFileName) ) {
						
						AddressManager manah = new AddressManager (a23[0], this.photoFolderAddress); // конструктор №4
						
						//System.out.println("i need text only, and in hidden there's two lines");

						return manah;
					}
					
					else if ( findFile2.exists() && a23[1].contains(textFileName) ) {
						
						AddressManager manah = new AddressManager (a23[1], this.photoFolderAddress); // конструктор №4
						
						//System.out.println("i need text only, and in hidden there's two lines");

						return manah;	
					}
				}
				
				else if ( stuff.size() == 1 && a==1 ) { // нужна только папка с фотками, и в скрытом 1 строка
					
					File findFile1 = new File(a23[0]); // формируем объект класса File с адресом из нашего массива найденых файлов
					
					if ( findFile1.exists() && a23[0].contains( photoFolderName.substring(0, photoFolderName.length() - 1)) ) {
						
						AddressManager manah = new AddressManager (this.fileAddress, a23[0]); // конструктор №4
	
						return manah;
					}
				}
				
				else if ( stuff.size() == 1 && a==2 ) { // нужен только текст файл, и в скрытом 1 строка
					
					File findFile1 = new File(a23[0]); // формируем объект класса File с адресом из нашего массива найденых файлов
					
					if ( findFile1.exists() && a23[0].contains(textFileName) ) {
						
						AddressManager manah = new AddressManager (a23[0], this.photoFolderAddress); // конструктор №4

						return manah;
					}
				}
				
				else { // это от условия if (stuff.size() == 2 && a==0 )

				/* т.е. если и число строк в скрытом файле не 2 и не 1, */
					
					//ServiceMethods.windowShow("Количество адресов в скрытом файле не равно двум");
					
					/* возвращаем объект как был (не пустой)*/
					
					return this;
				}

			} catch (IOException e) {

				//ServiceMethods.windowShow("something went wrong and the error is " + e.getMessage());
			}

		} // конец главного if (myTextFile.exists()), т.е. если Скрытый Тексе файл существует
		
		/* возвращаем объект как был (не пустой) */
		
		return this;

	} // конец Метода № 5. checkHiddenFile (int a)
	
	/* 6. Метод который осуществляет поиск файлов (папок) проекта а Компе и возвращает Менеджер,
	 * в котопый пишет найденные Адреса. Если после этого метода в Менеджере не хватает адресов,
	 * значит из нет на компьютере. (Третий из 3-х главных методов этого Класса).
	 * Используется в Методе №7. */
	
	private AddressManager compFilesFinder () {

		////////////// Блок открытия Окна длительности процесса в отдельном Потоке /////////////
		/* закрыние уже будет в методе setting */
		
//		SwingUtilities.invokeLater(() -> {
//
//			MyWindow.procesWindow("Внимание! Идет поиск файлов проекта...");
//		});

		/////////////////////////////////////////////////////////////////////////////////////////////////////////

		File[] roots = File.listRoots(); // составляем список жестких дисков

		/* Первый случай, когда требудтся оба файла.
		 * Будет формироваться 2 списка с адресами: findList and photoList */

		if ( this.fileAddress.equals("") && this.photoFolderAddress.equals("") ) {

			/* здась используем метод, который формирует 2 списка */

			InDirectorySearching(roots, textFileName,
					photoFolderName.substring(0, photoFolderName.length() - 1));

			String fileAdr = createFinalTextAddres(findList);

			String photoAdr = createFinalPhotoAddres(photoList); // photoList используется только в этом блоке

			AddressManager manah = new AddressManager (fileAdr, photoAdr); // конструктор №4
			
			findList.clear();  // очищаем Списки
			photoList.clear();
			
			writingFilesToHiddingFile(manah); // пишем адреса в скрытый файл
			
			return manah;	
		}

		/* Второй случай: нужно искать только Текст файл */
		/* Будет формироваться список адресов Текстового файла findList */

		else if ( this.fileAddress.equals("") && !this.photoFolderAddress.equals("") ) {

			/* надо чтобы этот метод формировал findList */

			inDirectorySearching(roots, this.textFileName);

			/* далее используем метод по обрабоке списка найденных адресов Текстовог файла и
			 * выделения из него одного подходящего нам */

			String fileAdr = createFinalTextAddres(findList);

			AddressManager manah = new AddressManager (fileAdr, this.photoFolderAddress); // конструктор №4
			
			findList.clear(); // очищаем Список
			
			writingFilesToHiddingFile(manah); // пишем адреса в скрытый файл

			return manah;	
		}
		
		/* Третий случай: нужно искать только Папку с фотками */
		/* Будет формироваться список адресов Папки с фотками findList */
		
		else if ( !this.fileAddress.equals("") && this.photoFolderAddress.equals("") ) {

			/* надо чтобы этот метод формировал findList */

			inDirectorySearching(roots,
					this.photoFolderName.substring(0, this.photoFolderName.length() - 1));

			/*далее используем метод по обрабоке списка найденных адресов Папки с
				 * фотографиями и выделения из него одного подходящего нам */

			String photoAdr = createFinalPhotoAddres(findList);

			AddressManager manah = new AddressManager (this.fileAddress, photoAdr); // конструктор №4
			
			findList.clear(); // очищаем Список
			
			writingFilesToHiddingFile(manah); // пишем адреса в скрытый файл

			return manah;
		}

		/* Четвертый случай: когда !this.fileAddress.equals("") && !this.photoFolderAddress.equals(""),
		 * т.е. когда адреса есть - значит возвращаем Объект как есть: this */
		
		else {

			return this;
		}

	} // конец Метода 6. compFilesFinder

	/* 7.1 Метод по определению адреса Текст Файла из Списка всех найденных Если
	 * Список пуст, вернется строка "". Применяется в предидущем методе 4. */

	private String createFinalTextAddres(ArrayList<String> MyList) {

		String adres = "";

		/* если передаем НЕ пустой список */

		if (MyList.size() != 0) {

			/* цикл отсеивания из Списка адресов, ссылающихся на Ресайкл Бины */

			String[] myTempArray = MyList.toArray(new String[0]); // переводим Список в Массив, а Список очищаем

			MyList.clear(); // очистка Списка

			for (int i = 0; i < myTempArray.length; i++) {

				if (myTempArray[i].contains("$Recycle.Bin") || myTempArray[i].contains("$RECYCLE.BIN")) {

					// GameClass.windowShowString("Этот адрес не пишем - " + myTempArray[i]);

				} else {

					// GameClass.windowShowString("Этот адрес оставляем - " + myTempArray[i]);
					MyList.add(myTempArray[i]);
				}

			}

			/* после этого блока в адресе Папки с фотками еще нет черты "\\" */

			// GameClass.windowShowString("После просеивания размер Списка - " +
			// findList.size());

			/* формируем Массив из строк с адресами найденных файлов/папок */

			String[] myArray = MyList.toArray(new String[0]);

			/* заводим два списка, в которые далее в цикле будем записывать адреса найденых
			 * текстовых файлов и адреса папок с фотками (если их на компе будет найдено
			 * несколько) чтобы в дальнейшем выбирать из них наиболее свежие по дате */

			List<String> adrFl = new ArrayList<String>(); // под текст файлы

			/* далее следует цикл проверки всех найденных адресов на существование файлов на
			 * которые они указывают */

			for (int i = 0; i < myArray.length; i++) {

				/* формируем объекты класса File по адресам, совпадений записанных в наш Список
				 * (т.е. Адреса: папки и в ней текстового файла и папки с фотками) и если все
				 * три данных объекта существуют, то присваиваем эти адреса нашим статическим
				 * переменным fileAddres и addres */

				File findTextFile = new File(myArray[i]);
				// File findPhotoFolder = new File (myArray[i]); // адрес еще без черты "\\"

				/* формируем адрес текстового файла */

				if (findTextFile.exists() && myArray[i].contains(this.textFileName)) {

					adrFl.add(myArray[i]); // собираем адреса всех найденых файлов Проекта
				}

			} // конец нового цикла for

			/* блок присваивания переменной GameClass.fileAddres значения из списка adrFl с
			 * адресом наиболее свежего по дате изменения файла */

			try {

				String newestFilePath = findNewestFile(adrFl);

				if (newestFilePath != null) {

					// System.out.println("The most recently modified file is: " + newestFilePath);

					/* вот здесь и присваиваем нашей переменной значиние адреса с самым новым файлом */

					adres = newestFilePath; // то что возвращает метод

				} else {
					//System.out.println("No valid files found.");
				}

			} catch (IOException e) {

				e.printStackTrace();
			}

			/* вот это и возвращаем, если в Метод передавался не пустой список, хотя если в
			 * нем были только Recycle файлы, то может и пустой здесь возвратиться */

			return adres;

		} else {

			/* если в Метод передали пустой Список, возвращается "" */

			return adres;
		}

	} // конец Метода 8.1

	/* 7.2 Метод по определению адреса Папки с Фотками из Списка всех найденных Если
	 * Список пустой, метод вернет строку "". Применяется в предидущем методе 4 */

	private String createFinalPhotoAddres(ArrayList<String> MyList) {

		String adres = "";

		/* если в Метод передали НЕ пустой список */

		if (MyList.size() != 0) {

			/* цикл отсеивания из Списка адресов, ссылающихся на Ресайкл Бины */

			String[] myTempArray = MyList.toArray(new String[0]); // переводим Список в Массив, а Список очищаем

			MyList.clear(); // очистка Списка

			for (int i = 0; i < myTempArray.length; i++) {

				if (myTempArray[i].contains("$Recycle.Bin") || myTempArray[i].contains("$RECYCLE.BIN")) {

					// GameClass.windowShowString("Этот адрес не пишем - " + myTempArray[i]);

				} else {

					// GameClass.windowShowString("Этот адрес оставляем - " + myTempArray[i]);
					MyList.add(myTempArray[i]);
				}

			}

			/* после этого блока в адресе Папки с фотками еще нет черты "\\" */

			// GameClass.windowShowString("После просеивания размер Списка - " +
			// findList.size());

			/* формируем Массив из строк с адресами найденных файлов/папок */

			String[] myArray = MyList.toArray(new String[0]);

			/*
			 * заводим два списка, в которые далее в цикле будем записывать адреса найденых
			 * текстовых файлов и адреса папок с фотками (если их на компе будет найдено
			 * несколько) чтобы в дальнейшем выбирать из них наиболее свежие по дате
			 */

			List<String> adrPh = new ArrayList<String>(); // под папки фоток

			/*
			 * далее следует цикл проверки всех найденных адресов на существование файлов на
			 * которые они указывают
			 */

			for (int i = 0; i < myArray.length; i++) {

				/*
				 * формируем объекты класса File по адресам, совпадений записанных в наш Список
				 * (т.е. Адреса: папки и в ней текстового файла и папки с фотками) и если все
				 * три данных объекта существуют, то присваиваем эти адреса нашим статическим
				 * переменным fileAddres и addres
				 */

				File findPhotoFolder = new File(myArray[i]); // адрес еще без черты "\\"

				/* формируем адрес Папки с фотографиями */

				if (findPhotoFolder.exists() && myArray[i]
						.contains(this.photoFolderName.substring(0, this.photoFolderName.length() - 1))) {

					adrPh.add(myArray[i]); // собирае адреса всех папок с фотками проекта

				}

			} // конец нового цикла for

			/*
			 * блок присваивания переменной GameClass.addres значения из списка adrFl с
			 * адресом наиболее свежего по дате изменения файла
			 */

			try {

				String newestFolderPath = findNewestFolder(adrPh);

				if (newestFolderPath != null) {

					// System.out.println("The most recently modified file is: " + newestFilePath);

					/*
					 * вот здесь и присваиваем нашей переменной значиние адреса с самой новой папкой
					 */

					adres = newestFolderPath + "\\"; // то что возвращает метод

				} else {
					//System.out.println("No valid folders found.");
				}

			} catch (IOException e) {

				e.printStackTrace();
			}

			/*
			 * вот это и возвращаем, если в Метод передавался не пустой список, хотя если в
			 * нем были только Recycle файлы, то может и пустой здесь возвратиться
			 */

			return adres;

		} else {

			return adres; // здесь возвращается "" - это если передали пустой Список
		}

	} // конец Метода 5.2 createFinalPhotoAddres

	/* 8. Это Метод, который пишет в Скрытый Текстовый файл переменные
	 * GameClass.fileAddres и GameClass.addres Применяется в  */

	private void writingFilesToHiddingFile(AddressManager manag) {

		/////////////// блок записи найденных адресов в текстовый файл на рабочем  столе ///////////////////

		/* проверим наличие на рабочем столе текстового файла и при отсутствии заведм
		 * его Создаем объект класса File со следующим Конструктором */

		/* метод cerateNewFile() может выбрасывать Исключения, соответственно его нельзя
		 * употреблять без конструкции try-catch */

		File deskTop = new File(manag.desktopPath); // директория - рабочий стол

		// File myTextFile = new File (deskTop, v); // текстовый файл с адресами на
		// рабочем столе

		try {

			// если такой папки нет, создаем ее, хотя рабочий стол должен быть всегда

			if (!deskTop.exists()) {

				deskTop.mkdirs();
			}

			/* В нашей Директории (на рабочем столе) создадим наш текстовый скрытый файл, а
			 * если он уже есть то перезапишем его */

			String x1 = manag.desktopPath + File.separator + v; // путь к файлу

			Path hiddenFile = Paths.get(x1);

			/* создаем скрытый текстовый файл, если его еще нет на Рабочем Столе */

			if (Files.notExists(hiddenFile)) {

				Files.createFile((hiddenFile)); // создаем файл
				Files.setAttribute(hiddenFile, "dos:hidden", true); // устанавливаем его свойства

				// GameClass.windowShowString("Hidden file was created.");

				try (BufferedWriter writer = new BufferedWriter(new FileWriter(x1, true))) {

					writer.write(manag.fileAddress);
					writer.newLine();
					writer.write(manag.photoFolderAddress + "\n");

				} catch (IOException e) {
					e.printStackTrace();
				}

				/* если наш скрытый файл уже существует, то перезаписываем туда наши адреса */

			} else {

				Files.setAttribute(hiddenFile, "dos:hidden", false); // делаем его НЕскрытым, пишем в него и Скрываем

				try (BufferedWriter writer = new BufferedWriter(new FileWriter(x1))) {

					writer.write(manag.fileAddress);
					writer.newLine();
					writer.write(manag.photoFolderAddress + "\n");

					Files.setAttribute(hiddenFile, "dos:hidden", true); // делаем файл Скрытым

				} catch (IOException e) {

					e.printStackTrace();
				}

				//ServiceMethods.windowShow("Наш скрытый текст файл перезаписан.");
			}

		} catch (IOException e) {

			//System.err.println("Error creating: " + e.getMessage());
		}

	} // конец метода 9. writingFilesToHiddingFile

	/* 9.1 Вот этот метод будет принимать список строк (ArrayList) т.е. адреса
	 * файлов, потом чекать атрибуты этих файлов и выбирать наиболее "свежий" файл,
	 * т.е. файл с самой свежей датой последней модификации и будет возвращать эту
	 * Строку (адрес). Применяться будте в методе №4 MyNewGameFilesAddress () при
	 * формировании переменной GameClass.fileAddres. */

	private String findNewestFile(List<String> filePaths) throws IOException {

		Path newestFilePath = null;

		long newestModifiedTime = Long.MIN_VALUE;

		for (String filePath : filePaths) {

			Path path = Paths.get(filePath);

			// Check if the path exists and is a file

			if (Files.exists(path) && Files.isRegularFile(path)) {

				BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
				long lastModifiedTime = attrs.lastModifiedTime().toMillis();

				// Update the newest file if this one is more recent
				if (lastModifiedTime > newestModifiedTime) {
					newestModifiedTime = lastModifiedTime;
					newestFilePath = path;
				}
			}
		}

		return (newestFilePath != null) ? newestFilePath.toString() : null;

	} // конец Метода № 10.1 findNewestFile

	/* 9.2 Вот этот метод такой же как предидущий только оперирует адресами Папок.
	 * Будет принимать список строк (ArrayList) т.е. адреса папок, потом чекать
	 * атрибуты этих папок и выбирать наиболее "свежую" папку, т.е. папку с самой
	 * свежей датой последней модификации и будет возвращать эту Строку (адрес).
	 * Применяться будте в методе №4 MyNewGameFilesAddress () при формировании
	 * переменной GameClass.addres. */

	private String findNewestFolder(List<String> folderPaths) throws IOException {

		Path newestFilePath = null;

		long newestModifiedTime = Long.MIN_VALUE;

		for (String filePath : folderPaths) {

			Path path = Paths.get(filePath);

			// Check if the path exists and is a file

			if (Files.exists(path) && Files.isDirectory(path)) {

				BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
				long lastModifiedTime = attrs.lastModifiedTime().toMillis();

				// Update the newest file if this one is more recent
				if (lastModifiedTime > newestModifiedTime) {
					newestModifiedTime = lastModifiedTime;
					newestFilePath = path;
				}
			}
		}

		return (newestFilePath != null) ? newestFilePath.toString() : null;

	} // конец Метода № 10.2 findNewestFolder

	///////////////// Далее идет 3 public метода, ради который все и затевалось ///////////////
	
	/* 10. Единственный метод, используемый в main и комбинирующий 3 главных
	 * метода данного класса т.е. сначала анализирует Файлы на Рабочем Столе,
	 * затем Адреса в Скрытом Текст. файле (compFilesAnalyzer()), затем (если это нужно)
	 * производит поиск файлов на Компе (compFilesFinder()) и проверяет наличие Интернета,
	 * заполняя поле isInetHere, а затем если что-либо отсутствует на Компе, скачивает
	 * (текстФайл) или предлагает скачать (ФотоПапка) */
	
	public AddressManager setting () {
		
		AddressManager manag = this;

		manag = manag.compFilesAnalyzer(); // анализируем Раб Стол и Скрыт файл
		manag = manag.compFilesFinder();   // ищем файлы на компе
		
		/* Блок закрытия окна длительности процесса, начатого в методе
		 * compFilesFinder(). Вынесен сюда, чтобы не влиять на другие процессы */
		
//		SwingUtilities.invokeLater(() -> {
//
//			for (Frame frame : Frame.getFrames()) {
//
//				frame.dispose();
//			}
//
//		});
		
		manag = manag.managWebRefresh(); // если надо скачиваем файлы с GitHub
		
		if ( manag.fileAddress.equals("") ) {
			
			//ServiceMethods.windowShow("Отсутствует файл проекта. Программа будет завершена.");
			
			return manag;
		}
		
		manag = manag.setReadFrom(0); // устанвалиаем считывание с Компа
		
		return manag;
	}
	
	/* 11. Метод служебный, для просмотра содержимого Менеджера.
	 * Применяю в методе inputAnalyse класса GameListClass в условии, когда ввели
	 * "managInfo" с клавиатуры для простомта Менеджера */
	
	public void showManagInfo () {
		
		String[] ar = new String[] { "Text file: \"" + this.fileAddress + "\"",
									 "Photo:    \"" + this.photoFolderAddress + "\"",
									 "Is Inet here? - \"" + this.isInetHere + "\"",
									 "reading from Inet: \"" + this.readFrom + "\"" };
		
		//ServiceMethods.windowShow(ar, "Менеждер содержит: ");
	}
	
	/* 12. Метод, который я применяю в методе inputAnalyse класса GameListClass,
	 * в условии, когда ввели InetOn или InetOff с клавиатуры, и который меняет
	 * значение поля AddressManager в зависимости от переданного числа (0 или 1) */
	
	public AddressManager setReadFrom (int a) {
		
		if (a==0) {
			
			this.readFrom = false;
			
			return this;
		}
		
		/* если хотим перейти на Инет, нужно чтобы Инет был на компе */
		
		else if ( a==1 && this.isInetHere) {
			
			this.readFrom = true;
			
			return this;
		}
		
		/* хотим перейти на Инет, а инета на компе нет, получим сообщуху */
		
		else if ( a==1 && !this.isInetHere ) {
			
			this.readFrom = false;
			
			//ServiceMethods.windowShow("С инетом какая-то хрень!");
			
			return this;
		}
	
		/* если ни одно условие не выполненно, возвращается неизменённый менеджер */
		
		else return this;	
		
	} // конец Метода 12
	
	/* 13. Метод, который анализирует "финальный" Менеджер и если
	 * отсутствует Адрес ТекстФайла или Адрес Папки с фотками - предлагает их скачать
	 * Метод обновит (скачает) инфу только если файлы на компе вообще не нашлись,
	 * если нашлись хотябы пустые, обновлять он их не будет (их можно будет обновить
	 * "принудительно" вводя refreshPhoto или refreshFile */
	
	private AddressManager managWebRefresh () {
		
		if ( this.fileAddress.equals("") && this.isInetHere ) {
			
			GitHubSynchronize synch = new GitHubSynchronize();
			
			this.fileAddress = synch.refreshTextFile(this);
			
		}
		
		else if ( this.fileAddress.equals("") && !this.isInetHere ) {
			
			//ServiceMethods.windowShow("текст файла нет и инета нет");
		}
		
		
		if ( this.photoFolderAddress.equals("") && this.isInetHere ) {
			
			if (ServiceMethods.yesNoWindow("Фоток на компе нет. Скачиваем с Инета (Yes) или продолжаем без них (No)")==0) {
				
				GitHubSynchronize synch = new GitHubSynchronize();
			
				this.photoFolderAddress = synch.downloadDiffArray(this);
				
				//ServiceMethods.windowShow("Фотки обновлены, адрес папки: " + this.photoFolderAddress);
				
			} else {
				
				//ServiceMethods.windowShow("Фотографий нигде нет");
			}
		}
		
		return this;
	}
	
	/* 14. Метод, который создает Папку Проекта на Рабочем столе
	 * Использую здесь в 15 методе и в GitHubSynchronize в 1 и 5 методах */
	
	public String createProjectFolder () {
		
		File folder = new File(desktopPath, projectFolderName);
		
		/* если папка не существует - создаем ее, иначе - она уже существует,
		 * в обоих случаях возвращаем Путь к этой папке */
		
		if (!folder.exists()) { 
			
			boolean folderCreated = folder.mkdir();
			
			if (folderCreated) { // если папка создалась - возвращаем Путь
				
				//System.out.println("Folder created: " + folder.getAbsolutePath());
				
				return folder.getAbsolutePath();
				
			} else { // если не создалась - возвращаем null
				
				//System.out.println("Failed to create folder.");
				
				return null; 
			}
			
		} else {
			
			//System.out.println("Folder already exists: " + folder.getAbsolutePath());
			
			return folder.getAbsolutePath();
		}
		
	} // конец Метода № 14
	
	/* 15. Метод, который создает Папку Проекта на Рабочем столе и в ней еще Папку
	 * Использую в GitHubSynchronize в 5 методе */
	
	public String createPhotoFolder () {
		
		/* создадим (если ее нет) папку Проекта на Рабочем */
		
		String mainFold = createProjectFolder();
		
		/* создадим File для нашей папки GamesPhoto */
		
		File folder = new File(mainFold, this.photoFolderName);
		
		/* если папка не существует - создаем ее, иначе - она уже существует,
		 * в обоих случаях возвращаем Путь к этой папке */
		
		if (!folder.exists()) { 
			
			boolean folderCreated = folder.mkdir();
			
			if (folderCreated) { // если папка создалась - возвращаем Путь
				
				//System.out.println("Folder created: " + folder.getAbsolutePath());
				
				return folder.getAbsolutePath();
				
			} else { // если не создалась - возвращаем null
				
				//System.out.println("Failed to create folder.");
				
				return ""; 
			}
			
		} else {
			
			//System.out.println("Folder already exists: " + folder.getAbsolutePath());
			
			return folder.getAbsolutePath();
		}
		
	} // конец Метода № 15
	
} // конец Класса
