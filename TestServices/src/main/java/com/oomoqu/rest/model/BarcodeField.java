package com.oomoqu.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "BarcodeApplicationField")
public class BarcodeField {
	private String barcodeApplicationId;
	/**Required fields */
	private String applicationIdentifierValue;
	
	private String gsOneCompanyPrefixVLI;
	private String gsOneCompanyPrefixValue;
	
	private String offerCodeValue;
	
	private String saveValueVLI;
	private String saveValueValue;
	
	private String primaryPurchaseReqVLI;
	private String primaryPurchaseReqValue;
	
	private String primaryPurchaseReqCodeVLI;
	
	private String primaryPurchaseFamilyCodeValue; 
	
	/** OPtional Data Field 1 */
	private String secondQualifyingPurchaseVLI;
	
	private String addIPurchaseRulesCodeVLI;
	
	private String secondPurchaseRequirementVLI;
	private String secondPurchaseRequirementValue;
	
	private String secondPurchaseRequirementCodeVLI;
	
	private String secondPurchaseFamilyCodeValue;
	
	private String secondCompanyPrefixVLI;
	private String secondCompanyPrefixValue;
	
	/** Optional Data Field2 */
	private String thirdQualifyingPurchaseVLI;
	
	private String thirdPurchaseRequirementVLI;
	private String thirdPurchaseRequirementValue;
	
	private String thirdPurchaseRequirementCodeVLI;
	
	private String thirdPurchaseFamilyCode;
	
	private String thirdCompanyPrefixVLI;
	private String thirdCompanyPrefixValue;
	
	/** Optional Data Field3 */
	private String expiryDateVLI;
	private String expiryDateValue;
	
	/** Optional Data Field4 */
	private String startDateVLI;
	private String startDateValue;
	
	/** Optional Data Field5 */
	private String serialNumberLVLI;
	private String serialNumberRVLI;
	private String serialNumberValue;
	
	/** Optional Data Field6 */
	private String retailerCompanyPrefixLVLI;
	private String retailerCompanyPrefixRVLI;
	private String retailerCompanyPrefixValue;
	
	/** Optional Data Field9 */
	private String miscellaneousElementsVLI;
	
	private String saveValueCodeVLI;
	
	private String appliesToWhichItemVLI;
	
	private String storeCouponVLI;
	
	private String dontMultiplyFlagVLI;
	
	/** Interim UPC Data(Optional) */
	private String interimDataValue;

