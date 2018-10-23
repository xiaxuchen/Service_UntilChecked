package com.cxyz.check.entity;

/**
 * Created by 夏旭晨 on 2018/9/23.
 */

public class Grade{
	//班级id
	private Integer _id;
	//班级名称
	private String _name;
	//所属学院
	private College college;
	//班主任
	private User headTeacher;
	//晚自习教室
	private ClassRoom classRoom;

	public Grade(){}

	public Grade(int id){
		this._id = id;
	}


	public User getHeadTeacher() {
		return headTeacher;
	}

	public void setHeadTeacher(User headTeacher) {
		this.headTeacher = headTeacher;
	}

	public Grade(Integer id) {
		set_id(id);
	}
	public Integer get_id() {
		return _id;
	}
	public void set_id(Integer _id) {
		this._id = _id;
	}
	public String get_name() {
		return _name;
	}
	public void set_name(String _name) {
		this._name = _name;
	}
	public College getCollege() {
		return college;
	}
	public void setCollege(College college) {
		this.college = college;
	}
	public ClassRoom getClassRoom() {
		return classRoom;
	}
	public void setClassRoom(ClassRoom classRoom) {
		this.classRoom = classRoom;
	}

	@Override
	public String toString() {
		return "Grade{" +
				"_id=" + _id +
				", _name='" + _name + '\'' +
				", college=" + college +
				", headTeacher=" + headTeacher +
				", classRoom=" + classRoom +
				'}';
	}
}
