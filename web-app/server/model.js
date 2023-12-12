const mysql = require("mysql2/promise");

let retryId;

function catchError(error) {
  clearTimeout(retryId);
  retryId = setTimeout(init, 10000);
  console.error(error);
}

let connectionPool;

init();

async function init() {
  try {
    let dbHost = process.env.OTAVA_DB_HOST;
    if (dbHost === undefined) dbHost = "localhost";

    connectionPool = await mysql.createPool({
      host: dbHost,
      user: "root",
      password: process.env.OTAVA_DB_PSW
    });

    await connectionPool.query("CREATE DATABASE IF NOT EXISTS otava;");
    await connectionPool.query("USE otava;");
    await connectionPool.query("CREATE TABLE IF NOT EXISTS `validations` (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT , `language` VARCHAR(20) NOT NULL , `style` VARCHAR(20) NOT NULL , `passive-tables` TEXT NOT NULL , `active-tables` TEXT NOT NULL , `passive-descriptors` TEXT NOT NULL , `active-descriptors` TEXT NOT NULL , `description` TEXT NOT NULL , `form` VARCHAR(20) NOT NULL , `request-time` DATETIME NOT NULL , `finish-time` DATETIME NULL , `state` VARCHAR(20) NOT NULL , `outcome-text` TEXT NULL , `outcome-json` TEXT NULL , `outcome-turtle` TEXT NULL , PRIMARY KEY (`id`));");

    console.log("Database is ready");
  }
  catch (error) {
    catchError(error);
  }
}

setInterval(deleteOld, 3600000);

async function deleteOld() {
  try {
    const d = new Date();
    d.setDate(d.getDate() - 7);
    await connectionPool.execute("DELETE FROM `validations` WHERE `request-time` < ?;", [d]);
  }
  catch (error) {
    catchError(error);
  }
}

async function addTableValidationRequest(language, url, active) {
  if (language === undefined || url === undefined) return;
  try {
    let style = "tables";
    let passiveTables = url;
    let activeTables = "";
    let form = "pas-table";
    if (active === "on") {
      style = "full";
      passiveTables = "";
      activeTables = url;
      form = "act-table";
    }

    const result = await connectionPool.execute("INSERT INTO `validations` (`language`, `style`, `passive-tables`, `active-tables`, `passive-descriptors`, `active-descriptors`, `description`, `form`, `request-time`, `state`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
      [language, style, passiveTables, activeTables, "", "", "", form, new Date(), "queueing"]);
    return result[0].insertId;
  }
  catch (error) {
    catchError(error);
  }
}

async function addDescriptorValidationRequest(language, url, active) {
  if (language === undefined || url === undefined) return;
  try {
    let style = "descs";
    let passiveDescriptors = url;
    let activeDescriptors = "";
    let form = "pas-desc";
    if (active === "on") {
      style = "full";
      passiveDescriptors = "";
      activeDescriptors = url;
      form = "act-desc";
    }

    const result = await connectionPool.execute("INSERT INTO `validations` (`language`, `style`, `passive-tables`, `active-tables`, `passive-descriptors`, `active-descriptors`, `description`, `form`, `request-time`, `state`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
      [language, style, "", "", passiveDescriptors, activeDescriptors, "", form, new Date(), "queueing"]);
    return result[0].insertId;
  }
  catch (error) {
    catchError(error);
  }
}

async function addExpertValidationRequest(language, style, passiveTables, activeTables, passiveDescriptors, activeDescriptors, description) {
  if (language === undefined || style === undefined || passiveTables === undefined || activeTables === undefined || passiveDescriptors === undefined || activeDescriptors === undefined || description === undefined) return;
  try {
    const result = await connectionPool.execute("INSERT INTO `validations` (`language`, `style`, `passive-tables`, `active-tables`, `passive-descriptors`, `active-descriptors`, `description`, `form`, `request-time`, `state`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
      [language, style, passiveTables, activeTables, passiveDescriptors, activeDescriptors, description, "expert", new Date(), "queueing"]);
    return result[0].insertId;
  }
  catch (error) {
    catchError(error);
  }
}

async function getValidationData(id) {
  if (id === undefined) return;
  try {
    const result = await connectionPool.execute("SELECT * FROM `validations` WHERE `id` = ?;", [id]);
    return result[0][0];
  }
  catch (error) {
    catchError(error);
  }
}

module.exports.addTableValidationRequest = addTableValidationRequest;
module.exports.addDescriptorValidationRequest = addDescriptorValidationRequest;
module.exports.addExpertValidationRequest = addExpertValidationRequest;
module.exports.getValidationData = getValidationData;
