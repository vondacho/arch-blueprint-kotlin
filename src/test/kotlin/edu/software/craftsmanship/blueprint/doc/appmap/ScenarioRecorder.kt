package edu.software.craftsmanship.blueprint.doc.appmap

import com.appland.appmap.config.Properties
import com.appland.appmap.record.IRecordingSession
import com.appland.appmap.record.Recorder
import io.cucumber.java.Scenario
import java.nio.file.Paths

object ScenarioRecorder {

    fun start(scenario: Scenario) {
        if (ready()) {
            val recorder = Recorder.getInstance()
            if (!recorder.hasActiveSession()) {
                val metadata = IRecordingSession.Metadata()
                metadata.scenarioName = scenario.name
                val fileName = "${scenario.name.replace("[^a-zA-Z0-9-_]".toRegex(), "_")}.appmap.json"
                recorder.start(fileName, metadata)
            }
        }
    }

    fun stop() {
        val recorder = Recorder.getInstance()
        if (recorder.hasActiveSession())
            recorder.stop()
    }

    private fun ready() = Paths.get(Properties.OutputDirectory).toFile().exists()
}
