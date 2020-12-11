logLevel := Level.Info

addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.0-RC4")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.15.0")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.10.9"