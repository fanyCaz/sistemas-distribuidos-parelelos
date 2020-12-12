package sistema;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Pruebas {
	public static void main(String[] args) {
		try {
			String fileName = "stress.py";
			String path= System.getProperty("user.dir") + "\\src\\sistema\\" + fileName;
			String stressSecs = "30";
			Process process = Runtime.getRuntime().exec(new String[] {"python",path,"30"});
		} catch(Exception ex) {
			System.out.println(ex);
		}
		
	}
}
