// Initialize the cart from localStorage or create an empty cart
let cart = JSON.parse(localStorage.getItem('cart')) || [];

// Function to add a product to the cart
function addToCart(productName, productPrice) {
    // Add to localStorage
    cart.push({ name: productName, price: productPrice });
    localStorage.setItem('cart', JSON.stringify(cart));  // Save the updated cart in localStorage

    // Send the product to the server to be added to the server-side cart
    fetch('/cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: productName, price: productPrice, quantity: 1 })  // Sending default quantity as 1
    })
        .then(response => response.text())
        .then(data => {
            alert(`${productName} has been added to your cart.`);
        })
        .catch(error => console.error('Error adding item to server cart:', error));
}

// Function to display the cart contents
function viewCart() {
    // Fetch the cart contents from the server (or use localStorage as a fallback)
    fetch('/cart')
        .then(response => response.text())
        .then(data => {
            // Display the cart content retrieved from the server
            document.getElementById('cartContents').innerHTML = data;
        })
        .catch(error => {
            console.error('Error retrieving cart:', error);
            document.getElementById('cartContents').innerHTML = "Failed to load cart.";
        });
}
