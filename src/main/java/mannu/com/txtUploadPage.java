package mannu.com;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class txtUploadPage {
	private Connection con;
	private String update;
	private PreparedStatement ps;
	private PreparedStatement ps1;
	private ResultSet rs;
	
	public static void main(String [] args) {
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				txtUploadPage txtupl=new txtUploadPage();
			}
		});
	}

	public txtUploadPage() {
		try {
			 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			 con=DriverManager.getConnection("jdbc:sqlserver://192.168.84.90;user=sa;password=Karvy@123;database=pandotnet");
			
			Calendar cal = Calendar.getInstance();
		   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		   cal.add(Calendar.DATE, -1);
		   String pvdat=dateFormat.format(cal.getTime());
		   File dpat=new File("C:\\Pantxtfile\\Jan_18\\"+pvdat+"\\");
		   if(!dpat.exists()) {
			   System.out.println("Foler Not Found");
		   } else {
			   
		        File[] txtFiles=dpat.listFiles();
		        for(File txtFile:txtFiles)
		        {
		                
		        if(txtFile.getName().contains(".txt"))
		        {
		        FileInputStream input = new FileInputStream(txtFile.getAbsolutePath());
		        BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		        
		        String line=reader.readLine();
		        while(line!=null)
		        {
		        
		        	String data[];
		        	int k=0;
		            data=line.split("");
		            for(int i=0;i<data.length;i++){
		                if(data[i].equals("^")){
		                    k++;
		                }

		            }
		            System.out.println("Count: "+k);

		            String[] args=line.split("\\^");
		            if(args.length==8)
		            {
		            	Date ud=new SimpleDateFormat("ddMMyyyy").parse(args[4]);
		            	SimpleDateFormat dff1=new SimpleDateFormat("yyyy-MM-dd");
		            	update=dff1.format(ud);
		            	
		            }else if(args.length!=8){
		            	if (k>80) {
		            		System.out.println(args[0]+","+args[67]+","+txtFile.getName());
			            	PreparedStatement ps=con.prepareStatement("uploadtxt '"+args[0]+"','"+args[67]+"','"+txtFile.getName()+"';");
			            	ps.execute();
			            	ps.close();
						} else {
							System.out.println(args[0]+","+args[56]+","+txtFile.getName());
			            	PreparedStatement ps=con.prepareStatement("uploadtxt '"+args[0]+"','"+args[56]+"','"+txtFile.getName()+"';");
				            ps.execute();
							ps.close();
						}
		            }
		            line=reader.readLine();      
		        }
		        input.close();
		        reader.close();
		        File nefil=new File(dpat+"\\importdone\\");
		        if (!nefil.exists()) {
					nefil.mkdir();
				}
		        txtFile.renameTo(new File(nefil+"//"+txtFile.getName()));
		        
		        
		        } else {
		        	System.out.println("Other Format File");
		        } 
		        }
			   
		        PreparedStatement psdel=con.prepareStatement("delete from textfiles where concat(ackno,id) in (select concat(ackno,min(id)) from textfiles where ackno in (select ackno from textfiles group by ackno having count(*)>1) group by ackno)");
		        psdel.execute();
		        psdel.close();
		        
			   cal.add(Calendar.DATE, +1);
			   String pvdat1=dateFormat.format(cal.getTime());
			   File dpat1=new File("C:\\Pantxtfile\\Jan_18\\"+pvdat1+"\\");
			   if(!dpat1.exists()) {
				   dpat1.mkdir();
			   }
			   con.close();
			   System.exit(0);
		   }
		   
		} catch (Exception e2) {
			e2.printStackTrace();
		}   
	}
	
}
