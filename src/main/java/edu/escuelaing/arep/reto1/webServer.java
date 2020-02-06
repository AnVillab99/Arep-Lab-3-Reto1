package edu.escuelaing.arep.reto1;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class webServer {

    static final File ROOT = new File(System.getProperty("user.dir") + "/src/main/java/edu/escuelaing/arep/resources");
    static final String DEFAULT = "/index.html";
    static final String FILE_NOT_FOUND = "/NOT_FOUND.html";
    static final String METHOD_NOT_ALLOWED = "/NOT_SUPPORTED.html";
    static final String UNSUPPORTED_MEDIA_TYPE = "/NOT_SUPPORTED_MEDIA.html";
    static final int PORT = 35000;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(PORT);
        System.out.println("Abierto");
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedOutputStream dataOut = null;
        Socket clientSocket = null;
        boolean conectado = true;
      
        while (conectado) {
            try{
            
                clientSocket = serverSocket.accept();
                System.out.println("Conectado");
            } catch (IOException e) {
                System.out.println("Error al conectar al cliente");
            }
            try{

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));// leer
            out = new PrintWriter(clientSocket.getOutputStream(), true); // devolver
            dataOut = new BufferedOutputStream(clientSocket.getOutputStream());
            String inputLine = in.readLine();
            String[] header = inputLine.split(" ");
            System.out.println("i");
            System.out.println(inputLine);
            while (inputLine != null) {
                if (!(in.ready())) {
                    //System.out.println("como entra aca");
                    System.out.println("xxxxxxxxxxxxxxx");
                    break;
                    //break;
                }
                inputLine = in.readLine();

            }

            if (header[0].equals("GET")) {
                File rFile = null;
                if (header[1].equals("/")) {
                    rFile = new File(ROOT, DEFAULT);
                    // respond(out, dataOut, rFile, "text/html", "200");
                } else {
                    System.out.println("header 1"+header[1]);

                    String[] s = soportado(header[1]);
                    System.out.println(s[0]);

                    if (s[0].equals("ok")) {
                        rFile = new File(ROOT, s[1] + header[1]);
                        if (rFile.exists()) {
                            respond(out, dataOut, rFile, s[2], "200");

                        } else {
                            rFile = new File(ROOT, FILE_NOT_FOUND);
                            respond(out, dataOut, rFile, "text/html", "404");
                        }
                    }

                    else {
                        rFile = new File(ROOT, UNSUPPORTED_MEDIA_TYPE);
                        respond(out, dataOut, rFile, "text/html", "415");
                    }

                }

            }

            else {
                File f = new File(ROOT, METHOD_NOT_ALLOWED);
                respond(out, dataOut, f, "text/html", "405");
            }
            out.close();
            in.close();
            
            clientSocket.close();
        }
        catch (Exception e){
            System.out.println("error misterioso");
            System.out.println(e);}
        }

        out.close();
        in.close();
        serverSocket.close();
        clientSocket.close();
   

    }

    private static String[] soportado(String peticionGet) {
        String[] ans = new String[3];
        System.out.println("peticion es : "+peticionGet);
        if (peticionGet.endsWith(".png")) {
            ans[0] = "ok";
            ans[1] = "/imgs";
            ans[2] = "image/png";
        }

        else if (peticionGet.endsWith(".jpg")) {
            ans[0] = "ok";
            ans[1] = "/imgs";
            ans[2] = "image/jpg";
        } 
        else if (peticionGet.endsWith(".html")) {
            ans[0] = "ok";
            ans[1] = "";
            ans[2] = "text/html";
        } 
        else {
            ans[0] = "error";
            ans[1] = "";
            ans[2] = "";
        }
        return ans;

    }

    private static void respond(PrintWriter out, BufferedOutputStream dataOut, File response, String type,
            String code) {
        out.println("HTTP/1.1 " + response);
        out.println("Server: Java HTTP Server from AnVillab99 : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + type);
        out.println("Content-length: " + response.length());
        out.println();
        out.flush();
        // Content

        String[] con = type.split("/");
        try {
            if (con[0].equals("image")) {

                BufferedImage image = ImageIO.read(response);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(image, con[1], os);
                dataOut.write(os.toByteArray());
                dataOut.flush();
            } else {
                byte[] fileByte = new byte[(int) response.length()];
                FileInputStream fileO = null;
                fileO = new FileInputStream(response);
                fileO.read(fileByte);

                if (fileO != null) {
                    fileO.close();
                }

                dataOut.write(fileByte, 0, (int) response.length());
                dataOut.flush();
            }
        } catch (Exception e) {
            System.out.println("erro en envio");
            System.out.println(e);
        }

    }
}
