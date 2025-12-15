
package githubborrra.game;

import githubborrra.service.*;
import githubborrra.inet.AddressManager;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

/* Класс в котором расположено описание Полей Игры, т.е. Поля и Конструкторы по формированию
* наший Объектов (Игр) а также Методы по обработке наших Объектов. Это и Поиск и Сортировка,
* вывод на экран Фотографий из папок и из Интернета. Также содержит статические переменные
* с агресами файлов проекта. Нужно будет соствить подроброе содержание этого Класса */

public class GameClass {

	/////////////////// Данные (Поля) моего Класса (10 штук) / /////////////////////////

	// 7 основных

	private String name;
	private String creator;
	private String mapper;
	private String year;
	private String comment;
	private int amount;

	private String[] pics;

	// 3 внутренних (использую только в Конструкторах)

	private String addr1 = "1. General/1.jpg"; // Файл Фоток по умолчанию
	private String addr2 = "1. General/2.jpg";
	private String addr3 = "1. General/3.jpg";

	/////////////////// Конструкторы моего Класса (6 штук) /////////////////////////

	/* 1. конструктор, если вводишь только Название (+ 3 дефолтных фотки) */

	public GameClass(String adres, String name) {
	
		this.name = name;
		this.creator = "no name";
		this.mapper = "unknown";
		this.year = "19**";
		this.comment = "stay sharp";
		this.amount = 3;
		this.pics = new String[] { adres + addr1, adres + addr2, adres + addr3 };
	}

	/* 2. конструктор, если вводишь Название и Издателя (+ 3 дефолтных фотки) */

	public GameClass(String adres, String name, String creator) {
		
		this.name = name;
		this.creator = creator;
		this.mapper = "unknown";
		this.year = "19**";
		this.comment = "stay sharp";
		this.amount = 3;
		this.pics = new String[] { adres + addr1, adres + addr2, adres + addr3 };
	}

	// 3. конструктор, если вводишь Название, Издателя и Год (+ 3 дефолтных фотки)

	public GameClass(String adres, String name, String creator, String year) {

		this.name = name;
		this.creator = creator;
		this.mapper = "unknown";
		this.year = year;
		this.comment = "stay sharp";
		this.amount = 3;
		this.pics = new String[] { adres + addr1, adres + addr2, adres + addr3 };
	}

	/* 4. конструктор, если вводишь Название, Издателя, Маппер и Год (+ 3 дефолтных фотки) */

	public GameClass(String adres, String name, String creator, String mapper, String year) {

		this.name = name;
		this.creator = creator;
		this.mapper = mapper;
		this.year = year;
		this.comment = "stay sharp";
		this.amount = 3;
		this.pics = new String[] { adres + addr1, adres + addr2, adres + addr3 };
	}

	/* 5. конструктор при вводе всех полей (+ 3 дефолтных фотки) */

	public GameClass(String adres, String name, String creator, String mapper, String year, String comment) {

		this.name = name;
		this.creator = creator;
		this.mapper = mapper;
		this.year = year;
		this.comment = comment;
		this.amount = 3;
		this.pics = new String[] { adres + addr1, adres + addr2, adres + addr3 };
	}

	/* 6. Конструктор для записи массива фоток целиком (1 и более фоток), этот
	 * Конструктор помог избавитья от большого кол-ва конструкторов отличающихся
	 * только кол-ом фотографий. Т.е. теперь можо записывать в конструктор
	 * бесконечное кол-во фотографий одной игры. Сколько фоток есть в считываемом
	 * файле, столько и запишем */

	public GameClass(String adres, String name, String creator, String mapper, String year, String comment, String[] pic) {

		this.name = name;
		this.creator = creator;
		this.mapper = mapper;
		this.year = year;
		this.comment = comment;

		if (pic.length == 1) { // если 1 фотка кастомная

			this.amount = 3;
			this.pics = new String[] { adres + pic[0], adres + addr2, 
					adres + addr3 };
		}

		else if (pic.length == 2) { // если 2 фотки кастомные

			this.amount = 3;
			this.pics = new String[] { adres + pic[0], adres + pic[1], adres + addr3 };
		}

		else { // если 3 и более фотки кастомные

			this.amount = pic.length; // соответственно кол-во фоток определяется размером Массива
			
			/* заведем Массив, который преобразует массив с общими адресами фоток (Dizzy/1.jpg) в
			 * массив полных адресов (C:\Users\Евгений\Desktop\...\Dizzy\1.jpg)  - его и вернем */
			
			String[] mas = new String [pic.length];
			
			for (int i=0; i<pic.length; i++) {
				
				mas[i] = adres + pic[i];
			}
			
			this.pics = mas; // записываем в Поле pics массив адресов фоток целиком
		}
	}

///////////////////////////// Методы моего Класса ///////////////////////////

