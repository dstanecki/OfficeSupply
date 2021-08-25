

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFileChooser;

public class FileGen {
	public static void main(String args[]) throws FileNotFoundException {
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(null);
		File f = fc.getSelectedFile();

		ArrayList<String> list = new ArrayList<String>();

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(f.getAbsolutePath()));

			String line;
			SimpleDateFormat fmt = new SimpleDateFormat("M/dd/yyyy");
			while ((line = reader.readLine()) != null)
				list.add(line);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		fc.showSaveDialog(null);
		f = fc.getSelectedFile();
		PrintWriter w = new PrintWriter(f);
		String l;
		for ( int i = 0; i < 4000; i ++ ) {
			if ( (l = list.get(new Random().nextInt(list.size()))).matches(".*,.*"))
				continue;
			w.println("O," +
						l + ", " + "QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray()[new Random().nextInt(26)] + new Random().nextInt(10) + "," +
					    (new Random().nextInt(12) + 1) + "/" + (new Random().nextInt(28) + 1 ) + "/" + (new Random().nextInt(19) + 2000) + "," + (new Random().nextInt(30) + 1)
					);
		}
		w.flush();
		w.close();

	}
}