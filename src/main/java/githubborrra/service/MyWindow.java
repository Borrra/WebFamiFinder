
package githubborrra.service;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/* этот Класс MyWindow формирует JPanel с возможностью нажатия клавишь, а Метод
 * в конце применяет эту JPanel в JOptionPane.showMessageDialog(null, myPanel,
 * a, JOptionPane.PLAIN_MESSAGE), и этот же метод (procesWindow) применяется
 * в окне длительности процесса Класс AddressManager Метод compFilesFinder() */

public class MyWindow extends JPanel implements ActionListener {

	/* т.е. мы как бы делаем свою Кастомную JPanel, которя применяет интерфейс
	 * ActionListener, для возможности использования кнопок */

/////////* устанавливаем поля нашего Класса SnakeGame *//////////////

	private final int CELL = 20;
	private final int WIDTH = 310; // ширина окна
	private final int HEIGHT = 30; // высота окна

	private Timer timer;
	private long startTime;

	private long elapsedTimer = 0;
	
	/* заводим Связанный Список для координат каждого Кубика Змеи */

	private LinkedList<Point> stuff = new LinkedList<Point>();

	/* Это такой большой Конструктор */
	
	public MyWindow(String k) {

		stuff.add(new Point(0, 7)); // начальное положение Змеи
		stuff.add(new Point(WIDTH - 180, 7));
		stuff.add(new Point(WIDTH - 180 + CELL, 7));

		/* задаем Размеры Окна, причем высоту задаем с учетом размера нижней,
		 * дополнительной зоны отображения Очков SCORE_HIGHT */

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.BLACK);
		setFocusable(true);

		/* регистрируем Приемник */

		addKeyListener(new KeyAdapter() {

			/* Класс KeyEvent */

			@Override
			public void keyPressed(KeyEvent e) {

				/* метод getKeyCode() возвращает код нажатой кнопки (просто число),
				 * соответственно по этому коду уже срабатывают Кейсы - их может быть сколько
				 * угодно */

				switch (e.getKeyCode()) {

				/* если менять координаты точки в этом блоке, то она будет двигаться только по
				 * мере нажатия клавиши, поэтому при нажатии стрелок меняется только значение
				 * token, а уже в Методе Snake будет действия на этот token */

				case KeyEvent.VK_ESCAPE:

					System.exit(0);
					break;

				} // конец Switch

			} // конец keyPressed

		}); // конец регистрации Приемника

		timer = new Timer(64, this); // скорость движения кубиков в окне
		timer.start();
		startTime = System.currentTimeMillis();

	} // конец Конструктора

	//////* переориентированные Методы класа JPanel */////

	@Override
	protected void paintComponent(Graphics g) { // то, что будет отображаться в Окне
		super.paintComponent(g);

		g.setColor(Color.RED);

		for (int i = 0; i < stuff.size(); i++) {

			g.fillRect(stuff.get(i).x, stuff.get(i).y, CELL, CELL);
		}

		long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

		g.drawString("Seconds: " + elapsedTime, WIDTH - 180, HEIGHT - 10);

		elapsedTimer = elapsedTime;
	}

	@Override
	public void actionPerformed(ActionEvent e) { // метод который и делает Экшен

		moveSnake();

		repaint();
	}

	/* 1. Метод формирующий двигающийся объект */
	
	public void moveSnake() { // метод будет как бы циклиться в actionPerformed

		stuff.addFirst(new Point(stuff.getFirst().x + CELL, stuff.getFirst().y));

		stuff.removeLast(); // убираем Хвост

		if (stuff.getFirst().x >= WIDTH) {
			stuff.getFirst().x = 0;
		}
	}

	/* 2. Метод используем в классе AddressManager в методе compFilesFinder ()
	 * для отображения длительности процесса */

	public static void procesWindow(String a) {

		/* активируем наше Кастомное Окно, оно является JPanel по сути */

		MyWindow myPanel = new MyWindow("stuff");

		JOptionPane.showMessageDialog(null, myPanel, a, JOptionPane.PLAIN_MESSAGE);

	}

} // конец 3-го Класса MyWindow
