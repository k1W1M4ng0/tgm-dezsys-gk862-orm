package syt.springorm.data;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
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
	private LocalDateTime timestamp;

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Product> productData;

	/**
	 * Constructor
	 */
	public WarehouseData() {
		this.timestamp = LocalDateTime.now();
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
