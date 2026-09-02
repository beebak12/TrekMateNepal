const fs = require('fs');
const path = require('path');
const multer = require('multer');

const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg', 'image/webp'];

const createUploadHandler = (folderName) => {
  const uploadDir = path.join(__dirname, `../../uploads/${folderName}`);

  if (!fs.existsSync(uploadDir)) {
    fs.mkdirSync(uploadDir, { recursive: true });
  }

  const storage = multer.diskStorage({
    destination: (req, file, cb) => {
      cb(null, uploadDir);
    },
    filename: (req, file, cb) => {
      const uniqueName = `${Date.now()}-${Math.round(Math.random() * 1e9)}${path.extname(file.originalname)}`;
      cb(null, uniqueName);
    },
  });

  const fileFilter = (req, file, cb) => {
    if (!allowedTypes.includes(file.mimetype)) {
      return cb(new Error('Only JPG, PNG, and WEBP image files are allowed'), false);
    }

    cb(null, true);
  };

  return multer({
    storage,
    fileFilter,
    limits: {
      fileSize: 2 * 1024 * 1024,
    },
  });
};

const uploadProfileImage = createUploadHandler('profiles');
const uploadGearImage = createUploadHandler('gears');

module.exports = {
  uploadProfileImage,
  uploadGearImage,
  createUploadHandler,
};
