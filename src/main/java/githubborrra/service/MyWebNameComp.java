package githubborrra.service;

import java.util.Arrays;
import java.util.Comparator;

import githubborrra.game.GameFolderClass;

//* создаем Компаратор для объектов типа GitHubSynchronize, но по-моему он не
//работает! */

public class MyWebNameComp implements Comparator<GameFolderClass> {

	@Override
	public int compare(GameFolderClass a, GameFolderClass b) {

		/* сначла сравниваем по Имени */

		int nameComparison = a.getName().compareTo(b.getName());

		if (nameComparison != 0) {

			return nameComparison;
		}

		/* если Имена равны, сравниваем по отсортированным адресам фоток (по массиву) */

		String[] sortedA = Arrays.copyOf(a.getPhotoNames(), b.getPhotoNames().length);
		String[] sortedB = Arrays.copyOf(b.getPhotoNames(), a.getPhotoNames().length);

		Arrays.sort(sortedA);
		Arrays.sort(sortedB);

		return Arrays.compare(sortedA, sortedB);
	}
}
