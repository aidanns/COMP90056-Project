name := "cdr-webapp"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "4.2.6.Final",
  "com.aidanns.streams.project" % "cdr-models" % "1.0-SNAPSHOT"
)     

play.Project.playJavaSettings

// The file containing the require.js bootstrap for the app.
requireJs += "main.js"

// The file containiner the require.js config for the app.
requireJsShim += "main.js"

// Add the local maven repo.
resolvers += "Local Maven Repo" at "file://" + Path.userHome.absolutePath + "/.m2/repository"