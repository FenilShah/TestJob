package com.oomoqu.rest.wrapper.portal;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.oomoqu.rest.model.Brand;

@JsonInclude(Include.NON_NULL)
public class BrandDetail extends Brand{
	
	private List<PromotionDetail> promotionDetails;
	public List<PromotionDetail> getPromotionDetails() {
		return promotionDetails;
	}
	public void setPromotionDetails(List<PromotionDetail> promotionDetails) {
		this.promotionDetails = promotionDetails;
	}
	/*private Set<PromotionDetail> promotionReport;
	public Set<PromotionDetail> getPromotionReport() {
		return promotionReport;
	}
	public void setPromotionReport(Set<PromotionDetail> promotionReport) {
		this.promotionReport = promotionReport;
	}*/
	
	public boolean equals(Object o) {
		return (o instanceof Brand) && (((Brand) o).getBrandId()).equals(this.getBrandId());
	}

	public int hashCode() {
		 return this.getBrandId().hashCode();
	}
}
