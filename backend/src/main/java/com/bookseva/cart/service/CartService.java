package com.bookseva.cart.service;
import com.bookseva.book.entity.ListingStatus;


import com.bookseva.book.entity.Book;
import com.bookseva.book.repository.BookRepository;
import com.bookseva.cart.entity.Cart;
import com.bookseva.cart.entity.CartItem;
import com.bookseva.cart.repository.CartItemRepository;
import com.bookseva.cart.repository.CartRepository;
import com.bookseva.user.entity.User;
import com.bookseva.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public Cart getCart(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user).orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));
    }

    @Transactional
    public Cart addToCart(Long bookId, String userEmail) {
        Cart cart = getCart(userEmail);
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        if (book.getStatus() != com.bookseva.book.entity.ListingStatus.ACTIVE) {
            throw new RuntimeException("This book is no longer available.");
        }
        if (cart.getUser().getId().equals(book.getSeller().getId())) {
            throw new RuntimeException("You cannot add your own book to the cart");
        }
        boolean alreadyInCart = cart.getItems().stream().anyMatch(item -> item.getBook().getId().equals(bookId));
        if (alreadyInCart) {
            throw new RuntimeException("Book is already in the cart");
        }
        CartItem newItem = CartItem.builder().cart(cart).book(book).build();
        cart.getItems().add(newItem);
        cartItemRepository.save(newItem);
        return cart;
    }

    @Transactional
    public void removeFromCart(Long cartItemId, String userEmail) {
        Cart cart = getCart(userEmail);
        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        cart.getItems().remove(item);
        cartItemRepository.delete(item);
    }

    public CartService(final CartRepository cartRepository, final CartItemRepository cartItemRepository, final BookRepository bookRepository, final UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }
}
