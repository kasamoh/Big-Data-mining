package slave;
import java.io.*;

import static java.util.stream.Collectors.toMap;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.Collections; import java.util.Comparator; import java.util.HashMap; import java.util.LinkedHashMap; import java.util.Map; 

import static java.util.stream.Collectors.*; import static java.util.Map.Entry.*;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.text.Normalizer;
import java.util.regex.Pattern;


public class SLAVE {
	public static String deAccent(String str) {
	    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
	    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	    return pattern.matcher(nfdNormalizedString).replaceAll("");
	}
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub

		
		String task= args[0];  // map   ou // reduce
		String fileprocess = args[1]; 
		/// file number 
		
		
		int value = Integer.parseInt(fileprocess.replaceAll("[^0-9]", ""));

		
		Path path = Paths.get("/tmp/mdhaoui/splits/"+ fileprocess);
	        
	        
		 ///// la partie map /////	
	     Map<String, Integer> wordCount= Files.lines(path).flatMap(line -> Arrays.stream(line.trim().split(" ")))
	    	       // .flatMap(word1 -> Arrays.stream( deAccent(word1).toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", " ").split(" ")))
	    	        .map(word -> deAccent(word).toLowerCase().trim().replaceAll(" ","").replaceAll("[^a-zA-Z0-9\\s]", ""))
	    	        .filter(word -> word.length() > 0)
	    	        .map(word -> new SimpleEntry<>(word, 1))
	    	        .collect(toMap(e -> e.getKey(), e -> e.getValue(), (v1, v2) -> v1 + v2));
	    	        

       BufferedWriter out = new BufferedWriter(new FileWriter("/tmp/mdhaoui/maps/UM"+String.valueOf(value)+".txt"));
       try {
      	 
    	   for (Entry<String, Integer> entry : wordCount.entrySet())
    	   {
    		   out.write(entry.getKey() + "\t" + entry.getValue()+ "\n"); 
    		   System.out.println(entry.getKey()) ;
    	   }
       
       out.close() ;
       
       } catch (IOException e) {}
       
       
  
	         
        
	}

}

