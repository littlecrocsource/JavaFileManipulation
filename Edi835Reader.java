import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.io.FileNotFoundException;


public class Edi835Reader{
	
	private String text;
	
	public String getText() {
		return text;
	}

	@FunctionalInterface
	public interface LoopWithIndex<T> {
	    void accept(T t, int i);
	}
	
	public static <T> void forEach(Collection<T> collection,
	                               LoopWithIndex<T> consumer) {
	   int index = 1;
	   for (T object : collection){
	      consumer.accept(object, index++);
	   }
	}
	
	public void readPdf(String pathToFile) throws Exception {
		
		File file = new File(pathToFile);
		
		try (PDDocument document = PDDocument.load(file) ){
			
			if (!document.isEncrypted()) {
						
				text = new PDFTextStripper().getText(document);
			
			}
			else {
				System.out.println("Unable to read file.");
				System.out.println(file.toString() + " is encrypted.");
			}
			
			
		} catch (FileNotFoundException e) {
			System.out.println("Can't find file: " + file.toString());		
			throw new Exception("File not found!");		

		} catch (IOException e) {
			System.out.println("Unable to read file: " + file.toString());
			throw new Exception("Read error!");
		}	
		
	}

	public static void main(String[] args){
		
		Edi835Reader ediReader = new Edi835Reader();
		
		try {
			ediReader.readPdf("Sample_835_File.pdf");
			
            List<String> lines = Arrays.asList(ediReader.getText().split("\\r?\\n"));
            
            forEach( lines, (line, i) -> System.out.println("Line "+ i + ": " + line));			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}


}
