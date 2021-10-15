package com.todo.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {
	//아이템 추가
	public static void createItem(TodoList list) {
		String title, desc, ctg, due_date;
		int repeat_num; String repeat_period= null;
		Scanner sc = new Scanner(System.in);

		System.out.print("\n" + "========== [항목 추가] \n" + "카테고리 > ");
		ctg = sc.next();

		System.out.print("제목 > ");
		title = sc.next();
		sc.nextLine(); // 제목 뒤에 들어오는 enter 제거
		
		if(list.isDuplicate(title)) {
			System.out.println("중복된 제목입니다.");
			return;
		}

		System.out.print("내용 > ");
		desc = sc.nextLine().trim(); // trim-> 좌우여백 제거

		System.out.print("기한 (yyyy/mm/dd) > ");
		due_date = sc.next();
		
		System.out.print("반복 횟수 > ");
		repeat_num = sc.nextInt();
		
		TodoItem t;
		if(repeat_num == 0) {
			t = new TodoItem(title, desc, ctg, due_date, 0, null);
		}
		else {
			sc.nextLine();
			System.out.print("반복 주기 (y/m/d) > ");
			repeat_period = sc.nextLine();
			t = new TodoItem(title, desc, ctg, due_date, repeat_num, repeat_period);
		}
		
		if(t.getRepeat_num()==0){
			if (list.addItem(t) > 0)
				System.out.println("아이템 추가 완료 !!");
		}
		else {
			int count =0;
			GregorianCalendar gc = new GregorianCalendar();
			for(int i=0; i<t.getRepeat_num(); i++) {
				String[] due = t.getDue_date().split("/");
				String[] period = t.getRepeat_period().split("/");
				int year = Integer.parseInt(due[0])+Integer.parseInt(period[0])*i;
				int mon = Integer.parseInt(due[1])+Integer.parseInt(period[1])*i;
				int day = Integer.parseInt(due[2])+Integer.parseInt(period[2])*i;
				while(true) { 
					if(mon>12) { 
						year += mon/12;
						mon = mon%12;
					}
					if((mon==1 || mon==3 ||mon==5 ||mon==7 ||mon==8 ||mon==10 ||mon==12)&& day>31) {
						mon += day/31;
						day = day%31;
					}
					else if(gc.isLeapYear(year) && mon==2 && day>29) { //윤년 
						mon += day/29;
						day = day%29;
					}
					else if(!gc.isLeapYear(year) && mon==2 && day>28) { //평년 
						mon += day/28;
						day = day%28;
					}
					else if((mon==4 || mon==6 ||mon==9 ||mon==11)&&day>30) {
						mon += day/30;
						day = day%30;
					}
					else break;
				}
				String new_due = Integer.toString(year)+"/";
				if(mon/10==0)
					new_due +=("0"+Integer.toString(mon)+"/");
				else new_due +=(Integer.toString(mon)+"/");
				if(day/10==0)
					new_due +=("0"+Integer.toString(day));
				else new_due += Integer.toString(day);
				TodoItem tmp;
				if(i==0){
					tmp = new TodoItem(title+Integer.toString(i+1), desc, ctg, new_due, repeat_num, repeat_period);
				}
				else {
					tmp = new TodoItem(title+Integer.toString(i+1), desc, ctg, new_due,0,null);
				}
				if(list.addItem(tmp)>0)
					count++;
			}
			if(count == t.getRepeat_num())
				System.out.println("아이템 추가 완료 !!");
		}
	}
	//아이템 삭제 
	public static void deleteItem(TodoList l) {
		Scanner sc = new Scanner(System.in);

		System.out.print("\n" + "========== [항목 삭제]\n" + "삭제할 항목의 번호 입력 > ");
		//int index = sc.nextInt();
		
		String oneline = sc.nextLine();
		String[] token = oneline.split(" ");
		
		int count =0;
		for(String index : token) {
			if(l.deleteItem(Integer.parseInt(index))>0)
				count++;
		}
		if(count>0)
			System.out.println("총 "+count+"개 아이템 삭제 완료 !!");
	}
	//아이템 수정 
	public static void updateItem(TodoList l) {

		Scanner sc = new Scanner(System.in);

		System.out.print("\n" + "========== [항목 수정]\n" + "수정할 항목의 번호 입력 > ");

		int index = sc.nextInt();

		System.out.print("새 카테고리 > ");
		String ctg = sc.next();
		System.out.print("새 제목 > ");
		String title = sc.next();
		sc.nextLine();
		
		if(l.isDuplicate(title)) {
			System.out.println("중복된 제목입니다.");
			return;
		}
		
		System.out.print("새 내용 > ");
		String desc = sc.nextLine().trim();
		System.out.print("새 기한 (yyyy/mm/dd) > ");
		String due_date = sc.next();
		
		
		TodoItem t = new TodoItem(index, title, desc, ctg, due_date, 0, null);
		
		if (l.updateItem(t) > 0)
			System.out.println("아이템 수정 완료 !!");

	}
	//아이템 완료 
	public static void completeItem(TodoList l) {
		Scanner sc = new Scanner(System.in);

		System.out.print("\n" + "========== [항목 체크]\n" + "완료할 항목의 번호 입력 > ");
		
		String oneline = sc.nextLine();
		String[] token = oneline.split(" ");
		int count =0;
		for(String index : token) {
			if(l.completeItem(Integer.parseInt(index)) > 0)
				count++;
		}
		if(count==token.length)
			System.out.println("총 "+count+"개 아이템 체크 완료 !!");
	}
	//전체 출력 
	public static void listAll(TodoList l, String orderby, int ordering) {
		System.out.println("\n" + "========== [전체 목록, 총 " + l.getCount() + "개]");
		for (TodoItem item : l.getOrderedList(orderby, ordering)) {
			System.out.println(item.toString());
		}
	}
	//완료된 것만 출력 
	public static void listAll(TodoList l, int comp) {
		int count =0;
		for(TodoItem t: l.getList(comp)) {
			System.out.println(t.toString());
			count++;
		}
		if (count == 0)
			System.out.println("완료된 항목이 없습니다.");
		else
			System.out.println("총 " + count + "개의 항목이 완료되었습니다.");
		
	}
	//find keyword 
	public static void findKeyword(TodoList l, String find) {
		int count = 0;
		for (TodoItem t : l.getList(find)) {
			System.out.println(t.toString());
			count++;
		}
		if (count == 0)
			System.out.println("해당 키워드를 포함하는 항목이 없습니다.");
		else
			System.out.println("총 " + count + "개의 항목을 찾았습니다.");
	}
	//find_cate keyword 
	public static void findCtg(TodoList l, String find) {
		int count = 0;
		for (TodoItem t : l.getCategory(find)) {
			count++;
			System.out.println(t.toString());
		}
		if (count == 0)
			System.out.println("해당 키워드를 포함하는 항목이 없습니다.");
		else
			System.out.println("총 " + count + "개의 항목을 찾았습니다.");
	}
	// list all categories 
	public static void listCate(TodoList l) {
		int count = 0;
		for (String t : l.getCategories()) {
			count++;
			System.out.print(t +" ");
		}
		if (count == 0)
			System.out.println("\n해당 키워드를 포함하는 항목이 없습니다.");
		else
			System.out.println("\n총 " + count + "개의 카테고리가 등록되어 있습니다.");
	}
	//json 포맷으로 파일 저장 
	public static void saveJson(TodoList l) {

		System.out.println("\n" + "========== [ json 파일 내보내기 ]");
		System.out.print("저장할 파일 이름 입력 > ");
		Scanner s = new Scanner(System.in);
		String filename = s.nextLine();
		Gson gson = new Gson();
		String jsonstr = gson.toJson(l.getList());
		
		try {
			FileWriter writer = new FileWriter(filename);
			writer.write(jsonstr);
			writer.close();
			System.out.println(filename+" 파일에 저장되었습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//json 포맷 파일 읽기 
	public static void loadJson(TodoList l) {
		System.out.println("\n" + "========== [ json 파일 불러오기 ]");
		System.out.print("로딩할 파일 이름 입력 > ");
		Scanner s = new Scanner(System.in);
		String filename = s.nextLine();
		Gson gson = new Gson();
		String jsonstr=null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			jsonstr = br.readLine();
			br.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(filename + " 파일이 존재하지 않습니다. ");
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<TodoItem> list = gson.fromJson(jsonstr,  new TypeToken<List<TodoItem>>() {}.getType());
		
		int count =0;
		for(TodoItem item : list) {
			if(l.addItem(item)>0)
				count++;
		}
		if(count == list.size())
			System.out.println("총 "+count+"개의 항목을 "+filename+" 파일로부터 읽었습니다.");
	}
}
