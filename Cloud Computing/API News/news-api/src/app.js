const express = require('express');
const cors = require('cors');
require('dotenv').config();

const newsRoutes = require('./routes/newsRoutes');

const app = express();
const PORT = process.env.PORT || 5000;
 
// Middleware
app.use(cors());
app.use(express.json());

// Routes
app.use('/api/news', newsRoutes);

// Start server
app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});
