const axios = require('axios');

const getNews = async (req, res) => {
    try {
        const { category = 'business', country = 'us' } = req.query;
        const apiKey = process.env.NEWS_API_KEY;

        // Fetch news from NewsAPI
        const response = await axios.get('https://newsapi.org/v2/top-headlines', {
            params: {
                category,
                country,
                apiKey,
            },
        });

        res.status(200).json({
            success: true,
            articles: response.data.articles,
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: 'Failed to fetch news',
            error: error.message,
        });
    }
};

module.exports = { getNews };
