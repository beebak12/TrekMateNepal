const { getPool } = require('./db');

const schemaStatements = [
  `CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name ENUM('USER', 'GUIDE', 'ADMIN') NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  )`,

  `CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    role_id INT NOT NULL DEFAULT 1,
    full_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(30),
    password_hash VARCHAR(255) NOT NULL,
    dob DATE,
    gender ENUM('MALE', 'FEMALE', 'OTHER', 'PREFER_NOT_TO_SAY'),
    profile_image VARCHAR(255),
    is_active TINYINT(1) DEFAULT 1,
    is_email_verified TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_users_email (email),
    INDEX idx_users_username (username)
  )`,

  `CREATE TABLE IF NOT EXISTS user_profiles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    bio TEXT,
    city VARCHAR(100),
    country VARCHAR(100),
    social_links JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_profiles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
  )`,

  `CREATE TABLE IF NOT EXISTS password_resets (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    token VARCHAR(255) NOT NULL,
    expires_at DATETIME NOT NULL,
    used_at DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_password_resets_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_password_resets_token (token)
  )`,

  `CREATE TABLE IF NOT EXISTS treks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    location VARCHAR(150) NOT NULL,
    duration_days INT NOT NULL,
    difficulty ENUM('Easy', 'Moderate', 'Difficult', 'Extreme') NOT NULL,
    max_altitude INT,
    price_min DECIMAL(10,2),
    price_max DECIMAL(10,2),
    description TEXT,
    best_season VARCHAR(100),
    starting_point VARCHAR(150),
    ending_point VARCHAR(150),
    required_permits TEXT,
    required_equipment TEXT,
    safety_info TEXT,
    image_url VARCHAR(255),
    status ENUM('active', 'draft', 'archived') DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_treks_location (location),
    INDEX idx_treks_difficulty (difficulty)
  )`,

  `CREATE TABLE IF NOT EXISTS trek_itineraries (
    id INT PRIMARY KEY AUTO_INCREMENT,
    trek_id INT NOT NULL,
    day_number INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trek_itineraries_trek FOREIGN KEY (trek_id) REFERENCES treks(id) ON DELETE CASCADE,
    UNIQUE KEY unique_day_plan (trek_id, day_number)
  )`,

  `CREATE TABLE IF NOT EXISTS trek_requirements (
    id INT PRIMARY KEY AUTO_INCREMENT,
    trek_id INT NOT NULL,
    requirement_type ENUM('permit', 'equipment', 'fitness') NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trek_requirements_trek FOREIGN KEY (trek_id) REFERENCES treks(id) ON DELETE CASCADE
  )`,

  `CREATE TABLE IF NOT EXISTS gear_categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  )`,

  `CREATE TABLE IF NOT EXISTS gear (
    id INT PRIMARY KEY AUTO_INCREMENT,
    category_id INT NOT NULL,
    owner_user_id INT,
    name VARCHAR(120) NOT NULL,
    description TEXT,
    price_per_day DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    availability ENUM('available', 'unavailable') NOT NULL DEFAULT 'available',
    condition_status VARCHAR(50),
    owner_name VARCHAR(100),
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_gear_category FOREIGN KEY (category_id) REFERENCES gear_categories(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_gear_owner_user FOREIGN KEY (owner_user_id) REFERENCES users(id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_gear_owner_user (owner_user_id),
    INDEX idx_gear_name (name)
  )`,

  `CREATE TABLE IF NOT EXISTS gear_rentals (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status ENUM('pending', 'confirmed', 'cancelled', 'completed') NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_gear_rentals_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_gear_rentals_user (user_id)
  )`,

  `CREATE TABLE IF NOT EXISTS rental_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    rental_id INT NOT NULL,
    gear_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rental_items_rental FOREIGN KEY (rental_id) REFERENCES gear_rentals(id) ON DELETE CASCADE,
    CONSTRAINT fk_rental_items_gear FOREIGN KEY (gear_id) REFERENCES gear(id) ON DELETE RESTRICT ON UPDATE CASCADE
  )`,

  `CREATE TABLE IF NOT EXISTS partner_posts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    trek_id INT,
    required_partners INT NOT NULL,
    travel_date DATE NOT NULL,
    expected_duration VARCHAR(60),
    experience_level ENUM('Beginner', 'Intermediate', 'Experienced', 'Any') DEFAULT 'Any',
    gender_preference ENUM('Any', 'Male', 'Female', 'Other') DEFAULT 'Any',
    description TEXT,
    status ENUM('open', 'matched', 'closed') DEFAULT 'open',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_partner_posts_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_partner_posts_trek FOREIGN KEY (trek_id) REFERENCES treks(id) ON DELETE SET NULL
  )`,

  `CREATE TABLE IF NOT EXISTS partner_requests (
    id INT PRIMARY KEY AUTO_INCREMENT,
    partner_post_id INT NOT NULL,
    requester_id INT NOT NULL,
    message TEXT,
    status ENUM('pending', 'accepted', 'rejected') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_partner_requests_post FOREIGN KEY (partner_post_id) REFERENCES partner_posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_partner_requests_user FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_partner_request (partner_post_id, requester_id)
  )`,

  `CREATE TABLE IF NOT EXISTS community_posts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(200),
    content TEXT NOT NULL,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_community_posts_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_community_posts_user (user_id)
  )`,

  `CREATE TABLE IF NOT EXISTS post_comments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    comment TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_comments_post FOREIGN KEY (post_id) REFERENCES community_posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_comments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
  )`,

  `CREATE TABLE IF NOT EXISTS guides (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    full_name VARCHAR(100) NOT NULL,
    license_number VARCHAR(100) NOT NULL UNIQUE,
    experience_years INT NOT NULL,
    languages VARCHAR(255),
    specializations VARCHAR(255),
    location VARCHAR(150),
    description TEXT,
    phone VARCHAR(30),
    profile_image VARCHAR(255),
    verification_status ENUM('pending', 'verified', 'rejected') DEFAULT 'pending',
    rating DECIMAL(3,2) DEFAULT 0.00,
    availability ENUM('available', 'busy', 'unavailable') DEFAULT 'available',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_guides_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
  )`,

  `CREATE TABLE IF NOT EXISTS guide_packages (
    id INT PRIMARY KEY AUTO_INCREMENT,
    guide_id INT NOT NULL,
    trek_id INT,
    name VARCHAR(150) NOT NULL,
    duration_days INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description TEXT,
    included_services TEXT,
    excluded_services TEXT,
    max_people INT,
    itinerary TEXT,
    image_url VARCHAR(255),
    availability ENUM('available', 'unavailable') DEFAULT 'available',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_guide_packages_guide FOREIGN KEY (guide_id) REFERENCES guides(id) ON DELETE CASCADE,
    CONSTRAINT fk_guide_packages_trek FOREIGN KEY (trek_id) REFERENCES treks(id) ON DELETE SET NULL
  )`,

  `CREATE TABLE IF NOT EXISTS package_bookings (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    package_id INT NOT NULL,
    booking_date DATE NOT NULL,
    number_of_people INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status ENUM('pending', 'confirmed', 'cancelled', 'completed') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_package_bookings_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_package_bookings_package FOREIGN KEY (package_id) REFERENCES guide_packages(id) ON DELETE CASCADE
  )`,

  `CREATE TABLE IF NOT EXISTS conversations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_one_id INT NOT NULL,
    user_two_id INT NOT NULL,
    last_message_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_conversations_user_one FOREIGN KEY (user_one_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_conversations_user_two FOREIGN KEY (user_two_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_conversation (user_one_id, user_two_id)
  )`,

  `CREATE TABLE IF NOT EXISTS messages (
    id INT PRIMARY KEY AUTO_INCREMENT,
    conversation_id INT NOT NULL,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    message_text TEXT,
    message_type ENUM('text', 'image', 'system') DEFAULT 'text',
    is_read TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_messages_conversation FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE,
    CONSTRAINT fk_messages_sender FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_messages_receiver FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_messages_conversation (conversation_id),
    INDEX idx_messages_sender (sender_id)
  )`,

  `CREATE TABLE IF NOT EXISTS notifications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    related_id INT,
    is_read TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_notifications_user (user_id)
  )`,

  `CREATE TABLE IF NOT EXISTS reviews (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    target_type ENUM('guide', 'package', 'gear', 'trek') NOT NULL,
    target_id INT NOT NULL,
    rating INT NOT NULL,
    review TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_reviews_target (target_type, target_id),
    INDEX idx_reviews_user (user_id)
  )`,

  `CREATE TABLE IF NOT EXISTS favorites (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    entity_type ENUM('trek', 'gear', 'guide', 'package') NOT NULL,
    entity_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_favorites_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_favorite (user_id, entity_type, entity_id)
  )`,

  `CREATE TABLE IF NOT EXISTS emergency_contacts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL,
    type ENUM('police', 'tourist_police', 'hospital', 'rescue', 'helipad', 'general') NOT NULL,
    phone VARCHAR(30) NOT NULL,
    location VARCHAR(150),
    description TEXT,
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_emergency_type (type)
  )`,

  `CREATE TABLE IF NOT EXISTS admin_logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    action VARCHAR(150) NOT NULL,
    entity_type VARCHAR(80),
    entity_id INT,
    details JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_admin_logs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
  )`,

  `CREATE TABLE IF NOT EXISTS payment_transactions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    transaction_reference VARCHAR(80) NOT NULL UNIQUE,
    gateway_transaction_id VARCHAR(150) UNIQUE,
    rental_id INT,
    package_booking_id INT,
    customer_id INT NOT NULL,
    provider_id INT,
    gateway ENUM('ESEWA', 'KHALTI', 'MANUAL', 'SANDBOX') NOT NULL DEFAULT 'SANDBOX',
    gross_amount DECIMAL(12,2) NOT NULL,
    commission_rate DECIMAL(5,2) NOT NULL DEFAULT 10.00,
    commission_amount DECIMAL(12,2) NOT NULL,
    provider_payable DECIMAL(12,2) NOT NULL,
    payment_status ENUM('PENDING', 'VERIFIED', 'FAILED', 'REFUNDED', 'PARTIALLY_REFUNDED') NOT NULL DEFAULT 'PENDING',
    verification_status ENUM('UNVERIFIED', 'VERIFIED', 'FAILED') NOT NULL DEFAULT 'UNVERIFIED',
    verified_at DATETIME,
    gateway_response JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_rental FOREIGN KEY (rental_id) REFERENCES gear_rentals(id) ON DELETE SET NULL,
    CONSTRAINT fk_payment_package_booking FOREIGN KEY (package_booking_id) REFERENCES package_bookings(id) ON DELETE SET NULL,
    CONSTRAINT fk_payment_customer FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_payment_provider FOREIGN KEY (provider_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_payment_status (payment_status),
    INDEX idx_payment_provider (provider_id),
    INDEX idx_payment_created_at (created_at)
  )`,

  `CREATE TABLE IF NOT EXISTS provider_payouts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    transaction_id INT NOT NULL UNIQUE,
    provider_id INT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'PAID', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    approved_by INT,
    approved_at DATETIME,
    paid_at DATETIME,
    payout_reference VARCHAR(150),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_payout_transaction FOREIGN KEY (transaction_id) REFERENCES payment_transactions(id) ON DELETE CASCADE,
    CONSTRAINT fk_payout_provider FOREIGN KEY (provider_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_payout_approved_by FOREIGN KEY (approved_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_payout_status (status),
    INDEX idx_payout_provider (provider_id)
  )`,

  `CREATE TABLE IF NOT EXISTS payment_refunds (
    id INT PRIMARY KEY AUTO_INCREMENT,
    transaction_id INT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    reason TEXT NOT NULL,
    status ENUM('REQUESTED', 'APPROVED', 'COMPLETED', 'REJECTED') NOT NULL DEFAULT 'REQUESTED',
    requested_by INT,
    approved_by INT,
    approved_at DATETIME,
    completed_at DATETIME,
    refund_reference VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_refund_transaction FOREIGN KEY (transaction_id) REFERENCES payment_transactions(id) ON DELETE CASCADE,
    CONSTRAINT fk_refund_requested_by FOREIGN KEY (requested_by) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_refund_approved_by FOREIGN KEY (approved_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_refund_status (status),
    INDEX idx_refund_transaction (transaction_id)
  )`
];

const columnExists = async (pool, tableName, columnName) => {
  const [rows] = await pool.query(
    `SELECT 1
     FROM information_schema.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?
     LIMIT 1`,
    [tableName, columnName]
  );
  return rows.length > 0;
};

const initializeSchema = async () => {
  const pool = getPool();

  for (const statement of schemaStatements) {
    await pool.query(statement);
  }

  await pool.query(
    `INSERT IGNORE INTO roles (id, name) VALUES (1, 'USER'), (2, 'GUIDE'), (3, 'ADMIN')`
  );

  if (!(await columnExists(pool, 'gear', 'owner_user_id'))) {
    await pool.query(
      `ALTER TABLE gear
       ADD COLUMN owner_user_id INT NULL AFTER category_id,
       ADD INDEX idx_gear_owner_user (owner_user_id),
       ADD CONSTRAINT fk_gear_owner_user
         FOREIGN KEY (owner_user_id) REFERENCES users(id)
         ON DELETE SET NULL ON UPDATE CASCADE`
    );
  }

  console.log('Database schema initialized successfully');
};

module.exports = {
  schemaStatements,
  initializeSchema,
};
