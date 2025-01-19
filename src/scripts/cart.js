// Load cart items from localStorage
function loadCart() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const cartContainer = document.getElementById('cart-items');
    const totalPriceElement = document.getElementById('total-price');
    cartContainer.innerHTML = '';

    let totalPrice = 0;

    cart.forEach((item, index) => {
        // Ensure valid data
        const price = parseFloat(item.price) || 0;
        const quantity = parseInt(item.quantity) || 1; // Default quantity to 1 if invalid

        const productRow = `
      <tr>
        <td>${item.name || 'Undefined'}</td>
        <td>RM ${price.toFixed(2)}</td>
        <td>${quantity}</td>
        <td>RM ${(price * quantity).toFixed(2)}</td>
        <td><button class="remove-btn" onclick="removeFromCart(${index})">Remove</button></td>
      </tr>
    `;
        cartContainer.innerHTML += productRow;
        totalPrice += price * quantity;
    });

    totalPriceElement.textContent = RM ${totalPrice.toFixed(2)};
}

// Remove item from cart
function removeFromCart(index) {
    if (confirm('Are you sure you want to remove this item?')) {
        const cart = JSON.parse(localStorage.getItem('cart')) || [];
        cart.splice(index, 1); // Remove the selected item
        localStorage.setItem('cart', JSON.stringify(cart)); // Update localStorage
        loadCart(); // Reload cart items
    }
}

// Clear entire cart
function clearCart() {
    if (confirm('Are you sure you want to clear the entire cart?')) {
        localStorage.removeItem('cart'); // Remove cart data from localStorage
        loadCart(); // Reload cart to reflect changes
    }
}

function redirectToCheckout() {
    // Redirect the user to the checkout page
    window.location.href = 'checkout.html';
}

// Initialize cart on page load
window.onload = function () {
    loadCart();
    document.getElementById('clear-cart-btn').addEventListener('click', clearCart);
};