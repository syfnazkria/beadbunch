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
function addToCart(button) {
    const productElement = button.parentElement;  // Get the parent element of the clicked button
    const productId = productElement.querySelector('button').getAttribute('data-id');  // Get the product ID
    const name = productElement.querySelector('button').getAttribute('data-name');  // Get the product name
    const price = parseFloat(productElement.querySelector('button').getAttribute('data-price'));  // Get the price (as a number)

    // Create the product object
    const product = { id: productId, name: name, price: price, quantity: 1 };

    // Get the existing cart from localStorage, or initialize it as an empty array
    let cart = JSON.parse(localStorage.getItem('cart')) || [];

    // Check if the product already exists in the cart
    const existingItemIndex = cart.findIndex(item => item.id === productId);

    if (existingItemIndex !== -1) {
        // If the product exists, update the quantity
        cart[existingItemIndex].quantity += 1;
    } else {
        // If the product doesn't exist, add it to the cart
        cart.push(product);
    }

    // Save the updated cart to localStorage
    localStorage.setItem('cart', JSON.stringify(cart));

    // Provide feedback to the user
    alert(`${name} has been added to your cart.`);
}

// Attach the addToCart function to all buttons with class 'add-to-cart-btn'
document.querySelectorAll('.add-to-cart-btn').forEach(button => {
    button.addEventListener('click', function() {
        addToCart(this);  // Pass the clicked button to the addToCart function
    });
});



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
