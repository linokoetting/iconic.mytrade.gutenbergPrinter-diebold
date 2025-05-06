package iconic.mytrade.gutenbergPrinter.eftpos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import iconic.mytrade.gutenbergPrinter.PrinterCommands;
import iconic.mytrade.gutenbergPrinter.SharedPrinterFields;
import jpos.JposException;

public class EftPos extends PrinterCommands {
	
	private static String EFTAuthorizationCode = "Eft Offline";
	
	private static HashMap<String, Long> EFTStanCodes = new HashMap<String, Long>();
	
	public static String getEFTAuthorizationCode(long amount) {
//        for (Map.Entry<String, Long> entry : EFTStanCodes.entrySet()) {
//            System.out.println("getEFTAuthorizationCode - StanCode: " + entry.getKey() + ", Amount: " + entry.getValue());
//        }
	    String reply = EFTAuthorizationCode;
	    if (EFTStanCodes != null) {
	        Iterator<Map.Entry<String, Long>> iterator = EFTStanCodes.entrySet().iterator();
	        while (iterator.hasNext()) {
	            Map.Entry<String, Long> entry = iterator.next();
	            if (entry.getValue().equals(amount)) {
	                reply = entry.getKey();
	                iterator.remove();
	                break;
	            }
	        }
	    }
//        for (Map.Entry<String, Long> entry : EFTStanCodes.entrySet()) {
//            System.out.println("getEFTAuthorizationCode - StanCode: " + entry.getKey() + ", Amount: " + entry.getValue());
//        }
		System.out.println("getEFTAuthorizationCode - reply:"+reply);
	    return reply;
	}
	
	public static void setEFTAuthorizationCode(String stanCode, long amount) {
		if (EFTStanCodes != null) {
			EFTStanCodes.put(stanCode, amount);
		}
//        for (Map.Entry<String, Long> entry : EFTStanCodes.entrySet()) {
//            System.out.println("setEFTAuthorizationCode - StanCode: " + entry.getKey() + ", Amount: " + entry.getValue());
//        }
	}
	
	public static void OfflineEftSetting(String authcode)
	{
		System.out.println("OfflineEftSetting - authcode:"+authcode);
		
		// non sapendo se la stampante l'ha già recepito almeno una volta nella vita, io devo per forza mandarlo una volta al giorno
		
        int cmdInt = 0;
        int[] mydata = {0};
        String cmd = SharedPrinterFields.KEY_PRG;
        try {
			fiscalPrinterDriver.directIO(cmdInt, mydata, cmd);
		} catch (JposException e) {
			System.out.println("OfflineEftSetting - Exception : " + e.getMessage());
		}
    	
        try {
        	cmd = "=C233/$1/("+authcode+")";
			fiscalPrinterDriver.directIO(cmdInt, mydata, cmd);
        	
        	cmd = "=C233/$2/("+authcode+")";
			fiscalPrinterDriver.directIO(cmdInt, mydata, cmd);
        	
        	cmd = "=C233/$3/("+authcode+")";
			fiscalPrinterDriver.directIO(cmdInt, mydata, cmd);
		} catch (JposException e) {
			System.out.println("OfflineEftSetting - Exception : " + e.getMessage());
		}
    	
        cmd = SharedPrinterFields.KEY_REG;
        try {
			fiscalPrinterDriver.directIO(cmdInt, mydata, cmd);
		} catch (JposException e) {
			System.out.println("OfflineEftSetting - Exception : " + e.getMessage());
		}
	}
	
	public static void OfflineEftHandling(long amount, String authcode)
	{
		System.out.println("OfflineEftHandling - amount:"+amount);
		System.out.println("OfflineEftHandling - authcode:"+authcode);
		
    	String[] printerdate = new String[1];
    	try {
			fiscalPrinterDriver.getDate(printerdate);
		} catch (JposException e) {
			System.out.println("OfflineEftHandling - Exception:"+e.getMessage());
			return;
		}
    	
		// il campo authcode può assumere qualsiasi valore visto che in modalita' online potrebbe essere diverso per ogni transazione
		// infatti la programmazione tramite il comando =C233 è utile per chi usa il tastierino e non un SW di cassa
		
		String date = printerdate[0].substring(0, 4)+printerdate[0].substring(6, 8);
		String time = printerdate[0].substring(8)+"00";
		
		try {
			int[] data = new int[] {0};
		  	if (!authcode.equalsIgnoreCase(EFTAuthorizationCode))
		  		authcode = "STAN"+authcode;
			String transcode = (authcode.length() <= 12 ? authcode : authcode.substring(0, 12));
			String offlineEftPayment = "=\"/$50/&"+date+"/["+time+"/("+transcode+")";
			System.out.println("OfflineEftHandling - offlineEftPayment:"+offlineEftPayment);
			fiscalPrinterDriver.directIO(0, data, offlineEftPayment);
		} catch(JposException je){ 
			System.out.println("OfflineEftHandling - Exception:"+je.getMessage());
		}
	}

}
