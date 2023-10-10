export const englishLocale = {
  noValidation: "The validation request does not exist.",
  queueing: "The validation request is waiting in the queue. Reload this page later.",
  setValLang: "Validation result language",
  english: "English",
  czech: "Czech",
  valStyle: "Validation style",
  fullVal: "Full validation",
  tablesVal: "Tables only validation",
  descVal: "Descriptors only validation",
  passTables: "Passive tables",
  activeTables: "Active tables",
  passDescs: "Passive descriptors",
  activeDescs: "Active descriptors",
  description: "Description of the request",
  submit: "Submit validation request",
  validation: "Validation",
  reqTime: "Request time",
  reqDetail: "Request details",
  langTag: "en",
  working: "The validation is in progress. Reload this page later.",
  finished: "The validation is finished.",
  report: "Validation report",
  finishTime: "Finish time",
  notFound: "Sorry, page not found",
  searchById: "Search validation by ID",
  search: "Search",
  validate: "Validate",
  searchValidation: "Search validation",
  otava: "Open Table Validator",
  webApp: "Web Application",
  introText: "This web application is a part of the larger OTAVA project. " +
    "The OTAVA project contains software for validation of CSV tables together with their metadata descriptor files according to the CSV on the Web W3C recommendations. " +
    "The CSV on the Web W3C recommendations specify, how to describe CSV files published on the Web using JSON-LD descriptors containing important metadata, such as column names, data types and more. " +
    "The core of the project is a Java library with the main functionality. " +
    "The project also contains a command line interface Java application which provides access to every feature of the library. " +
    "This web application provides a simplified interface with limited features for less experienced users.",
  manual: "Instruction manual",
  howToUse: "Users submit their validation requests using a web form. " +
    "The form offers three validation styles. " +
    "The full validation validates all tables and descriptors together. " +
    "The tables only validation validates only passive tables alone. " +
    "The descriptors only validation validates only passive descriptors alone. " +
    "There are four types of documents: passive table, active table, passive descriptor, and active descriptor. " +
    "An active table attempts to locate its descriptor, a passive table does not. " +
    "An active descriptor attempts to locate its tables, a passive descriptor does not. " +
    "Every document has a URL with the the actual location of the file. " +
    "Every document may have an alias. " +
    "An alias is an alternative URL. " +
    "If an alias is present, it is used in the validation instead of the actual URL. " +
    "A document is located and loaded according to its URL, but it pretends that it is on a different location according to the alias. " +
    "There is an input text field for each document type in the form. " +
    "Each line in a field represent one document, and it contains a URL of the document first and optionally an alias of the document second separated by a space. " +
    "If you want to validate a local file on your computer, you have to upload it somewhere on the web first using a web service of your choice. " +
    "The easiest option is probably GitHub. " +
    "Alternatively, you can use the command line interface Java application. " +
    "After the validation is finished, the results of the validation can be searched by the assigned ID. " +
    "Each validation request and results stay online for approximately seven days, and then they are deleted.",
  about: "About this application",
  descUrl: "Descriptor URL",
  switchActiveDesc: "Find and use tables from the descriptor",
  tableUrl: "Table URL",
  switchActiveTable: "Find and use the descriptor of the table",
  tableValidation: "Table validation",
  descValidation: "Descriptor validation",
  expertValidation: "Expert validation",
  chooseVal: "Choose your validation"
};
