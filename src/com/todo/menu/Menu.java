package com.todo.menu;
public class Menu {

    public static void displaymenu()
    {
        System.out.println();
        System.out.println("< TodoList 명령어 사용법 >");
        System.out.println("1. 항목 추가 ( add )");
        System.out.println("2. 항목 삭제 ( del )");
        System.out.println("3. 항목 수정  ( edit )");
        System.out.println("4. 항목 완료 ( comp )");
        System.out.println("5. 전체 목록 ( ls )");
        System.out.println("6. 카테고리 목록 ( ls_cate )");
        System.out.println("7. 완료된 목록 ( ls_comp )");
        System.out.println("8. 이름순으로 정렬 ( ls_name )");
        System.out.println("9. 이름 역순으로 정렬 ( ls_name_desc )");
        System.out.println("10. 날짜 순으로 정렬 ( ls_date )");
        System.out.println("11. 최신 순으로 정렬 ( ls_date_desc ) ");
        System.out.println("12. 검색 ( find <키워드> )");
        System.out.println("13. 카테고리 검색 ( find_cate <키워드> )");
        System.out.println("14. json 파일 내보내기 ( save )");
        System.out.println("15. json 파일 불러오기 ( load )");
        System.out.println("16. 종료 ( exit )");
    }
    
    public static void prompt()
    {
    	System.out.print("\nCommand > ");
    }
}
