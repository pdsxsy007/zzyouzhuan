package io.cordova.zhihuiyouzhuan.bean;

import java.io.Serializable;

/**
 * 学生
 * @ClassName: StudentBean
 * @Description: TODO
 * @author: hanpp
 * @date: 2018年12月18日
 * {
"success": true,
"msg": "操作成功",
"obj": {
"staffNumber": "admin",
"staffName": "张管理员",
"staffSex": "1",
"staffLevel": "中级",
"staffNational": "汉",
"staffPoliticalStatus": "党员",
"staffBelongUnit": "党委办",
"staffBirthday": "11-14",
"staffCredentialsType": "A",
"staffCredentialsId": "412566335585",
"staffCellphoneNumber": "110",
"staffMail": "11@qq.com",
"staffCurrentState": "在职",
"staffPostState": "在岗",
"staffTechnicalTitles": "工程师",
"staffWorkingYears": "2",
"staffAcademicDegree": "硕士",
"staffAcademicQualifications": "研究生",
"staffAdmissionDate": null,
"staffOrganization": null,
"staffGraduateSchool": null,
"staffGraduationDate": null,
"staffMajorStudied": null,
"staffWorkingDate": null,
"staffNative": null,
"staffBirthSpace": null,
"staffDepartment": null,
"staffPost": null,
"staffIdentity": null,
"staffAdministrativeDuties": null,
"staffPresentDuties": null,
"staffAdministrativeLevel": null,
"staffEntryCategory": null,
"staffPersonnelCategory": null
},
"count": null,
"attributes": null
}
 */
public class TeacherBean2 implements Serializable {
	private boolean success;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Obj getObj() {
		return obj;
	}

