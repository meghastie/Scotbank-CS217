package uk.co.asepstrath.bank.services;

import uk.co.asepstrath.bank.models.Transactions;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    public static ArrayList<Transactions> Parser() {
        try {
            int p = 0;
            NodeList nodeList;
            ArrayList<Transactions> transactionsList = new ArrayList<>();
            do {
                String urlString = "https://api.asep-strath.co.uk/api/transactions?size=1000&page=" + p; // URL to your API endpoint
                URL url = new URL(urlString);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                //security things
                dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                dbFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                dbFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
                dbFactory.setExpandEntityReferences(false);

                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(url.openStream());

                doc.getDocumentElement().normalize();

                nodeList = doc.getElementsByTagName("results");


                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element element = (Element) nodeList.item(i);
                    String from = null;
                    String to = null;
                    String time = element.getElementsByTagName("timestamp").item(0).getTextContent();
                    double amount = Double.parseDouble(element.getElementsByTagName("amount").item(0).getTextContent());
                    try {
                        from = element.getElementsByTagName("from").item(0).getTextContent();
                    } catch (NullPointerException e){
                        from = null;
                    }
                    String id = element.getElementsByTagName("id").item(0).getTextContent();

                    try{
                        to = element.getElementsByTagName("to").item(0).getTextContent();
                    } catch (NullPointerException e){
                        to = null;
                    }
                    String type = element.getElementsByTagName("type").item(0).getTextContent();

                    Transactions transaction = new Transactions(time, amount, from, id, to, type);
                    transactionsList.add(transaction);

                }
                p++;
            } while (nodeList.getLength() > 0);

            /*
            // Printing the transactions
            for (Transactions transaction : transactionsList) {
                System.out.println("Time: " + transaction.getTime());
                System.out.println("Amount: " + transaction.getAmount());
                System.out.println("From: " + transaction.getFrom());
                System.out.println("ID: " + transaction.getID());
                System.out.println("To: " + transaction.getTo());
                System.out.println("Type: " + transaction.getType());
                System.out.println();
            }
            */
            return transactionsList;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; }

    public boolean checkID(ArrayList<Transactions> allTransactions,String newID ){
        boolean valid = true;
        for(Transactions t: allTransactions){
            if(t.getID().equals(newID)){
                valid = false;
            }
            }
        return valid;
    }
}
