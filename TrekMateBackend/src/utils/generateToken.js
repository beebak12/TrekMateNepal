const jwt = require('jsonwebtoken');

const generateToken = (user) => {
  return jwt.sign(
    {
      id: user.id,
      email: user.email,
      role: user.role_name || user.role || 'USER',
      username: user.username,
    },
    process.env.JWT_SECRET || 'trekmate-secret',
    {
      expiresIn: process.env.JWT_EXPIRES_IN || '1d',
    }
  );
};

module.exports = { generateToken };
