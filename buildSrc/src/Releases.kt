const val RELEASE_USER = "hendraanggrian"
const val RELEASE_REPO = "material"
const val RELEASE_GROUP = "com.$RELEASE_USER.$RELEASE_REPO"
const val RELEASE_ARTIFACT = "errorbar"
const val RELEASE_DESC = "Larger Snackbar to display error and empty state"
const val RELEASE_WEBSITE = "https://github.com/$RELEASE_USER/$RELEASE_ARTIFACT"

val bintrayUserEnv = System.getenv("BINTRAY_USER")
val bintrayKeyEnv = System.getenv("BINTRAY_KEY")