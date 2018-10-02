package org.businessanddecisions.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import javax.xml.validation.Schema;

@Component
public final class AppRoutes {
    private List<String> adminRoutes;
    private List<String> managerRoutes;
    private List<String> employeeRoutes;
    private List<String> adminAndManagerRoutes;
    private List<String> forAllRoutes;
    public Element documentRootElement=null;

    public  AppRoutes() {
        this.adminRoutes = new LinkedList<>();
        this.managerRoutes = new LinkedList<>();
        this.employeeRoutes = new LinkedList<>();
        this.adminAndManagerRoutes = new LinkedList<>();
        this.forAllRoutes = new LinkedList<>();
        this.documentRootElement = AppRoutes.getAllDocumentRoutes();

        this.fillAdminRoutes();
        this.fillManagerRoutes();
        this.fillEmployeeRoutes();
        this.fillAdminAndManagerRoutes();
        this.fillforAllRoutes();
    }

    private static final Element getAllDocumentRoutes() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        File f =null;
        Document document =null;
        Schema schema = null;
        try {
           builder = factory.newDocumentBuilder();
           f =new File("src/main/resources/routes.xml");
            document = builder.parse(f);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return   document.getDocumentElement();
    }

    private  void fillAdminRoutes() {
      NodeList adminRoutesNodes= this.documentRootElement.getElementsByTagName("adminRoutes").item(0).getChildNodes();
      if(adminRoutesNodes != null  ) {
          for (int i = 0; i < adminRoutesNodes.getLength(); i++) {
              String expectNodeNamedUrl = adminRoutesNodes.item(i).getNodeName();
              if (expectNodeNamedUrl != null && expectNodeNamedUrl.equals("url")) {
                  if (!adminRoutesNodes.item(i).getTextContent().equals(""))
                      this.adminRoutes.add(adminRoutesNodes.item(i).getTextContent());
              }
          }
      }
    }

    private void fillManagerRoutes() {
        NodeList adminRoutesNodes= this.documentRootElement.getElementsByTagName("managerRoutes").item(0).getChildNodes();
        if(adminRoutesNodes != null) {
            for (int i = 0; i < adminRoutesNodes.getLength(); i++) {
                String expectNodeNamedUrl = adminRoutesNodes.item(i).getNodeName();
                if (expectNodeNamedUrl != null && expectNodeNamedUrl.equals("url")) {
                    if (!adminRoutesNodes.item(i).getTextContent().equals(""))
                        this.managerRoutes.add(adminRoutesNodes.item(i).getTextContent());
                }
            }
        }
    }

    private void fillEmployeeRoutes() {
        NodeList employeeRoutesNodes= this.documentRootElement.getElementsByTagName("employeeRoutes").item(0).getChildNodes();
        if(employeeRoutesNodes != null) {
            for (int i = 0; i < employeeRoutesNodes.getLength(); i++) {
                String expectNodeNamedUrl = employeeRoutesNodes.item(i).getNodeName();
                if (expectNodeNamedUrl != null && expectNodeNamedUrl.equals("url")) {
                    if (!employeeRoutesNodes.item(i).getTextContent().equals(""))
                        this.employeeRoutes.add(employeeRoutesNodes.item(i).getTextContent());
                }
            }
        }
    }



    private void fillAdminAndManagerRoutes() {
        NodeList adminAndManagerRoutesNodes = this.documentRootElement.getElementsByTagName("adminAndManagerRoutes").item(0).getChildNodes();
        if(adminAndManagerRoutesNodes !=null) {
            for (int i = 0; i < adminAndManagerRoutesNodes.getLength(); i++) {
                String expectedNodeNameUrl = adminAndManagerRoutesNodes.item(i).getNodeName();
                if (expectedNodeNameUrl != null && expectedNodeNameUrl.equals("url")) {
                    if (!adminAndManagerRoutesNodes.item(i).getTextContent().equals("")) {
                        this.adminAndManagerRoutes.add(adminAndManagerRoutesNodes.item(i).getTextContent());
                    }
                }
            }
        }
    }

    private void fillforAllRoutes() {
        NodeList forAllRoutesNodes = this.documentRootElement.getElementsByTagName("forAllRoutes").item(0).getChildNodes();
        if(forAllRoutesNodes != null) {
            for (int i = 0; i < forAllRoutesNodes.getLength(); i++) {
                String expectedNodeNameUrl = forAllRoutesNodes.item(i).getNodeName();
                if (expectedNodeNameUrl != null && expectedNodeNameUrl.equals("url")) {
                    if (!forAllRoutesNodes.item(i).getTextContent().equals("")) {
                        this.forAllRoutes.add(forAllRoutesNodes.item(i).getTextContent());
                    }
                }
            }
        }
    }

    public String[] getAdminRoutesArray() {
        return this.adminRoutes.toArray(new String[0]);
    }

    public String[] getManagerRoutesArray() {
        return this.managerRoutes.toArray(new String[0]);
    }

    public String[] getEmployeeRoutesArray() {
        return this.employeeRoutes.toArray(new String[0]);
    }

    public String[] getAdminAndManagerRoutesArray(){ return this.adminAndManagerRoutes.toArray(new String[0]);}

    public String[] getRoutesForAll() {return this.forAllRoutes.toArray(new String[0]);}
}
