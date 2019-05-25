package com.example.sonyadmin;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RxSchedulersOverrideRule implements TestRule {
//    @Override public Statement apply(final Statement base, Description description) {
//        return new Statement() {
//            @Override public void evaluate() throws Throwable {
//                RxAndroidPlugins.reset();
//                RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
//
//                RxJavaPlugins.reset();
//                RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
//                RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
//                RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
//
//                base.evaluate();
//
//                RxAndroidPlugins.reset();
//                RxJavaPlugins.reset();
//            }
//        };
//    }

    @Override
    public Statement apply(Statement base, Description description) {
        return null;
    }
}