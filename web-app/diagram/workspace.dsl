workspace "OTAVA" {
  model {
    beginner = person "Beginner"
    expert = person "Expert"

    webApp = softwareSystem "Web Application" {
      client = container "Client" "" "React" {
        tableValPage = component "Table Validation" "" "" "page"
        descValPage = component "Descriptor Validation" "" "" "page"
        expertValPage = component "Expert Validation" "" "" "page"
      }
      server = container "Server" "" "Node.js" {
        router = component "Router"
        model = component "Model"
      }
      database = container "Database" "" "MySQL" "database"
      worker = container "Worker" "" "Java" {
        dbConn = component "Database Connector"
        lib = component "Validation Library"
      }
    }

    beginner -> tableValPage "Uses"
    beginner -> descValPage "Uses"
    expert -> tableValPage "Uses"
    expert -> descValPage "Uses"
    expert -> expertValPage "Uses"

    client -> router "Communicates"
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
    component client {
      include *
      autoLayout
    }
    component server {
      include *
      autoLayout
    }
    component worker {
      include *
      autoLayout
    }

    styles {
      element page {
        shape WebBrowser
      }
      element database {
        shape Cylinder
      }
    }

    theme default
  }
}
