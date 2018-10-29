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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    Map <String, String> listkeys= new HashMap<String, String>();
    Map<String, Collection<String>> mapkeysmachine = new HashMap<String, Collection<String>>();
    boolean phasemap=true ;
    
    for (String linetxe:splitStrl){
        	
    	System.out.println(" ###### lancement du job sur la machine" +linetxe+"#####"); 
    	ProcessBuilder pbtest =new ProcessBuilder("ssh",linetxe ,"hostname") ;
   	    Process ptest=pbtest.start();
         BufferedReader stdErrortest = new BufferedReader(new InputStreamReader(ptest.getErrorStream()));     
         
         
         // si la machine répond , on lance le slave
         
         if (stdErrortest .readLine()==null){
        	 
         System.out.println("La machine répond : "+String.valueOf(stdErrortest .readLine()==null )); // si c'est true ; la machine réponds
	 
         /// creation de repository
         ProcessBuilder pbmkdir =new ProcessBuilder("ssh","mdhaoui@"+linetxe,"mkdir","-p","/tmp/mdhaoui/splits") ;
         Process pmkdir=pbmkdir.start();
         pmkdir.waitFor();
         //scp -p SLAVE.jar mdhaoui@c133-23:/tmp/mdhaoui
         
         
         
         //copier tous les fichiers dans le dossier split
         for (int i=0 ; i< 3 ;i++){
             ProcessBuilder pbcopy =new ProcessBuilder("scp","-p","/tmp/mdhaoui/splits/S"+String.valueOf(i)+".txt", "mdhaoui@"+linetxe+":/tmp/mdhaoui/splits") ;
             Process pcopy=pbcopy.start();
             //pcopy.waitFor();
         }
       
         
         
         /// creation du dossier map 
         ProcessBuilder pbmkdirmap =new ProcessBuilder("ssh","mdhaoui@"+linetxe,"mkdir","-p","/tmp/mdhaoui/maps") ;
         Process pmkdirmap=pbmkdirmap.start();
         pmkdirmap.waitFor();
         
         
         System.out.println(" Fin du copiage des splits"); 
         
         /// lancer le slave avec un fichier 
         ProcessBuilder pb =new ProcessBuilder("ssh","mdhaoui@"+linetxe,"java","-jar","/tmp/mdhaoui/SLAVE.jar","map","S"+String.valueOf(k)+".txt") ;
         Process p=pb.start();
	 
         
         Thread.sleep(2000);
         k=k+1;
         boolean b= p.waitFor(13, TimeUnit.SECONDS);
         if (b==false){
        	 System.out.println(p.isAlive()); 
        	 p.destroyForcibly();
        	 
        	 phasemap=false ;  // le processus n'est pas fini
         }else{
		 
             /// on met à jour le dict des machines_task
        	 listmachinetask.put("UM"+String.valueOf(k),linetxe) ;


	
	         /////////////////////// affichage des keys /////////
             System.out.println("  ######  Standard output #######"); 
                 
             
             BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        	 String retour = null;
        	 while ( (retour = stdInput.readLine()) != null) { //While there's something in the buffer
        	    System.out.println("UM"+String.valueOf(k)+"  "+ retour); 

   			    Collection<String> values = mapkeysmachine.get(retour);
   			    if (values==null) {
   			      values = new ArrayList<String>();
   			      mapkeysmachine.put(retour, values);
   			    }
   			  values.add("UM"+String.valueOf(k));
   			  mapkeysmachine.put(retour,values);
        		//listkeys.put("UM"+String.valueOf(k), retour) ;
        	 }	
        	// listkeys.forEach((kk, v) -> System.out.println(String.format("%s : %s", kk, v)));



      
            ///////////////////////////// erreur /////////////
	  
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
    
    System.out.println("############### resultat final ###############");
    if (phasemap) {
    	 System.out.println("La partie map a été finie avec succès");
    }else {
   	 	 System.out.println("La partie map n'est pas finie avec succès , ci-dessous les résultats des machines qui fonctionnent :");
	
    }
    listmachinetask.forEach((kk, v) -> System.out.println(String.format("%s : %s", kk, v)));

	System.out.println(Arrays.asList(mapkeysmachine)); // method 1

      
    }

	

}
