package insertcode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class InsertLinkSN {
	
	static int SCALED_WIDTH = 5;
	static int SCALED_HEIGHT = 5;
	static int fieldColsWidth = 30;
	static int outputAreaRows = 4;
	static Color errorColor = new Color(179,116,0);
	static Color background = new Color(211,211,211);
	static JLabel loading = new JLabel();
	static JFrame f = new JFrame();
	static JButton ok = new JButton("OK");
	static JButton copy = new JButton("Copy");
	static JButton paste = new JButton("Paste");
	static JButton clear = new JButton("Clear");
	static JTextField urlField = new JTextField();
	static JTextField dtField = new JTextField();
	static JTextField msgField = new JTextField();
	static JTextArea outputArea;
	static JPanel p1, p2, p3, p4;
	static JPanel base;
	
	public InsertLinkSN() {
		f.setLayout(new BorderLayout());
		urlField.setColumns(fieldColsWidth);
		dtField.setColumns(fieldColsWidth);
		msgField.setColumns(fieldColsWidth);
		outputArea = new JTextArea(outputAreaRows, fieldColsWidth);
		outputArea.setLineWrap(true);
		msgField.setBackground(background);
		//ok.setBackground(new Color(173, 216, 230));
		
		ok.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		outputArea.setText("");
        		String url = urlField.getText();
        		
        		if(validURL(url)) {
        			String displayText = getDT();
            		String output = getOutput(url, displayText);
            		outputArea.setText(output);
        		}else {
        			urlField.setText("");
        			showMsg("Please enter a valid URL");
        			clear();
        		}
        	}
        });
		
		copy.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(outputArea.getText().length() > 1) {
        			copyToClipboard(outputArea.getText());
        			showMsg("Copied to Clipboad");
        		}else {
        			outputArea.setText("");
            		String url = urlField.getText();
            		if(validURL(url)) {
            			String displayText = getDT();
                		String output = getOutput(url, displayText);
                		copyToClipboard(output);
                		outputArea.setText(output);
                		showMsg("Copied to Clipboad");
            		}else {
            			urlField.setText("");
            			showMsg("Please enter a valid URL");
            			clear();
            		}
        		}
        	}
        });
		
		paste.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		outputArea.setText("");
        		String url = copyFromClipboard();
        		if(validURL(url)) {
        			urlField.setText(url);
        			String displayText = getDT();
            		String output = getOutput(url, displayText);
            		outputArea.setText(output);
            		showMsg("Pasted from Clipboad");
        		}else {
        			urlField.setText("");
        			showMsg("Please enter a valid URL");
        			clear();
        		}
        	}
        });
		
		clear.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		clear();
        	}
        });
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());

		JPanel p0 = new JPanel();
		p0.setLayout(new BorderLayout());
		
		base = new JPanel();
		base.setLayout(new BorderLayout());
		base.setBackground(background);

		p1 = new JPanel(new BorderLayout());
		p1.setBorder(BorderFactory.createBevelBorder(0));
		p1.setBackground(background);

		p1.add(new JLabel("Link URL: "), BorderLayout.LINE_START);
		p1.add(urlField, BorderLayout.LINE_END);
		
		
		
		p2 = new JPanel(new BorderLayout());
		p2.setBorder(BorderFactory.createBevelBorder(0));
		p2.setBackground(background);

		p2.add(new JLabel("Text to Display: "), BorderLayout.LINE_START);
		p2.add(dtField, BorderLayout.LINE_END);
		
		p0.add(p1, BorderLayout.PAGE_START);
		p0.add(p2, BorderLayout.PAGE_END);
		
		p.add(p0, BorderLayout.PAGE_START);
		//p.add(new JPanel().add(new JLabel("Link in work note:")), BorderLayout.PAGE_END);
		
		p3 = new JPanel(new BorderLayout());
		p3.setBorder(BorderFactory.createBevelBorder(0));
		p3.setBackground(background);
		
		p3.add(new JScrollPane(outputArea), BorderLayout.CENTER);
		
		JPanel px = new JPanel(new BorderLayout());
		px.setBorder(BorderFactory.createBevelBorder(0));
		px.setBackground(background);
		
		px.add(p2, BorderLayout.PAGE_START);
		px.add(p3, BorderLayout.PAGE_END);
		
		p4 = new JPanel(new BorderLayout());
		p4.setBorder(BorderFactory.createBevelBorder(0));
		p4.setBackground(background);
		
		JPanel b1 = new JPanel(new BorderLayout());
		b1.setBackground(background);
		b1.add(copy, BorderLayout.LINE_START);
		b1.add(paste, BorderLayout.LINE_END);
		JPanel b2 = new JPanel(new BorderLayout());
		b2.setBackground(background);
		b2.add(clear, BorderLayout.LINE_START);
		b2.add(ok, BorderLayout.LINE_END);
		
		p4.add(b1, BorderLayout.WEST);
		p4.add(b2, BorderLayout.EAST);
		
		msgField.setForeground(errorColor);
		base.add(msgField);

//		JPanel b = new JPanel();
//		b.setLayout(new BorderLayout());
//		
//		b.add(p4, BorderLayout.PAGE_START);
//		b.add(base, BorderLayout.PAGE_END);
		
		p4.add(base, BorderLayout.CENTER);
		
		f.getRootPane().setBorder(BorderFactory.createBevelBorder(0));
		f.setBackground(background);
		f.add(p, BorderLayout.PAGE_START);
		f.add(px, BorderLayout.CENTER);
		f.add(p4, BorderLayout.PAGE_END);
		f.getRootPane().setDefaultButton(ok);
		f.setTitle("Add Link to SN Worknote");
//			f.add(p1, BorderLayout.PAGE_END);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
	}
	
	public static void clear() {
		outputArea.setText("");
		dtField.setText("");
		urlField.setText("");
	}
	public static boolean validURL(String url) {
		try {
			URL u = new URL(url);
		}catch (MalformedURLException murle) {
			return false;
		}
		return true;
	}
	
	public static String getDT() {
		String ff = dtField.getText();
		if(ff == null || ff.contentEquals("") || ff.length() < 1) {
			
			ff = urlField.getText();
			//if still empty, the user hasn't entered anything
			if(ff == null || ff.contentEquals("") || ff.length() < 1) {
				errorClear();
				return "";
			}
			
			if(validURL(ff)) {
				showMsg("Pulling url for title");
				dtField.setText(ff);
			}
		}
		return ff;
	}
	
	public static void errorClear() {
		dtField.setText("");
		urlField.setText("");
		showMsg("Please enter something (or copy a url and click paste)");
	}
	
	public static String getOutput(String url, String displayText) {
		return "[code]<a class=\"web\" target=\"_blank\" href=\"" + url + "\">" + displayText + "</a>";
	}
	
	public static void showMsg(String text) {
		msgField.setText(text);
	}
	
	public static String copyFromClipboard() {
		try {
			String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor); 
			return data;
		}catch(Exception ex) {
			ex.printStackTrace();
			return "error";
		}
	}
    
    public static boolean ask(Object msg, String title) {
		int x = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_CANCEL_OPTION);
		if(x == JOptionPane.CLOSED_OPTION || x == JOptionPane.CANCEL_OPTION ) {
			System.exit(0);
		}else if(x == JOptionPane.NO_OPTION) {
			return false;
		}
		return true;
	}

    public static void copyToClipboard(String toCopy) {
		StringSelection ss = new StringSelection(toCopy);
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		cb.setContents(ss,null);
	}

	public static void main(String[] args) {
		new InsertLinkSN();
	}

}
