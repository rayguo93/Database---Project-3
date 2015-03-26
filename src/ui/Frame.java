package ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Frame extends JFrame{
	public Frame(){
		JPanel paneltop = new JPanel();
		JPanel panelbot = new JPanel();
		JPanel panelcen = new JPanel();
		JButton query1Button = new JButton("Query 1");
		JButton query2Button = new JButton("Query 2");
		JButton query3Button = new JButton("Query 3");
		JButton query4Button = new JButton("Query 4");
		JButton query5Button = new JButton("Query 5");
		JTextArea params = new JTextArea("Parameters:");
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
		DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
		decimalFormat.setGroupingUsed(false);
		JFormattedTextField paramEdit = new JFormattedTextField(decimalFormat);
		paramEdit.setColumns(15);
		JTextArea answer = new JTextArea("Answer");
		query1Button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Query 1");
				int param = new Integer(paramEdit.getText());
				System.out.println(param);
			}
		});
		query2Button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Query 2");
			}
		});
		query3Button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Query 3");
			}
		});
		query4Button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Query 4");
			}
		});
		query5Button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Query 5");
			}
		});
		this.setTitle("Classifieds Database");
		this.setSize(500, 120);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		paneltop.setBackground(new Color(255,255,255));
		panelcen.setBackground(new Color(255,255,255));
		panelbot.setBackground(new Color(255,255,255));
		paneltop.add(query1Button);
		paneltop.add(query2Button);
		paneltop.add(query3Button);
		paneltop.add(query4Button);
		paneltop.add(query5Button);
		panelcen.add(params);
		panelcen.add(paramEdit);
		panelbot.add(answer);
		this.getContentPane().add(paneltop, BorderLayout.NORTH);
		this.getContentPane().add(panelcen, BorderLayout.CENTER);
		this.getContentPane().add(panelbot, BorderLayout.SOUTH);
		this.setVisible(true);
		this.setResizable(false);
	}
}
