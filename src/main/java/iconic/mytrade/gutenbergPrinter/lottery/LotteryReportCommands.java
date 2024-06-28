package iconic.mytrade.gutenbergPrinter.lottery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import iconic.mytrade.gutenbergPrinter.PrinterCommands;

public class LotteryReportCommands extends PrinterCommands {
	
	public void printLotteryReport()
	{
		return;
	}
	
	private LotteryStatus ReadLotteryStatus(int requesttype, String till, String date, int repz) {
		LotteryStatus status = null;
		
		return status;
	}
	
	private LotteryReceiptStatus ReadLotteryReceiptStatus(String till, int repz, int recnum, String date, int type) {
		LotteryReceiptStatus status = null;
		
		return status;
	}
	
	private void logILTStatus(LotteryStatus status, String filename)
	{
		   String HEADER = "LOTTERIA ISTANT. ";
		   String TILLID = "CASSA : ";
		   String ZREPNUM = "CHIUSURA FISCALE : ";
		   String DATE = "DATA DEL PRIMO CODICE DISP. : ";
		   String FILESTOSEND = "FILES DA INVIARE : ";
		   String OLDFILESTOSEND = "FILES VECCHI DA INVIARE : ";
		   String REJECTEDFILES = "RICHIESTE CODICI RIFIUTATE : ";
		   String WAITINGRECEIPTS = "SCONTRINI IN ATTESA : ";
		   String RECEIPTSTOSEND = "SCONTRINI DA INVIARE : ";
		   String ACCEPTEDRECEIPTS = "SCONTRINI ACCETTATI : ";
		   String REJECTEDRECEIPTS = "SCONTRINI RIFIUTATI : ";
		   String NUMREMAININGCODES = "CHIAVI DISPONIBILI RIMANENTI : ";
		   String ILVERSION = "VERSIONE LOTTERIA ISTANTANEA : ";
		   String LASTREQRESULT = "ULTIMO CODICE RISPOSTA ADE : ";
		   String SUBERROR = "ULTIMO ERRORE RISPOSTA ADE : ";
		   
		   File inout = null;		
		   FileOutputStream fos = null;
		   PrintStream ps = null;
		   
		   Calendar c = Calendar.getInstance();
           SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
           
           String s = sdf.format(c.getTime()); 
		   
		   s = s + " - " + HEADER;
		   
		   try {
			   //String dayofmonth = Sprint.f("%02d", c.getTime().getDate());
			   //String filename = rtlog_folder+ltlog_name+"_"+dayofmonth+rtlog_ext;
			   inout = new File(filename);
			   fos = new FileOutputStream(inout,false);
			   ps = new PrintStream(fos);
			   
			   ps.println(s);
			   ps.println(TILLID + status.getTillId());
			   ps.println(ZREPNUM + status.getzRepNum());
			   ps.println(DATE + status.getDate());
			   //ps.println("Lottery kindOfRequest : " + status.kindOfRequest);
			   ps.println(FILESTOSEND + status.getFilesToSend());
			   ps.println(OLDFILESTOSEND + status.getOldFilesToSend());
			   ps.println(REJECTEDFILES + status.getRejectedFiles());
			   ps.println(WAITINGRECEIPTS + status.getWaitingReceipts());
			   ps.println(RECEIPTSTOSEND + status.getReceiptsToSend());
			   ps.println(ACCEPTEDRECEIPTS + status.getAcceptedReceipts());
			   ps.println(REJECTEDRECEIPTS + status.getRejectedReceipts());
			   ps.println(NUMREMAININGCODES + status.getNumRemainingCodes());
			   ps.println(ILVERSION + status.getILVersion());
			   ps.println(LASTREQRESULT + status.getLastReqResult());
			   ps.println(SUBERROR + status.getSubError());
			   
			   ps.close();
			   ps = null;
			   fos.close();
			   fos = null;
			   inout = null;
		   } catch(Exception e) {
			   System.out.println("logILTStatus - Exception : " + e.getMessage());
		   }
	}
	
