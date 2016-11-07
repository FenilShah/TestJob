package com.oomoqu.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document(collection="role")
public class Role implements GrantedAuthority{
	
	private static final long serialVersionUID = 1L;
	private String roleId;
    private String name;
    
    
    @Id
    public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
    public String getAuthority() {
        return this.name;
    }
 
    

}
