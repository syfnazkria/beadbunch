document.addEventListener('DOMContentLoaded', function() {
    // Select all 'Add to Cart' buttons
    const addToCartBtns = document.querySelectorAll('.add-to-cart-btn');

    // Add event listener to each button
    addToCartBtns.forEach(button => {
        button.addEventListener('click', function() {
            // Get item data from button attributes
            const itemName = this.getAttribute('data-name');
            const itemPrice = parseFloat(this.getAttribute('data-price'));

            // Create a payload to send to the server
            const payload = {
                name: itemName,
                price: itemPrice
            };

            // Send a POST request to add the item to the cart
            fetch('/cart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            })
                .then(response => response.text())
                .then(data => {
                    console.log('Item added to cart:', data);
                    alert(data); // Show success message to the user
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Failed to add item to cart.');
                });
        });
    });
});