	private void logLTStatus(LotteryStatus status, String filename)
	{
		   String HEADER = "LOTTERIA DIFFER. ";
		   String TILLID = "CASSA : ";
		   String ZREPNUM = "CHIUSURA FISCALE : ";
		   String DATE = "DATA : ";
		   String FILESTOSEND = "FILES DA INVIARE : ";
		   String OLDFILESTOSEND = "FILES VECCHI DA INVIARE : ";
		   String REJECTEDFILES = "FILES RIFIUTATI : ";
		   String WAITINGRECEIPTS = "SCONTRINI IN ATTESA : ";
		   String RECEIPTSTOSEND = "SCONTRINI DA INVIARE : ";
		   String ACCEPTEDRECEIPTS = "SCONTRINI ACCETTATI : ";
		   String REJECTEDRECEIPTS = "SCONTRINI RIFIUTATI : ";
		   
		   File inout = null;		
		   FileOutputStream fos = null;
		   PrintStream ps = null;
		   
		   Calendar c = Calendar.getInstance();
           SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
           
           String s = sdf.format(c.getTime()); 
		   
		   s = s + " - " + HEADER;
		   
		   try {
			   //String dayofmonth = Sprint.f("%02d", c.getTime().getDate());
			   //String filename = rtlog_folder+ltlog_name+"_"+dayofmonth+rtlog_ext;
			   inout = new File(filename);
			   fos = new FileOutputStream(inout,false);
			   ps = new PrintStream(fos);
			   
			   ps.println(s);
			   ps.println(TILLID + status.getTillId());
			   ps.println(ZREPNUM + status.getzRepNum());
			   ps.println(DATE + status.getDate());
			   //ps.println("Lottery kindOfRequest : " + status.kindOfRequest);
			   ps.println(FILESTOSEND + status.getFilesToSend());
			   ps.println(OLDFILESTOSEND + status.getOldFilesToSend());
			   ps.println(REJECTEDFILES + status.getRejectedFiles());
			   ps.println(WAITINGRECEIPTS + status.getWaitingReceipts());
			   ps.println(RECEIPTSTOSEND + status.getReceiptsToSend());
			   ps.println(ACCEPTEDRECEIPTS + status.getAcceptedReceipts());
			   ps.println(REJECTEDRECEIPTS + status.getRejectedReceipts());
			   
			   ps.close();
			   ps = null;
			   fos.close();
			   fos = null;
			   inout = null;
		   } catch(Exception e) {
			   System.out.println("logLTStatus - Exception : " + e.getMessage());
		   }
	}
	
	private void logLTStatus(LotteryReceiptStatus status, String filename)
	{
		   //String HEADER = "\nREPORT SCONTRINI LOTTERIA";
		   String TILLID = "\nCASSA : ";
		   String ZREPNUM = "CHIUSURA FISCALE : ";
		   String RECNUM = "NUMERO SCONTRINO : ";
		   String DATE = "DATA : ";
		   String RESULT = "ESITO RICHIESTA : ";
		   String ERRCODE = "CODICE DI ERRORE : ";
		   String ANSWERID = "ID. RISPOSTA : ";
		   
		   File inout = null;		
		   FileOutputStream fos = null;
		   PrintStream ps = null;
		   
		   //Calendar c = Calendar.getInstance();
           //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
           
           //String s = sdf.format(c.getTime()); 
		   
		   //s = s + " - " + HEADER;
		   
		   try {
			   //String dayofmonth = Sprint.f("%02d", c.getTime().getDate());
			   //String filename = rtlog_folder+ltlog_name+"_"+dayofmonth+rtlog_ext;
			   inout = new File(filename);
			   fos = new FileOutputStream(inout,true);
			   ps = new PrintStream(fos);
			   
			   //ps.println(s);
			   ps.println(TILLID + status.getTillId());
			   ps.println(ZREPNUM + status.getzRepNum());
			   ps.println(RECNUM + status.getRecNum());
			   ps.println(DATE + status.getRecDate());
			   ps.println(RESULT + status.getResult());
			   ps.println(ERRCODE + status.getErrCode());
			   ps.println(ANSWERID + status.getIdAnswer());
			   //ps.println("Lottery Receipt kindOfReceipt : " + status.kindOfReceipt);
			   
			   ps.close();
			   ps = null;
			   fos.close();
			   fos = null;
			   inout = null;
		   } catch(Exception e) {
			   System.out.println("logLTStatus - Exception : " + e.getMessage());
		   }
	}
	
	private void printLTStatus(String filename)
	{
	}
	
}
