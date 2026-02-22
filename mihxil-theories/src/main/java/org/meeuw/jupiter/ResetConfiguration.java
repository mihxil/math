package org.meeuw.jupiter;

import lombok.extern.java.Log;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

import org.meeuw.configuration.ConfigurationService;

@Log
public class ResetConfiguration implements TestExecutionListener {

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        log.info("" + testPlan);
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        log.info("Resetting configuration to defaults");
        ConfigurationService.resetToDefaults();
    }
}
