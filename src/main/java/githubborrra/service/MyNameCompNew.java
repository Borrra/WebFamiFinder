package githubborrra.service;

import java.util.Comparator;

import githubborrra.game.GameClass;

/* Компаратор для сортировки объектов (игр) по Именам */

public class MyNameCompNew implements Comparator <GameClass> {

	public int compare(GameClass a, GameClass b) {

		GameClass aStr, bStr;
		aStr = a;
		bStr = b;
		return aStr.getName().compareTo(bStr.getName());
	}
}
