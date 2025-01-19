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
        const quantity = parseInt(item.quantity) || 1;

        const productRow = `
        <tr>
            <td>${item.name || 'Undefined'}</td>
            <td>RM ${price.toFixed(2)}</td>
            <td>${quantity}</td>
            <td>RM ${(price * quantity).toFixed(2)}</td>
            <td><button class="remove-btn" data-index="${index}">Remove</button></td>
        </tr>`;
        cartContainer.innerHTML += productRow;
        totalPrice += price * quantity;
    });

    totalPriceElement.textContent = `RM ${totalPrice.toFixed(2)}`;

    // Attach remove button event listeners
    document.querySelectorAll('.remove-btn').forEach((button) => {
        button.addEventListener('click', () => {
            const index = button.getAttribute('data-index');
            removeFromCart(index);
        });
    });
}

// Function to add item to cart
function addToCart(productName, productPrice) {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const itemIndex = cart.findIndex((item) => item.name === productName);

    if (itemIndex > -1) {
        cart[itemIndex].quantity += 1;
    } else {
        cart.push({ name: productName, price: parseFloat(productPrice), quantity: 1 });
    }

    localStorage.setItem('cart', JSON.stringify(cart));
    alert(`${productName} has been added to the cart.`);
    loadCart();
}

// Function to remove item from cart
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
        localStorage.removeItem('cart');
        loadCart(); // Reload cart to reflect changes
    }
}

// Redirect to checkout page
function redirectToCheckout() {
    window.location.href = 'checkout.html';
}

// Initialize cart and attach event listeners
window.onload = function () {
    loadCart();
    document.getElementById('clear-cart-btn').addEventListener('click', clearCart);

    // Attach event listeners to add-to-cart buttons
    document.querySelectorAll('.add-to-cart-btn').forEach((button) => {
        button.addEventListener('click', () => {
            const productName = button.getAttribute('data-name');
            const productPrice = button.getAttribute('data-price');
            addToCart(productName, productPrice);
        });
    });
};
