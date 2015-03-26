package ui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.Key;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable {
	JFrame f;
	Thread t;

	public Panel(JFrame frame) {
		this.f = frame;
		this.setBackground(Color.WHITE);
		initialize();
	}

	public void initialize() {
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		while (true) {
			fps();
			repaint();
		}
	}

	public void fps() {
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) {
		super.paintComponent(g);
	}
}
