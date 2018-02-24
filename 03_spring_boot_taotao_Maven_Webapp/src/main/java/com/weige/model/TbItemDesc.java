package com.weige.model;

import java.util.Date;

public class TbItemDesc {
    private Long itemId;

    private Date created;

    private Date updated;

    private String itemDesc;
    
    public TbItemDesc() {
	}
    
    public TbItemDesc(Date created, Date updated) {
		super();
		this.created = created;
		this.updated = updated;
	}

	public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc == null ? null : itemDesc.trim();
    }
}