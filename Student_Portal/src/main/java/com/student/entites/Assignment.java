package com.student.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "assignment")
public class Assignment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "faculity_id")
	private long faculityId;

	@Column(name = "faculity_name")
	private String faculityName;

	@Column(name = "subject_name")
	private String subjectName;

	private String description;

	@Column(name = "file_name")
	private String fileName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFaculityId() {
		return faculityId;
	}

	public void setFaculityId(long faculityId) {
		this.faculityId = faculityId;
	}

	public String getFaculityName() {
		return faculityName;
	}

	public void setFaculityName(String faculityName) {
		this.faculityName = faculityName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
