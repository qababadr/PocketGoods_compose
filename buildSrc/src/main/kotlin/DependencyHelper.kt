import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

fun versionCatalogue(project: Project): VersionCatalog {
    return project
        .extensions
        .getByType<VersionCatalogsExtension>()
        .named("libs")
}

fun DependencyHandler.hilt(project: Project) {
    val libs = versionCatalogue(project)

    add("implementation", libs.findLibrary("hilt-android").get())
    add("ksp", libs.findLibrary("hilt-android-compiler").get())
    add("implementation", libs.findLibrary("androidx-hilt-navigation-compose").get())
}

fun DependencyHandler.roomDatabase(project: Project) {
    val libs = versionCatalogue(project)
    add("implementation", libs.findLibrary("androidx-room-runtime").get())
    add("ksp", libs.findLibrary("androidx-room-compiler").get())
}

fun DependencyHandler.roomDatabaseTesting(project: Project) {
    val libs = versionCatalogue(project)
    add("testImplementation", libs.findLibrary("androidx-room-testing").get())
}

fun DependencyHandler.ktor(project: Project) {
    val libs = versionCatalogue(project)
    add("implementation", libs.findLibrary("ktor-client-core").get())
    add("implementation", libs.findLibrary("ktor-client-cio").get())
    add("implementation", libs.findLibrary("ktor-client-logging").get())
    add("implementation", libs.findLibrary("ktor-client-content-negotiation").get())
    add("implementation", libs.findLibrary("ktor-serialization-gson").get())
    add("implementation", libs.findLibrary("ktor-client-mock").get())
}

fun DependencyHandler.compose(project: Project) {
    val libs = versionCatalogue(project)

    add("implementation", libs.findLibrary("androidx-core-ktx").get())
    add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
    add("implementation", libs.findLibrary("androidx-activity-compose").get())
    add("implementation", platform(libs.findLibrary("androidx-compose-bom").get()))
    add("implementation", libs.findLibrary("androidx-ui").get())
    add("implementation", libs.findLibrary("androidx-ui-graphics").get())
    add("implementation", libs.findLibrary("androidx-ui-tooling-preview").get())
    add("implementation", libs.findLibrary("androidx-material3").get())
}

fun DependencyHandler.dataModuleAndroidTestImplementation(project: Project) {
    val libs = versionCatalogue(project)
    add("androidTestImplementation", libs.findLibrary("turbine").get())
    add("androidTestImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
    add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
    add("androidTestImplementation", libs.findLibrary("androidx-espresso-core").get())
    add("androidTestImplementation", libs.findLibrary("ktor-client-mock").get())
    add("androidTestImplementation", libs.findLibrary("gson").get())
    add("androidTestImplementation", kotlin("test"))
}

fun DependencyHandler.presentationModuleAndroidTestImplementation(project: Project) {
    val libs = versionCatalogue(project)
    add("androidTestImplementation", libs.findLibrary("turbine").get())
    add("androidTestImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
    add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
    add("androidTestImplementation", libs.findLibrary("androidx-espresso-core").get())
    add("kspAndroidTest", libs.findLibrary("hilt-android-compiler").get())
    add("androidTestImplementation", libs.findLibrary("hilt-android-testing").get())
    add("androidTestImplementation", libs.findLibrary("androidx-room-runtime").get())
    add("ksp", libs.findLibrary("androidx-room-compiler").get())
    add("androidTestImplementation", libs.findLibrary("gson").get())
    add("androidTestImplementation", libs.findLibrary("ktor-client-mock").get())
    add("androidTestImplementation", kotlin("test"))
}