	public BarcodeField(String barcodeString) {
		barcodeString = barcodeString.replaceAll("[\\[\\](){}]","");
		decomposeStringToBarcodeApplicationfield(barcodeString);
	}
	public String composeBarcodeApplicationfieldToString(BarcodeField baf, String serialNumber){
		StringBuilder sb = new StringBuilder();
		sb.append(baf.getApplicationIdentifierValue().toString() + 
				String.valueOf(baf.getGsOneCompanyPrefixVLI()) + 
				baf.getGsOneCompanyPrefixValue().toString() +
				baf.getOfferCodeValue().toString() +
				baf.getSaveValueVLI().toString() +
				baf.getSaveValueValue().toString() +
				baf.getPrimaryPurchaseReqVLI().toString() +
				baf.getPrimaryPurchaseReqValue().toString() +
				baf.getPrimaryPurchaseReqCodeVLI().toString() +
				baf.getPrimaryPurchaseFamilyCodeValue().toString()
				);
		if(baf.getSecondQualifyingPurchaseVLI() != null){
			sb.append(baf.getSecondQualifyingPurchaseVLI().toString() +
					baf.getAddIPurchaseRulesCodeVLI().toString() +
					baf.getSecondPurchaseRequirementVLI().toString() +
					baf.getSecondPurchaseRequirementValue().toString() +
					baf.getSecondPurchaseRequirementCodeVLI().toString() +
					baf.getSecondPurchaseFamilyCodeValue().toString() +
					baf.getSecondCompanyPrefixVLI().toString() +
					baf.getSecondCompanyPrefixValue().toString()
					);
		}
		if(baf.getThirdQualifyingPurchaseVLI() != null){
			sb.append(baf.getThirdQualifyingPurchaseVLI().toString() +
					baf.getThirdPurchaseRequirementVLI().toString() +
					baf.getThirdPurchaseRequirementValue().toString() +
					baf.getThirdPurchaseRequirementCodeVLI().toString() +
					baf.getThirdPurchaseFamilyCode().toString() +
					baf.getThirdCompanyPrefixVLI().toString() +
					baf.getThirdCompanyPrefixValue().toString()
					);
		}
		if(baf.getExpiryDateVLI() != null){
			sb.append(baf.getExpiryDateVLI().toString() + baf.getExpiryDateValue().toString());
		}
		if(baf.getStartDateVLI() != null){
			sb.append(baf.getStartDateVLI().toString() + baf.getStartDateValue().toString());
		}
		//if(baf.getSerialNumberLVLI() != null){
		int len = serialNumber.length();
		if(len != 0){
		baf.setSerialNumberLVLI("5");
	    switch (len) {
	            case 6:  baf.setSerialNumberRVLI("0");
	                     break;
	            case 7:  baf.setSerialNumberRVLI("1");
	                     break;
	            case 8:  baf.setSerialNumberRVLI("2");
	                     break;
	            case 9:  baf.setSerialNumberRVLI("3");
	                     break;
	            case 10: baf.setSerialNumberRVLI("4");
	                     break;
	            case 11: baf.setSerialNumberRVLI("5");
	                     break;
	            case 12: baf.setSerialNumberRVLI("6");
	                     break;
	            case 13: baf.setSerialNumberRVLI("7");
                		 break;
	            case 14: baf.setSerialNumberRVLI("8");
                		 break;
			    case 15: baf.setSerialNumberRVLI("9");
			             break;
			    case 16: baf.setSerialNumberRVLI("10");
			             break;
			    case 17: baf.setSerialNumberRVLI("11");
			             break;
			    case 18: baf.setSerialNumberRVLI("12");
			             break;
			    case 19: baf.setSerialNumberRVLI("13");
			             break;
			    case 20: baf.setSerialNumberRVLI("14");
			             break;
			    case 21: baf.setSerialNumberRVLI("15");
			       		 break;
			    case 22: baf.setSerialNumberRVLI("16");
	       		 		 break;
			    case 23: baf.setSerialNumberRVLI("17");
	       		 		 break;
			    case 24: baf.setSerialNumberRVLI("18");
	       		 		 break;
			    case 25: baf.setSerialNumberRVLI("19");
	       		 		 break;
			    case 26: baf.setSerialNumberRVLI("20");
	       		 		 break;
			    case 27: baf.setSerialNumberRVLI("21");
	       		 		 break;
			    case 28: baf.setSerialNumberRVLI("22");
	       		 		 break;
			    case 29: baf.setSerialNumberRVLI("23");
	       		 		 break;
			    case 30: baf.setSerialNumberRVLI("24");
	       		 		 break;
	        }
			baf.setSerialNumberValue(serialNumber);
			sb.append(baf.getSerialNumberLVLI().toString() + baf.getSerialNumberRVLI().toString() + serialNumber.toString());
		}
		if(baf.getRetailerCompanyPrefixLVLI() != null){
			sb.append(baf.getRetailerCompanyPrefixLVLI().toString() + baf.getRetailerCompanyPrefixRVLI().toString() + baf.getRetailerCompanyPrefixValue().toString());
		}
		if(baf.getMiscellaneousElementsVLI() != null){
			sb.append(baf.getMiscellaneousElementsVLI().toString() + baf.getSaveValueCodeVLI().toString() + baf.getAppliesToWhichItemVLI().toString() + baf.getStoreCouponVLI().toString() + baf.getDontMultiplyFlagVLI().toString());
		}
		return sb.toString();
	}
	
