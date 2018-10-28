package masterslave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MasterSlave {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
	
		
	////////////// Lectur de la liste des machines ///////
    File file = new File("/tmp/mdhaoui/listmachines.txt"); 
        
        BufferedReader br = new BufferedReader(new FileReader(file)); 
        String txt="";
        String st; 
        int k=0 ;
        while ((st = br.readLine()) != null) {
        	if ( k==0){
        		txt=st ;
        	}
        	else {
        		txt=txt+"/n"+st ;
        	}
        	k=k+1;
        } 
        System.out.println(txt);    
        String[] splitStrl = txt.split("/n");
   
 //////// on pourcoure les machines //////////////    
        
    k=0 ;
    Map <String, String> listmachinetask= new HashMap<String, String>();
   
    for (String linetxe:splitStrl){
        	
    	
    	System.out.println(" ###### lancement du job sur la machine" +linetxe+"#####"); 
    	ProcessBuilder pbtest =new ProcessBuilder("ssh",linetxe ,"hostname") ;
   	    Process ptest=pbtest.start();
         BufferedReader stdErrortest = new BufferedReader(new InputStreamReader(ptest.getErrorStream()));     
         
         
         // si la machine répond , on lance le slave
         
         if (stdErrortest .readLine()==null){
            System.out.println(stdErrortest .readLine()==null); // si c'est true ; la machine réponds
	 
         /// creation de repository
         ProcessBuilder pbmkdir =new ProcessBuilder("ssh","mdhaoui@"+linetxe,"mkdir","-p","/tmp/mdhaoui/splits") ;
         Process pmkdir=pbmkdir.start();
         pmkdir.waitFor();
         //scp -p SLAVE.jar mdhaoui@c133-23:/tmp/mdhaoui
         
         
         
         //copier tous les fichiers dans le dossier split
         for (int i=0 ; i< 3 ;i++){
             ProcessBuilder pbcopy =new ProcessBuilder("scp","-p","/tmp/mdhaoui/splits/S"+String.valueOf(i)+".txt", "mdhaoui@"+linetxe+":/tmp/mdhaoui/splits") ;
             Process pcopy=pbcopy.start();
             pcopy.waitFor();
         }
         

         
         //BufferedReader stdErrorcreate = new BufferedReader(new InputStreamReader(pcopy.getErrorStream()));     
         
   
         // si la machine répond , on lance le slave
         // String retour2 ;
         // while (( retour2 = stdErrorcreate .readLine()) != null) { //While there's something in the buffer
             //read&print - replace with a buffered read (into an array) if the output doesn't contain CR/LF
          	//	 System.out.println("# error #"); 
          		// System.out.println(retour2); 
           //}
            
         Thread.sleep(2000);
         
         /// creation du dossier map 
         ProcessBuilder pbmkdirmap =new ProcessBuilder("ssh","mdhaoui@"+linetxe,"mkdir","-p","/tmp/mdhaoui/maps") ;
         Process pmkdirmap=pbmkdirmap.start();
         pmkdirmap.waitFor();
         
         
         System.out.println(" fin copiage"); 
         
         /// lancer le slave avec un fichier 
         ProcessBuilder pb =new ProcessBuilder("ssh","mdhaoui@"+linetxe,"java","-jar","/tmp/mdhaoui/SLAVE.jar","map","S"+String.valueOf(k)+".txt") ;
         Process p=pb.start();
	 
         
         Thread.sleep(2000);
         k=k+1;
         boolean b= p.waitFor(13, TimeUnit.SECONDS);
         if (b==false){
        	 System.out.println(p.isAlive()); 
        	 p.destroyForcibly();
		 
         }else{
		 
             /// on met à jour le dict des machines_task
        	 listmachinetask.put("UM"+String.valueOf(k)+".txt",linetxe) ;

	 //p.destroy();
	// p.destroyForcibly();
	// p.waitFor();
    // System.out.println(p.isAlive()); 


	
	
             System.out.println("  ######  Standard output #######"); 
             
             p.waitFor();
     
             
             BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
             Thread.sleep(2000); //Sleep for 2 seconds

             
	
	
	
        	 //BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        	 // BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        	 // Thread.sleep(2000); //Sleep for 2 seconds
        	 String retour = null;
        	 while ( (retour = stdInput.readLine()) != null) { //While there's something in the buffer
        		 System.out.println(retour); 
        	 }
      
    
      
      
      
	  
        	 BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        	 //Thread.sleep(2000); //Sleep for 2 seconds
        	 retour = null;
        	 while ((retour = stdError .readLine()) != null) { //While there's something in the buffer
           //read&print - replace with a buffered read (into an array) if the output doesn't contain CR/LF
        		 System.out.println("# error #"); 

        		 System.out.println(retour); 
        	 }
         }
	 
    }
	}
    
    // print listmachines avec les fichiers de map 
    listmachinetask.forEach((kk, v) -> System.out.println(String.format("%s : %s", kk, v)));

    
      
    }

	

}
