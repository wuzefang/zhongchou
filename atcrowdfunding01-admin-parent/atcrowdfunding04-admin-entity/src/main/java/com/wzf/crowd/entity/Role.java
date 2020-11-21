package com.wzf.crowd.entity;

public class Role {
    private Integer id;

    private String tRole;

    @Override
	public String toString() {
		return "Role [id=" + id + ", tRole=" + tRole + "]";
	}

	public Role() {
		super();
	}

	public Role(Integer id, String tRole) {
		super();
		this.id = id;
		this.tRole = tRole;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String gettRole() {
        return tRole;
    }

    public void settRole(String tRole) {
        this.tRole = tRole == null ? null : tRole.trim();
    }
}