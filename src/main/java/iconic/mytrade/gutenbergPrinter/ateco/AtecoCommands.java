package iconic.mytrade.gutenbergPrinter.ateco;

import java.util.HashMap;

import iconic.mytrade.gutenberg.jpos.printer.service.Extra;
import iconic.mytrade.gutenberg.jpos.printer.service.PosApp;
import iconic.mytrade.gutenberg.jpos.printer.service.tax.AtecoInfo;
import iconic.mytrade.gutenberg.jpos.printer.utils.Sprint;
import iconic.mytrade.gutenbergPrinter.PrinterCommands;
import iconic.mytrade.gutenbergPrinter.SharedPrinterFields;
import iconic.mytrade.gutenbergPrinter.tax.DicoTaxLoad;

public class AtecoCommands extends PrinterCommands {

    private static int MultiAttivita = 0;
    
	public static boolean isMultiAttivita() {
		System.out.println("isMultiAttivita - MultiAttivita = "+MultiAttivita);
    	return (MultiAttivita > 1);
    }
    
	public void resetATECOtable(int TodayFiscalTicket){
		if (fiscalPrinterDriver.isfwRT2disabled())
			return;

		DicoTaxLoad.setIvaAllaPrinter();
		
		if ( DicoTaxLoad.isIvaAllaPrinter() ){
			
			if (TodayFiscalTicket == 1) {
				StringBuffer key = new StringBuffer(SharedPrinterFields.KEY_SRV);
				fiscalPrinterDriver.executeRTDirectIo(0, 0, key);

				StringBuffer sbcmd = new StringBuffer(">C913");
				fiscalPrinterDriver.executeRTDirectIo(0, 0, sbcmd);

				key = new StringBuffer(SharedPrinterFields.KEY_REG);
				fiscalPrinterDriver.executeRTDirectIo(0, 0, key);
			}
			
		}
	}

	public static void setATECOtable(int... args){
		if (fiscalPrinterDriver.isfwRT2disabled())
			return;

		DicoTaxLoad.setIvaAllaPrinter();
		
		if ( DicoTaxLoad.isIvaAllaPrinter() ){
			
			try {
				System.out.println("RT2 - setATECOtable- cerco l'ateco code nella tabella atecoinfo");
				
				AtecoInfo[] atecoInfo = AtecoInfoLookup(SharedPrinterFields.atecoInfoMap);
				
				if ((atecoInfo == null) || (atecoInfo.length == 0))
				{
					System.out.println("RT2 - setATECOtable - tabella atecoInfo vuota");
					
					String[] atecoInfoDefault = null;
					atecoInfoDefault = DefaultAtecoCode();
					if ( atecoInfoDefault != null )
					{
						MultiAttivita = atecoInfoDefault.length;
						
						for ( int i = 0 ; i < atecoInfoDefault.length ; i ++ )
						{
							String atecocode = atecoInfoDefault[i];
							while (atecocode.length() < 6)
								atecocode = atecocode+"0";
							int ventilazione = 0;
							int printvi = 0;
							if (args.length > 0) {
								ventilazione = args[0];
								printvi = args[1];
							}
							setAtecoCode(i, atecocode, ventilazione, printvi);
						}
					}
					return;
				}
				
				if ( atecoInfo != null )
				{		
					MultiAttivita = atecoInfo.length;
					
					for ( int i = 0 ; i < atecoInfo.length ; i ++ )
					{
						int number = atecoInfo[i].getAtecoNr();
						int code = atecoInfo[i].getAtecoCode();
						String atecocode = ""+code;
						while (atecocode.length() < 6)
							atecocode = atecocode+"0";
						int ventilazione = atecoInfo[i].getVentilazione();
						int printvi = atecoInfo[i].getPrintVi();
						if (args.length > 0) {
							ventilazione = args[0];
							printvi = args[1];
						}
						setAtecoCode(number, atecocode, ventilazione, printvi);
					}
				}
			} catch (Exception e) {
				System.out.println("RT2 - setATECOtable - error: "+e);
			}
		}
	}

	private static AtecoInfo[] AtecoInfoLookup (HashMap<String, AtecoInfo> atecoInfoMap)
	{
		AtecoInfo[] ret = new AtecoInfo[atecoInfoMap.size()];
		
		int i = 0;
	    for (String key : atecoInfoMap.keySet()) {
	    	ret[i++] = atecoInfoMap.get(key);
	    }
	    
	    return ret;
	}
	
	private static AtecoInfo AtecoInfoLookup (HashMap<String, AtecoInfo> atecoInfoMap, int atecoNr)
	{
		AtecoInfo ret = atecoInfoMap.get(""+atecoNr);
		
	    return ret;
	}
	
