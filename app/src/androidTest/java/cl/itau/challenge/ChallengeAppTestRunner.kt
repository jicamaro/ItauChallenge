package cl.itau.challenge

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import cl.itau.challenge.presentation.ChallengeAppTest

class ChallengeAppTestRunner: AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, ChallengeAppTest::class.java.name, context)
    }
}