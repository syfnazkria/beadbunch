const express = require('express');
const path = require('path');
const app = express();
const PORT = 3000;

// Serve static files (like HTML, CSS, and JS) from the public folder
app.use(express.static(path.join(__dirname, 'public')));

// Middleware to parse incoming request bodies
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Start the server
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});