	private static void setAtecoCode(int index, String code, int ventilazione, int printvi)
	{
		System.out.println("RT2 - setAtecoCode - index = "+index+" - code = "+code+" - ventilazione = "+ventilazione+" - printvi = "+printvi);
		
		String AtecoIndex = Sprint.f("%02d", index);
		String AtecoCode = Sprint.f("%06s", code);
		String Ventilazione = ""+ventilazione;
		String PrintVi = ""+printvi;
		String Spare = "0000000000";
		
		System.out.println("RT2 - setAtecoCode - AtecoIndex = "+AtecoIndex+" - AtecoCode = "+AtecoCode+" - Ventilazione = "+Ventilazione+" - PrintVi = "+PrintVi+" - Spare = "+Spare);
	}
	
	private static String[] DefaultAtecoCode()
	{
		String atecodefault = "471140";
		String[] result = null;
		
	    try {
	    	System.out.println("RT2 - DefaultAtecoCode- cerco l'ateco code nella tabella store");
	    	
			if ((PosApp.getNumFatt() != null) && (PosApp.getNumFatt().length() > 0)) {
				result = new String[1];
				result[0] = PosApp.getNumFatt();
			}
		} catch (Exception e) {
			System.out.println("RT2 - DefaultAtecoCode - error: "+e);
		}
	    
	    if (result == null) {
	    	System.out.println("RT2 - DefaultAtecoCode- cerco l'ateco code nel file ateco.properties");
	    	
	    	result = Extra.getAtecoRT();
		    if (result == null) {
		    	System.out.println("RT2 - DefaultAtecoCode- imposto l'ateco code di default");
		    	
				result = new String[1];
				result[0] = atecodefault;
				DicoTaxLoad.setRT2enabled(false);	// senza tabella Ateco niente cutover
		    }
	    }
	    
		System.out.println("RT2 - DefaultAtecoCode - result.length = "+result.length);
		return (result);
	}
	
	private static String DefaultAtecoCode(int idx)
	{
		String atecodefault = "471140";
		String[] result = null;
		String atecocode = null;
		
	    try {
	    	System.out.println("RT2 - DefaultAtecoCode- cerco l'ateco code nella tabella store");
	    	
			if ((PosApp.getNumFatt() != null) && (PosApp.getNumFatt().length() > 0)) {
				atecocode = PosApp.getNumFatt();
			}
		} catch (Exception e) {
			System.out.println("RT2 - DefaultAtecoCode - error: "+e);
		}
	    
	    if (atecocode == null) {
	    	System.out.println("RT2 - DefaultAtecoCode- cerco l'ateco code nel file ateco.properties");
	    	
	    	result = Extra.getAtecoRT();
		    if (result != null) {
		    	atecocode = result[idx];
		    }
		    else {
		    	System.out.println("RT2 - DefaultAtecoCode- imposto l'ateco code di default");
		    	
		    	atecocode = atecodefault;
		    }
	    }
	    
		System.out.println("RT2 - DefaultAtecoCode - atecocode = "+atecocode);
		return (atecocode);
	}
	
	public static String getAtecoCode(int atecoNr){
		String atecoCode = "";
		AtecoInfo atecoInfo;
		
		try {
			atecoInfo = AtecoInfoLookup(SharedPrinterFields.atecoInfoMap, atecoNr);
			
			if (atecoInfo == null)
			{
				System.out.println("RT2 - getATECOtable - tabella vuota");
				
				atecoCode = DefaultAtecoCode(atecoNr);
			}
			else
			{
				atecoCode = ""+atecoInfo.getAtecoCode();
			}
		} catch (Exception e) {
			System.out.println("RT2 - getATECOtable - error: "+e);
		}
		
		System.out.println("RT2 - getATECOtable - atecoCode : "+atecoCode);
		return(atecoCode);
	}
	
	private static int getAtecoVI(int atecoNr){
		int atecoVI = 0;
		AtecoInfo atecoInfo;
		
		try {
			atecoInfo = AtecoInfoLookup(SharedPrinterFields.atecoInfoMap, atecoNr);
			
			if (atecoInfo == null)
			{
				System.out.println("RT2 - getAtecoVI - tabella vuota");
			}
			else
			{
				atecoVI = atecoInfo.getVentilazione();
			}
		} catch (Exception e) {
			System.out.println("RT2 - getAtecoVI - error: "+e);
		}
		
		System.out.println("RT2 - getAtecoVI - atecoVI : "+atecoVI);
		return(atecoVI);
	}

}
