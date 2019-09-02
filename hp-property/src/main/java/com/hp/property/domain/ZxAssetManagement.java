package com.hp.property.domain;

import com.hp.common.annotation.Excel;
import com.hp.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Id;
import java.util.Date;

/**
 * 资产信息对象 zx_asset_management
 * 
 * @author hp
 * @date 2019-09-02
 */
public class ZxAssetManagement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资产ID */
    @Id
    private Long id;

    /** 资产名称 */
    @Excel(name = "资产名称")
    private String name;

    /** 资产编号 */
    @Excel(name = "资产编号")
    private String assetNum;

    /** 品牌 */
    @Excel(name = "品牌")
    private String brand;

    /** 资产规格 */
    @Excel(name = "资产规格")
    private String assetSpecification;

    /** 数量 */
    @Excel(name = "数量")
    private Integer number;

    /** 资产类型 */
    @Excel(name = "资产类型")
    private Integer type;

    /** 单位 */
    @Excel(name = "单位")
    private Integer units;

    /** 供货商 */
    @Excel(name = "供货商")
    private String supplier;

    /** 图片 */
    @Excel(name = "图片")
    private String picture;

    /** 资产原值 */
    @Excel(name = "资产原值")
    private Long price;

    /** 购置时间 */
    @Excel(name = "购置时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date purchasingTime;

    /** 入库时间 */
    @Excel(name = "入库时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date storageTime;

    /** 入库校区 */
    @Excel(name = "入库校区")
    private Integer warehousingCampus;

    /** 状态 */
    @Excel(name = "状态")
    private Integer state;

    /** 操作人员 */
    @Excel(name = "操作人员")
    private String operator;

    /** 存放地点 */
    @Excel(name = "存放地点")
    private Integer location;

    /** 领用时间 */
    @Excel(name = "领用时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date recipientsTime;

    /** 报修时间 */
    @Excel(name = "报修时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date repairTime;

    /** 转移时间 */
    @Excel(name = "转移时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date transferTime;

    /** 报废时间 */
    @Excel(name = "报废时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date discardTime;

    /** 退还时间 */
    @Excel(name = "退还时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date returnTime;

    /** 校区 */
    @Excel(name = "校区")
    private Integer campus;

    /** 部门 */
    @Excel(name = "部门")
    private Integer department;

    /** $column.columnComment */
    @Excel(name = "")
    private String extend1;

    /** $column.columnComment */
    @Excel(name = "")
    private String extend2;

    /** $column.columnComment */
    @Excel(name = "")
    private String extend3;

    /** $column.columnComment */
    @Excel(name = "")
    private String extend4;

    /** $column.columnComment */
    @Excel(name = "")
    private String extend5;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setAssetNum(String assetNum) 
    {
        this.assetNum = assetNum;
    }

    public String getAssetNum() 
    {
        return assetNum;
    }
    public void setBrand(String brand) 
    {
        this.brand = brand;
    }

    public String getBrand() 
    {
        return brand;
    }
    public void setAssetSpecification(String assetSpecification) 
    {
        this.assetSpecification = assetSpecification;
    }

    public String getAssetSpecification() 
    {
        return assetSpecification;
    }
    public void setNumber(Integer number) 
    {
        this.number = number;
    }

    public Integer getNumber() 
    {
        return number;
    }
    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }
    public void setUnits(Integer units) 
    {
        this.units = units;
    }

    public Integer getUnits() 
    {
        return units;
    }
    public void setSupplier(String supplier) 
    {
        this.supplier = supplier;
    }

    public String getSupplier() 
    {
        return supplier;
    }
    public void setPicture(String picture) 
    {
        this.picture = picture;
    }

    public String getPicture() 
    {
        return picture;
    }
    public void setPrice(Long price) 
    {
        this.price = price;
    }

    public Long getPrice() 
    {
        return price;
    }
    public void setPurchasingTime(Date purchasingTime) 
    {
        this.purchasingTime = purchasingTime;
    }

    public Date getPurchasingTime() 
    {
        return purchasingTime;
    }
    public void setStorageTime(Date storageTime) 
    {
        this.storageTime = storageTime;
    }

    public Date getStorageTime() 
    {
        return storageTime;
    }
    public void setWarehousingCampus(Integer warehousingCampus) 
    {
        this.warehousingCampus = warehousingCampus;
    }

    public Integer getWarehousingCampus() 
    {
        return warehousingCampus;
    }
    public void setState(Integer state) 
    {
        this.state = state;
    }

    public Integer getState() 
    {
        return state;
    }
    public void setOperator(String operator) 
    {
        this.operator = operator;
    }

    public String getOperator() 
    {
        return operator;
    }
    public void setLocation(Integer location) 
    {
        this.location = location;
    }

    public Integer getLocation() 
    {
        return location;
    }
    public void setRecipientsTime(Date recipientsTime) 
    {
        this.recipientsTime = recipientsTime;
    }

    public Date getRecipientsTime() 
    {
        return recipientsTime;
    }
    public void setRepairTime(Date repairTime) 
    {
        this.repairTime = repairTime;
    }

    public Date getRepairTime() 
    {
        return repairTime;
    }
    public void setTransferTime(Date transferTime) 
    {
        this.transferTime = transferTime;
    }

    public Date getTransferTime() 
    {
        return transferTime;
    }
    public void setDiscardTime(Date discardTime) 
    {
        this.discardTime = discardTime;
    }

    public Date getDiscardTime() 
    {
        return discardTime;
    }
    public void setReturnTime(Date returnTime) 
    {
        this.returnTime = returnTime;
    }

    public Date getReturnTime() 
    {
        return returnTime;
    }
    public void setCampus(Integer campus) 
    {
        this.campus = campus;
    }

    public Integer getCampus() 
    {
        return campus;
    }
    public void setDepartment(Integer department) 
    {
        this.department = department;
    }

    public Integer getDepartment() 
    {
        return department;
    }
    public void setExtend1(String extend1) 
    {
        this.extend1 = extend1;
    }

    public String getExtend1() 
    {
        return extend1;
    }
    public void setExtend2(String extend2) 
    {
        this.extend2 = extend2;
    }

    public String getExtend2() 
    {
        return extend2;
    }
    public void setExtend3(String extend3) 
    {
        this.extend3 = extend3;
    }

    public String getExtend3() 
    {
        return extend3;
    }
    public void setExtend4(String extend4) 
    {
        this.extend4 = extend4;
    }

    public String getExtend4() 
    {
        return extend4;
    }
    public void setExtend5(String extend5) 
    {
        this.extend5 = extend5;
    }

    public String getExtend5() 
    {
        return extend5;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("assetNum", getAssetNum())
            .append("brand", getBrand())
            .append("assetSpecification", getAssetSpecification())
            .append("number", getNumber())
            .append("type", getType())
            .append("units", getUnits())
            .append("supplier", getSupplier())
            .append("picture", getPicture())
            .append("price", getPrice())
            .append("purchasingTime", getPurchasingTime())
            .append("storageTime", getStorageTime())
            .append("warehousingCampus", getWarehousingCampus())
            .append("state", getState())
            .append("operator", getOperator())
            .append("location", getLocation())
            .append("recipientsTime", getRecipientsTime())
            .append("repairTime", getRepairTime())
            .append("transferTime", getTransferTime())
            .append("discardTime", getDiscardTime())
            .append("returnTime", getReturnTime())
            .append("campus", getCampus())
            .append("department", getDepartment())
            .append("extend1", getExtend1())
            .append("extend2", getExtend2())
            .append("extend3", getExtend3())
            .append("extend4", getExtend4())
            .append("extend5", getExtend5())
            .toString();
    }
}
