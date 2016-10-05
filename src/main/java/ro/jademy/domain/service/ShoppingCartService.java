package ro.jademy.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.jademy.domain.entities.ShoppingCart;
import ro.jademy.domain.entities.User;
import ro.jademy.persistance.ShoppingCartPropDAO;

@Service
public class ShoppingCartService {
	
	@Autowired
	MailService mailService;
	
	@Autowired
	ShoppingCartPropDAO shoppingCartPropDAO;
	
	public void saveShoppingCart(ShoppingCart shoppingCart, User user){
		mailService.sendCheckoutMail(shoppingCart, user);
		shoppingCartPropDAO.createCart(shoppingCart, user);
	}
}