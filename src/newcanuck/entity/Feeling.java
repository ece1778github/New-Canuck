package newcanuck.entity;

import java.io.Serializable;

public class Feeling implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;


	private Long missionId;
	private String missionName;
	private String missionDescription;
	private String missionAddress;
	private Double missionLatitude;
	private Double missionLongitude;
	
	private Double myRating;
	private String myFeeling;
	private Long createDate;
	private String myImgFileName;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getMissionId() {
		return missionId;
	}
	
	public void setMissionId(Long missionId) {
		this.missionId = missionId;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public String getMissionName() {
		return missionName;
	}

	public void setMissionName(String missionName) {
		this.missionName = missionName;
	}

	public Double getMyRating() {
		return myRating;
	}

	public void setMyRating(Double myRating) {
		this.myRating = myRating;
	}

	public String getMyFeeling() {
		return myFeeling;
	}

	public void setMyFeeling(String myFeeling) {
		this.myFeeling = myFeeling;
	}

	public String getMyImgFileName() {
		return myImgFileName;
	}

	public void setMyImgFileName(String myImgFileName) {
		this.myImgFileName = myImgFileName;
	}

	public String getMissionDescription() {
		return missionDescription;
	}

	public void setMissionDescription(String missionDescription) {
		this.missionDescription = missionDescription;
	}

	public String getMissionAddress() {
		return missionAddress;
	}

	public void setMissionAddress(String missionAddress) {
		this.missionAddress = missionAddress;
	}

	public Double getMissionLatitude() {
		return missionLatitude;
	}

	public void setMissionLatitude(Double missionLatitude) {
		this.missionLatitude = missionLatitude;
	}

	public Double getMissionLongitude() {
		return missionLongitude;
	}

	public void setMissionLongitude(Double missionLongitude) {
		this.missionLongitude = missionLongitude;
	}
}
