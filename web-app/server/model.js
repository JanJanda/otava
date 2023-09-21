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
  await connectionPool.query("CREATE TABLE IF NOT EXISTS `validations` (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT , `language` VARCHAR(20) NOT NULL , `style` VARCHAR(20) NOT NULL , `passive-tables` TEXT NOT NULL , `active-tables` TEXT NOT NULL , `passive-descriptors` TEXT NOT NULL , `active-descriptors` TEXT NOT NULL , `description` TEXT NOT NULL , `request-time` DATETIME NOT NULL , `finish-time` DATETIME NULL , `state` VARCHAR(20) NOT NULL , `result-text` TEXT NULL , `result-json` TEXT NULL , `result-turtle` TEXT NULL , PRIMARY KEY (`id`));");

  setInterval(deleteOld, 3600000);

  console.log("Database is ready");
}

async function deleteOld() {
  const d = new Date();
  d.setDate(d.getDate() - 7);
  await connectionPool.execute("DELETE FROM `validations` WHERE `request-time` < ?", [d]);
}

async function addValidationRequest(language, style, passiveTables, activeTables, passiveDescriptors, activeDescriptors, description) {
  const result = await connectionPool.execute("INSERT INTO `validations` (`language`, `style`, `passive-tables`, `active-tables`, `passive-descriptors`, `active-descriptors`, `description`, `request-time`, `state`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);",
    [language, style, passiveTables, activeTables, passiveDescriptors, activeDescriptors, description, new Date(), "queueing"]);
  return result[0].insertId;
}

async function getValidationData(id) {
  const result = await connectionPool.execute("SELECT * FROM `validations` WHERE `id` = ?", [id]);
  return result[0][0];
}

module.exports.addValidationRequest = addValidationRequest;
module.exports.getValidationData = getValidationData;
