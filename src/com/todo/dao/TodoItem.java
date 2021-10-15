package com.todo.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoItem {
    private String title;  //제목
    private String desc;   //내용
    private String current_date;  //입력한 시간 
    private String category; //카테고리 
    private String due_date; //마감일자 
    private int id; 
    private int is_completed; // 완료 여부 
    private int repeat_num; //반복 횟수
    private String repeat_period; //반복 주기 
    
    //제목, 내용, 카테고리, 마감날짜
    public TodoItem(String title, String desc, String category, String due_date){
        this.title=title;
        this.desc=desc;
    	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        this.current_date = format.format(new Date());
        this.category = category;
        this.due_date = due_date;
    }
    //제목, 내용, 카테고리, 마감날짜, 반복횟수, 반복주기 
    public TodoItem(String title, String desc, String category, String due_date, int r_n, String r_p){
        this.title=title;
        this.desc=desc;
    	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        this.current_date = format.format(new Date());
        this.category = category;
        this.due_date = due_date;
        this.repeat_num = r_n;
        this.repeat_period = r_p;
    }
    
    //모든 필드 포함 
    public TodoItem(int id, String title, String desc, String current_date, String category, String due_date, int comp, int r_n, String r_p) {
		super();
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.current_date = current_date;
		this.category = category;
		this.due_date = due_date;
		this.is_completed = comp;
		this.repeat_num = r_n;
		this.repeat_period = r_p;
	}
    //아이디 포함, current_date, comp 미포함 
    public TodoItem(int id, String title, String desc, String category, String due_date, int r_n, String r_p) {
		super();
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.category = category;
		this.due_date = due_date;
		this.repeat_num = r_n;
		this.repeat_period = r_p;
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        this.current_date = format.format(new Date());
	}

	public int getRepeat_num() {
		return repeat_num;
	}

	public void setRepeat_num(int repeat_num) {
		this.repeat_num = repeat_num;
	}

	public String getRepeat_period() {
		return repeat_period;
	}

	public void setRepeat_period(String repeat_period) {
		this.repeat_period = repeat_period;
	}

	public int getIs_completed() {
		return is_completed;
	}

	public void setIs_completed(int is_completed) {
		this.is_completed = is_completed;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		if(is_completed == 1) {
			return id + " " + "[" + category + "] " + title + "[V]" + " - " + desc + " - " + due_date 
					+ " (" + current_date + " 작성)" ;
		}
		return id + " " + "[" + category + "] " + title + " - " + desc + " - " + due_date 
				+ " (" + current_date + " 작성)" ;
	}
	
	public String toSaveString() {
		return title + "##" + desc + "##" + current_date + "##" + category + "##" + due_date+ "\n";
	}
    
}
