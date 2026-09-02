const app = require('./app');
const { initializeDatabase, testConnection } = require('./config/db');
const { initializeSchema } = require('./config/schema');

const PORT = Number(process.env.PORT) || 5000;

const startServer = async () => {
  try {
    await initializeDatabase();
    const isConnected = await testConnection();

    if (!isConnected) {
      throw new Error('Database connection failed');
    }

    await initializeSchema();

    app.listen(PORT, () => {
      console.log(`TrekMate Nepal backend running on http://localhost:${PORT}`);
    });
  } catch (error) {
    console.error('Server startup failed because the database is unavailable.');
    console.error(error.message);
    process.exit(1);
  }
};

startServer();
