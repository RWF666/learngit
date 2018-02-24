package com.weige.model;

import java.util.Date;

public class TbItemParamItem {
    private Long id;

    private Long itemId;

    private String paramData;

    private Date created;

    private Date updated;
    
    public TbItemParamItem() {
   	}
       public TbItemParamItem(Date created, Date updated) {
   		super();
   		this.created = created;
   		this.updated = updated;
   	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getParamData() {
        return paramData;
    }

    public void setParamData(String paramData) {
        this.paramData = paramData == null ? null : paramData.trim();
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
}