	public void setObj(Obj obj) {
		this.obj = obj;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	private String msg;
	private Obj obj;
	private String count;
	private String attributes;

	public class Obj{

		private String staffNumber;
		private String staffName;
		private String staffSex;
		private String staffNational;
		private String staffPoliticalStatus;
		private String staffBelongUnit;
		private String staffBirthday;
		private String staffCredentialsType;
		private String staffCredentialsId;
		private String staffCellphoneNumber;
		private String staffMail;
		private String staffCurrentState;
		private String staffPostState;
		private String staffTechnicalTitles;
		private String staffWorkingYears;
		private String staffAcademicDegree;
		private String staffAcademicQualifications;
		private String staffAdmissionDate;
		private String staffOrganization;
		private String staffGraduateSchool;
		private String staffGraduationDate;
		private String staffMajorStudied;
		private String staffWorkingDate;
		private String staffNative;
		private String staffBirthSpace;
		private String staffDepartment;
		private String staffPost;
		private String staffIdentity;

		public String getStaffLevel() {
			return staffLevel;
		}

		public void setStaffLevel(String staffLevel) {
			this.staffLevel = staffLevel;
		}

		private String staffLevel;
		private String staffAdministrativeDuties;
		private String staffPresentDuties;

		public String getStaffNumber() {
			return staffNumber;
		}

		public void setStaffNumber(String staffNumber) {
			this.staffNumber = staffNumber;
		}

		public String getStaffName() {
			return staffName;
		}

		public void setStaffName(String staffName) {
			this.staffName = staffName;
		}

		public String getStaffSex() {
			return staffSex;
		}

		public void setStaffSex(String staffSex) {
			this.staffSex = staffSex;
		}

		public String getStaffNational() {
			return staffNational;
		}

		public void setStaffNational(String staffNational) {
			this.staffNational = staffNational;
		}

		public String getStaffPoliticalStatus() {
			return staffPoliticalStatus;
		}

		public void setStaffPoliticalStatus(String staffPoliticalStatus) {
			this.staffPoliticalStatus = staffPoliticalStatus;
		}

		public String getStaffBelongUnit() {
			return staffBelongUnit;


		}

		public void setStaffBelongUnit(String staffBelongUnit) {
			this.staffBelongUnit = staffBelongUnit;
		}

		public String getStaffBirthday() {
			return staffBirthday;
		}

		public void setStaffBirthday(String staffBirthday) {
			this.staffBirthday = staffBirthday;
		}

		public String getStaffCredentialsType() {
			return staffCredentialsType;
		}

		public void setStaffCredentialsType(String staffCredentialsType) {
			this.staffCredentialsType = staffCredentialsType;
		}

		public String getStaffCredentialsId() {
			return staffCredentialsId;
		}

		public void setStaffCredentialsId(String staffCredentialsId) {
			this.staffCredentialsId = staffCredentialsId;
		}

		public String getStaffCellphoneNumber() {
			return staffCellphoneNumber;
		}

		public void setStaffCellphoneNumber(String staffCellphoneNumber) {
			this.staffCellphoneNumber = staffCellphoneNumber;
		}

		public String getStaffMail() {
			return staffMail;
		}

		public void setStaffMail(String staffMail) {
			this.staffMail = staffMail;
		}

		public String getStaffCurrentState() {
			return staffCurrentState;
		}

		public void setStaffCurrentState(String staffCurrentState) {
			this.staffCurrentState = staffCurrentState;
		}

		public String getStaffPostState() {
			return staffPostState;
		}

		public void setStaffPostState(String staffPostState) {
			this.staffPostState = staffPostState;
		}

		public String getStaffTechnicalTitles() {
			return staffTechnicalTitles;
		}

		public void setStaffTechnicalTitles(String staffTechnicalTitles) {
			this.staffTechnicalTitles = staffTechnicalTitles;
		}

		public String getStaffWorkingYears() {
			return staffWorkingYears;
		}

		public void setStaffWorkingYears(String staffWorkingYears) {
			this.staffWorkingYears = staffWorkingYears;
		}

		public String getStaffAcademicDegree() {
			return staffAcademicDegree;
		}

		public void setStaffAcademicDegree(String staffAcademicDegree) {
			this.staffAcademicDegree = staffAcademicDegree;
		}

		public String getStaffAcademicQualifications() {
			return staffAcademicQualifications;
		}

		public void setStaffAcademicQualifications(String staffAcademicQualifications) {
			this.staffAcademicQualifications = staffAcademicQualifications;
		}

		public String getStaffAdmissionDate() {
			return staffAdmissionDate;
		}

		public void setStaffAdmissionDate(String staffAdmissionDate) {
			this.staffAdmissionDate = staffAdmissionDate;
		}

		public String getStaffOrganization() {
			return staffOrganization;
		}

		public void setStaffOrganization(String staffOrganization) {
			this.staffOrganization = staffOrganization;
		}

		public String getStaffGraduateSchool() {
			return staffGraduateSchool;
		}

		public void setStaffGraduateSchool(String staffGraduateSchool) {
			this.staffGraduateSchool = staffGraduateSchool;
		}

		public String getStaffGraduationDate() {
			return staffGraduationDate;
		}

		public void setStaffGraduationDate(String staffGraduationDate) {
			this.staffGraduationDate = staffGraduationDate;
		}

		public String getStaffMajorStudied() {
			return staffMajorStudied;
		}

		public void setStaffMajorStudied(String staffMajorStudied) {
			this.staffMajorStudied = staffMajorStudied;
		}

		public String getStaffWorkingDate() {
			return staffWorkingDate;
		}

		public void setStaffWorkingDate(String staffWorkingDate) {
			this.staffWorkingDate = staffWorkingDate;
		}

		public String getStaffNative() {
			return staffNative;
		}

		public void setStaffNative(String staffNative) {
			this.staffNative = staffNative;
		}

		public String getStaffBirthSpace() {
			return staffBirthSpace;
		}

		public void setStaffBirthSpace(String staffBirthSpace) {
			this.staffBirthSpace = staffBirthSpace;
		}

		public String getStaffDepartment() {
			return staffDepartment;
		}

		public void setStaffDepartment(String staffDepartment) {
			this.staffDepartment = staffDepartment;
		}

		public String getStaffPost() {
			return staffPost;
		}

		public void setStaffPost(String staffPost) {
			this.staffPost = staffPost;
		}

		public String getStaffIdentity() {
			return staffIdentity;
		}

		public void setStaffIdentity(String staffIdentity) {
			this.staffIdentity = staffIdentity;
		}

		public String getStaffAdministrativeDuties() {
			return staffAdministrativeDuties;
		}

		public void setStaffAdministrativeDuties(String staffAdministrativeDuties) {
			this.staffAdministrativeDuties = staffAdministrativeDuties;
		}

		public String getStaffPresentDuties() {
			return staffPresentDuties;
		}

		public void setStaffPresentDuties(String staffPresentDuties) {
			this.staffPresentDuties = staffPresentDuties;
		}

		public String getStaffAdministrativeLevel() {
			return staffAdministrativeLevel;
		}

		public void setStaffAdministrativeLevel(String staffAdministrativeLevel) {
			this.staffAdministrativeLevel = staffAdministrativeLevel;
		}

		public String getStaffEntryCategory() {
			return staffEntryCategory;
		}

		public void setStaffEntryCategory(String staffEntryCategory) {
			this.staffEntryCategory = staffEntryCategory;
		}

		public String getStaffPersonnelCategory() {
			return staffPersonnelCategory;
		}

		public void setStaffPersonnelCategory(String staffPersonnelCategory) {
			this.staffPersonnelCategory = staffPersonnelCategory;
		}

		private String staffAdministrativeLevel;
		private String staffEntryCategory;
		private String staffPersonnelCategory;



	}

}