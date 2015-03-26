package ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import database.DBWrapper;
import database.Query1Result;
import database.Query2Result;
import database.Query5Result;


public class Frame extends JFrame{
	public Frame(DBWrapper db){
		JPanel paneltop = new JPanel();
		JPanel panelbot = new JPanel();
		JPanel panelcen = new JPanel();
		paneltop.setSize(400, 100);
		panelcen.setSize(400, 100);
		panelbot.setSize(400, 100);
		JButton query1Button = new JButton("Query 1");
		JButton query2Button = new JButton("Query 2");
		JButton query3Button = new JButton("Query 3");
		JButton query4Button = new JButton("Query 4");
		JButton query5Button = new JButton("Query 5");
		JTextArea params = new JTextArea("Parameters:");
		params.setEditable(false);
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
		DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
		decimalFormat.setGroupingUsed(false);
		JFormattedTextField paramEdit = new JFormattedTextField(decimalFormat);
		paramEdit.setColumns(15);
		JTextArea answer = new JTextArea();
		answer.setEditable(false);
		query1Button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String params = paramEdit.getText();
				int param = 0;
				if(params == null || params.isEmpty()){
					JOptionPane.showMessageDialog(Frame.this, "Error: Please enter a parameter value");
					return;
				} else{
					param = new Integer(params);
				}
				
				if(param <= 0){
					JOptionPane.showMessageDialog(Frame.this, "Error: Please enter a parameter value bigger than 0");
					return;
				}
				
				try {
					StringBuilder sb = new StringBuilder();
					sb.append("Query 1\nGet "+param+" most popular tags:\n\n");
					for(Query1Result res: db.query1(param)){
						sb.append(res.getName()+" : "+res.getCount()+"\n");
					}
					String result = sb.toString();
					answer.setText(result);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		query2Button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub			
				try {
					StringBuilder sb = new StringBuilder();
					sb.append("Query 2\nSelect Username and number of Ad Views of the user with \nthe most views:\n\n");
					Query2Result res = db.query2();
					sb.append(res.getUsername()+", "+res.getViewCount()+"\n");
					String result = sb.toString();
					answer.setText(result);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		query3Button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String params = paramEdit.getText();
				int param = 0;
				if(params == null || params.isEmpty()){
					JOptionPane.showMessageDialog(Frame.this, "Error: Please enter a parameter value");
					return;
				} else{
					param = new Integer(params);
				}
				
				if(param <= 0){
					JOptionPane.showMessageDialog(Frame.this, "Error: Please enter a parameter value bigger than 0");
					return;
				}
				
				try {
					StringBuilder sb = new StringBuilder();
					sb.append("Query 3\nSelect the "+param+" most recent messages:\n\n");
					for(String res: db.query3(param)){
						sb.append(res+"\n");
					}
					String result = sb.toString();
					answer.setText(result);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		query4Button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String params = paramEdit.getText();
				int param = 0;
				if(params == null || params.isEmpty()){
					JOptionPane.showMessageDialog(Frame.this, "Error: Please enter a parameter value");
					return;
				} else{
					param = new Integer(params);
				}
				
				if(param <= 0){
					JOptionPane.showMessageDialog(Frame.this, "Error: Please enter a parameter value bigger than 0");
					return;
				}
				
				try {
					StringBuilder sb = new StringBuilder();
					sb.append("Query 4\nSelect Usernames of users who posted at least 2 items \nwith price greater than "+param+":\n\n");
					for(String res: db.query4(param)){
						sb.append(res+"\n");
					}
					String result = sb.toString();
					answer.setText(result);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		query5Button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String params = paramEdit.getText();
				int param = 0;
				if(params == null || params.isEmpty()){
					JOptionPane.showMessageDialog(Frame.this, "Error: Please enter a parameter value");
					return;
				} else{
					param = new Integer(params);
				}
				
				if(param <= 0){
					JOptionPane.showMessageDialog(Frame.this, "Error: Please enter a parameter value bigger than 0");
					return;
				}
				
				try {
					StringBuilder sb = new StringBuilder();
					sb.append("Query 5\nSelect Usernames and Emails of users who put up an \nadvertisement and have sent more than "+param+" messages:\n\n");
					for(Query5Result res: db.query5(0, param)){
						sb.append(res.getUsername()+" : "+res.getEmail()+"\n");
					}
					String result = sb.toString();
					answer.setText(result);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		this.setTitle("Classifieds Database");
		this.setSize(500, 500);
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
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				db.close();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
