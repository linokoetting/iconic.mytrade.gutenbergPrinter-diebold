package iconic.mytrade.gutenbergPrinter.tax;

import iconic.mytrade.gutenberg.jpos.printer.utils.Sprint;
import iconic.mytrade.gutenbergPrinter.PrinterCommands;
import iconic.mytrade.gutenbergPrinter.SharedPrinterFields;
import iconic.mytrade.gutenbergPrinter.ateco.AtecoCommands;
import jpos.JposException;

public class VatCommands extends PrinterCommands {
	
	private boolean IvaVentilata = false;
	
	public void resetVATtable(){
		return;
	}
	
	private void SetVatValue(String taxnumber, String vatrate, int atecoid) throws JposException {
		if (fiscalPrinterDriver.isfwRT2enabled()) {
			SetVatAtecoValue(taxnumber, vatrate, atecoid);
			return;
		}
		
		System.out.println("EPSON - setVatValue("+Integer.parseInt(taxnumber)+","+vatrate+")");
		fiscalPrinterDriver.setVatValue(Integer.parseInt(taxnumber), vatrate);
	}
	
	private void SetVatTable() throws JposException {
		if (fiscalPrinterDriver.isfwRT2enabled()) {
			return;
		}
		
		fiscalPrinterDriver.setVatTable();
	}
	
	private void SetVatAtecoValue(String taxnumber, String vatrate, int atecoid) throws JposException {
		if (fiscalPrinterDriver.isfwRT2disabled())
			return;

		String vatateco = "0";
		if (AtecoCommands.isMultiAttivita())
			vatateco = Sprint.f("%06d", Integer.parseInt(AtecoCommands.getAtecoCode(atecoid)));
		String vatvalue = vatrate;
		String vattype = "0";						// aliquota
		if (Integer.parseInt(taxnumber) >= 15) {
			vattype = "2";							// ventilazione
			IvaVentilata = true;
		}
		else if (Integer.parseInt(vatrate) == 0) {
			vattype = "1";							// natura
			
			// rimappo secondo la tabella Epson
			if (Integer.parseInt(taxnumber) == 10)
				vatvalue = "1";
			else if (Integer.parseInt(taxnumber) == 11)
				vatvalue = "2";
			else if (Integer.parseInt(taxnumber) == 12)
				vatvalue = "3";
			else if (Integer.parseInt(taxnumber) == 13)
				vatvalue = "5";
			else if (Integer.parseInt(taxnumber) == 14)
				vatvalue = "6";
			else if (Integer.parseInt(taxnumber) == 0)
				vatvalue = "4";
		}
		
		StringBuffer command = new StringBuffer(taxnumber+","+vattype+","+vatvalue+","+vatateco);
		System.out.println("SetVatAtecoValue - command = "+command);
		fiscalPrinterDriver.executeRTDirectIo(5300, 0, command);
	}

