
package githubborrra.service;

import githubborrra.game.GameClass;

import java.util.Comparator;

/* Компаратор для сортировки объектов (игр) по Годам */

public class MyYearCompNew implements Comparator<GameClass> {

	public int compare(GameClass a, GameClass b) {

		GameClass aStr, bStr;
		aStr = a;
		bStr = b;
		return aStr.getYear().compareTo(bStr.getYear());
	}
}



