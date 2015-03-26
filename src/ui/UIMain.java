package ui;

import java.sql.SQLException;

import javax.swing.JFrame;

import database.DBWrapper;


public class UIMain {
	public static void main(String[] args){
		DBWrapper db = new DBWrapper();
		Frame f = new Frame(db);
	}
}