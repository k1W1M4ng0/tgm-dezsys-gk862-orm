package syt.springorm.data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productID;
    private String productName;
    private String productCategory;
    private int productQuantity;
    private String productUnit;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouseID")
    private WarehouseData warehouse;
    
    // setters and getters

    /**
     * @return the productID
     */
    public Integer getProductID() {
        return productID;
    }

    /**
     * @param productID the productID to set
     */
    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the productCategory
     */
    public String getProductCategory() {
        return productCategory;
    }

    /**
     * @param productCategory the productCategory to set
     */
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    /**
     * @return the productQuantity
     */
    public int getProductQuantity() {
        return productQuantity;
    }

    /**
     * @param productQuantity the productQuantity to set
     */
    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    /**
     * @return the productUnit
     */
    public String getProductUnit() {
        return productUnit;
    }

    /**
     * @param productUnit the productUnit to set
     */
    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    @Override
    public String toString() {
        String out = String.format(
            "a"
            
        );
        return out;
    }

    public WarehouseData getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseData warehouse) {
        this.warehouse = warehouse;
    }
}
