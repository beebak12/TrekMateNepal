const mysql = require('mysql2/promise');
require('dotenv').config();

const dbConfig = {
  host: process.env.DB_HOST || 'localhost',
  user: process.env.DB_USER || 'root',
  password: process.env.DB_PASSWORD || '',
  database: process.env.DB_NAME || 'trekmate_db',
  port: Number(process.env.DB_PORT || 3306),
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0,
};

let pool;

const initializeDatabase = async () => {
  try {
    const rootPool = mysql.createPool({
      host: dbConfig.host,
      user: dbConfig.user,
      password: dbConfig.password,
      port: dbConfig.port,
      waitForConnections: true,
      connectionLimit: 5,
      queueLimit: 0,
    });

    await rootPool.query(`CREATE DATABASE IF NOT EXISTS \`${dbConfig.database}\``);
    await rootPool.end();

    pool = mysql.createPool(dbConfig);
    return pool;
  } catch (error) {
    console.error('Database initialization failed');
    throw error;
  }
};

const getPool = () => {
  if (!pool) {
    throw new Error('Database pool has not been initialized.');
  }

  return pool;
};

const testConnection = async () => {
  try {
    const currentPool = pool || (await initializeDatabase());
    await currentPool.query('SELECT 1 AS ok');
    console.log('Database connected successfully');
    return true;
  } catch (error) {
    console.error('Database connection failed');
    console.error(error.message);
    return false;
  }
};

module.exports = {
  initializeDatabase,
  getPool,
  testConnection,
};