	// 6 getters 

	// 1.1 getter of "name" field (метод по получению Данных поля "name")

	public String getName() {
		return name;
	}

	// 1.2 getter of "name" field (метод по получению Данных поля "name")

	public String getCreator() {
		return creator;
	}

	// 1.3 getter of "name" field (метод по получению Данных поля "name")

	public String getMapper() {
		return mapper;
	}

	// 1.4 getter of "year" field - используется только в MyComp (для сортировки по
	// году)

	public String getYear() {
		return year;
	}

	// 1.5 getter of "year" field - используется только в MyComp (для сортировки по
	// году)

	public String getComment() {
		return comment;
	}

	/* 1.6 получаем кол-во фоток данного объекта (игры) */
	
	public int getAmount () {
		
		return amount;
	}
	
	/* 1.7 получаем массив фоток данного объекта (игры) */
	
	public String[] getPics () {
		
		return pics;
	}
	
	/* 2.1. Метод по просмотру фоток игры */
	
	public void showPics(boolean inet) {
		
		/* показываем Инфу по игре */
		
		gameInfo();
		
		/* если читаем с Инета и Инет есть */
		
		if ( inet && AddressManager.isInetAvalible() ) {
			
			for (int i=0; i<this.getAmount(); i++) {
				
				this.webOneGameShow(this.pics[i]);
			 
			}
		} 
		
		/* если в листе у нас стоит считывание с компа либо Инета нет */
		
		else {
			
			ImageIcon img;

			/* можно будет сделать так, что перед показом первой фотки, она по адресу
			 * проверялась на существование, если не сущ. - не показ. остальные */
			
			for (int i = 0; i < this.getAmount(); i++) {

				File check = new File (pics[0]);
				
				/* проверим адрес первой фотки на существование: да, смотрим дальше,
				 * нет - выходим из цикла */
				
				if (check.exists()) {

					img = new ImageIcon(pics[i]);
					JOptionPane.showMessageDialog(null, img, "That's the Game Photo", JOptionPane.PLAIN_MESSAGE);
					
				} else {

					//ServiceMethods.windowShow("Something's wrong with pics of this Game");
					break;
				}
			}
		}
		
	} // конец Метода 2.1 showPics()
	
	// 2.2. Метод по выводу в Окно (JOptionPane) информации об Игре (издатель, маппе и т.д.)

	private void gameInfo() {

		String text = "";
		String title;

		text = "  " + text + "\n" + "        Creator: " + this.getCreator() + "\n" + "        Maper:   "
				+ this.getMapper() + "\n" + "        Year:     " + this.getYear() + "\n" + "        Comment: "
				+ this.getComment() + "\n\n";

		// формируем надпись в заголовке Окна используя Статич. Переменные j и k

		title = this.getName();

		JTextArea textArea = new JTextArea(text);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(400, 200));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода 2.2
	
	/* 2.3 Метод по показу одной фотки из Инета */
	
	private void webOneGameShow (String webURL) {

		String fullURL = webURL.replace(" ", "%20");
				
		try {
			
			URL url = new URL(fullURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000); // Set timeout
			connection.setReadTimeout(5000); // Set timeout

		    // Connect to the URL
			
			connection.connect();

			// Check the response code
			
			int responseCode = connection.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) { // если картинка доступна выводим ее
				
				//GameClass.windowShowString("Input stream can be opened.");
				
				try {
					
					Image image = ImageIO.read(url);
				
					// Create an ImageIcon from the image
					
					ImageIcon imageIcon = new ImageIcon(image);

					// Create a JLabel to hold the ImageIcon
					
					JOptionPane.showMessageDialog(null, imageIcon, "That's the Photo of The Game", JOptionPane.PLAIN_MESSAGE);
					
					} catch (MalformedURLException e) {
						
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						
					} catch (IOException e) {
						
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				
			} else {
				
				ServiceMethods.windowShow ("Cannot open input stream. Response code: " + responseCode);
			}
			
		} catch (IOException e) {
			
			System.out.println("An error occurred: " + e.getMessage());	
	    }
		
	} // конец метода 2.3 webOneGameShow (String webURL)

} // конец Класса GameClass

