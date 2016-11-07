
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class RestClient {

	
	public static void main(String...args) throws JsonMappingException, JsonMappingException, IOException{
		String plainCreds = "brandregistrar@oomoqu.com:ZmVuaWxhYmM=";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		

		HttpHeaders headers = new HttpHeaders();
		//headers.add("Authorization", "Basic " + base64Creds);
		headers.add("method", "restaurant_list");
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://192.168.0.127/foodieshunt_webservices/restaurant.php";
		//URI uri = new UriTemplate(url).expand("testabghtmail@gmail.com");
		
		
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
		String account = response.getBody();
		System.out.println(account);
		
		/*ObjectMapper objectMapper = new ObjectMapper();
		List<PromotionReport> promotionReports = objectMapper.readValue(account, TypeFactory.defaultInstance().constructCollectionType(List.class, PromotionReport.class));
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		for(PromotionReport promotionReport : promotionReports){
			PromotionReport newPromotionReport;
			
			Map<String, Object> promotionReportsMap = new HashMap<String, Object>();
			
			Date date = promotionReport.getStartDate();
			String endDate = CommonUtility.getFormattedDate(promotionReport.getEndDate());
			boolean breakLoop = false;
			
			while(true){
				String keyDate = CommonUtility.getFormattedDate(date);
				
				if(breakLoop){
					break;
				}
				
				if(keyDate.equals(endDate)){
					breakLoop = true;
				}
				
				newPromotionReport = new PromotionReport();
				newPromotionReport.setCreatedCoupons(0);
				newPromotionReport.setIssuedCoupons(0);
				newPromotionReport.setReedemedCoupons(0);
				newPromotionReport.setSharedCoupons(0);
	
				promotionReportsMap.put(keyDate, newPromotionReport);
				
				date = DateUtils.addDays(date, 1);
			}
			
			newPromotionReport = new PromotionReport();
			for(Coupon coupon : promotionReport.getCoupons()){
				String createdDate = CommonUtility.getFormattedDate(coupon.getCreatedDate());
				String issuedDate = CommonUtility.getFormattedDate(coupon.getUsedDate());
				String reedemedDate = CommonUtility.getFormattedDate(coupon.getReedemedDate());
				
				if(createdDate != null){
					newPromotionReport = (PromotionReport) promotionReportsMap.get(createdDate);
					newPromotionReport.setCreatedCoupons(newPromotionReport.getCreatedCoupons() + 1);
					promotionReportsMap.put(createdDate, newPromotionReport);
					newPromotionReport = new PromotionReport();
				}
				
				if(issuedDate != null){
					newPromotionReport = (PromotionReport) promotionReportsMap.get(issuedDate);
					newPromotionReport.setIssuedCoupons(newPromotionReport.getIssuedCoupons() + 1);
					promotionReportsMap.put(issuedDate, newPromotionReport);
					newPromotionReport = new PromotionReport();
				}
				
				if(reedemedDate != null){
					newPromotionReport = (PromotionReport) promotionReportsMap.get(reedemedDate);
					newPromotionReport.setReedemedCoupons(newPromotionReport.getReedemedCoupons() + 1);
					promotionReportsMap.put(reedemedDate, newPromotionReport);
					newPromotionReport = new PromotionReport();
				}
			}
			
			Map<String, Object> treeMap = new TreeMap<String, Object>(
					new Comparator<String>() {
		 
					@Override
					public int compare(String o1, String o2) {
						//return o2.compareTo(o1);
						Date date1 = CommonUtility.getDateFromString(o1);
						Date date2 = CommonUtility.getDateFromString(o2);
						return date1.compareTo(date2);
					}
		 
			});
			
			treeMap.putAll(promotionReportsMap);
				
			for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
				System.out.println("Key : " + entry.getKey());
				PromotionReport promotionReport4 = (PromotionReport)entry.getValue();
				System.out.println(promotionReport4.getCreatedCoupons());
				System.out.println(promotionReport4.getIssuedCoupons());
				System.out.println(promotionReport4.getReedemedCoupons());
				
			}
			result.add(treeMap);
		}	
		System.out.println(result.size());
*/	}
}
