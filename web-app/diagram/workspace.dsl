workspace "OTAVA" {
  model {
    beginner = person "Beginner"
    expert = person "Expert"

    webApp = softwareSystem "Web Application" {
      webClient = container "Web Client" "" "React" {
        tableValPage = component "Table Validation" "" "" "webPage"
        descValPage = component "Descriptor Validation" "" "" "webPage"
        expertValPage = component "Expert Validation" "" "" "webPage"
      }
      webServer = container "Web Server" "" "Node.js" {
        router = component "Router"
        model = component "Model"
      }
      database = container "Database" "" "MySQL" "database"
      webWorker = container "Web Worker" "" "Java" {
        dbConn = component "Database Connector"
        lib = component "Validation Library"
      }
    }

    beginner -> tableValPage "Uses"
    beginner -> descValPage "Uses"
    expert -> tableValPage "Uses"
    expert -> descValPage "Uses"
    expert -> expertValPage "Uses"

    webClient -> router "Communicates"
    router -> model "Uses"
    model -> database "Reads & Writes"

    dbConn -> database "Reads & Writes"
    dbConn -> lib "Uses"
  }

  views {
    systemContext webApp {
      include *
      autoLayout
    }
    container webApp {
      include *
      autoLayout
    }
    component webClient {
      include *
      autoLayout
    }
    component webServer {
      include *
      autoLayout
    }
    component webWorker {
      include *
      autoLayout
    }

    styles {
      element webPage {
        shape WebBrowser
      }
      element database {
        shape Cylinder
      }
    }

    theme default
  }
}
