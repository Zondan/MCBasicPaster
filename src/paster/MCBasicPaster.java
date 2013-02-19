package paster;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.table.DefaultTableModel;

public class MCBasicPaster extends JFrame {

	private JScrollPane scrollPane;
	private JTable table;
	private JButton butgetCb;
	private JButton butSend;
	private JButton butLoadFile;
	private DefaultTableModel table_content;

	private String[] title = { "line", "code" };
	private JProgressBar progressbar;
	private KeySend keysend;
	private Thread thread;
	private Timer timer;
	private TimerTask task;
	private MyClipboard cb;

	public MCBasicPaster() {

		initComponents();

		table_content = new DefaultTableModel();

		table_content.setDataVector(new Object[][] { { "" } }, title);

		table.setModel(table_content);

		cb = new MyClipboard();
		keysend = new KeySend();
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				progressbar.setValue(keysend.getProgress());
			}
		};

		timer.schedule(task, 10, 10);

	}

	private void initComponents() {
		this.setTitle("Minecraft Basic Paster");
		scrollPane = new JScrollPane();
		table = new JTable();
		butgetCb = new JButton("get Cb");
		butLoadFile = new JButton("load File");
		butSend = new JButton("Send");

		progressbar = new JProgressBar();
		progressbar.setMinimum(0);
		progressbar.setMaximum(100);

		scrollPane.setViewportView(table);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 627,
						Short.MAX_VALUE)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(butgetCb)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(butLoadFile)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(butSend)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(progressbar).addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(scrollPane,
										GroupLayout.DEFAULT_SIZE, 290,
										Short.MAX_VALUE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(progressbar)
												.addComponent(butgetCb)
												.addComponent(butLoadFile)
												.addComponent(butSend))

								.addGap(10, 10, 10)));

		pack();

		butgetCb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				butActionPerformed(0, e);
			}
		});

		butLoadFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				butActionPerformed(1, e);
			}

		});

		butSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				butActionPerformed(2, e);
			}

		});

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				timer.cancel();
				keysend.running = false;
				thread = null;
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});

	}

	private void butActionPerformed(int key, ActionEvent e) {
		switch (key) {
		case 0: //getCb
			setData(cb.getCb(), null);
			break;
		case 1: //LoadFile

			JFileChooser filechooser = new JFileChooser(
					System.getProperty("user.home"));

			filechooser.showOpenDialog(this);
			File file = filechooser.getSelectedFile();
			if (file != null) {
				setData(null, file);
			}
			break;
		case 2: //Send
			if (thread != null) {
				keysend.running = false;
				thread = null;
			}
			keysend.sendData(getData(), 50, this);

			thread = new Thread(keysend);
			thread.start();

			break;
		default:
			break;
		}

	}

	private void setData(String cb, File f) {
		ArrayList<String> line = new ArrayList<String>();
		ArrayList<String> code = new ArrayList<String>();

		Scanner sc = null;
		if (cb != null)
			sc = new Scanner(cb);
		else
			try {
				sc = new Scanner(f);
			} catch (FileNotFoundException e) {
				table_content.setDataVector(new Object[][] { { "" } }, title);
			}
		while (sc.hasNextLine()) {
			if (sc.hasNextInt())
				line.add(String.valueOf(sc.nextInt()));
			else
				line.add("");
			code.add(sc.nextLine());

		}
		Object ret[][] = new Object[line.size()][2];
		for (int i = 0; i < line.size(); i++) {
			ret[i][0] = line.get(i);
			ret[i][1] = code.get(i);
		}

		table_content.setDataVector(ret, title);

	}

	@SuppressWarnings("rawtypes")
	private String getData() {
		String ret = "";

		for (Object e : table_content.getDataVector()) {
			Vector e1 = (Vector) e;
			ret += (String) e1.get(0) + (String) e1.get(1) + "\n";
		}
		return ret;
	}

	public static void main(String[] args) {
		new MCBasicPaster().setVisible(true);
	}

}
