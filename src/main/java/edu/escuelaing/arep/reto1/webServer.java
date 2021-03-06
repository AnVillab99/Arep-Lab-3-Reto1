package edu.escuelaing.arep.reto1;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class webServer {

    static final String ROOT = System.getProperty("user.dir") +"/src/main/java/edu/escuelaing/arep/resources";
    static final String DEFAULT = "/index.html";
    static final String FILE_NOT_FOUND = "/NOT_FOUND.html";
    static final String METHOD_NOT_ALLOWED = "/NOT_SUPPORTED.html";
    static final String UNSUPPORTED_MEDIA_TYPE = "/NOT_SUPPORTED_MEDIA.html";
    static int PORT;
    
    /**
     * Este metodo es el que se encarga de recibir y analizar las peticiones que le llegan al servidor, rechaza las 
     * que no son peticiones get, preguntan por archivos que no existen o tratan de usar metodos
     * no soportados
     */
    public static void main(String[] args) throws IOException {
        PORT = getPort();
        System.out.println("puerto "+PORT);

        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(PORT);
        System.out.println("Abierto");
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedOutputStream dataOut = null;
        OutputStream outS =null;
        
        Socket clientSocket = null;
        boolean conectado = true;
        while (conectado) {
            try{
            
                clientSocket = serverSocket.accept();
                System.out.println("Conectado");
                try{

                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));// leer
                    out = new PrintWriter(clientSocket.getOutputStream(), true); // devolver
                    dataOut = new BufferedOutputStream(clientSocket.getOutputStream());
                    outS = clientSocket.getOutputStream();
                    String inputLine = in.readLine();
                    String[] header = inputLine.split(" ");
                
        
                    if (header[0].equals("GET")) {
                        File rFile = null;
                        if (header[1].equals(" ") ||header[1].equals("") ||header[1].equals("/")) {
                            rFile = new File(ROOT, DEFAULT);
                            respond(out, dataOut, rFile, "text/html", "200",ROOT+ DEFAULT,outS);
                        
                        } else {
                            String[] s = soportado(header[1]);
                            if (s[0].equals("ok")) {
                                rFile = new File(ROOT+ s[1] + header[1]);
                                if (rFile.exists()) {
                                    respond(out, dataOut, rFile, s[2], "200",ROOT+ s[1] + header[1],outS);
                                } else {
                                    rFile = new File(ROOT, FILE_NOT_FOUND);
                                    respond(out, dataOut, rFile, "text/html", "404",ROOT+ FILE_NOT_FOUND,outS);
                                }
                            }
                            else {
                                    rFile = new File(ROOT, UNSUPPORTED_MEDIA_TYPE);
                                    respond(out, dataOut, rFile, "text/html", "415",ROOT+ UNSUPPORTED_MEDIA_TYPE,outS);
                            }
                        }
                    }
                    
                    else {
                        
                            File f = new File(ROOT, METHOD_NOT_ALLOWED);
                            respond(out, dataOut, f, "text/html", "405",ROOT+METHOD_NOT_ALLOWED,outS);
                        
                    }
                    out.close();
                    in.close();
                    dataOut.close();
                    clientSocket.close();
                }
                catch (Exception e){
                    System.out.println("error misterioso de me quiero morir");
                    System.out.println(e);
                }
            } 
            catch (IOException e) {
                System.out.println("Error al conectar al cliente");
            }
            
        }

        out.close();
        in.close();
        dataOut.close();
        clientSocket.close();
        serverSocket.close();

    }


    /**
     * Este metodo analiza la peticion, el tipo de archivo pedido. Si es un archivo soportado inidca donde encontrarlo
     * y el MIME type que debe tener el header de la peticion
     * @param String la peticion
     * @return String[] un array indicando si la peticion es valida, donde esta el archivo y el mime type
     */
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
        else if (peticionGet.endsWith(".js")) {
            ans[0] = "ok";
            ans[1] = "";
            ans[2] = "application/javascript";
        } 
        else {
            ans[0] = "error";
            ans[1] = "";
            ans[2] = "text/html";
        }
        return ans;

    }

    /**
     * metodo retorna el puerto por el que se aceptan peticiones
     * @retunr int el puerto
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
        return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set
        }

    /**
     * Este metodo responde la peticion al cliente
     * @param out printwriter
     * @param dataOut bufferedoutputstream
     * @param response espuesta
     * @param type mime type
     * @param code codigo http
     * @param filePath path del archivo
     * @param outS outputstream
     */
    private static void respond(PrintWriter out, BufferedOutputStream dataOut, File response, String type,String code, String filePath, OutputStream outS) {
        String header = "HTTP/1.1 " + code+"\r\n"+
        "Access-Control-Allow-Origin: *\r\n"+
        "Content-type: " + type+"\r\n";
        
        String[] con = type.split("/");
        try {
            if (con[0].equals("image")) {
                System.out.println(type);
                BufferedImage image = ImageIO.read(response);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(image, con[1], os);
                byte[] r = os.toByteArray();
                System.out.println("tamaño "+r.length);
                header+= "Connection: close\r\n"+
                "Content-length: " + r.length+"\r\n"+
                "\r\n";
                byte[] d = header.getBytes();
                dataOut.write(d);
                
                
                dataOut.write(os.toByteArray());
                dataOut.flush();

                System.out.println(os.toByteArray().length);
                dataOut.close();



            } else {
                header+="\r\n";
                out.println(header);
                BufferedReader reader = new BufferedReader(new FileReader(response));
                StringBuffer result = new StringBuffer();
                String res = "";
                while ((res = reader.readLine()) != null) {
                    result.append(res);
                }
                out.println(result);
                out.flush();
                out.close();
                reader.close();
            }
        } catch (Exception e) {
            System.out.println("erro en envio");
            System.out.println(e);
        }
        out.flush();
        out.close();

    }
}
