package com.koreait.cset.command.cart;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.ui.Model;

import com.koreait.cset.common.CsetCommand;
import com.koreait.cset.dao.CartDAO;
import com.koreait.cset.dto.CartDTO;
import com.koreait.cset.dto.CartJoinVO;
import com.koreait.cset.dto.MemberDTO;

public class CartListCommand implements CsetCommand {

	@Override
	public void execute(SqlSession sqlSession, Model model) {
		
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		
		HttpSession session = request.getSession();
		MemberDTO loginDTO = (MemberDTO) session.getAttribute("loginDTO");
		String mId = loginDTO.getmId();
		
		CartDAO cDAO = sqlSession.getMapper(CartDAO.class);
		
		List<CartJoinVO> list = cDAO.cartListPage(mId);
		
		for(CartJoinVO cJVO : list) {
			int pPrice = cJVO.getpPrice();
			int pDisrate = cJVO.getpDisrate();
			
			int productTotalPrice = Math.round(pPrice * (1-(pDisrate/100)) / 100 ) * 100;
			
			cJVO.setProductTotalPrice(productTotalPrice);
		}
		
		int sumMoney = cDAO.cartSumMoney(mId);
		int fee = sumMoney >= 100000 ? 0 : 3000;
		int total = sumMoney + fee;
		
		model.addAttribute("list", list);
		model.addAttribute("sumMoney", sumMoney);
		model.addAttribute("fee", fee);
		model.addAttribute("total", total);
		
	}

}
