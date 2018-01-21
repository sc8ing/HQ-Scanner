import java.io.*;

public class OCRScanner {
	private static final String pathToTesseractBinary = "/usr/local/Cellar/tesseract/3.05.01/bin/tesseract";

	public String scan(String imgPath) {
		String contents = "";
		String cmd = pathToTesseractBinary + " " + imgPath + " /private/tmp/tes";
		try { 
			CmdExecutor.execCmd(cmd);
			try(BufferedReader br = new BufferedReader(new FileReader("/private/tmp/tes.txt"))) {
			    StringBuilder sb = new StringBuilder();
			    String line = br.readLine();

			    while (line != null) {
			        sb.append(line);
			        sb.append(System.lineSeparator());
			        line = br.readLine();
			    }
			    contents = sb.toString();
			}
		}
		catch (IOException e) { e.printStackTrace(); }
		return contents;
	}
}
