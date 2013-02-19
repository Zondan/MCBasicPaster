package paster;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class KeySend extends Thread implements Runnable {

	private Robot robot = null;

	private String data;
	private int wait, pos;

	public boolean running = false;

	private JFrame parent;

	public KeySend() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.exit(1);
		}
	}

	public void sendData(String data, int wait, JFrame parent) {
		this.data = data;
		this.wait = wait;
		this.parent = parent;
		running = true;
		pos = 0;
	}

	public void run() {
		sleep(3000);
		for (char c : data.toCharArray()) {
			pos++;
			if (!running) {
				return;
			}
			while (parent.hasFocus()) {
				if (!running)
					return;
			}
			keypress(Character.toUpperCase(c));
			sleep(wait);
		}
	}

	private void keypress(char c) {
		int keycode = c;
		boolean shift = false;
		if (c == '=' || c == '/')
			shift = true;
		else if (c == ':') {
			keycode = KeyEvent.VK_PERIOD;
			shift = true;
		} else if (c == ';') {
			keycode = KeyEvent.VK_SEMICOLON;
			shift = true;
		}

		else if (c == '+')
			keycode = KeyEvent.VK_PLUS;
		else if (c == '*')
			keycode = KeyEvent.VK_MULTIPLY;
		else if (c == '\'') {
			keycode = KeyEvent.VK_QUOTE;
			shift = true;
		} else if (c == '"') {
			keycode = KeyEvent.VK_QUOTEDBL;
			shift = true;
		} else if (c == '$') {
			keycode = KeyEvent.VK_DOLLAR;
			shift = true;
		} else if (c == '(') {
			keycode = KeyEvent.VK_OPEN_BRACKET;
			shift = true;
		} else if (c == ')') {
			keycode = KeyEvent.VK_CLOSE_BRACKET;
			shift = true;
		} else if (c == '<') {
			keycode = KeyEvent.VK_LESS;
		} else if (c == '>') {
			keycode = KeyEvent.VK_GREATER;
			shift = true;
		}

		if (shift)
			robot.keyPress(KeyEvent.VK_SHIFT);

		robot.keyPress(keycode);
		sleep(10);
		robot.keyRelease(keycode);
		if (shift)
			robot.keyRelease(KeyEvent.VK_SHIFT);
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getProgress() {
		if (data == null)
			return 0;
		return (int) (100.0 * pos / data.length());
	}

}
