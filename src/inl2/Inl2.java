
package inl2;

import java.awt.BorderLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Inl2 {
    
Connection con;
Statement statement;
ResultSet rs;
Properties p; 

int temp;

String förnamn;
String efternamn;

String märke;
String kategori;
int pris;

String temps;
String temps2;

int tempId; //
String färg; //
String storlek; //
String tempInput;
int kundId; //
int varuId; //
String svar; //
String beställningsDatum;
int lastId;

    
    public Inl2(){
    try {
        Class.forName("com.mysql.jdbc.Driver");
        p = new Properties();
        p.load(new FileInputStream("src/inl2/settings.properties"));
        
        con = DriverManager.getConnection(
        p.getProperty("driverConnection"),
        p.getProperty("name"),
        p.getProperty("password"));
        
        statement = con.createStatement();
        

        
        //getMedlemmar(); // KLAR
        //getProduktKategori(); //KLAR
        addToCart();
        
    } catch (ClassNotFoundException | FileNotFoundException ex) {
        Logger.getLogger(Inl2.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException | SQLException ex) {
        Logger.getLogger(Inl2.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    }
    //Klar
    public void getMedlemmar(){
    try {
        Scanner sc = new Scanner(System.in);        
        System.out.println("Ange ditt kundId eller skriv \"visa\" för att se alla kunder ");
        String ss = sc.nextLine();
        
        if(isInteger(ss)){
            System.out.println("Det är en integer!");
            
            //Kollar om id finns med bland kunder
            int i = Integer.parseInt(ss);
            rs = statement.executeQuery("select id from kund where id = " + ss);
        
            String s  ="";
            while(rs.next()){
                temp = rs.getInt("id");
                //convert int to string
                s = Integer.toString(temp);
            }
            //String s = Integer.toString(i);
        
            if(ss.equals(s)){
            
                rs = statement.executeQuery("Select kund.fornamn as Förnamn, kund.efternamn as Efternamn, sum(varor.pris * innehaller.antal) as Pris from kund\n" +
                "inner join bestallning on bestallning.kundId = kund.id\n" +
                "inner join innehaller on innehaller.bestallningId = bestallning.id\n" +
                "inner join varor on varor.id = innehaller.varuId\n" +
                "where kund.id = " + i+";");
                while(rs.next()){
                    förnamn = rs.getString("Förnamn");
                    efternamn = rs.getString("Efternamn");
                    pris = rs.getInt("Pris");
                    System.out.println(förnamn + " " + efternamn + " " + pris);
                }
            }else{
                System.out.println("Det Id du angav är felaktig. nu listas alla kunder istället.");
                rs = statement.executeQuery("Select kund.fornamn as Förnamn, kund.efternamn as Efternamn, sum(varor.pris * innehaller.antal) as Pris from kund\n" +
                "inner join bestallning on bestallning.kundId = kund.id\n" +
                "inner join innehaller on innehaller.bestallningId = bestallning.id\n" +
                "inner join varor on varor.id = innehaller.varuId\n" +
                "group by kund.fornamn DESC, Pris;");
                while(rs.next()){
                    förnamn = rs.getString("Förnamn");
                    efternamn = rs.getString("Efternamn");
                    pris = rs.getInt("Pris");
                    System.out.println(förnamn + " " + efternamn + " " + pris);
                }
            }   
            
        }
        /*
        //int i = sc.nextInt();
        
        //Kollar om id finns med bland kunder
        int i = Integer.parseInt(ss);
        rs = statement.executeQuery("select id from kund where id = " + ss);
        
        String s  ="";
        while(rs.next()){
            temp = rs.getInt("id");
            //convert int to string
            s = Integer.toString(temp);
        }
        //String s = Integer.toString(i);
        
        if(ss.equals(s)){
            
            rs = statement.executeQuery("Select kund.fornamn as Förnamn, kund.efternamn as Efternamn, sum(varor.pris * innehaller.antal) as Pris from kund\n" +
            "inner join bestallning on bestallning.kundId = kund.id\n" +
            "inner join innehaller on innehaller.bestallningId = bestallning.id\n" +
            "inner join varor on varor.id = innehaller.varuId\n" +
            "where kund.id = " + i+";");
            while(rs.next()){
                förnamn = rs.getString("Förnamn");
                efternamn = rs.getString("Efternamn");
                pris = rs.getInt("Pris");
                System.out.println(förnamn + " " + efternamn + " " + pris);
            }
        }else if(ss.equals("visa"){
            System.out.println("Det Id du angav är felaktig. nu listas alla kunder istället.");
            rs = statement.executeQuery("Select kund.fornamn as Förnamn, kund.efternamn as Efternamn, sum(varor.pris * innehaller.antal) as Pris from kund\n" +
            "inner join bestallning on bestallning.kundId = kund.id\n" +
            "inner join innehaller on innehaller.bestallningId = bestallning.id\n" +
            "inner join varor on varor.id = innehaller.varuId\n" +
            "group by kund.fornamn DESC, Pris;");
            while(rs.next()){
                förnamn = rs.getString("Förnamn");
                efternamn = rs.getString("Efternamn");
                pris = rs.getInt("Pris");
                System.out.println(förnamn + " " + efternamn + " " + pris);
            }
        }
        */
        
        
        } catch (SQLException ex) {
            Logger.getLogger(Inl2.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Nummret du angav blev fel.");
        }
    }
    //Klar
    public boolean isInteger(String string) {
    try {
        Integer.valueOf(string);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}
    
    public void getProduktKategori(){
        try {
            rs = statement.executeQuery("Select varor.märke as Märke, kategori.namn as Kategori from harkategori\n" +
            "inner join kategori on kategori.id = harkategori.kategoriId\n" +
            "inner join varor on varor.id = harkategori.varuId\n" +
            "order by kategori.namn ASC;");
            while(rs.next()){
                märke = rs.getString("Märke");
                kategori = rs.getString("Kategori");
                System.out.println(märke + " " + kategori);
            }
            
            
        } catch (Exception e) {
            System.out.println("Någonting gick snett");
        }
    }
    
    public void addToCart(){
        try {
            Scanner sc = new Scanner(System.in);
            
            rs = statement.executeQuery("Select id, fornamn, efternamn from kund\n" +
            "order by id;");
            while(rs.next()){
                temps = rs.getString("fornamn");
                temps2 = rs.getString("efternamn");
                System.out.println(temps + " " + temps2);
            }
            
            
            System.out.println("Ange förnamn: ");
            String anv = sc.nextLine();
            System.out.println("Ange efternamn: ");
            String los = sc.nextLine(); 
            
            rs = statement.executeQuery("Select id, fornamn, efternamn from kund\n" +
            "where fornamn = '" + anv  +"' and efternamn = '"+ los + "'\n" +
            "order by id;");
            while(rs.next()){
                temps = rs.getString("fornamn");
                temps2 = rs.getString("efternamn");
                kundId = rs.getInt("id");
                System.out.println(temps + temps2+ kundId);
                if (temps.equals(anv) && temps2.equals(los)){
                    System.out.println("Du är nu inloggad!");
                    //TODO DU ÄR INLOGGAD OCH KAN NU GÅ VIDARE ID SPARAT I temp
                    rs = statement.executeQuery("Select pris, märke, lagerAntal from varor\n" +
                    "where lagerAntal > 0;");
                    System.out.println("Pris\t" + "Märke\t" + "Lagerantal");
                    while(rs.next()){
                        temps = rs.getString("pris");
                        temps2 = rs.getString("märke");
                        temp = rs.getInt("lagerAntal");
                        System.out.println("Pris: " + temps+ "\t" + "Märke:  " + temps2+ "\t"+ "Lagerantal:  " + temp);
                    }
                    
                    System.out.println("Ange märke!");
                    String marke = sc.nextLine();
                    rs = statement.executeQuery("Select pris, märke, lagerAntal from varor\n" +
                    "where lagerAntal > 0 and märke = '"+ marke +"';");
                    while(rs.next()){
                        temps = rs.getString("pris");
                        temps2 = rs.getString("märke");
                        temp = rs.getInt("lagerAntal");
                        System.out.println("Pris: " + temps+ "\t" + "Märke:  " + temps2+ "\t"+ "Lagerantal:  " + temp);
                    }
                    
                    System.out.println("Ange pris!");
                    String pris = sc.nextLine();
                    rs = statement.executeQuery("Select id, pris, märke, lagerAntal from varor\n" +
                    "where lagerAntal > 0 and märke = '"+ marke +"' and pris = '" + pris + "';");
                    while(rs.next()){
                        varuId = rs.getInt("id");
                        temps = rs.getString("pris");
                        temps2 = rs.getString("märke");
                        temp = rs.getInt("lagerAntal");
                        System.out.println("Pris: " + temps+ "\t" + "Märke:  " + temps2+ "\t"+ "Lagerantal:  " + temp);
                    }
                    
                    
                    rs = statement.executeQuery("select farg, storlek from varudetalj\n" +
                        "inner join detaljlista on detaljlista.varuDetaljId = varudetalj.id\n" +
                        "where varuId = '" + varuId + "';");
                    while(rs.next()){
                        färg = rs.getString("farg");
                        storlek = rs.getString("storlek");
                        
                        System.out.println("Tillgängliga storlekar: " + storlek + "\t Tillgängliga färger: " + färg);
                    }
                    System.out.println("Ange storlek! eller tryck s för att se alla ");
                    storlek = sc.nextLine();
                    System.out.println("Ange färg du vill ha på skorna!");
                    färg = sc.nextLine();
                    System.out.println("Vill du lägga till skorna i din beställning? (ja/nej");
                    svar = sc.nextLine();
                    switch(svar){
                        case "ja":
                            //TODO LÄGG TILL KOD FÖR ATT LÄGGA tiLL VARAN IGENOM ATT KALLA PÅ  ADD CART
                            
                            //Lista alla ickeexpedierade beställningar som kunden har
                            System.out.println(kundId);
                            System.out.println("Dina ickeexpedierade beställningar är:\n");
                            int tempCounter = 0;
                            rs = statement.executeQuery("Select id, datum from bestallning where kundId = '" + kundId + "' and expedierad = 0;");
                            while(rs.next()){
                                tempCounter++;
                                svar = rs.getString("id");
                                beställningsDatum = rs.getString("datum");
                            
                                System.out.println("Alternativ nr: " + tempCounter + "\tDatum order skapades:\t" + beställningsDatum);
                            }
                           
                            
                            System.out.println("\nvälj beställningen du vill lägga till varan i igenom att skriva alternativNr\n eller skriv \"ny\" för att skapa ny order som varan hamnar i ");
                            svar = sc.nextLine();
                            
                            if (svar.equals("ny")){
                                //TODO lägg till kod för att lägga till ny beställning
                                statement.executeUpdate("Insert into bestallning(kundId) values\n" +
                                    "(" + kundId + ");");
                                
                                rs = statement.executeQuery("select last_insert_id();");
                                while(rs.next()){
                                    lastId = rs.getInt("last_insert_id()");
                                }
                                //BeställningsID ligger nu i lastiD
                                //Nu är det bara att köra stored procedure
                                
                                addItem(kundId, lastId, varuId);
                                
                                System.out.println("Den nya beställningen är nu gjord!");
                                
                                
                                
                                
                            }else{
                                int tempCounter2 = 0;
                                rs = statement.executeQuery("Select id, datum from bestallning where kundId = '" + kundId + "' and expedierad = 0;");
                                while(rs.next()){
                                    tempCounter2++;
                                    lastId = rs.getInt("id");
                                    beställningsDatum = rs.getString("datum");
                                
                                    if(tempCounter2 == tempCounter){
                                        System.out.println("Beställning hittad och id sparat!");
                                    }
                                }
                                
                                addItem(kundId, lastId, varuId);
                                
                            }
                            
                            
                            
                            
                            
                            /*
                            Class.forName("com.mysql.jdbc.Driver");
                            
                            
                            
                            try (Connection con = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/Inl1",
                            "root",
                            "375698sebbe");
                                    
                            CallableStatement AddToCart  = con.prepareCall("CALL AddToCart(?,?,?,?)");) {
                            AddToCart.setInt(1, kundId);
                            AddToCart.setInt(2, bestallningsId);
                            AddToCart.setInt(3, varuID);
                            AddToCart.setInt(4, 1);
                            AddToCart.execute();

                            
                            System.out.println("KÖPT VARA");
                            */
                            break;
                        case "nej":
                            break;
                    }
                    
                }
            }
            
                /*
            if (temps.equalsIgnoreCase(anv) && temps2.equalsIgnoreCase(los)){
                // TODO ADD REST OF CODE - YOU'RE LOGGED IN
                rs = statement.executeQuery("Select pris, märke, lagerAntal from varor\n" +
                "where lagerAntal > 0;");
                System.out.println("Pris\t" + "Märke\t" + "Lagerantal");
                while(rs.next()){
                    temps = rs.getString("pris");
                    temps2 = rs.getString("märke");
                    temp = rs.getInt("lagerAntal");
                    System.out.println(temps+ "\t" + temps2+ "\t" + temp);
                }
                
                    System.out.println("Ange pris");
                    int pris = sc.nextInt();
                    System.out.println("Ange märke");
                    String märke = sc.nextLine(); 
                
                    rs = statement.executeQuery("Select id, fornamn, efternamn from kund\n" +
                    "where fornamn = '" + anv  +"' and efternamn = '"+ los + "'\n" +
                    "order by id;");
                    while(rs.next()){
                    temps = rs.getString("fornamn");
                    temps2 = rs.getString("efternamn");
                    temp = rs.getInt("id");;
                    if (temps == anv){
                        System.out.println("rätt");
                    }
                }
            }*/
             
        } catch (Exception e) {
            System.out.println("Whoopsie");
        }         
    }
    
    public void addItem(int kundId, int lastId, int varuId) throws ClassNotFoundException, SQLException {
        
        System.out.println("Kundid; " + kundId + lastId + " varuId   " + varuId);
        Class.forName("com.mysql.jdbc.Driver");

        ;
        ;
        ;
        
        try (Connection con = DriverManager.getConnection(
                p.getProperty("driverConnection"),
                p.getProperty("name"),
                p.getProperty("password"));
                CallableStatement stm
                = con.prepareCall("CALL AddToCart(?,?,?,?)");) {
            stm.setInt(1, kundId);
            stm.setInt(2, lastId);
            stm.setInt(3, varuId);
            stm.setInt(4, 1);
            stm.execute();

        }
        
        System.out.println("Allting gick bra");
    }
    
    
    public static void main(String[] args) {
        new Inl2();
    }
    
}
