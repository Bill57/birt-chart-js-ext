package applets;

import java.applet.Applet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadURL extends Applet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6887700004172624409L;

	public int finished = 0;

	public String fileContent = "";

	public void readFile(String filePath) {
		finished = 0;
		System.out.println(filePath);
		File file = new File(filePath);
		if (file.exists()) {
			try {
				FileReader reader = new FileReader(file);
				StringBuffer str = new StringBuffer();
				char[] buffer = new char[1024];
				int readSize = 0;
				while ((readSize = reader.read(buffer)) != -1) {
					str.append(buffer, 0, readSize);
				}
				reader.close();
				fileContent = str.toString();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				finished = 1;
			}
		}
	}
}