	public boolean setVATtable(){
		int MaxVatRates = 9;
		if (fiscalPrinterDriver.isfwRT2disabled())	// con fw RT2 seguiamo il modello Epson anche se più restrittivo del fw RT2 Rch
			MaxVatRates = 7;
		
		int taxinput[] = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		int taxoutput[] = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		boolean esito = true;
		
		DicoTaxLoad.DicoTaxLoadInit ( );
		
		if ( DicoTaxLoad.isIvaAllaPrinter() ){
			
			try {
				if (fiscalPrinterDriver.getDayOpened())
				{
	    	      	System.out.println("setVATtable - isDailyOpen() = "+fiscalPrinterDriver.getDayOpened());
					return true;
				}
			} catch (JposException e) {
				System.out.println("setVATtable - getDayOpened error: "+e.getMessage());
			}
			
			try {
				if (!(fiscalPrinterDriver.getCapHasVatTable() && fiscalPrinterDriver.getCapSetVatTable())){
					System.out.println("setVATtable - getCapHasVatTable: "+fiscalPrinterDriver.getCapHasVatTable());
					System.out.println("setVATtable - getCapSetVatTable: "+fiscalPrinterDriver.getCapSetVatTable());
					return true;
				}
			} catch (JposException e) {
				System.out.println("setVATtable - error: "+e.getMessage());
				return true;
			}
			
			int NumVatRates = MaxVatRates;
			try {
				NumVatRates = fiscalPrinterDriver.getNumVatRates();
				System.out.println("setVATtable - getNumVatRates: "+NumVatRates);
			} catch (JposException e) {
				System.out.println("setVATtable - getNumVatRates error: "+e.getMessage());
			}
			if (NumVatRates > MaxVatRates)
				NumVatRates = MaxVatRates;

			int myVATRate[] = {0};
			for(int i = 1; i <= NumVatRates; i++) {
				try {
					fiscalPrinterDriver.getVatEntry(i, 0, myVATRate) ;
					System.out.println("setVATtable - getVatEntry VAT ID "+ i + ": " + myVATRate[0]);
				} catch (JposException e) {
					System.out.println("setVATtable - getVatEntry error: "+e.getMessage());
				}
			}

			int quellodellaprinter = 1;
			while (true) {
				
				DicoTaxObject oo = DicoTaxToPrinter.next();
				if ( oo == null ){
					break;
				}
				
				try {
					if ((!TaxData.isOmaggio(oo.getTaxnumber())) && (!TaxData.isAcconti(oo.getTaxnumber())) && (!TaxData.isBuonoMonouso(oo.getTaxnumber())))
					{
				        String VATRate = Sprint.f("%04d", oo.getTaxrate()*100);
				        
						//String salesType = Sprint.f("%02d", TaxData.isServizi(oo.getTaxnumber()) ? 1 : 0);
						String salesType = (TaxData.isServizi(oo.getTaxnumber()) ? "1" : "0");
						String salesAttribute = "00";
						String atecoIndex = (AtecoCommands.isMultiAttivita() ? Sprint.f("%02d", oo.getAtecoId()) : "00");
						String VATGroup = "";
						String description = "";
						String department = "";
						String price1 = "000000000";
						String price2 = "000000000";
						String price3 = "000000000";
						String singleItem = "0";
						String priceLimit = "999999999";
						String printGroup = "00";
						String superGruppoMerceologico = "00";
						//String fatturaUnitMeasure = " ";
						String fatturaUnitMeasure = "KG";
			            String DEPARTMENTVATGRP = "DEPARTMENT VAT GRP";
						
				        if ((Integer.parseInt(VATRate) > 0) && ((quellodellaprinter <= NumVatRates) || (TaxData.isServizi(oo.getTaxnumber())))){
							System.out.println("setVATtable - setVatValue idx="+quellodellaprinter+" VATRate="+VATRate+" getPrinterTaxnumber="+oo.getPrinterTaxnumber());
				            taxinput[Integer.parseInt(oo.getPrinterTaxnumber())] = Integer.parseInt(VATRate);
							System.out.println("setVATtable - taxinput["+Integer.parseInt(oo.getPrinterTaxnumber())+"] = "+taxinput[Integer.parseInt(oo.getPrinterTaxnumber())]);
							SetVatValue(oo.getPrinterTaxnumber(), VATRate, oo.getAtecoId());
							
							VATGroup = Sprint.f("%02d", Integer.parseInt(oo.getPrinterTaxnumber()));
				            description = DEPARTMENTVATGRP+VATGroup; // Must be exactly 20 characters
							department = Sprint.f("%02d", oo.getPrinterDeptnumber());
					        
							System.out.println("setVATtable - VATGroup: " + VATGroup+" - department: "+department+" - description: <"+description+"> - salesType: " + salesType+" - salesAttribute: "+salesAttribute+" - atecoIndex: <"+atecoIndex+">");
							SetDepartment(description,
										  VATGroup,
										  department,
										  price1,
										  price2,
										  price3,
										  singleItem,
										  priceLimit,
										  printGroup,
										  superGruppoMerceologico,
										  fatturaUnitMeasure,
										  salesType,
										  salesAttribute,
										  atecoIndex);
							quellodellaprinter++;
				        }
				        else if (Integer.parseInt(VATRate) == 0) {
							System.out.println("setVATtable - setVatValue idx="+quellodellaprinter+" VATRate="+VATRate+" getPrinterTaxnumber="+oo.getPrinterTaxnumber());
							if (fiscalPrinterDriver.isfwRT2enabled()) {
								SetVatValue(oo.getPrinterTaxnumber(), VATRate, oo.getAtecoId());
							}
							
							VATGroup = Sprint.f("%02d", Integer.parseInt(oo.getPrinterTaxnumber()));
							if (Integer.parseInt(oo.getPrinterTaxnumber()) == SharedPrinterFields.VAT_N4_Index)
								VATGroup = Sprint.f("%02d", Integer.parseInt(SharedPrinterFields.VAT_N4_Dept));
				            description = DEPARTMENTVATGRP+VATGroup; // Must be exactly 20 characters
							department = Sprint.f("%02d", oo.getPrinterDeptnumber());
							if (oo.getPrinterDeptnumber() == SharedPrinterFields.VAT_N4_Index)
								department = Sprint.f("%02d", Integer.parseInt(SharedPrinterFields.VAT_N4_Dept));
							if (Integer.parseInt(oo.getPrinterTaxnumber()) == SharedPrinterFields.VAT_N4_Index)
								VATGroup = Sprint.f("%02d", ""+SharedPrinterFields.VAT_N4_Index);
					        
							System.out.println("setVATtable - VATGroup: " + VATGroup+" - department: "+department+" - description: <"+description+"> - salesType: " + salesType+" - salesAttribute: "+salesAttribute+" - atecoIndex: <"+atecoIndex+">");
							SetDepartment(description,
										  VATGroup,
										  department,
										  price1,
										  price2,
										  price3,
										  singleItem,
										  priceLimit,
										  printGroup,
										  superGruppoMerceologico,
										  fatturaUnitMeasure,
										  salesType,
										  salesAttribute,
										  atecoIndex);
				        }
				        else {
				        	if (isRT2On())
				        		System.out.println("setVATtable - WARNING scartato per ERRORE - getTaxnumber:"+oo.getTaxnumber()+" - type="+oo.getType()+" - getPrinterTaxnumber:"+oo.getPrinterTaxnumber()+" - getPrinterDeptnumber:"+oo.getPrinterDeptnumber());
				        }
					}
					else
					{
						System.out.println("setVATtable - scartato - getTaxnumber:"+oo.getTaxnumber()+" - type="+oo.getType()+" - getPrinterTaxnumber:"+oo.getPrinterTaxnumber()+" - getPrinterDeptnumber:"+oo.getPrinterDeptnumber());
					}
				} catch (JposException e) {
					System.out.println("setVATtable - setVatValue error: "+e.getMessage());
				}
				
				DicoTaxToPrinter.setTaxPrinterCode (Integer.parseInt(oo.getPrinterTaxnumber()));
				
			}
			
			DicoTaxToPrinter.setTaxConverted();
			
			try {
				SetVatTable();
			} catch (JposException e) {
				System.out.println("setVATtable - setVatTable error: "+e.getMessage());
			}
			
			for(int i = 1; i <= NumVatRates; i++) {
				try {
					fiscalPrinterDriver.getVatEntry(i, 0, myVATRate) ;
					System.out.println("setVATtable - getVatEntry VAT ID "+ i + ": " + myVATRate[0]);
		            taxoutput[i] = myVATRate[0];
				} catch (JposException e) {
					System.out.println("setVATtable - getVatEntry error: "+e.getMessage());
				}
			}
			if (fiscalPrinterDriver.isfwRT2enabled())
				System.out.println("setVATtable - IvaVentilata: "+IvaVentilata);
			else {
				System.out.println("setVATtable - IvaVentilata: "+"NON SIGNIFICATIVO");
				System.out.println("setVATtable - IvaVentilata: "+"CONTROLLARE LA VENTILAZIONE DELLE ALIQUOTE DIRETTAMENTE SULLA STAMPANTE");
			}
		}
		System.out.println("setVATtable - esito = "+esito);
		return esito;
	}
	
	private void SetDepartment(String desc, String VATGroup, String dept, String price1, String price2,
			   String price3, String singleItem, String priceLimit, String printGroup, 
			   String superGruppoMerceologico, String fatturaUnitMeasure,
			   String salesType, String salesAttribute, String atecoIndex) throws JposException {
		
		StringBuffer sbcmd = new StringBuffer("");
		
		String type = "";
		if (fiscalPrinterDriver.isfwRT2enabled()) {
			type = ","+Integer.parseInt(salesType);
		}
		System.out.println("SetDepartment - command = executeRTDirectIo(5200,"+Integer.parseInt(dept)+",'"+new StringBuffer(""+Integer.parseInt(VATGroup)+type).toString()+"')");
		fiscalPrinterDriver.executeRTDirectIo(5200, Integer.parseInt(dept), new StringBuffer(""+Integer.parseInt(VATGroup)+type));
	}
	
}
