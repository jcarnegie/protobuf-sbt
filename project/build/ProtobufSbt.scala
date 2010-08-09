class ProtobufSbt(info: sbt.ProjectInfo) extends sbt.PluginProject(info) {
  /**
   * Dependencies
   */
  val protostuffRepo = "Protostuff Repo" at "http://protostuff.googlecode.com/svn/repos/maven2"
  val protostuffApi = "com.dyuproject.protostuff" % "protostuff-api" % "1.0.0.M3" withSources()
  val protostuffCore = "com.dyuproject.protostuff" % "protostuff-core" % "1.0.0.M3" withSources()
  val protostuffCompiler = "com.dyuproject.protostuff" % "protostuff-compiler" % "1.0.0.M3" withSources() intransitive()
  val protostuffParser = "com.dyuproject.protostuff" % "protostuff-parser" % "1.0.0.M3" withSources() intransitive()
  val stringTemplate = "org.antlr" % "stringtemplate" % "3.2.1" withSources() intransitive()
  val antlr = "antlr" % "antlr" % "2.7.7" intransitive()
  val antlrRuntime = "org.antlr" % "antlr-runtime" % "3.2" withSources() intransitive()
  
  /**
   * Publish to a local temp repo, then rsync the files over to repo.codahale.com.
   */
  override def managedStyle = sbt.ManagedStyle.Maven
  val publishTo = sbt.Resolver.file("Local Cache", ("." / "target" / "repo").asFile)
  def publishToLocalRepoAction = super.publishAction
  override def publishAction = task {
    log.info("Uploading to repo.codahale.com")
    sbt.Process("rsync", "-avz" :: "target/repo/" :: "codahale.com:/home/codahale/repo.codahale.com" :: Nil) ! log
    None
  } describedAs("Publish binary and source JARs to repo.codahale.com") dependsOn(test, publishToLocalRepoAction)
}