	// decompose method
	private BarcodeField decomposeStringToBarcodeApplicationfield(String barcodeStringValue){
		

		String sSTR = new String(barcodeStringValue);
		/** initialize cursor, start, end variable value */
		int valueIndicator = 4;
		int start=5,end=0;

		this.applicationIdentifierValue="8110";
		
		switch(sSTR.charAt(valueIndicator)){
				case '0':
					end=start+6;
					this.gsOneCompanyPrefixVLI="0";
					this.gsOneCompanyPrefixValue=sSTR.substring(start,end);
					valueIndicator=end;
					break;
				case '1':
					end=start+7;
					this.gsOneCompanyPrefixVLI="1";
					this.gsOneCompanyPrefixValue=sSTR.substring(start,end);
					valueIndicator=end;
					break;
				case '2':
					end=start+8;
					this.gsOneCompanyPrefixVLI="2";
					this.gsOneCompanyPrefixValue=sSTR.substring(start,end);
					valueIndicator=end;
					break;
				case '3':
					end=start+9;
					this.gsOneCompanyPrefixVLI="3";
					this.gsOneCompanyPrefixValue=sSTR.substring(start,end);
					valueIndicator=end;
					break;
				case '4':
					end=start+10;
					this.gsOneCompanyPrefixVLI="4";	
					this.gsOneCompanyPrefixValue=sSTR.substring(start,end);
					valueIndicator=end;
					break;
				case '5':
					end=start+11;
					this.gsOneCompanyPrefixVLI="5";	
					this.gsOneCompanyPrefixValue=sSTR.substring(start,end);
					valueIndicator=end;
					break;
				case '6':
					end=start+12;
					this.gsOneCompanyPrefixVLI="6";	
					this.gsOneCompanyPrefixValue=sSTR.substring(start,end);
					valueIndicator=end;
					break;
			
		}
		
		/** offer code */
		start=end;end=end+6;
		this.offerCodeValue=sSTR.substring(start,end);
		valueIndicator=end;
		
		/** save value */
		switch(sSTR.charAt(valueIndicator)){
			case '1' :
				start=end+1;end=start+1;
				this.saveValueVLI="1";
				this.saveValueValue=sSTR.substring(start,end);
				valueIndicator=end;
				break;
			case '2' :
				start=end+1;end=start+2;
				this.saveValueVLI="2";	
				this.saveValueValue=sSTR.substring(start,end);
				valueIndicator=end;
				break;
			case '3' :
				start=end+1;end=start+3;
				this.saveValueVLI="3";	
				this.saveValueValue=sSTR.substring(start,end);
				valueIndicator=end;
				break;
			case '4' :
				start=end+1;end=start+4;
				this.saveValueVLI="4";	
				this.saveValueValue=sSTR.substring(start,end);
				valueIndicator=end;
				break;
			case '5' :
				start=end+1;end=start+5;
				this.saveValueVLI="5";
				this.saveValueValue=sSTR.substring(start,end);
				valueIndicator=end;
				break;
			}
		
			/** primary purchase value */
			switch(sSTR.charAt(valueIndicator)){
			case '1' :
				start=end+1;end=start+1;
				this.primaryPurchaseReqVLI="1";
				this.primaryPurchaseReqValue=sSTR.substring(start,end);
				valueIndicator=end;
				break;
			case '2' :
				start=end+1;end=start+2;
				this.primaryPurchaseReqVLI="2";
				this.primaryPurchaseReqValue=sSTR.substring(start,end);
				valueIndicator=end;
				break;
			case '3' :
				start=end+1;end=start+3;
				this.primaryPurchaseReqVLI="3";
				this.primaryPurchaseReqValue=sSTR.substring(start,end);
				valueIndicator=end;
				break;
			case '4' :
				start=end+1;end=start+4;
				this.primaryPurchaseReqVLI="4";
				this.primaryPurchaseReqValue=sSTR.substring(start,end);
				valueIndicator=end;
				break;
			case '5' :
				start=end+1;end=start+5;
				this.primaryPurchaseReqVLI="5";
				this.primaryPurchaseReqValue=sSTR.substring(start,end);
				valueIndicator=end;
				break;
			}
			
			/** primary purchase req code */
			String cString = String.valueOf(sSTR.charAt(valueIndicator));
			this.primaryPurchaseReqCodeVLI=cString;
			start = end + 1; end = start+3;
			
			/** primary purchase family code */
			this.primaryPurchaseFamilyCodeValue=sSTR.substring(start, end);
			
			valueIndicator=end;
			
			if(sSTR.length() > valueIndicator){
					if(sSTR.charAt(valueIndicator) == '1') {
								String sQulify = String.valueOf(sSTR.charAt(valueIndicator));
								this.secondQualifyingPurchaseVLI=sQulify;
								valueIndicator=valueIndicator+1;

								String sAddIpurchase = String.valueOf(sSTR.charAt(valueIndicator));
								this.addIPurchaseRulesCodeVLI=sAddIpurchase;
								valueIndicator=valueIndicator+1;
								
								switch(sSTR.charAt(valueIndicator)){
								case '1':
										start = valueIndicator+1; end = start+1;
										this.secondPurchaseRequirementVLI="1";
										this.secondPurchaseRequirementValue=sSTR.substring(start,end);
										valueIndicator=end;
										break;
								case '2':
										start = valueIndicator+1; end = start+2;
										this.secondPurchaseRequirementVLI="2";
										this.secondPurchaseRequirementValue=sSTR.substring(start,end);
										valueIndicator=end;
										break;
								case '3':
										start = valueIndicator+1; end = start+3;
										this.secondPurchaseRequirementVLI="3";
										this.secondPurchaseRequirementValue=sSTR.substring(start,end);
										valueIndicator=end;
										break;
								case '4':
										start = valueIndicator+1; end = start+4;
										this.secondPurchaseRequirementVLI="4";
										this.secondPurchaseRequirementValue=sSTR.substring(start,end);
										valueIndicator=end;
										break;
								case '5':
										start = valueIndicator+1; end = start+5;
										this.secondPurchaseRequirementVLI="5";
										this.secondPurchaseRequirementValue=sSTR.substring(start,end);
										valueIndicator=end;
										break;
								}
								
								String secondPurchaeRCVLI = String.valueOf(sSTR.charAt(valueIndicator));
								this.secondPurchaseRequirementCodeVLI=secondPurchaeRCVLI;
								valueIndicator=end+1;
								
								start = end + 1; end = start+3;
								this.secondPurchaseFamilyCodeValue=sSTR.substring(start, end);
								valueIndicator=end;
								
								
								switch(sSTR.charAt(valueIndicator)){
									case '0':
											start=end+1;end=start+6;
											this.secondCompanyPrefixVLI="0";
											this.secondCompanyPrefixValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '1':
											start=end+1;end=start+7;
											this.secondCompanyPrefixVLI="1";
											this.secondCompanyPrefixValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '2':
											start=end+1;end=start+8;
											this.secondCompanyPrefixVLI="2";
											this.secondCompanyPrefixValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '3':
											start=end+1;end=start+9;
											this.secondCompanyPrefixVLI="3";
											this.secondCompanyPrefixValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '4':
											start=end+1;end=start+10;
											this.secondCompanyPrefixVLI="4";
											this.secondCompanyPrefixValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '5':
											start=end+1;end=start+11;
											this.secondCompanyPrefixVLI="5";
											this.secondCompanyPrefixValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '6':
											start=end+1;end=start+12;
											this.secondCompanyPrefixVLI="6";
											this.secondCompanyPrefixValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
								}
					}
				if(sSTR.length() > valueIndicator){
					if(sSTR.charAt(valueIndicator) == '2') {
								String tQulify = String.valueOf(sSTR.charAt(valueIndicator));
								this.thirdQualifyingPurchaseVLI=tQulify;
								valueIndicator=valueIndicator+1;
								
								switch(sSTR.charAt(valueIndicator)){
									case '1':
											start = valueIndicator+1; end = start+1;
											this.thirdPurchaseRequirementVLI="1";
											this.thirdPurchaseRequirementValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '2':
											start = valueIndicator+1; end = start+2;
											this.thirdPurchaseRequirementVLI="2";
											this.thirdPurchaseRequirementValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '3':
											start = valueIndicator+1; end = start+3;
											this.thirdPurchaseRequirementVLI="3";
											this.thirdPurchaseRequirementValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '4':
											start = valueIndicator+1; end = start+4;
											this.thirdPurchaseRequirementVLI="4";
											this.thirdPurchaseRequirementValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '5':
											start = valueIndicator+1; end = start+5;
											this.thirdPurchaseRequirementVLI="5";
											this.thirdPurchaseRequirementValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
								}
								
								String tPurReqCode = String.valueOf(sSTR.charAt(valueIndicator));
								this.thirdPurchaseRequirementCodeVLI=tPurReqCode;
								valueIndicator=end+1;
								
								start = end+1; end = start+3;
								this.thirdPurchaseFamilyCode=sSTR.substring(start, end);
								valueIndicator=end;
								
								switch(sSTR.charAt(valueIndicator)){
									case '0':
										start=end+1;end=start+6;
										this.thirdCompanyPrefixVLI="0";
										this.thirdCompanyPrefixValue=sSTR.substring(start, end);
										valueIndicator=end;
										break;
									case '1':
										start=end+1;end=start+7;
										this.thirdCompanyPrefixVLI="1";
										this.thirdCompanyPrefixValue=sSTR.substring(start, end);
										valueIndicator=end;
										break;
									case '2':
										start=end+1;end=start+8;
										this.thirdCompanyPrefixVLI="2";
										this.thirdCompanyPrefixValue=sSTR.substring(start, end);
										valueIndicator=end;
										break;
									case '3':
										start=end+1;end=start+9;
										this.thirdCompanyPrefixVLI="3";
										this.thirdCompanyPrefixValue=sSTR.substring(start, end);
										valueIndicator=end;
										break;
									case '4':
										start=end+1;end=start+10;
										this.thirdCompanyPrefixVLI="4";
										this.thirdCompanyPrefixValue=sSTR.substring(start, end);
										valueIndicator=end;
										break;
									case '5':
										start=end+1;end=start+11;
										this.thirdCompanyPrefixVLI="5";
										this.thirdCompanyPrefixValue=sSTR.substring(start, end);
										valueIndicator=end;
										break;
									case '6':
										start=end+1;end=start+12;
										this.thirdCompanyPrefixVLI="6";
										this.thirdCompanyPrefixValue=sSTR.substring(start, end);
										valueIndicator=end;
										break;
								}
								
					}
			}
			if(sSTR.length() > valueIndicator){
				if(sSTR.charAt(valueIndicator) == '3') {
								String expDate = String.valueOf(sSTR.charAt(valueIndicator));
								this.expiryDateVLI=expDate;
								valueIndicator=end+1;
								
								start=end+1; end=start+6;
								this.expiryDateValue=sSTR.substring(start, end);
								valueIndicator=end;
				}
			}
			if(sSTR.length() > valueIndicator){
				if(sSTR.charAt(valueIndicator) == '4') {		// case '4':
								String startDate = String.valueOf(sSTR.charAt(valueIndicator));
								this.startDateVLI=startDate;
								valueIndicator=end+1;
								
								start=end+1; end=start+6;
								this.startDateValue=sSTR.substring(start, end);
								valueIndicator=end;
				}
			}
			if(sSTR.length() > valueIndicator){
				if(sSTR.charAt(valueIndicator) == '5') {
								String serialNumber = String.valueOf(sSTR.charAt(valueIndicator));
								this.serialNumberLVLI=serialNumber;
								valueIndicator=end+1;
								
								switch(sSTR.charAt(valueIndicator)){
									case '0':
											start = end+2; end=start+6;
											this.serialNumberRVLI="0";
											this.serialNumberValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '1':
											start = end+2; end=start+7;
											this.serialNumberRVLI="1";
											this.serialNumberValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '2':
											start = end+2; end=start+8;
											this.serialNumberRVLI="2";
											this.serialNumberValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '3':
											start = end+2; end=start+9;
											this.serialNumberRVLI="3";
											this.serialNumberValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '4':
											start = end+2; end=start+10;
											this.serialNumberRVLI="4";
											this.serialNumberValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '5':
											start = end+2; end=start+11;
											this.serialNumberRVLI="5";
											this.serialNumberValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '6':
											start = end+2; end=start+12;
											this.serialNumberRVLI="6";
											this.serialNumberValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '7':
											start = end+2; end=start+13;
											this.serialNumberRVLI="7";
											this.serialNumberValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '8':
											start = end+2; end=start+14;
											this.serialNumberRVLI="8";
											this.serialNumberValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
									case '9':
											start = end+2; end=start+15;
											this.serialNumberRVLI="9";
											this.serialNumberValue=sSTR.substring(start, end);
											valueIndicator=end;
											break;
								}
				}
			}
			if(sSTR.length() > valueIndicator){
				if(sSTR.charAt(valueIndicator) == '6') {
						String retailerCPrefix = String.valueOf(sSTR.charAt(valueIndicator));
						this.retailerCompanyPrefixLVLI=retailerCPrefix;
						valueIndicator=end+1;
						
						switch(sSTR.charAt(valueIndicator)){
						case '1':
							start=end+2; end=start+7;
							this.retailerCompanyPrefixRVLI="1";
							this.retailerCompanyPrefixValue=sSTR.substring(start, end);
							valueIndicator=end;
							break;
						case '2':
							start=end+2; end=start+8;
							this.retailerCompanyPrefixRVLI="2";
							this.retailerCompanyPrefixValue=sSTR.substring(start, end);
							valueIndicator=end;
							break;
						case '3':
							start=end+2; end=start+9;
							this.retailerCompanyPrefixRVLI="3";
							this.retailerCompanyPrefixValue=sSTR.substring(start, end);
							valueIndicator=end;
							break;
						case '4':
							start=end+2; end=start+10;
							this.retailerCompanyPrefixRVLI="4";
							this.retailerCompanyPrefixValue=sSTR.substring(start, end);
							valueIndicator=end;
							break;
						case '5':
							start=end+2; end=start+11;
							this.retailerCompanyPrefixRVLI="5";
							this.retailerCompanyPrefixValue=sSTR.substring(start, end);
							valueIndicator=end;
							break;	
						case '6':
							start=end+2; end=start+12;
							this.retailerCompanyPrefixRVLI="6";
							this.retailerCompanyPrefixValue=sSTR.substring(start, end);
							valueIndicator=end;
							break;
						case '7':
							start=end+2; end=start+13;
							this.retailerCompanyPrefixRVLI="7";
							this.retailerCompanyPrefixValue=sSTR.substring(start, end);
							valueIndicator=end;
							break;
						}
				}
			}
			if(sSTR.length() > valueIndicator){
				if(sSTR.charAt(valueIndicator) == '9') {
					/** miscellaneous elements */
					String miscellElement = String.valueOf(sSTR.charAt(valueIndicator));
					this.miscellaneousElementsVLI=miscellElement;
					valueIndicator=end+1;
					
					/** save value code */
					String saveValue = String.valueOf(sSTR.charAt(valueIndicator));
					this.saveValueCodeVLI=saveValue;
					valueIndicator=valueIndicator+1;
					
					/** applies to which item */
					String appToWhichItem = String.valueOf(sSTR.charAt(valueIndicator));
					this.appliesToWhichItemVLI=appToWhichItem;
					valueIndicator=valueIndicator+1;
					
					/** store coupon */
					String storeCoupon = String.valueOf(sSTR.charAt(valueIndicator));
					this.storeCouponVLI=storeCoupon;
					valueIndicator=valueIndicator+1;
					
					/** don't multiply flag */
					String dontMultiply = String.valueOf(sSTR.charAt(valueIndicator));
					this.dontMultiplyFlagVLI=dontMultiply;	
					valueIndicator=valueIndicator+1;
				}
			}	
			/** Interim UPC Data(Optional) no decoded */
			} else {
				System.out.println("there is not other fields");
			}
			return this;
	} // end of decompose method
	
