package com.oomoqu.rest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.json.JSONObject;

import com.oomoqu.rest.exception.CustomException;

public class PortalServices {
		
	public static void test(){
		System.out.println("Test " + Properties.portalIp );
		System.out.println("Test " + Properties.portalScheme );
		System.out.println("Test " + Properties.portalPort );
	}
	
	public static String[] addOrganizaion(String orgName) throws CustomException{
		
		String SERVICE_URL = "organization/add-organization/parent-organization-id/0/name/"+orgName+"/type/regular-organization/recursable/true/region-id/0/country-id/0/status-id/12017/-comments/site/true";
		
		String result = callPortal(SERVICE_URL);
		
		JSONObject jobj = new JSONObject(result.toString());
		 
		
		Object organizationId = jobj.opt("organizationId");
		Object companyId = jobj.opt("companyId");
		
		if(organizationId == null){
			Object exception = jobj.opt("exception");
			if(exception != null){
				throw new CustomException(exception.toString());
			}else{
				throw new CustomException("Some error occured please try again letter");
			}
		}else{
			System.out.println("CREATED ORG ID  : " + organizationId);
			return new String[]{organizationId.toString(),companyId.toString()};
		}
		
	}
	
	public static String createUser(String email, String organizationId, String roleId, String companyId) throws CustomException {
		//String SERVICE_URL = "user/add-user/company-id/"+ companyId +"/auto-password/false/password1/test/password2/test/auto-screen-name/true/-screen-name/email-address/"+email+"/facebook-id/0/-open-id/-locale/first-name/Test/middle-name/T/last-name/Test/prefix-id/0/suffix-id/0/male/true/birthday-month/09/birthday-day/12/birthday-year/1986/job-title/carpenter/-group-ids/-organization-ids/" + organizationId + "/-role-ids/" + roleId + "-user-group-ids/send-email/false";
		String SERVICE_URL = "";
		
		if(StringUtils.isNotBlank(organizationId) && StringUtils.isNotBlank(roleId)){
			SERVICE_URL = "user/add-user/company-id/" + companyId + "/auto-password/false/password1/test/password2/test/auto-screen-name/true/-screen-name/email-address/" + email + "/facebook-id/0/-open-id/-locale/first-name/Test/middle-name/T/last-name/Test/prefix-id/0/suffix-id/0/male/true/birthday-month/09/birthday-day/12/birthday-year/1989/-job-title/-group-ids/organization-ids/" + organizationId + "/role-ids/" + roleId + "/-user-group-ids/send-email/false";
		}else{
			SERVICE_URL = "user/add-user/company-id/" + companyId + "/auto-password/false/password1/test/password2/test/auto-screen-name/true/-screen-name/email-address/" + email + "/facebook-id/0/-open-id/-locale/first-name/Test/middle-name/T/last-name/Test/prefix-id/0/suffix-id/0/male/true/birthday-month/09/birthday-day/12/birthday-year/1989/-job-title/-group-ids/-organization-ids/-role-ids/-user-group-ids/send-email/false";
		}
		
		String result = callPortal(SERVICE_URL);
		
		JSONObject jobj = new JSONObject(result.toString());
        
		Object userId = jobj.opt("userId");
		
		if(userId == null){
			Object exception = jobj.opt("exception");
			if(exception != null){
				throw new CustomException(exception.toString());
			}else{
				throw new CustomException("Some error occured please try again letter");
			}
		}else{
			System.out.println("Brand User ID  : " + userId);
			return userId.toString();
		}
	}
	
	public static String findRole(String roleName, String companyId) throws CustomException{
		String serviceURL = "role/get-role/company-id/" + companyId + "/name/" + roleName;
		
		String result = callPortal(serviceURL);
		
		JSONObject jobj = new JSONObject(result.toString());
		
		Object roleId = jobj.opt("roleId");
        
		if(roleId != null){
			return roleId.toString();
		}else{
			Object exception = jobj.opt("exception");
			if(exception != null){
				throw new CustomException(exception.toString());
			}else{
				throw new CustomException("Some error occured please try again letter");
			}
		}
	}
	
	public static String assignSiteTemplate(String orgId) throws CustomException{
		String serviceURL = "junit-portlet.junittest/update-org-site/orgs/" + orgId; 

		String result = callPortal(serviceURL);
		
		/*JSONObject jobj = new JSONObject(result.toString());
		
		Object roleId = jobj.opt("roleId");
        
		if(roleId != null){
			return roleId.toString();
		}else{
			Object exception = jobj.opt("exception");
			if(exception != null){
				throw new RegisterOmoquUserException(exception.toString());
			}else{
				throw new RegisterOmoquUserException("Some error occured please try again letter");
			}
		}*/
		return null;
	}
	
	private static String callPortal(String serviceURL) throws CustomException{
		HttpHost targetHost = new HttpHost(Properties.portalIp, Properties.portalPort, Properties.portalScheme);
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getCredentialsProvider().setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()),new UsernamePasswordCredentials(Properties.portalUser, Properties.portalPassword));

        /** Create AuthCache instance */
        AuthCache authCache = new BasicAuthCache();
       
        /** Generate BASIC scheme object and add it to the local */
        /** Authentication cache */
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        /** Add AuthCache to the execution context */
        BasicHttpContext ctx = new BasicHttpContext();
        ctx.setAttribute(ClientContext.AUTH_CACHE, authCache);
        
        HttpPost post = new HttpPost("/api/jsonws/" + serviceURL);

        /** make actual HTTP request and print results to System.out */
        HttpResponse resp = null;
        
		try {
			resp = httpclient.execute(targetHost, post, ctx);
		} catch (IOException e) {
			throw new CustomException("error creating Organization");
		}
		
		//System.out.println("Response Code : " + resp.getStatusLine().getStatusCode());
		
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
		} catch (IllegalStateException | IOException e1) {
			throw new CustomException("error creating Organization");
		}

		StringBuffer result = new StringBuffer();
		String line = "";
		try {
			while ((line = rd.readLine()) != null) {
			    result.append(line);
			}
		} catch (IOException e) {
			throw new CustomException("error creating oomoqu user");
		}
		System.out.println(result);
		
		httpclient.getConnectionManager().shutdown();
		
		return result.toString();
	}

}
