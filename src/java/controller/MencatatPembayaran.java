/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import model.Pembayaran;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.io.output.*;

/**
 *
 * @author Michael Donny Kusuma
 */
@WebServlet(name = "MencatatPembayaran", urlPatterns = {"/MencatatPembayaran"})
public class MencatatPembayaran extends HttpServlet {
    String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Calendar.getInstance().getTime());
    String timeStamp2 = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
    private final String UPLOAD_DIRECTORY = "C:\\Apache\\";
    private String fileType = ".csv";
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Pembayaran p = new Pembayaran();
        DatabaseManager db = new DatabaseManager();
            
            //Menyimpan file ke dalam sistem
            if(ServletFileUpload.isMultipartContent(request)){

                try {
                    List<FileItem> multiparts = new ServletFileUpload(
                                             new DiskFileItemFactory()).parseRequest(request);
                    for(FileItem item : multiparts){
                        if(!item.isFormField()){
                            String fileNameSource = new File(item.getName()).getName();//Mengambil nama sumber file
                            String name = "DataPembayaran_"+timeStamp+".csv"; //Membuat nama file untuk disimpan
                            item.write( new File(UPLOAD_DIRECTORY + File.separator + name));
                            if (fileNameSource.isEmpty()) { //Mengecek apakah ada file yang diupload
                                throw new Exception("Tidak ada file yang diupload");
                            }
                            if (!fileNameSource.contains(".csv")) { //Mengecek apakah file bertipe .csv
                                throw new Exception("Format file salah");
                            }
              
                        }
                    }
                } catch (Exception ex) {
                    returnError(request, response, ex);
                }          

            }else{
                returnError(request, response, new Exception("Error mengupload file"));
            }
            
            
            //Membaca file dari dalam sistem
                String csvFile = UPLOAD_DIRECTORY + "DataPembayaran_"+timeStamp+".csv";
                BufferedReader br = null;
                String line = "";
                String cvsSplitBy = ",";

                try {

                    br = new BufferedReader(new FileReader(csvFile));
                    int counter = 1;
                    while ((line = br.readLine()) != null) {

                        // use comma as separator
                        String[] dataSet = line.split(cvsSplitBy);
                        
                        p.setID(timeStamp2+"_"+counter);
                        p.setWaktuPembayaran(dataSet[0]);
                        p.setNoRekening(dataSet[1]);
                        p.setJumlahPembayaran(Double.parseDouble(dataSet[2])); 
                        p.setNis(dataSet[3].substring(0, 5)); // Mengubah berita acara menjadi NIS
                        p.setJenisPembayaran(dataSet[3].substring(6)); // Mengubah berita acara menjadi jenis pembayaran
                        
                        db.simpanPembayaran(p);
                        counter++;

                    }
                    this.tampil(request, response, "Data Tersimpan");

                } catch (FileNotFoundException e) {
                    returnError(request, response, e);
                } catch (IOException e) {
                    returnError(request, response, e);
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
    }

    public void returnError(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        request.setAttribute("error", e.getMessage());
        dispatcher = request.getRequestDispatcher("error.jsp");
        dispatcher.forward(request, response);
    }

    public void tampil(HttpServletRequest request, HttpServletResponse response, String information) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        request.setAttribute("info", information);
        dispatcher = request.getRequestDispatcher("info.jsp");
        dispatcher.forward(request, response);
    }
}
