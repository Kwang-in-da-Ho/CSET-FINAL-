package com.koreait.cset.command.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.ui.Model;

import com.koreait.cset.common.CsetCommand;
import com.koreait.cset.dao.CartDAO;
import com.koreait.cset.dao.OrderDAO;
import com.koreait.cset.dto.CartJoinVO;

public class OrderListPageCommand implements CsetCommand {

	@Override
	public void execute(SqlSession sqlSession, Model model) {
		
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest)map.get("request");
		OrderDAO oDAO = sqlSession.getMapper(OrderDAO.class);
		
		String[] chk = request.getParameterValues("chk");  // 체크된 장바구니 번호
		int total_price = Integer.parseInt(request.getParameter("total_price"));
		int fee = Integer.parseInt(request.getParameter("fee"));
		
		int amount = 0; // 장바구니에 담긴 상품 수량 초기화
		int result = 0; // 재고가 없는 상품이 나타난 경우 result = 1 로 변환 후 controller 에서 값을 체크 하고 경로 정함
		
		CartJoinVO cJVO = new CartJoinVO();
		CartDAO cDAO = sqlSession.getMapper(CartDAO.class);
		
		List<String> cartNos = Arrays.asList(chk);
		
		List<CartJoinVO> cjList = new ArrayList<CartJoinVO>();
		
		for (String cNo : cartNos) {
			cJVO = cDAO.selectCartBycNo(Integer.parseInt(cNo));
			cjList.add(cJVO);
		}
		
		model.addAttribute("cjList", cjList);
		model.addAttribute("total_price", total_price);
		model.addAttribute("fee", fee);
	}
}
