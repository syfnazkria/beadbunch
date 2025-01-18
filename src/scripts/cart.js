// Initialize the cart from localStorage or create an empty cart
let cart = JSON.parse(localStorage.getItem('cart')) || [];

// Function to add a product to the cart
function addToCart(productName, productPrice) {
    cart.push({ name: productName, price: productPrice });
    localStorage.setItem('cart', JSON.stringify(cart));  // Save the updated cart in localStorage
    alert(`${productName} has been added to your cart.`);
}

// Function to display the cart contents
function viewCart() {
    if (cart.length === 0) {
        document.getElementById('cartContents').innerHTML = "Your cart is empty.";
    } else {
        let cartHTML = "<h2>Your Cart:</h2><ul>";
        cart.forEach((item, index) => {
            cartHTML += `<li>${item.name} - $${item.price}</li>`;
        });
        cartHTML += "</ul>";
        document.getElementById('cartContents').innerHTML = cartHTML;
    }
}
