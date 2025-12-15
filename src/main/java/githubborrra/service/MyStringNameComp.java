package githubborrra.service;

import java.util.Comparator;

/* вот этот Компаратор заработал, он сортирует имена фотографий в Папках на
 * компе и при формировании папки General (я вставляю ее в Массив Git Hub
 * вручную). Для него нужно чтобы строки начинались с цифры (1.jpg, 15.jpg и
 * т.д.) */

public class MyStringNameComp implements Comparator<String> {

	@Override

	public int compare(String s1, String s2) {

		// Extract the leading number from both strings

		Integer num1 = extractLeadingNumber(s1);
		Integer num2 = extractLeadingNumber(s2);

		// Compare based on extracted numbers

		if (num1 != null && num2 != null) {

			return num1.compareTo(num2);

		} else if (num1 != null) {

			return -1; // s1 comes before s2 if s1 starts with a number

		} else if (num2 != null) {

			return 1; // s2 comes after s1 if s2 starts with a number

		} else {

			return s1.compareTo(s2); // If neither starts with a number, compare lexicographically
		}
	}

	private Integer extractLeadingNumber(String str) {

		Integer ku = 100;

		String adr = str.substring( str.indexOf('/')+1, str.length() ); // должно оставить только "11.jpg" от адреса
		
		String norm = adr.substring(0, adr.indexOf('.')); // берем только номер фотки (если это номер)

		/* если название фотки это число от 1 до 99 */

		if (norm.length() < 3 && isInteger(norm)) {

			ku = Integer.parseInt(norm);
		}

		return ku;

	}

	/* добавил этот метод для определения число ли это или нет использую выше, для
	 * проверки названия фотки */

	boolean isInteger(String st) {

		try {

			Integer.parseInt(st);

			return true;

		} catch (NumberFormatException e) {

			return false;
		}
	}

} // конец Класса MyStringNameComp
