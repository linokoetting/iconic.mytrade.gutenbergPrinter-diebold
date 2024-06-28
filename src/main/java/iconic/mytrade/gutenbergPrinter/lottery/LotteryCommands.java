package iconic.mytrade.gutenbergPrinter.lottery;

import iconic.mytrade.gutenberg.jpos.printer.service.properties.LotteriaInstant;
import iconic.mytrade.gutenberg.jpos.printer.service.properties.SRTPrinterExtension;
import iconic.mytrade.gutenberg.jpos.printer.utils.Sprint;
import iconic.mytrade.gutenbergPrinter.PrinterCommands;
import iconic.mytrade.gutenbergPrinter.SharedPrinterFields;

public class LotteryCommands extends PrinterCommands {
	
	public void setILotteryProperties() {
		if (fiscalPrinterDriver.isfwILotteryenabled()) {
			if (LotteriaInstant.getDate() != null && LotteriaInstant.getDate().isEmpty() == false) {
				if (!GetILotteryDate().equalsIgnoreCase(LotteriaInstant.getDate())) {
					SetILotteryDate(LotteriaInstant.getDate());
					System.out.println("setILotteryProperties - "+GetILotteryDate());
				}
			}
			if 
			(LotteriaInstant.getSize() != null && LotteriaInstant.getSize().isEmpty() == false) {
				if (GetILotteryQRCodeSize() != Integer.parseInt(LotteriaInstant.getSize())) {
					SetILotteryQRCodeSize(Integer.parseInt(LotteriaInstant.getSize()));
					System.out.println("setILotteryProperties - "+GetILotteryQRCodeSize());
				}
			}
		}
	}

	public static void SendLotteryCode(String IdLotteryCode) {
		SharedPrinterFields.Lotteria.LotteryTrace("SendLotteryCode = "+IdLotteryCode+" - length = "+IdLotteryCode.length());
		
		if (!SRTPrinterExtension.isPRT())
			return;
		
		if (!SharedPrinterFields.Lotteria.checkLotteryCode(IdLotteryCode))
			return;
		
		if (SharedPrinterFields.Lotteria.isLotteryOn())
		{
			int enable = (SharedPrinterFields.Lotteria.checkLotteryCode(IdLotteryCode) ? 1 : 0);
			StringBuffer command = new StringBuffer(IdLotteryCode);
			SharedPrinterFields.Lotteria.LotteryTrace("SendLotteryCode - command : <"+command.toString()+">");
	      	fiscalPrinterDriver.executeRTDirectIo(3000, enable, command);
			SharedPrinterFields.Lotteria.LotteryTrace("SendLotteryCode - result : "+command.toString());
			return;
		}
	}
	
	private String GetILotteryDate()
	{
		String result = "";
		
		if (!SRTPrinterExtension.isPRT())
			return result;
		
		if (SharedPrinterFields.Lotteria.isLotteryOn())
		{
			//result = getAllRTSettings("C495");
			String command = "C495";
			result = fiscalPrinterDriver.getRTSettings(command).substring(command.length(), command.length()+6);
		}
		
		SharedPrinterFields.Lotteria.LotteryTrace("GetILotteryDate - result : "+result);
		return result;
	}

	private void SetILotteryDate(int dd, int mm, int yy)
	{
		if (!SRTPrinterExtension.isPRT())
			return;
		
		String DD = Sprint.f("%02d", dd);
		String MM = Sprint.f("%02d", mm);
		String YY = Sprint.f("%02d", yy);
		
		if (SharedPrinterFields.Lotteria.isLotteryOn())
		{
			StringBuffer key = new StringBuffer(SharedPrinterFields.KEY_SRV);
			fiscalPrinterDriver.executeRTDirectIo(0, 0, key);
	
			StringBuffer command = new StringBuffer("=C495/$"+DD+MM+YY);
			fiscalPrinterDriver.executeRTDirectIo(0, 0, command);
	
			key = new StringBuffer(SharedPrinterFields.KEY_REG);
			fiscalPrinterDriver.executeRTDirectIo(0, 0, key);
		}
		
	}

	private void SetILotteryDate(String date)
	{
		if (!SRTPrinterExtension.isPRT())
			return;
		
		if (SharedPrinterFields.Lotteria.isLotteryOn())
		{
			StringBuffer key = new StringBuffer(SharedPrinterFields.KEY_SRV);
			fiscalPrinterDriver.executeRTDirectIo(0, 0, key);
	
			StringBuffer command = new StringBuffer("=C495/$"+date);
			fiscalPrinterDriver.executeRTDirectIo(0, 0, command);
	
			key = new StringBuffer(SharedPrinterFields.KEY_REG);
			fiscalPrinterDriver.executeRTDirectIo(0, 0, key);
		}
		
	}

	private int GetILotteryQRCodeSize()
	{
		int result = 0;
		
		if (!SRTPrinterExtension.isPRT())
			return result;
		
		if (SharedPrinterFields.Lotteria.isLotteryOn())
		{
			
		}
		
		return result;
	}

	private void SetILotteryQRCodeSize(int size)
	{
		if (!SRTPrinterExtension.isPRT())
			return;
		
		String QRCodeSize = Sprint.f("%03d", size);
		
		if (SharedPrinterFields.Lotteria.isLotteryOn())
		{
			
		}
	}

	private void ForcedILotteryCodesUpdate()
	{
		if (!SRTPrinterExtension.isPRT())
			return;
		
		if (SharedPrinterFields.Lotteria.isLotteryOn())
		{
			StringBuffer key = new StringBuffer(SharedPrinterFields.KEY_Z);
			fiscalPrinterDriver.executeRTDirectIo(0, 0, key);
	
			StringBuffer command = new StringBuffer("=C490");
			fiscalPrinterDriver.executeRTDirectIo(0, 0, command);
	
			key = new StringBuffer(SharedPrinterFields.KEY_REG);
			fiscalPrinterDriver.executeRTDirectIo(0, 0, key);
		}
		
	}
	
}
