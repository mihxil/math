package org.meeuw.jupiter.impl;

import lombok.extern.java.Log;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.*;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.meeuw.functional.Predicates.biAlwaysTrue;

@Log
public class ResetConfiguration implements TestExecutionListener {

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        log.info("Test plan started " + testPlan);

    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (ConfigurationService.resetToDefaults()) {
            log.info("Resetting configuration to defaults " + testIdentifier);
        }

        assert !ConfigurationService.getConfigurationAspect(UncertaintyConfiguration.class).getStripZeros().equals(biAlwaysTrue());
    }
}
