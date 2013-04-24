package newcanuck.entity;

import java.io.Serializable;

public class Mission implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private Long ratetimes;
	private Double rating;
	
	private Double latitude;
	private Double longitude;
	private String address;
	
	private String description;
	
	//client data
	//state: 0:not taking, 1:taking, 2:have completed
	private Long state;
	
	//server and client use the same name.
	//thumb (small img file name add _s)
	private String ImgFileName;

	private Long createDate;
	private Long completeDate;
	private Long addDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public Long getRatetimes() {
		return ratetimes;
	}

	public void setRatetimes(Long ratetimes) {
		this.ratetimes = ratetimes;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public String getImgFileName() {
		return ImgFileName;
	}

	public void setImgFileName(String imgFileName) {
		ImgFileName = imgFileName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Long completeDate) {
		this.completeDate = completeDate;
	}

	public Long getAddDate() {
		return addDate;
	}

	public void setAddDate(Long addDate) {
		this.addDate = addDate;
	} 
}
