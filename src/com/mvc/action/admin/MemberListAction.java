package com.mvc.action.admin;

import java.util.List;

import javax.servlet.http.*;

import com.baemin.member.MemberDAO;
import com.baemin.member.MemberDTO;
import com.mvc.action.CommandAction;

public class MemberListAction implements CommandAction {

    @Override
    public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {

        request.setCharacterEncoding("UTF-8");
        System.out.println("- - - - MemberListAction - - - - ");
        ///////////////// 이 아래에 내용 넣으세요 /////////////////////////

        
        request.setCharacterEncoding("utf-8");
        System.out.println("------ MemberListAll.jsp --- ");

        MemberDAO dao=MemberDAO.getInstance();
        List<MemberDTO> list = dao.getListAll();
        request.setAttribute("list", list);        

        ///////////////// 이 위에 내용 넣으세요 /////////////////////////
        return "/admin/MemberList.jsp";
    }

}
