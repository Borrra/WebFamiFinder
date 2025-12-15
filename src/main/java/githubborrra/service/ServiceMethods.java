package githubborrra.service;

import githubborrra.game.GameClass;
import githubborrra.game.GameFolderClass;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.awt.*;

import javax.swing.*;

import java.lang.reflect.Field;


/* В данном файле 4 Класса: Первый - основной и остальные 3 вспомогательные */

////////////////////////// ПЕРВЫЙ КЛАСС /////////////////////////////

/* В этом классе будут рутинные (служебные Методы) для моего проекта */

public class ServiceMethods {

	/* 10 Методов windowShow/windowShoww по показу даных и 4 разных:
	 * 
	 * 1.  windowShow (String arr);
	 * 2.  windowShow (String arr, String akk);
	 * 3.  windowShow (String[] arr);
	 * 4.  windowShow (String[] arr, String a);
	 * 5.  windowShoww(List<String> arr);
	 * 6.  windowShoww(List<String> arr, String a);
	 * 7.  windowShow (GameClass[] arrg);
	 * 8.  windowShow (List<GameClass> arrg);
	 * 9.  windowShow (GameFolderClass[] arrg, String p); вывод всей инфы
	 * 10. windowShoww(GameFolderClass[] arrg, String p); вывод только назв. Папки
	 * 
	 * 11. informWindow();
	 * 12. List<String> getFieldListNew(List<GameClass> objects, String fieldName);
	 * 13. boolean isStarCodeOk(String k);
	 * 14. int yesNoWindow(String k); */

	// 1. Метод выводит в Окно передаваюмую ему Строку

