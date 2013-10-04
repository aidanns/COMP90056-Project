name := "webapp"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)     

play.Project.playJavaSettings

// The file containing the require.js bootstrap for the app.
requireJs += "main.js"

// The file containiner the require.js config for the app.
requireJsShim += "main.js"