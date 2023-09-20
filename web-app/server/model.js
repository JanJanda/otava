const mysql = require("mysql2/promise");

let connectionPool;

init();

async function init() {
  connectionPool = await mysql.createPool({
    host: "localhost",
    user: "root",
    password: process.env.OTAVA_DB_PSW,
  });

  await connectionPool.query("CREATE DATABASE IF NOT EXISTS otava;");
  await connectionPool.query("USE otava;")
  await connectionPool.query("CREATE TABLE IF NOT EXISTS `validations` (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT , `language` VARCHAR(10) NOT NULL , `style` VARCHAR(10) NOT NULL , `passive-tables` TEXT NOT NULL , `active-tables` TEXT NOT NULL , `passive-descriptors` TEXT NOT NULL , `active-descriptors` TEXT NOT NULL , `description` TEXT NOT NULL , `request-time` DATETIME NOT NULL , `finish-time` DATETIME NULL , `state` VARCHAR(10) NOT NULL , `result-text` TEXT NULL , `result-json` TEXT NULL , `result-turtle` TEXT NULL , PRIMARY KEY (`id`));");

  console.log("Database is ready");
}

async function addValidationRequest(language, style, passiveTables, activeTables, passiveDescriptors, activeDescriptors, description) {
  const result = await connectionPool.execute("INSERT INTO `validations` (`language`, `style`, `passive-tables`, `active-tables`, `passive-descriptors`, `active-descriptors`, `description`, `request-time`, `state`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);",
    [language, style, passiveTables, activeTables, passiveDescriptors, activeDescriptors, description, new Date(), "waiting"]);
  return result[0].insertId;
}

module.exports.addValidationRequest = addValidationRequest;
