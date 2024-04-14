package syt.springorm.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class WarehouseData {
	
    private String warehouseApplicationID;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer warehouseID;
	private String warehouseName;
	private String warehouseAddress;
	private String warehousePostalCode;
	private String warehouseCity;
	private String warehouseCountry;
	private String timestamp;

    @OneToMany(fetch = FetchType.LAZY)
	private List<Product> productData;

	/**
	 * Constructor
	 */
	public WarehouseData() {
		this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}
	
	/**
	 * Setter and Getter Methods
	 */
	public Integer getWarehouseID() {
		return warehouseID;
	}

	public String getWarehouseApplicationID() {
        return warehouseApplicationID;
    }

    public void setWarehouseApplicationID(String warehouseApplicationID) {
        this.warehouseApplicationID = warehouseApplicationID;
    }


    public void setWarehouseID(Integer warehouseID) {
		this.warehouseID = warehouseID;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	/**
	 * @return the warehouseAddress
	 */
	public String getWarehouseAddress() {
		return warehouseAddress;
	}

	/**
	 * @param warehouseAddress the warehouseAddress to set
	 */
	public void setWarehouseAddress(String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}
	
	/**
	 * @return the warehousePostalCode
	 */
	public String getWarehousePostalCode() {
		return warehousePostalCode;
	}

	/**
	 * @param warehousePostalCode the warehousePostalCode to set
	 */
	public void setWarehousePostalCode(String warehousePostalCode) {
		this.warehousePostalCode = warehousePostalCode;
	}

	/**
	 * @return the warehouseCity
	 */
	public String getWarehouseCity() {
		return warehouseCity;
	}
	
	/**
	 * @param warehouseCity the warehouseCity to set
	 */
	public void setWarehouseCity(String warehouseCity) {
		this.warehouseCity = warehouseCity;
	}
	
	/**
	 * @return the warehouseCountry
	 */
	public String getWarehouseCountry() {
		return warehouseCountry;
	}

	/**
	 * @param warehouseCountry the warehouseCountry to set
	 */
	public void setWarehouseCountry(String warehouseCountry) {
		this.warehouseCountry = warehouseCountry;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
	/**
	 * Methods
	 */
	@Override
	public String toString() {
		String info = String.format("Warehouse Info: ID = %s, timestamp = %s", warehouseID, timestamp );
		return info;
	}

    public List<Product> getProductData() {
        return productData;
    }

    public void setProductData(List<Product> productData) {
        this.productData = productData;
    }

}
