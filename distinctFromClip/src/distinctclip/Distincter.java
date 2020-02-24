package distinctclip;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

public class Distincter {


	static File outFile;
	
	public static void main(String[] args) {
		outFile = new File("G:\\DOCUMENT\\DontDelete\\out.txt");
		try {
			String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor); 
			String [] spl = data.split("\n");
			ArrayList<String> lst = new ArrayList<>();
			for(String s : spl) {
				if(s.trim().length() > 0)
					lst.add(s.trim());
			}
			Collections.sort(lst, new Comparator<String>() {
				@Override
				public int compare(String s1, String s2) {
					return s1.compareToIgnoreCase(s2);
				}
			});
			
			Set<String> set = new HashSet<>();
			for(String a : lst)
				set.add(a);
//			writeToFile(lst, outFile);
//			openInNotepad(outFile);
			StringBuilder sb = new StringBuilder();
			for(String x : set)
				sb.append(x).append("\n");
			copyToClipboard(sb.toString());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
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
	
	private static File writeToFile(ArrayList<String> lines, File f) {

        FileWriter fr = null;
        BufferedWriter br = null;

        try{
            fr = new FileWriter(f);
            br = new BufferedWriter(fr);
            for(String l : lines) {
                br.write(l);
                br.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally{
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return f;
    }
	
    private static void openInNotepad(File file) {

    	if (Desktop.isDesktopSupported()) {
    		try {
    			Desktop.getDesktop().edit(file);
    		}catch(IOException ioe) {
    			ioe.printStackTrace();
    		}
    	} else {
    	    System.out.println("****Desktop not supported.****");
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

}
