package ro.jademy.presentation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ro.jademy.domain.entities.Media;
import ro.jademy.domain.entities.ProductType;
import ro.jademy.domain.entities.ShoppingCart;
import ro.jademy.domain.entities.User;
import ro.jademy.domain.service.MediaService;
import ro.jademy.domain.service.ShoppingCartService;

@Controller
public class CartController {

	@Autowired
	ShoppingCartService shoppingCartService;

	@Autowired
	MediaService mediaService;

	@RequestMapping("/addProductToCart")
	public ModelAndView addProductToCart(String productCode, int cantitate, ProductType productType,
			HttpServletRequest request) {
		
		if (cantitate == 0) {
			return new ModelAndView("redirect:productList/" + productType.toString());
		}

		Media media = mediaService.getProductByProductTypeAndCode(productType, productCode);
		ShoppingCart shoppingCart = (ShoppingCart) request.getSession().getAttribute("shoppingCart");
		if (shoppingCart == null) {
			shoppingCart = new ShoppingCart();
			request.getSession().setAttribute("shoppingCart", shoppingCart);
		}

		shoppingCart.addToCart(media, cantitate);
		return new ModelAndView("redirect:productList/" + productType.toString());
	}

	@RequestMapping("/removeProductFromCart")
	public ModelAndView removeProductFromCart(String productCode, HttpServletRequest request) {
		
		if (productCode == null) {
			return new ModelAndView("redirect:displayCart");	
		}
		
		ShoppingCart shoppingCart = (ShoppingCart) request.getSession().getAttribute("shoppingCart");
		if (shoppingCart == null) {
			shoppingCart = new ShoppingCart();
			request.getSession().setAttribute("shoppingCart", shoppingCart);
		}
		
		shoppingCart.removeFromCartByProductCode(productCode);
		return new ModelAndView("redirect:displayCart");
	}

	@RequestMapping("/displayCart")
	public ModelAndView displayCart() {
		return new ModelAndView("displayCart");
	}

	@RequestMapping("/checkout")
	public ModelAndView checkout(HttpServletRequest request) {
		ShoppingCart shoppingCart = (ShoppingCart) request.getSession().getAttribute("shoppingCart");
		if (shoppingCart == null) {
			return new ModelAndView("displayCart");
		}
		if (shoppingCart.getCartItems().size() == 0) {
			return new ModelAndView("displayCart");
		}
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		shoppingCartService.saveShoppingCart(shoppingCart, currentUser);
		ShoppingCart dbCart = new ShoppingCart();
		request.getSession().setAttribute("shoppingCart", dbCart);
		return new ModelAndView("redirect:/mainMenu");
	}

}
