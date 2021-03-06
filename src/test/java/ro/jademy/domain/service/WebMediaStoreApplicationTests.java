package ro.jademy.domain.service;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ro.jademy.domain.entities.AgeCategory;
import ro.jademy.domain.entities.CD;
import ro.jademy.domain.entities.DVD;
import ro.jademy.domain.entities.Media;
import ro.jademy.domain.entities.MediaGenre;
import ro.jademy.domain.entities.PriceCategory;
import ro.jademy.domain.entities.ShoppingCart;
import ro.jademy.domain.entities.User;
import ro.jademy.domain.service.ServiceLocator;
import ro.jademy.domain.service.UserService;
import ro.jademy.persistance.UserDAO;

@RunWith(MockitoJUnitRunner.class)
public class WebMediaStoreApplicationTests {

	@Mock
	ServiceLocator serviceLocator;
	@Mock
	UserDAO mockUserDao;

	@Test
	public void testShoppingCartsbyUser() {
		User user = new User("test", "test", "test");
		UserService userService = new UserService();
		userService.setMockServiceLocator(serviceLocator);
		when(serviceLocator.getUserDao()).thenReturn(mockUserDao);
		when(mockUserDao.getShoppingCartsByUser(anyObject())).thenReturn(getShoppingCarts());

		int lp = userService.getLoyaltyPointsForUser(user);
		Assert.assertThat(lp, is(1150));
	}

	private List<ShoppingCart> getShoppingCarts() {
		ArrayList<ShoppingCart> shoppingCarts = new ArrayList<ShoppingCart>();
		for (int i = 0; i < 200; i++) {
			ShoppingCart shoppingCart = new ShoppingCart();
			Media cd = new CD("", 120, "", MediaGenre.ADVENTURE, "");
			if (i < 50) {
				cd.setAgeCategory(AgeCategory.CHILDREN);
				cd.setPriceCategory(PriceCategory.REGULAR);
			} else if (i < 150) {
				cd.setAgeCategory(AgeCategory.PLUS18);
				cd.setPriceCategory(PriceCategory.INFREQUENT_SALE);
			} else {
				cd.setAgeCategory(AgeCategory.GENERAL_AUDIENCE);
				cd.setPriceCategory(PriceCategory.NEW_RELEASE);
			}
			shoppingCart.addToCart(cd, i + 2);
			shoppingCart.addToCart(new DVD("", 120, "", MediaGenre.ADVENTURE, "", ""), (i + 1) * i);
			shoppingCarts.add(shoppingCart);
		}
		return shoppingCarts;
	}

}
