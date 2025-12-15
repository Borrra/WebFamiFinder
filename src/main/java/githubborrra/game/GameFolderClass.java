
package githubborrra.game;

import githubborrra.inet.AddressManager;
import githubborrra.service.ConsoleWindow;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public class GameFolderClass {
	
	/* у объекта будет 2 поля: имя папки и массив адресов Фотографий */

	private String   folderName;
	private String[] photoName;

	/* Единственный Конструктор Класса */

	public GameFolderClass (String a, String[] b) {

		this.folderName = a;  // Dizzy
		this.photoName = b;   // 1.jpg, 2.jpg, 3.jpg
	}
	
	/////////// Переопределим equals and hashCode Класса Object //////////////

	/* это для того, чтобы элементы моего класса, имели способность сравниватья друг
	 * с другом в Коллекциях, при применении метода (например) removeAll() */

	@Override
	public boolean equals(Object obj) {

		// Check if the same reference

		if (this == obj)
			return true;

		// Check for null and type

		if (obj == null || getClass() != obj.getClass())
			return false;

		GameFolderClass myClass = (GameFolderClass) obj; // Cast to MyClass

		// Compare the name field

		if (!Objects.equals(folderName, myClass.folderName))
			return false;

		// Compare the values array
		return Arrays.equals(photoName, myClass.photoName);
	}

	@Override
	public int hashCode() {

		// Use Objects.hash for the name and Arrays.hashCode for the values array

		return Objects.hash(folderName) + Arrays.hashCode(photoName);
	}

/////////////////////////////////////////////////////////////////////////////////////////

	/* 1. Метод получения Имени Папки 
	 * Использую в Компараторе и в методе вывода списка на Экран (ServiceMethods) */

	public String getName() {

		String a = this.folderName;

		return a;
	}

	/* 2. Метод получения Массива Адресов Фоток игр данной Папки
	 * Использую в Компараторе и в методе вывода списка на Экран (ServiceMethods) */

	public String[] getPhotoNames() {

		String[] a = this.photoName;

		return a;
	}
	
	/* 1. Метод, который создаёт в GamePhoto папке проекта папку
	 * с названием Объекта (папкой), если ее нет (затестил - работает) */
	
	private String create(AddressManager manag) {

		File folder;

		/* если адрес папки с фотками есть, тогда создадим объект файла по адресу Папки */
		
		if ( !manag.photoFolderAddress.equals("") ) {
			
			folder = new File(manag.photoFolderAddress, this.folderName );
			
			/* если папка не существует - создаем ее, иначе - она уже существует */

			if (!folder.exists()) {

				boolean folderCreated = folder.mkdir();

				if (folderCreated) { // если папка создалась - возвращаем Путь

					//System.out.println("\nFolder created: " + folder.getAbsolutePath() + "\n");
					ConsoleWindow.toConsole("\nFolder created: " + folder.getAbsolutePath() + "\n");
					
					return folder.getAbsolutePath();

				} else { // если не создалась - возвращаем null

					//System.out.println("Failed to create folder.");
					ConsoleWindow.toConsole("Failed to create folder.");
					
					return null;
				}

			} else { // если папка уже существует

				//System.out.println("Folder already exists: " + folder.getAbsolutePath());
				ConsoleWindow.toConsole("\nFolder already exists: " + folder.getAbsolutePath() + "\n");
				
				return folder.getAbsolutePath();
			}
			
			//return null;
			
		} // конец if ( !manag.photoFolderAddress.equals("") ), т.е. если есть где создавать
		
		return null;

	} // конец Метода № 1 create(AddressManager manag)
	
	/* 2. Метод по удалению всех файлов из Папки */

	private void delete (AddressManager manag) {

		// Create a File object for the directory

		File directory = new File(manag.photoFolderAddress, this.folderName );

		// Check if the directory exists and is indeed a directory

		if ( directory.exists() && directory.isDirectory() ) {

			// List all files in the directory

			File[] files = directory.listFiles();

			if (files != null) {

				// Iterate through each file and delete it

				for (File file : files) {

					if (file.isFile()) {

						boolean deleted = file.delete();

						if (deleted) { // папка удалена

							//System.out.println("Deleted file: " + file.getAbsolutePath());

						} else { // если папка не удалилась

							System.out.println("Failed to delete file: " + file.getName());
						}
					}
				}

			} else { // это если папка пуста

				System.out.println("The directory is empty or an I/O error occurred.");
			}

		} else { // это если папка не существует

			 System.out.println("The specified path is not a directory or does not exist.");
		}

	} // конец 2. метода delete (AddressManager manag)
	
	/* 3. Метод который скачивает один файл с Git Hub по его адресу
	 * Использую в предыдущем Методе */

	private void downloadOneFile (AddressManager manag, String phName, String foldPath) throws IOException {

		/* - из Менеджера (manag) берем web адрес папки на GitHub
		 * - phName - это название фотки (1.jpg) 
		 * - foldPath - адрес папки куда скачиваем файл, не проверяется на наличие,
		 * т.к. она будет проверяться в методе, где наш метод будет испльзован
		 * (перед передачей в него) */
		
		String webAdr = manag.webPhotoAddress + this.folderName + "/" + phName;
		
		String fullURL = webAdr.replace(" ", "%20");
		
		URL url = new URL(fullURL);

		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

		httpConn.setRequestMethod("GET");

		// Check for HTTP response code

		int responseCode = httpConn.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {

			// Input stream to read the file

			InputStream inputStream = new BufferedInputStream(httpConn.getInputStream());

			/* передаем в аргумент полный адрес назначения (С://Programming/.../Dizzy/1.jpg) */
			
			FileOutputStream outputStream = new FileOutputStream(foldPath + "/" + phName);

			byte[] buffer = new byte[4096];
			
			int bytesRead;

			// Read from input stream and write to output stream

			while ((bytesRead = inputStream.read(buffer)) != -1) {

				outputStream.write(buffer, 0, bytesRead);
			}

			//System.out.println ("Downloaded file: " + fullURL);
			ConsoleWindow.toConsole("Downloaded file: " + fullURL);
			
			outputStream.close();
			inputStream.close();

			httpConn.disconnect();
		}

	} // конец Метода № 3 downloadOneFile
	
	/* 4. Метод который загружает все фотки Игры в соответствующую папку */
	
	public void download (AddressManager manag) { // потом сделаю private
		
		/* создадим папку Игры, куда будем скачивать фотки, и возьмем ее адрес */
		
		String adr = create (manag);
			
		if (adr != null) { // если папка создалась идем дальше
			
			this.delete(manag); // удаляем все файлы из папки	
			
			/* качаем одну за одной фотки с GitHub */
			
			for (int i=0; i<this.photoName.length; i++) {
			
				try {
	
					downloadOneFile(manag, this.photoName[i], adr);
				
				} catch (IOException e) {
	
					e.printStackTrace();
				}
			}
			
		} // если нужно здесь сдела else, если папка не создалась 
		
	} // конец 4. Метода download

} // конец Класса GameFolderClass

