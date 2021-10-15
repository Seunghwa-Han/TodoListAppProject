package com.todo.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.todo.service.DbConnect;
import com.todo.service.TodoSortByDate;
import com.todo.service.TodoSortByName;

public class TodoList {
	private Connection conn;

	public TodoList() {
		this.conn = DbConnect.getConnection();
	}
	
	public boolean isNull() {
		if(conn==null)
			return true;
		return false;
	}
	
	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	//아이템 추가 
	public int addItem(TodoItem t) {
		String sql = "insert into list (title, memo, category, current_date, due_date, repeat_num"
				+ ", repeat_period)" + " values (?,?,?,?,?,?,?);";
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, t.getCategory());
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			pstmt.setInt(6, t.getRepeat_num());
			pstmt.setString(7, t.getRepeat_period());
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	// 아이템 삭제 
	public int deleteItem(int index) {
		String sql = "delete from list where id = ? ;";
		PreparedStatement pstmt;
		int count =0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, index);
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	//아이템 수정 
	public int updateItem(TodoItem t) {
		String sql = "update list set title=?, memo=?, category=?, current_date=?, due_date=?"
				+ ", repeat_num=?, repeat_period=?"
				+ " where id = ?;";
		PreparedStatement pstmt;
		int count =0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, t.getCategory());
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			pstmt.setInt(6, t.getRepeat_num());
			pstmt.setString(7, t.getRepeat_period());
			pstmt.setInt(8, t.getId());
			count = pstmt.executeUpdate();
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	//아이템 체크 
	public int completeItem(int index) {
		String sql = "update list set is_completed = 1 where id = ? ;";
		int count =0;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, index);
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	// 모든 리스트 얻기 
	public ArrayList<TodoItem> getList() {
		ArrayList<TodoItem> list = new ArrayList<>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "select * from list;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("memo");
				String category = rs.getString("category");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int r_n = rs.getInt("repeat_num");
				String r_p = rs.getString("repeat_period");
				int comp = rs.getInt("is_completed");
				TodoItem t = new TodoItem(id, title, desc, current_date, category, due_date, comp,
						r_n, r_p);
				list.add(t);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	// 완료된 리스트 얻기 
	public ArrayList<TodoItem> getList(int complement){
		ArrayList<TodoItem> list = new ArrayList<>();
		String sql = "select * from list where is_completed = ?;";
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, complement);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("memo");
				String category = rs.getString("category");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int r_n = rs.getInt("repeat_num");
				String r_p = rs.getString("repeat_period");
				int comp = rs.getInt("is_completed");
				TodoItem t = new TodoItem(id, title, desc, current_date, category, due_date, comp,
						r_n, r_p);
				list.add(t);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	// 특정 키워드 포함 리스트 얻기
	public ArrayList<TodoItem> getList(String keyword){
		ArrayList<TodoItem> list = new ArrayList<>();
		String sql = "select * from list where title like ? or memo like ? ;";
		PreparedStatement pstmt;
		keyword = "%"+keyword+"%";
		ResultSet rs;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			pstmt.setString(2, keyword);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("memo");
				String category = rs.getString("category");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int r_n = rs.getInt("repeat_num");
				String r_p = rs.getString("repeat_period");
				int comp = rs.getInt("is_completed");
				TodoItem t = new TodoItem(id, title, desc, current_date, category, due_date, comp,
						r_n, r_p);
				list.add(t);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	// 모든 카테고리 
	public ArrayList<String> getCategories(){
		ArrayList<String> list = new ArrayList<>();
		String sql = "select distinct category from list;" ;
		Statement stmt;
		ResultSet rs;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				list.add(rs.getString("category"));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	// 특정 키워드 포함 카테고리 
	public ArrayList<TodoItem> getCategory(String keyword){
		ArrayList<TodoItem> list = new ArrayList<>();
		PreparedStatement pstmt;
		String sql = "select * from list where category = ? ;";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("memo");
				String category = rs.getString("category");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int r_n = rs.getInt("repeat_num");
				String r_p = rs.getString("repeat_period");
				int comp = rs.getInt("is_completed");
				TodoItem t = new TodoItem(id, title, desc, current_date, category, due_date, comp,
						r_n, r_p);
				list.add(t);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	// get ordered list 
	public ArrayList<TodoItem> getOrderedList(String orderby, int ordering){
		ArrayList<TodoItem> list = new ArrayList<>();
		String sql = "select * from list order by " + orderby;
		if(ordering == 0) {
			sql += " desc";
		}
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql+";");
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("memo");
				String category = rs.getString("category");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int r_n = rs.getInt("repeat_num");
				String r_p = rs.getString("repeat_period");
				int comp = rs.getInt("is_completed");
				TodoItem t = new TodoItem(id, title, desc, current_date, category, due_date, comp,
						r_n, r_p);
				list.add(t);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	// 아이템 개수 
	public int getCount() {
		Statement stmt;
		int count=0;
		try {
			stmt = conn.createStatement();
			String sql = "select count(id) from list;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count = rs.getInt("count(id)");
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	// for ls 명령어 
	public void listAll() {
		System.out.println("\n"
				+ "========== [전체 목록, 총 "+ this.getCount() +"개]");
		for (TodoItem myitem : this.getList()) {
			System.out.println(myitem.toString());
		}
	}
	//중복 체크 
	public Boolean isDuplicate(String title) {
		String sql = "select count(*) from list where title = ? ;";
		int count =0;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			ResultSet rs = pstmt.executeQuery();
			count = rs.getInt(1);
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(count>0)
			return true;
		return false;
	}
	//기존 파일 기반의 텍스트파일에 있던 거 읽어오기
	public void importData(String filename) {   
		try {
			BufferedReader br = new BufferedReader(new FileReader (filename));
			
			String oneline;
			String sql = "insert into list (title, memo, category, current_date, due_date)"
					+ " values (?, ?, ?, ?, ?);";
			int records = 0;
			
			while((oneline=br.readLine())!=null) {
				StringTokenizer st = new StringTokenizer(oneline, "##");
				String title = st.nextToken();
				String desc = st.nextToken();
				String current_date = st.nextToken();
				String ctg = st.nextToken();
				String due_date = st.nextToken();
				
				try {
					PreparedStatement ptmt = conn.prepareStatement(sql);
					ptmt.setString(1, title);
					ptmt.setString(2, desc);
					ptmt.setString(3, ctg);
					ptmt.setString(4, current_date);
					ptmt.setString(5, due_date);
					int count = ptmt.executeUpdate();
					if (count > 0) 
						records++;
					ptmt.close();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(records + " records read!");
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("todolist.txt 파일이 없습니다.\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