	@Id
	public String getBarcodeApplicationId() {
		return barcodeApplicationId;
	}
	public void setBarcodeApplicationId(String barcodeApplicationId) {
		this.barcodeApplicationId = barcodeApplicationId;
	}
	public String getApplicationIdentifierValue() {
		return applicationIdentifierValue;
	}
	public void setApplicationIdentifierValue(String applicationIdentifierValue) {
		this.applicationIdentifierValue = applicationIdentifierValue;
	}
	public String getGsOneCompanyPrefixVLI() {
		return gsOneCompanyPrefixVLI;
	}
	public void setGsOneCompanyPrefixVLI(String gsOneCompanyPrefixVLI) {
		this.gsOneCompanyPrefixVLI = gsOneCompanyPrefixVLI;
	}
	public String getGsOneCompanyPrefixValue() {
		return gsOneCompanyPrefixValue;
	}
	public void setGsOneCompanyPrefixValue(String gsOneCompanyPrefixValue) {
		this.gsOneCompanyPrefixValue = gsOneCompanyPrefixValue;
	}
	public String getOfferCodeValue() {
		return offerCodeValue;
	}
	public void setOfferCodeValue(String offerCodeValue) {
		this.offerCodeValue = offerCodeValue;
	}
	public String getSaveValueVLI() {
		return saveValueVLI;
	}
	public void setSaveValueVLI(String saveValueVLI) {
		this.saveValueVLI = saveValueVLI;
	}
	public String getSaveValueValue() {
		return saveValueValue;
	}
	public void setSaveValueValue(String saveValueValue) {
		this.saveValueValue = saveValueValue;
	}
	public String getPrimaryPurchaseReqVLI() {
		return primaryPurchaseReqVLI;
	}
	public void setPrimaryPurchaseReqVLI(String primaryPurchaseReqVLI) {
		this.primaryPurchaseReqVLI = primaryPurchaseReqVLI;
	}
	public String getPrimaryPurchaseReqValue() {
		return primaryPurchaseReqValue;
	}
	public void setPrimaryPurchaseReqValue(String primaryPurchaseReqValue) {
		this.primaryPurchaseReqValue = primaryPurchaseReqValue;
	}
	public String getPrimaryPurchaseReqCodeVLI() {
		return primaryPurchaseReqCodeVLI;
	}
	public void setPrimaryPurchaseReqCodeVLI(String primaryPurchaseReqCodeVLI) {
		this.primaryPurchaseReqCodeVLI = primaryPurchaseReqCodeVLI;
	}
	public String getPrimaryPurchaseFamilyCodeValue() {
		return primaryPurchaseFamilyCodeValue;
	}
	public void setPrimaryPurchaseFamilyCodeValue(
			String primaryPurchaseFamilyCodeValue) {
		this.primaryPurchaseFamilyCodeValue = primaryPurchaseFamilyCodeValue;
	}
	public String getSecondQualifyingPurchaseVLI() {
		return secondQualifyingPurchaseVLI;
	}
	public void setSecondQualifyingPurchaseVLI(String secondQualifyingPurchaseVLI) {
		this.secondQualifyingPurchaseVLI = secondQualifyingPurchaseVLI;
	}
	public String getAddIPurchaseRulesCodeVLI() {
		return addIPurchaseRulesCodeVLI;
	}
	public void setAddIPurchaseRulesCodeVLI(String addIPurchaseRulesCodeVLI) {
		this.addIPurchaseRulesCodeVLI = addIPurchaseRulesCodeVLI;
	}
	public String getSecondPurchaseRequirementVLI() {
		return secondPurchaseRequirementVLI;
	}
	public void setSecondPurchaseRequirementVLI(String secondPurchaseRequirementVLI) {
		this.secondPurchaseRequirementVLI = secondPurchaseRequirementVLI;
	}
	public String getSecondPurchaseRequirementValue() {
		return secondPurchaseRequirementValue;
	}
	public void setSecondPurchaseRequirementValue(
			String secondPurchaseRequirementValue) {
		this.secondPurchaseRequirementValue = secondPurchaseRequirementValue;
	}
	public String getSecondPurchaseRequirementCodeVLI() {
		return secondPurchaseRequirementCodeVLI;
	}
	public void setSecondPurchaseRequirementCodeVLI(
			String secondPurchaseRequirementCodeVLI) {
		this.secondPurchaseRequirementCodeVLI = secondPurchaseRequirementCodeVLI;
	}
	public String getSecondPurchaseFamilyCodeValue() {
		return secondPurchaseFamilyCodeValue;
	}
	public void setSecondPurchaseFamilyCodeValue(
			String secondPurchaseFamilyCodeValue) {
		this.secondPurchaseFamilyCodeValue = secondPurchaseFamilyCodeValue;
	}
	public String getSecondCompanyPrefixVLI() {
		return secondCompanyPrefixVLI;
	}
	public void setSecondCompanyPrefixVLI(String secondCompanyPrefixVLI) {
		this.secondCompanyPrefixVLI = secondCompanyPrefixVLI;
	}
	public String getSecondCompanyPrefixValue() {
		return secondCompanyPrefixValue;
	}
	public void setSecondCompanyPrefixValue(String secondCompanyPrefixValue) {
		this.secondCompanyPrefixValue = secondCompanyPrefixValue;
	}
	public String getThirdQualifyingPurchaseVLI() {
		return thirdQualifyingPurchaseVLI;
	}
	public void setThirdQualifyingPurchaseVLI(String thirdQualifyingPurchaseVLI) {
		this.thirdQualifyingPurchaseVLI = thirdQualifyingPurchaseVLI;
	}
	public String getThirdPurchaseRequirementVLI() {
		return thirdPurchaseRequirementVLI;
	}
	public void setThirdPurchaseRequirementVLI(String thirdPurchaseRequirementVLI) {
		this.thirdPurchaseRequirementVLI = thirdPurchaseRequirementVLI;
	}
	public String getThirdPurchaseRequirementValue() {
		return thirdPurchaseRequirementValue;
	}
	public void setThirdPurchaseRequirementValue(
			String thirdPurchaseRequirementValue) {
		this.thirdPurchaseRequirementValue = thirdPurchaseRequirementValue;
	}
	public String getThirdPurchaseRequirementCodeVLI() {
		return thirdPurchaseRequirementCodeVLI;
	}
	public void setThirdPurchaseRequirementCodeVLI(
			String thirdPurchaseRequirementCodeVLI) {
		this.thirdPurchaseRequirementCodeVLI = thirdPurchaseRequirementCodeVLI;
	}
	public String getThirdPurchaseFamilyCode() {
		return thirdPurchaseFamilyCode;
	}
	public void setThirdPurchaseFamilyCode(String thirdPurchaseFamilyCode) {
		this.thirdPurchaseFamilyCode = thirdPurchaseFamilyCode;
	}
	public String getThirdCompanyPrefixVLI() {
		return thirdCompanyPrefixVLI;
	}
	public void setThirdCompanyPrefixVLI(String thirdCompanyPrefixVLI) {
		this.thirdCompanyPrefixVLI = thirdCompanyPrefixVLI;
	}
	public String getThirdCompanyPrefixValue() {
		return thirdCompanyPrefixValue;
	}
	public void setThirdCompanyPrefixValue(String thirdCompanyPrefixValue) {
		this.thirdCompanyPrefixValue = thirdCompanyPrefixValue;
	}
	public String getExpiryDateVLI() {
		return expiryDateVLI;
	}
	public void setExpiryDateVLI(String expiryDateVLI) {
		this.expiryDateVLI = expiryDateVLI;
	}
	public String getExpiryDateValue() {
		return expiryDateValue;
	}
	public void setExpiryDateValue(String expiryDateValue) {
		this.expiryDateValue = expiryDateValue;
	}
	public String getStartDateVLI() {
		return startDateVLI;
	}
	public void setStartDateVLI(String startDateVLI) {
		this.startDateVLI = startDateVLI;
	}
	public String getStartDateValue() {
		return startDateValue;
	}
	public void setStartDateValue(String startDateValue) {
		this.startDateValue = startDateValue;
	}
	public String getSerialNumberLVLI() {
		return serialNumberLVLI;
	}
	public void setSerialNumberLVLI(String serialNumberLVLI) {
		this.serialNumberLVLI = serialNumberLVLI;
	}
	public String getSerialNumberRVLI() {
		return serialNumberRVLI;
	}
	public void setSerialNumberRVLI(String serialNumberRVLI) {
		this.serialNumberRVLI = serialNumberRVLI;
	}
	public String getSerialNumberValue() {
		return serialNumberValue;
	}
	public void setSerialNumberValue(String serialNumberValue) {
		this.serialNumberValue = serialNumberValue;
	}
	public String getRetailerCompanyPrefixLVLI() {
		return retailerCompanyPrefixLVLI;
	}
	public void setRetailerCompanyPrefixLVLI(String retailerCompanyPrefixLVLI) {
		this.retailerCompanyPrefixLVLI = retailerCompanyPrefixLVLI;
	}
	public String getRetailerCompanyPrefixRVLI() {
		return retailerCompanyPrefixRVLI;
	}
	public void setRetailerCompanyPrefixRVLI(String retailerCompanyPrefixRVLI) {
		this.retailerCompanyPrefixRVLI = retailerCompanyPrefixRVLI;
	}
	public String getRetailerCompanyPrefixValue() {
		return retailerCompanyPrefixValue;
	}
	public void setRetailerCompanyPrefixValue(String retailerCompanyPrefixValue) {
		this.retailerCompanyPrefixValue = retailerCompanyPrefixValue;
	}
	public String getMiscellaneousElementsVLI() {
		return miscellaneousElementsVLI;
	}
	public void setMiscellaneousElementsVLI(String miscellaneousElementsVLI) {
		this.miscellaneousElementsVLI = miscellaneousElementsVLI;
	}
	public String getSaveValueCodeVLI() {
		return saveValueCodeVLI;
	}
	public void setSaveValueCodeVLI(String saveValueCodeVLI) {
		this.saveValueCodeVLI = saveValueCodeVLI;
	}
	public String getAppliesToWhichItemVLI() {
		return appliesToWhichItemVLI;
	}
	public void setAppliesToWhichItemVLI(String appliesToWhichItemVLI) {
		this.appliesToWhichItemVLI = appliesToWhichItemVLI;
	}
	public String getStoreCouponVLI() {
		return storeCouponVLI;
	}
	public void setStoreCouponVLI(String storeCouponVLI) {
		this.storeCouponVLI = storeCouponVLI;
	}
	public String getDontMultiplyFlagVLI() {
		return dontMultiplyFlagVLI;
	}
	public void setDontMultiplyFlagVLI(String dontMultiplyFlagVLI) {
		this.dontMultiplyFlagVLI = dontMultiplyFlagVLI;
	}
	public String getInterimDataValue() {
		return interimDataValue;
	}
	public void setInterimDataValue(String interimDataValue) {
		this.interimDataValue = interimDataValue;
	}
}
