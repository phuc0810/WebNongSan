package controller.client;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ProductDAO;
import model.Item;
import model.Order;
import model.Product;


public class DeleteProductInCartController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	ProductDAO productDao = new ProductDAO();
	DecimalFormat df = new DecimalFormat("#.000");
	DecimalFormat df1 = new DecimalFormat("#.0");
	
	@Override
	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		HttpSession session = req.getSession(true);
		Product product = productDao.get(Integer.parseInt(id));
		Order order = (Order) session.getAttribute("order");
		List<Item> listItems = order.getItems();
		for(Item item: listItems)
		{
			if(Integer.parseInt(item.getProduct().getId()) == Integer.parseInt(product.getId()))
			{
				order.setSumPrice(order.getSumPrice() - item.getPrice());
				listItems.remove(item);
				break;
			}
		}
		order.setItems(listItems);
		session.setAttribute("order", order);
		resp.sendRedirect(req.getContextPath() + "/view/client/cart");
		if(order.getSumPrice() == 0)
		{
			session.setAttribute("sumprice", "0");
		} else {
			session.setAttribute("sumprice", df.format(order.getSumPrice()));
		}
		
	}
}
