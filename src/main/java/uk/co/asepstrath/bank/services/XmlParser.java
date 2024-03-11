package uk.co.asepstrath.bank.services;

import uk.co.asepstrath.bank.models.Transactions;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    public static List<Transactions> Parser() {
        try {
            URL url = new URL("https://api.asep-strath.co.uk/api/transactions"); // URL to your API endpoint

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url.openStream());

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("results");
            List<Transactions> transactionsList = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                String time = element.getElementsByTagName("timestamp").item(0).getTextContent();
                double amount = Double.parseDouble(element.getElementsByTagName("amount").item(0).getTextContent());
                String from = element.getElementsByTagName("from").item(0).getTextContent();
                String id = element.getElementsByTagName("id").item(0).getTextContent();
                String to = element.getElementsByTagName("to").item(0).getTextContent();
                String type = element.getElementsByTagName("type").item(0).getTextContent();

                Transactions transaction = new Transactions(time, amount, from, id, to, type);
                transactionsList.add(transaction);
            }

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
        return null;
    }
}