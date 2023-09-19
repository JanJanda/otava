const mysql = require('mysql2/promise');

let connectionPool;

init();

async function init() {
  connectionPool = await mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: process.env.OTAVA_DB_PSW,
  });

  await connectionPool.query('CREATE DATABASE IF NOT EXISTS otava;');
  await connectionPool.query('USE otava;')
  await connectionPool.query('CREATE TABLE IF NOT EXISTS `validations` (`id` VARCHAR(10) NOT NULL , `language` VARCHAR(10) NOT NULL , `style` VARCHAR(10) NOT NULL , `passive-tables` TEXT NOT NULL , `active-tables` TEXT NOT NULL , `passive-descriptors` TEXT NOT NULL , `active-descriptors` TEXT NOT NULL , `description` TEXT NOT NULL , `requested-time` DATETIME NOT NULL , `finished-time` DATETIME NULL , `state` VARCHAR(10) NOT NULL , `result-text` TEXT NULL , `result-json` TEXT NULL , `result-turtle` TEXT NULL , PRIMARY KEY (`id`));');

  console.log('Database is ready');
}

async function addValidationRequest(language, style, passiveTables, activeTables, passiveDescriptors, activeDescriptors, description) {
  let id = makeId(); // add transactions
  const [rows] = await connectionPool.execute('SELECT `id` FROM `validations` WHERE `id` = ?', [id]);
}

function makeId() {
  const chars = 'abcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  for (let i = 1; i <= 10; i++) {
    result = result + chars[Math.floor(Math.random() * chars.length)];
  }
  return result;
}

module.exports.addValidationRequest = addValidationRequest;
