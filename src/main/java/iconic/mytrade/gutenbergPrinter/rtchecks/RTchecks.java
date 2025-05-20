package iconic.mytrade.gutenbergPrinter.rtchecks;

import iconic.mytrade.gutenberg.jpos.printer.service.properties.Lotteria;
import iconic.mytrade.gutenberg.jpos.printer.service.properties.SRTPrinterExtension;
import jpos.FiscalPrinterControl17;
import jpos.JposException;

public class RTchecks {

    static int SI = 89;
    static int NO = 78;
    static int ERR = 99;
    
	public static boolean checkSRT(FiscalPrinterControl17 fp)
	{
		int reply = NO;
		
		reply = getStatus(fp);
		
		if (reply != ERR) {
			return(updateProperties(reply == SI));
		}
		return false;
	}
	
	private static int getStatus(FiscalPrinterControl17 fp) {
        int[] dt={0};
        String[] arr = {"0"};
        
		int reply = NO;
		
        try {
			fp.directIO(5001, dt, arr);
			int ret = dt[0];
			log("getStatus - ret = "+ret);
			if (((ret & 0x04) >> 2) == 1)
				reply = SI;
			else
				reply = NO;
		} catch (JposException e) {
			reply = ERR;
			log("getStatus exception = "+e.getMessage());
		}
		return reply;
	}
	
	public static boolean checkLottery(boolean lotteryFW)
	{
		return(updateLotteryProperties(lotteryFW));
	}
	
    private static boolean updateProperties(boolean propValue) {
        boolean updated = false;
        
        if (propValue != SRTPrinterExtension.isPRT()) {
        	SRTPrinterExtension.setPRT(propValue);
        	updated = true;
            log("updateProperties - updated " + "isPRT" + "=" + propValue);
        }
        
        return updated;
    }
    
    private static boolean updateLotteryProperties(boolean lotteryFW) {
		try {
			boolean updated = false;
			boolean isEnable = Lotteria.isEnable();
			if ( lotteryFW && !isEnable )
			{
				Lotteria.setEnable(lotteryFW);
				updated = true;
                log("updateLotteryProperties - updated " + "Enable" + "=" + String.valueOf(lotteryFW));
			}
			else if ( !lotteryFW && isEnable )
				log("updateLotteryProperties - (Enable) La risposta false e' inaffidabile, perfavore verificare manualmente se e' veramente false.");
			
			boolean isPrintBarcode = Lotteria.isPrintBarcode();
			if ( lotteryFW && !isPrintBarcode )
			{
				Lotteria.setPrintBarcode(lotteryFW);
				updated = true;
                log("updateLotteryProperties - updated " + "PrintBarcode" + "=" + String.valueOf(lotteryFW));
			}
			else if ( !lotteryFW && isPrintBarcode )
				log("updateLotteryProperties - (PrintBarcode) La risposta false e' inaffidabile, perfavore verificare manualmente se e' veramente false.");
			
			return updated;
		}catch (Exception e) {
            log("updateLotteryProperties exception = " + e.getMessage());
		}
		
		return false;
	}
    
	private static void log(String s)
	{
		System.out.println("RTchecks - "+s);
	}
	
}
