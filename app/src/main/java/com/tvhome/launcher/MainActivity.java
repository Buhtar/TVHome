Run gradle assembleDebug --no-daemon
To honour the JVM settings for this build a single-use Daemon process will be forked. For more on this, please refer to https://docs.gradle.org/8.5/userguide/gradle_daemon.html#sec:disabling_the_daemon in the Gradle documentation.
Daemon will be stopped at the end of the build 
Warning: SDK processing. This version only understands SDK XML versions up to 3 but an SDK XML file of version 4 was encountered. This can happen if you use versions of Android Studio and the command-line tools that were released at different times.
Checking the license for package Android SDK Build-Tools 33.0.1 in /usr/local/lib/android/sdk/licenses
License for package Android SDK Build-Tools 33.0.1 accepted.
Preparing "Install Android SDK Build-Tools 33.0.1 v.33.0.1".
"Install Android SDK Build-Tools 33.0.1 v.33.0.1" ready.
Installing Android SDK Build-Tools 33.0.1 in /usr/local/lib/android/sdk/build-tools/33.0.1
"Install Android SDK Build-Tools 33.0.1 v.33.0.1" complete.
"Install Android SDK Build-Tools 33.0.1 v.33.0.1" finished.
> Task :app:preBuild UP-TO-DATE
> Task :app:preDebugBuild UP-TO-DATE
> Task :app:mergeDebugNativeDebugMetadata NO-SOURCE
> Task :app:javaPreCompileDebug
> Task :app:generateDebugResValues
> Task :app:checkDebugAarMetadata
> Task :app:mapDebugSourceSetPaths
> Task :app:generateDebugResources
> Task :app:packageDebugResources
> Task :app:createDebugCompatibleScreenManifests
> Task :app:extractDeepLinksDebug
> Task :app:mergeDebugResources
> Task :app:parseDebugLocalResources
> Task :app:processDebugMainManifest
> Task :app:processDebugManifest
> Task :app:mergeDebugShaders
> Task :app:compileDebugShaders NO-SOURCE
> Task :app:generateDebugAssets UP-TO-DATE
> Task :app:mergeDebugAssets
> Task :app:compressDebugAssets
> Task :app:processDebugJavaRes NO-SOURCE
> Task :app:checkDebugDuplicateClasses
> Task :app:mergeDebugJavaResource
> Task :app:desugarDebugFileDependencies
> Task :app:processDebugManifestForPackage
> Task :app:mergeLibDexDebug
> Task :app:mergeDebugJniLibFolders
> Task :app:mergeDebugNativeLibs NO-SOURCE
> Task :app:stripDebugDebugSymbols NO-SOURCE
> Task :app:processDebugResources
> Task :app:validateSigningDebug
> Task :app:mergeExtDexDebug

> Task :app:compileDebugJavaWithJavac FAILED
/home/runner/work/TVHome/TVHome/app/src/main/java/com/tvhome/launcher/MainActivity.java:78: error: ';' expected
    protected void onCreate(Bundle savedInstanceState) {
                           ^
/home/runner/work/TVHome/TVHome/app/src/main/java/com/tvhome/launcher/MainActivity.java:78: error: ';' expected
    protected void onCreate(Bundle savedInstanceState) {
                                                     ^
/home/runner/work/TVHome/TVHome/app/src/main/java/com/tvhome/launcher/MainActivity.java:113: error: ';' expected
    protected void onResume() {
                           ^
/home/runner/work/TVHome/TVHome/app/src/main/java/com/tvhome/launcher/MainActivity.java:128: error: ';' expected
    protected void onPause() {
                          ^
/home/runner/work/TVHome/TVHome/app/src/main/java/com/tvhome/launcher/MainActivity.java:135: error: illegal start of expression
    private void applyFonts() {
    ^
/home/runner/work/TVHome/TVHome/app/src/main/java/com/tvhome/launcher/MainActivity.java:320: error: reached end of file while parsing
}
 ^
6 errors

gradle/actions: Writing build results to /home/runner/work/_temp/.gradle-actions/build-results/__run-1792570146832.json
FAILURE: Build failed with an exception.
24 actionable tasks: 24 executed

* What went wrong:
Execution failed for task ':app:compileDebugJavaWithJavac'.
> Compilation failed; see the compiler error output for details.

* Try:
> Run with --info option to get more log output.
> Run with --scan to get full insights.

BUILD FAILED in 17s
Error: Process completed with exit code 1.