	public static void windowShow(String arr) {

		String text = arr;
		String title = "Let's see what we got here:";

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 100));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 1

	/* 2. Метод выводит в Окно передаваюмую ему Строку и вторую передаваемую ему
	 * Строку он выводит в заголовок Окна */

	public static void windowShow(String arr, String akk) {

		String text = arr;
		String title = akk;

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 100));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 2

	// 3. Метод выводит в Окно списком Массив Строк

	public static void windowShow(String[] arr) {

		String text = "";
		String title;

		for (int i = 0; i < arr.length; i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arr[i] + "\n";
		}

		title = "There're " + arr.length + " elements we have";

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 3

	// 4. Метод выводит в Окно списком Массив Строк и Заголовок Окна

	public static void windowShow(String[] arr, String a) {

		String text = "";
		String title;

		for (int i = 0; i < arr.length; i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arr[i] + "\n";
		}

		title = arr.length + " elements. " + a;

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 4

	// 5. Метод выводит в Окно списком Список List <String>

	public static void windowShoww(List<String> arr) {

		String text = "";
		String title;

		for (int i = 0; i < arr.size(); i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arr.get(i) + "\n";
		}

		title = "There're " + arr.size() + " elements we have";

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 5

	// 6. Метод выводит в Окно списком Список List <String> и Заголовок Окна

	public static void windowShoww(List<String> arr, String a) {

		String text = "";
		String title;

		for (int i = 0; i < arr.size(); i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arr.get(i) + "\n";
		}

		title = arr.size() + " elements. " + a;

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 6

	/* 7. Метод по выводу Имен Массива Объектов GameClass[] в Окно (JOptionPane) со
	 * Слайдером в заголовок передаются Статич. переменные чтобы значть что ищем  */

	public static void windowShow(GameClass[] arrg) {

		String text = "";
		String title;

		for (int i = 0; i < arrg.length; i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arrg[i].getName() + "\n";
		}

		// формируем надпись в заголовке Окна используя Статич. Переменные j и k

		title = "There're " + arrg.length + " elements found. Serching for ";

		if (arrg.length == 0) {

			text = "\n\tNothing is found, dude!";
		}

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец метода № 7

	// 8. Метод по выводу Имен Списка Объектов List <GameClass> в Окно (JOptionPane) со Слайдером

	public static void windowShow(List<GameClass> arrg) {

		String text = "";
		String title;

		for (int i = 0; i < arrg.size(); i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arrg.get(i).getName() + "\n";
		}

		// формируем надпись в заголовке Окна используя Статич. Переменные j и k

		title = "There're " + arrg.size() + " elements found.";

		if (arrg.size() == 0) {

			text = "\n\tNothing is found, dude!";
		}

		JTextArea textArea = new JTextArea(text);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец метода № 8

	/* 9. Метод по выводу в Окно списка всех Папок и адресов Фоток в них */

	public static void windowShow(GameFolderClass[] arrg, String p) {

		String text = "";
		String title;

		for (int i = 0; i < arrg.length; i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arrg[i].getName() + "\n";

			for (int k = 0; k < arrg[i].getPhotoNames().length; k++) {

				text = text + arrg[i].getPhotoNames()[k] + "\n";
			}

			text = text + "\n";
		}

		// формируем надпись в заголовке Окна используя Статич. Переменные j и k

		title = "There're " + arrg.length + " elements. " + p;

		JTextArea textArea = new JTextArea(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(500, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 9

	/* 10. Метод по выводу в Окно только Имен папок  */

	public static void windowShoww(GameFolderClass[] arrg, String p) {

		String text = "";
		String title;

		for (int i = 0; i < arrg.length; i++) {

			String ch = String.format("%03d", i + 1);
			text = text + ch + ". " + arrg[i].getName() + "\n";
		}

		// формируем надпись в заголовке Окна используя Статич. Переменные j и k

		title = "There're " + arrg.length + " elements. " + p;

		JTextArea textArea = new JTextArea(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(500, 500));

		JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 10
	
	/* 11. Метод по выводу инфрмации по использованию Приложения */

	public static void informWindow() {

		String text = "       В окне моего поисковика можно вводить названия игр, либо просто символы\n"
				+ " содержащиеся в названии игры (если введена одна буква, программа выдаст все\n"
				+ " игры начинающиеся на эту букву) плюс при введении конкретного имени Издателя\n"
				+ " игры либо Маппера либо Года выпуска, также будет выведен список игр относящихся\n"
				+ " к данному Издателю, Мапперу или Году.\n"
				+ "       Также существует ряд ключевых слов с определенными функциями:\n\n"
				+ " creators                  - вывод списка всех Издателей\n" + " издатели\n\n"
				+ " mappers                 - вывод списка всех Мапперов\n" + " мапперы\n\n"
				+ " years                      - вывод списка всех Годов выпуска\n" + " года или годы\n\n"
				+ " InetOff                    - включаем работу с Компьютера\n\n"
				+ " InetOn                    - включаем работу с Интернета\n\n"
				+ " refreshFile              - обновляем Текстовый Файл\n\n"
				+ " refreshPhoto          - обновляем Папку с Фотками\n\n"
				+ " refreshManag         - обновляем данные по файлам\n\n"
				+ " managInfo              - смотрим состав Менеджера файлов\n\n"
				+ " listInfo                    - смотрим состав Списка\n\n"
				+ " Info                        - смотрим Провила Пользования Прогой\n\n"
				+ " end или конец       - выход из Программы\n";

		JTextArea textArea = new JTextArea(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(600, 500));

		JOptionPane.showMessageDialog(null, scrollPane, "Pay attention! Important Information!",
				JOptionPane.PLAIN_MESSAGE);

	} // конец Метода № 11

	/* 12 Экспериментальный Метод по получению Массива Строк Определенного Поля из Массива Объектов
	 * GameClass, название нужного Поля передается вторым Аргументом.
	 * Использую его в Конструкторе №5 класса GameListClass. */

	public static List<String> getFieldListNew(List<GameClass> objects, String fieldName) {

		List<String> list = new ArrayList<String>();

		Set<String> set = new TreeSet<>();

		for (int i = 0; i < objects.size(); i++) {

			try {

				Field field = GameClass.class.getDeclaredField(fieldName);
				field.setAccessible(true); // Allow access to private fields

				list.add((String) field.get(objects.get(i)));

			} catch (NoSuchFieldException e) {

				System.out.println("Field not found: " + fieldName);

				return list; // Return empty array if field is not found

			} catch (IllegalAccessException e) {

				e.printStackTrace();
				return list; // Return empty array if access fails
			}
		}

		set.addAll(list);

		list.clear();

		list.addAll(set);

		set.clear();

		return list;

	} // конец Метода № 12
	
	/* 13. Метод по обработке Кода "*_ _ _" по которому в Проекте происходит выбор
	 * чего-либо из списка. Принимает Строку и если она начинается с "*" и
	 * возвращает true (если это число) если оно соответствующей длины и не 0.
	 * Использую его в Методе InputAnalyse, в одном из условий */

	public static boolean isStarCodeOk(String k) {

		boolean w = false; // то, что возвращаем
		int e = 4; // максимальный разряд проверяемого числа включая '*' ( *999 )

		if (k.equals("")) { // вот без этог условия у меня была ошибка!

			// System.out.println("Here this shit!");

			return w;
		}

		char[] chr = k.toCharArray();

		if ((k.charAt(0) == '*') && (k.length() > 1 && (k.length() <= e)) && chr[1] != '0') {

			int r = 0;

			for (int i = 1; i < k.length(); i++) {

				if (Character.isDigit(chr[i])) {

					r++;
				}
			}

			if (r == k.length() - 1) {

				w = true;
			}
		}

		return w;

	} // конец Метода № 13

	/* 14. Простой Метод Да/Нет, возвращает 0 - если Да, 1 - если Нет, и -1 - если Крестик.
	 * Аргументом передается строка, где написано, что выбираем.
	 * Метод Использую в:
	 * - Класс AddressManager, 13 метод managWebRefresh
	 * - Класс GitHubSynchronize, 5 метод public String downloadDiffArray (AddressManager manag) */

	public static int yesNoWindow(String b) {

		// Show a dialog with Yes and No options

		int response = JOptionPane.showConfirmDialog(null, b, "Выберите вариант",
				JOptionPane.YES_NO_OPTION);

		return response;
	}

} // конец 1-го Класса ServiceMethods
